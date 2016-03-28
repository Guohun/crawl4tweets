/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rebootflashservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 *
 * @author uqgzhu1
 */
public class Rebootflashservices {

      //static String ServerStr = "http://flashlite.rcc.uq.edu.au:8080/";
    static String ServerStr = "http://localhost:9999";
    //static String ServerStr = "http://localhost:8080";
    static String Address[] = {
        "/rootRest/resources/rootSearch?DefKey=root&DefUni=UQ&subKey=",
        "/negativeRest/resources/negativeSearch?DefKey=negative&DefUni=UQ&subKey=",
        "/researchRest/resources/researchSearch?DefKey=study&DefUni=UQ&subKey=",
        "/studyRest/resources/studySearch?DefKey=study&DefUni=UQ&subKey=",     
        "/teachingRest/resources/teachingSearch?DefKey=teaching&DefUni=UQ&subKey=",
        "/livingRest/resources/livingSearch?DefKey=living&DefUni=UQ&subKey=",
        "/safetyRest/resources/safetySearch?DefKey=safety&DefUni=UQ&subKey=", 
        "/postiveRest/resources/postiveSearch?DefKey=postive&DefUni=UQ&subKey="
    };
   static char Useit[]={'R','N','r','S','T','L','a','P'};
    /**
             * @param args the command line arguments
             */

   
   
    public static void main(String[] args) {
        BufferedReader br = null;

        try {
            // TODO code application logic here
            //System.out.println("Please make MongoDB  running at first correctly!");
            
            DefaultHttpClient httpClient = new DefaultHttpClient();
            for (int i = 0; i < Address.length; i++) {
                
                if (args.length>0){  
                    char inputP=args[0].charAt(0);
                    if (inputP!=Useit[i]) continue;
                
                }
                
                String querystr = ServerStr + Address[i]+"UQ";
                System.out.println(querystr);
                HttpGet getRequest = new HttpGet(querystr);
                getRequest.addHeader("accept", "text/html");
                HttpResponse response1 = httpClient.execute(getRequest);
                if (response1.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response1.getStatusLine().getStatusCode());
                }
                br = new BufferedReader(new InputStreamReader(
                        (response1.getEntity().getContent())));
                String output;
                output = br.readLine();
                if (output == null) {
                    System.err.println(Address[i]+"\t failure");
                }else{
                    output=output.trim();
                    if (output.isEmpty())
                        System.err.println(Address[i]+"\t failure");
                }
                br.close();
            }

            httpClient.getConnectionManager().shutdown();
        } catch (IOException ex) {
            Logger.getLogger(Rebootflashservices.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IllegalStateException ex) {
            Logger.getLogger(Rebootflashservices.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } finally {
            try {
                if (br!=null)
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Rebootflashservices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Reboot Flashlite Services Successful!");
    }

}
