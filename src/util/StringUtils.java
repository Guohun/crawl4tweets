package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StringUtils {
	static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1
	  
	public static boolean isPureAscii(String v) {
		return asciiEncoder.canEncode(v);
	}
        
        public static String getHttpLinkStr(String inputStr){
            char []tempChar=inputStr.toCharArray();
            int i=0;
            if (tempChar.length>13){                
                for (i=0;i<tempChar.length;i++){
                     if (tempChar[i]=='h')
                         if (tempChar[i+1]=='r')
                             if (tempChar[i+2]=='e')
                                 if (tempChar[i+3]=='f')
                                     if (tempChar[i+4]=='=')
                                          break;
                }
                int x=i+6 ;           
                for (i=x;i<tempChar.length;i++){
                     if (tempChar[i]=='\"')
                         if (tempChar[i+1]=='\t'||tempChar[i+1]==' ')
                             if (tempChar[i+2]=='r')
                                 if (tempChar[i+3]=='e')
                                     if (tempChar[i+4]=='l')
                                             if (tempChar[i+5]=='=')
                                                    if (tempChar[i+6]=='\"')                                             
                                          break;
                }                
                return inputStr.substring(x, i);
            }else return null;
            
        }
                
	public static String removeString(String orgStr,String removeStr) {
		int start = 0;
		int end = 0;
		
		start = orgStr.indexOf(removeStr);
		while (start >= 0) {
			end = orgStr.indexOf(" ",start);
			if (end >=0)
				orgStr = orgStr.substring(0,start) + orgStr.substring(end +1);
			else
				orgStr = orgStr.substring(0,start);
			start = orgStr.indexOf(removeStr);
		}
		return orgStr;
	}
	
	public static String cleanMassage(String post) {
		post = removeString(post, "http://");
		post = removeString(post, "@");
		post = removeString(post, "RT");
		post = post.replaceAll("#","");
		return post;
	}
	
	public static boolean isEnglishLanguage(String post) {
		boolean result = false;
		try {
			URI uri = new URI("http","ajax.googleapis.com","/ajax/services/language/detect","v=1.0&q="+post,null);
			URL url = uri.toURL();
			URLConnection connection = url.openConnection();
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			
			Writer writer = new StringWriter();
			if (isr != null) {
				char[] buffer = new char[1024];
				try {
					Reader reader = new BufferedReader(isr);
					int n;
					while ((n = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, n);
					}
				} finally {
					isr.close();
				}
			}
			String txt = writer.toString();
			txt = txt.replace("\"", "");
			if (txt.indexOf("language:en") >= 0 ) {
				result = true;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}      				
		return result;
	}
	
}
