/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;

/**
 *
 * @author zgh
 */
public class Crawler extends WebCrawler {

    
public void visit(Page page) {

    int docid = page.getWebURL().getDocid();
    String url = page.getWebURL().getURL();
    String domain = page.getWebURL().getDomain();
    String path = page.getWebURL().getPath();
    String subDomain = page.getWebURL().getSubDomain();
    String parentUrl = page.getWebURL().getParentUrl();
    String anchor = page.getWebURL().getAnchor();

    System.out.println("Docid: " + docid);
    System.out.println("URL: " + url);
    System.out.println("Domain: '" + domain + "'");
    System.out.println("Sub-domain: '" + subDomain + "'");
    System.out.println("Path: '" + path + "'");
    System.out.println("Parent page: " + parentUrl);
    System.out.println("Anchor text: " + anchor);

    if (page.getParseData() instanceof HtmlParseData) {
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        String text = htmlParseData.getText();
        String html = htmlParseData.getHtml();
        Set<WebURL> links = htmlParseData.getOutgoingUrls();

        System.out.println("Text length: " + text.length());
        System.out.println("Html length: " + html.length());
        System.out.println("Number of outgoing links: " + links.size());
    }

    Header[] responseHeaders = page.getFetchResponseHeaders();
    if (responseHeaders != null) {
        System.out.println("Response headers:");
        for (Header header : responseHeaders) {
            System.out.println("\t" + header.getName() + ": " + header.getValue());
        }
    }
    System.out.println("=============");

    ArrayList<String> SearchTerms = (ArrayList<String>) this.getMyController().getCustomData();
    ArrayList<String> UrlHits = (ArrayList<String>) this.getMyController().getCustomData();
    System.out.println("zgh"+SearchTerms);
    synchronized(UrlHits){
        Iterator  <String> Site=SearchTerms.iterator();
        //for (String Keyword : SearchTerms) 
            {
            String Keyword=Site.next();
            System.out.println("Searching Keyword: " + Keyword);

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            int KeywordCounter = 0;
            String pagetext = htmlParseData.getText();
            Pattern pattern = Pattern.compile(Keyword);
            Matcher match1 = pattern.matcher(pagetext);

            if (match1.find()) {
                while (match1.find()) {
                    KeywordCounter++;
                }
                System.out.println("FOUND " + Keyword + " in page text. KeywordCount: " + KeywordCounter);

                UrlHits.add(url);
                Iterator <String> ite=UrlHits.iterator();
                while(ite.hasNext()){
                    String value=ite.next();
                    System.out.print(value+ "\n");
                    System.out.println("=============");                    
                    
                }
//                for (int i = 0; i < UrlHits.size(); i++) {
  //                  System.out.print(UrlHits.get(i) + "\n");
    //                System.out.println("=============");
      //          }

            } else {
                System.out.println("Keyword search was unsuccesful");

                System.out.println("=============");
            }

        }
    }

}
}