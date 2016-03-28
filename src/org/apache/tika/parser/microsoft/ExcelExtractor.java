/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.microsoft;

import java.awt.Point;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/*import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.CountryRecord;
import org.apache.poi.hssf.record.DateWindow1904Record;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.hssf.record.UnicodeString;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
*/
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.SAXException;

/**
 * Excel parser implementation which uses POI's Event API
 * to handle the contents of a Workbook.
 * <p>
 * The Event API uses a much smaller memory footprint than
 * <code>HSSFWorkbook</code> when processing excel files
 * but at the cost of more complexity.
 * <p>
 * With the Event API a <i>listener</i> is registered for
 * specific record types and those records are created,
 * fired off to the listener and then discarded as the stream
 * is being processed.
 *
 * @see org.apache.poi.hssf.eventusermodel.HSSFListener
 * @see <a href="http://poi.apache.org/hssf/how-to.html#event_api">
 * POI Event API How To</a>
 */
public class ExcelExtractor {

    /**
     * <code>true</code> if the HSSFListener should be registered
     * to listen for all records or <code>false</code> (the default)
     * if the listener should be configured to only receive specified
     * records.
     */
    private boolean listenForAllRecords = false;

    /**
     * Returns <code>true</code> if this parser is configured to listen
     * for all records instead of just the specified few.
     */
    public boolean isListenForAllRecords() {
        return listenForAllRecords;
    }

    /**
     * Specifies whether this parser should to listen for all
     * records or just for the specified few.
     * <p>
     * <strong>Note:</strong> Under normal operation this setting should
     * be <code>false</code> (the default), but you can experiment with
     * this setting for testing and debugging purposes.
     *
     * @param listenForAllRecords <code>true</code> if the HSSFListener
     * should be registered to listen for all records or <code>false</code>
     * if the listener should be configured to only receive specified records.
     */
    public void setListenForAllRecords(boolean listenForAllRecords) {
        this.listenForAllRecords = listenForAllRecords;
    }

    /**
     * Extracts text from an Excel Workbook writing the extracted content
     * to the specified {@link Appendable}.
     *
     * @param filesystem POI file system
     * @throws IOException if an error occurs processing the workbook
     * or writing the extracted content
     */
   // protected void parse(
      //      POIFSFileSystem filesystem, XHTMLContentHandler xhtml,
        //    Locale locale) throws IOException, SAXException {
    	//TikaHSSFListener listener = new TikaHSSFListener(xhtml, locale);
    	//listener.processFile(filesystem, isListenForAllRecords());
        //listener.throwStoredException();
    //}

    // ======================================================================

    /**
     * HSSF Listener implementation which processes the HSSF records.
     */
    private static class TikaHSSFListener {// implements HSSFListener {

        /**
         * XHTML content handler to which the document content is rendered.
         */
        private final XHTMLContentHandler handler;

        /**
         * Potential exception thrown by the content handler. When set to
         * non-<code>null</code>, causes all subsequent HSSF records to be
         * ignored and the stored exception to be thrown when
         * {@link #throwStoredException()} is invoked.
         */
        private SAXException exception = null;

     //   private SSTRecord sstRecord;

        /**
         * Internal <code>FormatTrackingHSSFListener</code> to handle cell
         * formatting within the extraction.
      //   */
      //  private FormatTrackingHSSFListener formatListener;

        /**
         * List of worksheet names.
         */
        private List<String> sheetNames = new ArrayList<String>();

        /**
         * Index of the current worksheet within the workbook.
         * Used to find the worksheet name in the {@link #sheetNames} list.
         */
        private short currentSheetIndex;

        /**
         * Content of the current worksheet, or <code>null</code> if no
         * worksheet is currently active.
         */
        private SortedMap<Point, Cell> currentSheet = null;

        /**
         * Format for rendering numbers in the worksheet. Currently we just
         * use the platform default formatting.
         *
         * @see <a href="https://issues.apache.org/jira/browse/TIKA-103">TIKA-103</a>
         */
        private final NumberFormat format;

        /**
         * Construct a new listener instance outputting parsed data to
         * the specified XHTML content handler.
         *
         * @param handler Destination to write the parsed output to
         */
        private TikaHSSFListener(XHTMLContentHandler handler, Locale locale) {
            this.handler = handler;
            this.format = NumberFormat.getInstance(locale);
        }

        /**
         * Entry point to listener to start the processing of a file.
         *
         * @param filesystem POI file system.
         * @param listenForAllRecords sets whether the listener is configured to listen
         * for all records types or not.
         * @throws IOException on any IO errors.
         * @throws SAXException on any SAX parsing errors.
         */
  /*  	public void processFile(POIFSFileSystem filesystem, boolean listenForAllRecords)
    		throws IOException,	SAXException {

    		// Set up listener and register the records we want to process
    		formatListener = new FormatTrackingHSSFListener(this);
            HSSFRequest hssfRequest = new HSSFRequest();
            if (listenForAllRecords) {
                hssfRequest.addListenerForAllRecords(formatListener);
            } else {
                hssfRequest.addListener(formatListener, BOFRecord.sid);
                hssfRequest.addListener(formatListener, EOFRecord.sid);
                hssfRequest.addListener(formatListener, DateWindow1904Record.sid);
                hssfRequest.addListener(formatListener, CountryRecord.sid);
                hssfRequest.addListener(formatListener, BoundSheetRecord.sid);
                hssfRequest.addListener(formatListener, SSTRecord.sid);
                hssfRequest.addListener(formatListener, FormulaRecord.sid);
                hssfRequest.addListener(formatListener, LabelRecord.sid);
                hssfRequest.addListener(formatListener, LabelSSTRecord.sid);
                hssfRequest.addListener(formatListener, NumberRecord.sid);
                hssfRequest.addListener(formatListener, RKRecord.sid);
                hssfRequest.addListener(formatListener, HyperlinkRecord.sid);
                hssfRequest.addListener(formatListener, TextObjectRecord.sid);
                hssfRequest.addListener(formatListener, FormatRecord.sid);
                hssfRequest.addListener(formatListener, ExtendedFormatRecord.sid);
            }

            // Create event factory and process Workbook (fire events)
            DocumentInputStream documentInputStream = filesystem.createDocumentInputStream("Workbook");
            HSSFEventFactory eventFactory = new HSSFEventFactory();
            eventFactory.processEvents(hssfRequest, documentInputStream);
    	}
*/
        /**
         * Process a HSSF record.
         *
         * @param record HSSF Record
         */
   /*     public void processRecord(Record record) {
            if (exception == null) {
                try {
                    internalProcessRecord(record);
                } catch (SAXException e) {
                    exception = e;
                }
            }
        }
*/
        public void throwStoredException() throws SAXException {
            if (exception != null) {
                throw exception;
            }
        }

 /*       private void internalProcessRecord(Record record) throws SAXException {
            switch (record.getSid()) {
            case BOFRecord.sid: // start of workbook, worksheet etc. records
                BOFRecord bof = (BOFRecord) record;
                if (bof.getType() == BOFRecord.TYPE_WORKBOOK) {
                    currentSheetIndex = -1;
                } else if (bof.getType() == BOFRecord.TYPE_WORKSHEET) {
                    currentSheetIndex++;
                    currentSheet =
                        new TreeMap<Point, Cell>(new PointComparator());
                }
                break;

            case EOFRecord.sid: // end of workbook, worksheet etc. records
                if (currentSheet != null) {
                    processSheet();
                }
                currentSheet = null;
                break;

            case BoundSheetRecord.sid: // Worksheet index record
                BoundSheetRecord boundSheetRecord = (BoundSheetRecord) record;
                sheetNames.add(boundSheetRecord.getSheetname());
                break;

            case SSTRecord.sid: // holds all the strings for LabelSSTRecords
                sstRecord = (SSTRecord) record;
                break;

            case FormulaRecord.sid: // Cell value from a formula
                FormulaRecord formula = (FormulaRecord) record;
                addCell(record, new NumberCell(formula.getValue(), format));
                break;

            case LabelRecord.sid: // strings stored directly in the cell
                LabelRecord label = (LabelRecord) record;
                addTextCell(record, label.getValue());
                break;

            case LabelSSTRecord.sid: // Ref. a string in the shared string table
                LabelSSTRecord sst = (LabelSSTRecord) record;
                UnicodeString unicode = sstRecord.getString(sst.getSSTIndex());
                addTextCell(record, unicode.getString());
                break;

            case NumberRecord.sid: // Contains a numeric cell value
                NumberRecord number = (NumberRecord) record;
                addTextCell(record, formatListener.formatNumberDateCell(number));
                break;

            case RKRecord.sid: // Excel internal number record
                RKRecord rk = (RKRecord) record;
                addCell(record, new NumberCell(rk.getRKNumber(), format));
                break;

            case HyperlinkRecord.sid: // holds a URL associated with a cell
                if (currentSheet != null) {
                    HyperlinkRecord link = (HyperlinkRecord) record;
                    Point point =
                        new Point(link.getFirstColumn(), link.getFirstRow());
                    Cell cell = currentSheet.get(point);
                    if (cell != null) {
                        addCell(record, new LinkedCell(cell, link.getAddress()));
                    }
                }
                break;
            case TextObjectRecord.sid:
                TextObjectRecord tor = (TextObjectRecord) record;
                addTextCell(record, tor.getStr().getString());
                break;
            }
        }
*/
        /**
         * Adds the given cell (unless <code>null</code>) to the current
         * worksheet (if any) at the position (if any) of the given record.
         *
         * @param record record that holds the cell value
         * @param cell cell value (or <code>null</code>)
         */
  /*      private void addCell(Record record, Cell cell) throws SAXException {
            if (cell == null) {
                // Ignore empty cells
            } else if (currentSheet != null
                    && record instanceof CellValueRecordInterface) {
                // Normal cell inside a worksheet
                CellValueRecordInterface value =
                    (CellValueRecordInterface) record;
                Point point = new Point(value.getColumn(), value.getRow());
                currentSheet.put(point, cell);
            } else {
                // Cell outside the worksheets
                handler.startElement("div", "class", "outside");
                cell.render(handler);
                handler.endElement("div");
            }
        }
*/
        /**
         * Adds a text cell with the given text comment. The given text
         * is trimmed, and ignored if <code>null</code> or empty.
         *
         * @param record record that holds the text value
         * @param text text content, may be <code>null</code>
         * @throws SAXException
         */
/*        private void addTextCell(Record record, String text) throws SAXException {
            if (text != null) {
                text = text.trim();
                if (text.length() > 0) {
                    addCell(record, new TextCell(text));
                }
            }
        }

        /**
         * Process an excel sheet.
         *
         * @throws SAXException if an error occurs
         */
        private void processSheet() throws SAXException {
            // Sheet Start
            handler.startElement("div", "class", "page");
            if (currentSheetIndex < sheetNames.size()) {
                handler.element("h1", sheetNames.get(currentSheetIndex));
            }
            handler.startElement("table");
            handler.startElement("tbody");

            // Process Rows
            int currentRow = 0;
            int currentColumn = 0;
            handler.startElement("tr");
            handler.startElement("td");
            for (Map.Entry<Point, Cell> entry : currentSheet.entrySet()) {
                while (currentRow < entry.getKey().y) {
                    handler.endElement("td");
                    handler.endElement("tr");
                    handler.startElement("tr");
                    handler.startElement("td");
                    currentRow++;
                    currentColumn = 0;
                }

                while (currentColumn < entry.getKey().x) {
                    handler.endElement("td");
                    handler.startElement("td");
                    currentColumn++;
                }

                entry.getValue().render(handler);
            }
            handler.endElement("td");
            handler.endElement("tr");

            // Sheet End
            handler.endElement("tbody");
            handler.endElement("table");
            handler.endElement("div");
        }

    }

    /**
     * Utility comparator for points.
     */
    private static class PointComparator implements Comparator<Point> {

        public int compare(Point a, Point b) {
            int diff = a.y - b.y;
            if (diff == 0) {
                diff = a.x - b.x;
            }
            return diff;
        }

    }

}
