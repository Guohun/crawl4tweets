/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author uqgzhu1
 */
public class FoursquareAPI_backup{
    private YelpAPI yelpApi = null;
    private static final String API_HOST = "api.foursquare.com";
  //private static final String DEFAULT_TERM = "dinner";
    //private static final String DEFAULT_LOCATION = "San Francisco, CA";
    private static final int SEARCH_LIMIT = 3;
    private static final String SEARCH_PATH = "/v2/venues/search";
    private static final String BUSINESS_PATH = "/v2/business";

    OAuthService service;
    Token accessToken;

    static final String FOURSQUARE_OAUTH_REQUEST_TOKEN = "http://foursquare.com/oauth/request_token";
    static final String FOURSQUARE_OAUTH_ACCESS_TOKEN = "http://foursquare.com/oauth/access_token";
    static final String FOURSQUARE_OAUTH_AUTHORIZE = "http://foursquare.com/oauth/authorize";
    private static final String CONSUMER_KEY = "SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ";
    private static final String CONSUMER_SECRET = "DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK";
    private StringBuffer G_htp = new StringBuffer("https://api.foursquare.com/v2/venues/search?client_id=SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ&client_secret=DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK&v=20130815&ll=");
    private StringBuffer Google_htp = new StringBuffer("http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=");//-26.929948,142.401002

    //https://api.foursquare.com/v2/venues/search?client_id=SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ&client_secret=DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK&v=20130815&ll=-36.4575,148.262222

    
    public FoursquareAPI_backup(String user_name, String mypassword) {
//              yelpApi = new YelpAPI();
//    this.accessToken = new Token(TOKEN, TOKEN_SECRET);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (user_name==null)
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            else 
            conn = DriverManager.getConnection(DB_URL, user_name, mypassword);                
            stmt = conn.createStatement();                
        } catch (ClassNotFoundException ex) {
            System.err.println("mysql "+ex);
        } catch (SQLException ex) {
            System.err.println("stmt "+ex);
        }              
    }
    

    /**
     * Creates and sends a request to the Search API by term and location.
     * <p>
     * See
     * <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp
     * Search API V2</a>
     * for more info.
     *
     * @param term <tt>String</tt> of the search term to be queried
     * @param location <tt>String</tt> of the location
     * @return <tt>String</tt> JSON Response
     */
    public String searchForBusinessesByLocation(String term, String location) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p>
     * See
     * <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp
     * Business API V2</a>
     * for more info.
     *
     * @param businessID <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint
     * specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
//    this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Queries the Search API based on the command line arguments and takes the
     * first result to query the Business API.
     *
     * @param yelpApi <tt>YelpAPI</tt> service instance
     * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
     */
    /*
     private static void queryAPI(YelpAPI yelpApi, YelpAPICLI yelpApiCli) {
    
     String searchResponseJSON =
     yelpApi.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location);
        
     JSONParser parser = new JSONParser();
     JSONObject response = null;
     try {
     response = (JSONObject) parser.parse(searchResponseJSON);
     } catch (ParseException pe) {
     System.out.println("Error: could not parse JSON response:");
     System.out.println(searchResponseJSON);
     System.exit(1);
     }
        
     JSONArray businesses = (JSONArray) response.get("businesses");
     JSONObject firstBusiness = (JSONObject) businesses.get(0);
     String firstBusinessID = firstBusiness.get("id").toString();
     System.out.println(String.format(
     "%s businesses found, querying business info for the top result \"%s\" ...",
     businesses.size(), firstBusinessID));
        
     // Select the first business and display business details
     String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
     System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
     System.out.println(businessResponseJSON);
    
     }
     */
    /**
     * Command-line interface for the sample Yelp API runner.
     */
    /*private static class YelpAPICLI {
     //@Parameter(names = {"-q", "--term"}, description = "Search Query Term")
     //public String term = DEFAULT_TERM;
     //http://api.yelp.com/v2/search?term=german+food&location=Hayes&cll=37.77493,-122.419415
     //cll=latitude,longitude
     //@Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
     @Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
     public String location = DEFAULT_LOCATION;
     }
     */
  //http://maps.googleapis.com/maps/api/geocode/json?latlng=-9.56189,113.15437&sensor=true
    //http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=-26.929948,142.401002
    public JSONObject searchGoogleMap(String term, double latitude, double longitude) {
        try {
            //OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.foursquare.com/v2/venues/search");
            int len = Google_htp.length();
            Google_htp.append(latitude);
            Google_htp.append(',');
            Google_htp.append(longitude);
            int len1 = Google_htp.length();
            //String contents = java.net.URL();
            URL url = new URL(Google_htp.toString());
            Google_htp.delete(len, len1);
            URLConnection con = url.openConnection();

            BufferedReader br
                    = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));

            String input = null;
            boolean Begin_City = false;
            int number = 0;
            JSONObject list1 = new JSONObject();
            StringBuffer SaveString = new StringBuffer();
            while ((input = br.readLine()) != null) {                
                String[] tempS = input.split(":");
                if (Begin_City) {                    
                    if (tempS[0].contains("short_name")) {
                        SaveString.append(tempS[1].trim());
                    }
                    if (tempS[0].contains("types")) {
                        String tempType = tempS[1].trim();
                        if (tempType.length() < 3) {
                            if (tempS.length < 3) {
                                SaveString.setLength(0);
                                continue;
                            }
                            tempType = tempS[2].trim();
                        }
                        int loc = 1;
                        do {
                            loc = -1;
                            for (int i = 0; i < SaveString.length(); i++) 
                                switch (SaveString.charAt(i)) {
                                    case '\"':
                                    case ',':
                                    case ':':
                                        loc = i;
                                        break;
                                }
                            
                            if (loc >= 0)                                 SaveString.delete(loc, loc + 1);                            
                        } while (loc >= 0);
                        if (tempType.contains("postal_code")) {
                            list1.put("post_code", SaveString.toString());
                            number++;
                        } else 
                            if (tempType.contains("administrative_area_level_1")) {
                                list1.put("state_code", SaveString.toString());
                                number++;
                            } else if (tempType.contains("country")) {
                                list1.put("country_code", SaveString.toString());
                                number++;
                            } else if (tempType.contains("locality")) {
                                list1.put("city_name", SaveString.toString());
                                number++;
                            }
                //              else
                            //              System.out.println(tempType+":"+SaveString.toString());
                        SaveString.setLength(0);
                    }
                }
                if (tempS[0].contains("address_components")) {
                    if (Begin_City) break;
                    Begin_City = true;
                }
            }
            br.close();

            return list1;
        } catch (IOException ex) {
            Logger.getLogger(FoursquareAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public JSONObject  search4Square(String term, double latitude, double longitude) {
        try {
            //OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.foursquare.com/v2/venues/search");
            int len = G_htp.length();
            G_htp.append(latitude);
            G_htp.append(',');
            G_htp.append(longitude);
            int len1 = G_htp.length();
            //String contents = java.net.URL();
            URL url = new URL(G_htp.toString());
            G_htp.delete(len, len1);
            URLConnection con = url.openConnection();
              //SSLException thrown here if server certificate is invalid

            BufferedReader br
                    = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
            boolean Begin_City = false;
            int number = 0;
            JSONObject list1 = new JSONObject();
            String input = null;
            while ((input = br.readLine()) != null) {
//                System.out.println(input);
                String[] tempS = input.split(",");
                for (int index=0;index<tempS.length;index++){
                    String [] IntempS= tempS[index].split(":");                    
                    if (IntempS.length<2) continue;
                    char [] SaveStringArry=IntempS[1].toCharArray();
                    for (int i=0;i<SaveStringArry.length;i++){
                        if (SaveStringArry[i]=='\"') SaveStringArry[i]=' ';
                        else if (SaveStringArry[i]=='[') SaveStringArry[i]=' ';
                        if (SaveStringArry[i]==']') SaveStringArry[i]=' ';
                    }
                    String SaveString=String.copyValueOf(SaveStringArry).trim();
                    
                    if (IntempS[0].contains("postalCode")){
                            list1.put("post_code", SaveString);
                            number++;
                        } else 
                            if (IntempS[0].contains("state")) {
                                list1.put("state_code", SaveString);
                                number++;
                            } else if (IntempS[0].contains("cc")) {
                                list1.put("country_code", SaveString);
                                number++;
                            } else if (IntempS[0].contains("city")) {
                                list1.put("city_name", SaveString);
                                number++;
                            }else if (IntempS[0].contains("formattedAddress")) {
                                list1.put("address", SaveString);
                                number++;
                            }else if (IntempS[0].contains("name")) {
                                list1.put("place_name", SaveString);
                                number++;
                            }
                                    

                if (number>5) break;
                }
            }
            br.close();

            return list1;
        } catch (IOException ex) {
            Logger.getLogger(FoursquareAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String search(String term, double latitude, double longitude) {
        try {
            //OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.foursquare.com/v2/venues/search");
            int len = G_htp.length();
            G_htp.append(latitude);
            G_htp.append(',');
            G_htp.append(longitude);
            int len1 = G_htp.length();
            //String contents = java.net.URL();
            URL url = new URL(G_htp.toString());
            G_htp.delete(len, len1);
            URLConnection con = url.openConnection();
              //SSLException thrown here if server certificate is invalid

            BufferedReader br
                    = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));

            StringBuffer inputBuffer = new StringBuffer();
            String input = null;
            while ((input = br.readLine()) != null) {
                inputBuffer.append(input);
                //System.out.println(input);
            }
            br.close();
              //request.addQuerystringParameter("v", "20130815");
            //request.addQuerystringParameter("ll", latitude + "," + longitude);
            //request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
//    this.service.signRequest(this.accessToken, request);
            //Response response = request.send();

            return inputBuffer.toString();//response.getBody();
        } catch (IOException ex) {
            Logger.getLogger(FoursquareAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

 //
    /**
     * Main entry for sample Yelp API requests.
     * <p>
     * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to
     * run this example.
     */
    public static void main(String[] args) {
  //  YelpAPICLI yelpApiCli = new YelpAPICLI();
        //new JCommander(yelpApiCli, args);

        FoursquareAPI_backup yelpApi = new FoursquareAPI_backup(null,null);
      //String searchResponseJSON = yelpApi.search("city", -27.56, 152.34);
        //String searchResponseJSON1 = yelpApi.search("city", -28.636765,153.637283);//-27.497835,153.017472);
        //String searchResponseJSON = yelpApi.search("city", -27.497835,153.017472);//);      
//        JSONObject searchResponseJSON1 = yelpApi.getAuStateCity(-27.497835,153.017472);//);      
        //JSONObject searchResponseJSON = yelpApi.getAuStateCity(-28.636765,153.637283);//);      
        //JSONObject searchResponseJSON = yelpApi.getAuStateCity(0.0,0.0);//);
        JSONObject searchResponseJSON = yelpApi.getAuStateCity(57.0,-1.0);//);
        //JSONObject searchResponseJSON = yelpApi.getAuStateCity(44.636765,-75.637283);//);      
//        JSONObject searchResponseJSON3 = yelpApi.getAuStateCity(39.78225, -84.364132);//);      
  //      JSONObject searchResponseJSON4 = yelpApi.getAuStateCity(-5.899762, 144.264132);//);      
        yelpApi.Close();
        //JSONObject searchResponseJSON = yelpApi.searchGoogleMap("city", -5.899762, 144.264132);//);                    
        //System.out.println(response);
/*        JSONParser parser = new JSONParser();
         JSONObject response=null;
         try {
         response = (JSONObject) parser.parse(searchResponseJSON);
         } catch (ParseException pe) {
         System.err.println("Error: could not parse JSON response:");
         System.err.println(searchResponseJSON);
         System.exit(1);
         }
        
         JSONObject temp= (JSONObject) response.get("response");
         //        for (    Object key : temp.keySet()) {
         //            System.out.println((String)key +":"+temp.get(key));
         //          System.out.println("---------------");
         //      }
        
         JSONArray businesses = (JSONArray) temp.get("venues");
         JSONObject firstBusiness = (JSONObject) businesses.get(0);        
         JSONObject Location =  (JSONObject)firstBusiness.get("location");
         */

        String country_code = (String) searchResponseJSON.get("country_code");
        String city_code = (String) searchResponseJSON.get("city_name");
        String state_code = (String) searchResponseJSON.get("state_code");
        String post_code = (String) searchResponseJSON.get("post_code");

        //String firstBusinessID = firstBusiness.get("id").toString();        
        //System.out.println(String.format(
        //      "%s businesses found, querying business info for the top result \"%s\" ...",
        //    businesses.size(), firstBusinessID));
        // Select the first business and display business details
    //    System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
  //      String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());        
//        System.out.println(businessResponseJSON);
        //String loudScreaming = firstBusiness.getJSONObject("LabelData").getString("slogan");    
        //JSONObject json = new JSONObject(businessResponseJSON);
        System.out.println(String.format("post_code= \"%s\" :", post_code));
        System.out.println(String.format("city_code= \"%s\" :", city_code));
        System.out.println(String.format("state_code= \"%s\" :", state_code));
        System.out.println(String.format("country_code= \"%s\" :", country_code));
        System.out.println(searchResponseJSON.toJSONString());
        //queryAPI(yelpApi, yelpApiCli);
    }
    public JSONObject getPostCodeByGeo(Double v_Latitude, Double v_Longtitude) {
        boolean searchAgain = false;
        JSONObject businesses = null;
        if (v_Latitude == null) {
            return null;
        }
//        businesses = yelpApi.search4Yelp("city", v_Latitude, v_Longtitude);//-27.497835,153.017472);                      

        if (businesses == null) {
            searchAgain = true;
        } else if (businesses.size() < 1) {
            searchAgain = true;
        }
        if (searchAgain) 
        {           
            businesses = search4Square("city", v_Latitude, v_Longtitude);
            searchAgain = false;
        }
        if (businesses == null) {
            searchAgain = true;
        } else if (businesses.size() < 1) {
            searchAgain = true;
        }
        if (searchAgain) {
            searchAgain = false;
            System.out.println("La:" + v_Latitude + "\tLo:" + v_Longtitude);
            businesses = searchGoogleMap("city", v_Latitude, v_Longtitude);
            if (businesses == null) {
                //                                System.out.println("\t" + v_tweet_Country_Code + "\t:" + v_tweet_Place_Name);
                searchAgain = true;
            } else if (businesses.size() < 1) {
                //System.out.println("\t" + v_tweet_Country_Code + "\t:" + v_tweet_Place_Name);
                searchAgain = true;
            }
        }

        return businesses;
    }
        public JSONObject getAuStateCity(Double v_Latitude, Double v_Longtitude){
            int BaseNum=10;
        try {
            ResultSet rs=null;
            double normal_La=v_Latitude.doubleValue();
            double normal_Lo=v_Longtitude.doubleValue();
            if (normal_La>=-7.3&&(normal_La!=0&&v_Longtitude!=0)) {                
                if (normal_La>49) return getUKStateCity(v_Latitude,v_Longtitude);                
                return getUSAStateCity(v_Latitude,v_Longtitude);
            }
            while(true)
            {
                
                double small_La=((int)(normal_La*BaseNum))/(double)BaseNum;                                        
                double small_Lo=((int)(normal_Lo*BaseNum))/(double)BaseNum;

                String sql="select postcode,suburb,state,latitude,longitude from postcodes_geo where latitude<="+ Double.toString(small_La) + " and latitude>="+normal_La +" and longitude<=" +normal_Lo+ "  and longitude>="+ Double.toString(small_Lo);            
               // System.err.println(sql);
                rs = stmt.executeQuery(sql);            
                if (rs.next()) break;
                BaseNum/=10;
                if (BaseNum==0) break;
                rs.close();
            }
            if (BaseNum<0.01) {
              //  System.err.println("geoDB did not find");
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }
            
            
            String returnStr[]=new String [2];
            double  Max_dis=Double.MAX_VALUE;
            String country_code = "AU";
            String city_code = null;
            String state_code = null;
            String post_code = null;            
            do {
                //Retrieve by column name
                double x = rs.getDouble("latitude");
                double y= rs.getDouble("longitude");
                double dis=Math.sqrt( (x-normal_La)*(x-normal_La)+(y-normal_Lo)*(y-normal_Lo));
                if (dis<Max_dis){
                    Max_dis=dis;                    
                    city_code = rs.getString("suburb");
                    state_code = rs.getString("state");
                    post_code = rs.getString("postcode");                    
                }
            }while (rs.next());
            
            if (Max_dis>100){
                System.err.println("It is impossible so distance, another country"+Max_dis);
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }            
            rs.close();
            
            JSONObject businesses = new JSONObject();
            businesses.put("country_code", "AU");
            businesses.put("city_name", state_code);
            businesses.put("state_code", city_code);
            businesses.put("post_code", post_code);            
                    
            return businesses;
            
        } catch (SQLException ex) {
            System.err.println("MySQL:getAuStateCity: "+ex);
        }
        return null;
        
    }
        public JSONObject getUSAStateCity(Double v_Latitude, Double v_Longtitude){
            int BaseNum=10;
        try {
            ResultSet rs=null;
            double normal_La=v_Latitude.doubleValue();
            double normal_Lo=v_Longtitude.doubleValue();
            while(true)
            {
                
                double small_La=((int)(normal_La*BaseNum))/(double)BaseNum;                                        
                double small_Lo=((int)(normal_Lo*BaseNum))/(double)BaseNum;

                String sql="select ZIP,City,State,Latitude,Longitude from zipcodes where Latitude>="+ Double.toString(small_La) + " and Latitude<="+normal_La +" and Longitude>=" +normal_Lo+ "  and Longitude<="+ Double.toString(small_Lo);            
           //     System.err.println(sql);
                rs = stmt.executeQuery(sql);            
                if (rs.next()) break;
                BaseNum/=10;
                if (BaseNum==0) break;
                rs.close();
            }
            if (BaseNum<0.01) {
             //   System.err.println("geoDB did not find");
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }
            
            
            String returnStr[]=new String [2];
            double  Max_dis=Double.MAX_VALUE;
            String country_code = "USA";
            String city_code = null;
            String state_code = null;
            String post_code = null;            
            do {
                //Retrieve by column name
                double x = rs.getDouble("Latitude");
                double y= rs.getDouble("Longitude");
                double dis=Math.sqrt( (x-normal_La)*(x-normal_La)+(y-normal_Lo)*(y-normal_Lo));
                if (dis<Max_dis){
                    Max_dis=dis;                    
                    city_code = rs.getString("City");
                    state_code = rs.getString("State");
                    post_code = rs.getString("ZIP");                    
                }
            }while (rs.next());
            
            if (Max_dis>100){
                System.err.println("It is impossible so distance, another country"+Max_dis);
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }            
            rs.close();
            
            JSONObject businesses = new JSONObject();
            businesses.put("country_code", country_code);
            businesses.put("city_name", state_code);
            businesses.put("state_code", city_code);
            businesses.put("post_code", post_code);            
                    
            return businesses;
            
        } catch (SQLException ex) {
            System.err.println("MySQL:getUSAtateCity: "+ex);
        }
        return null;
        
    }
        public JSONObject getUKStateCity(Double v_Latitude, Double v_Longtitude){
            int BaseNum=10;
        try {
            ResultSet rs=null;
            double normal_La=v_Latitude.doubleValue();
            double normal_Lo=v_Longtitude.doubleValue();
            while(true)
            {
                
                double small_La=((int)(normal_La*BaseNum))/(double)BaseNum;                                        
                double small_Lo=((int)(normal_Lo*BaseNum))/(double)BaseNum;
                if (small_La==normal_La) {small_La--; normal_La++;}
                if (small_Lo==normal_Lo) {small_Lo++; normal_Lo--;}
                String sql="select postcode,latitude,longitude, town, region, country from uk_postcode_05 where latitude>="+ Double.toString(small_La) + " and Latitude<="+normal_La +" and longitude>=" +normal_Lo+ "  and longitude<="+ Double.toString(small_Lo);            
                System.err.println(sql);
                rs = stmt.executeQuery(sql);            
                if (rs.next()) break;
                BaseNum/=10;
                if (BaseNum==0) break;
                rs.close();
            }
            if (BaseNum<0.01) {
             //   System.err.println("geoDB did not find");
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }
            
            
            String returnStr[]=new String [2];
            double  Max_dis=Double.MAX_VALUE;
            String country_code =null;
            String city_code = null;
            String state_code = null;
            String post_code = null;            
            do {
                //Retrieve by column name
                double x = rs.getDouble("latitude");
                double y= rs.getDouble("longitude");
                double dis=Math.sqrt( (x-normal_La)*(x-normal_La)+(y-normal_Lo)*(y-normal_Lo));
                if (dis<Max_dis){
                    Max_dis=dis;                    
                    city_code = rs.getString("town");
                    state_code = rs.getString("region");
                    post_code = rs.getString("postcode");                    
                    country_code=rs.getString("country");
                }
            }while (rs.next());
            
            if (Max_dis>100){
                System.err.println("It is impossible so distance, another country"+Max_dis);
                return getPostCodeByGeo(v_Latitude, v_Longtitude);
            }            
            rs.close();
            
            JSONObject businesses = new JSONObject();
            businesses.put("country_code", country_code);
            businesses.put("city_name", state_code);
            businesses.put("state_code", city_code);
            businesses.put("post_code", post_code);            
                    
            return businesses;
            
        } catch (SQLException ex) {
            System.err.println("MySQL:getUKtateCity: "+ex);
        }
        return null;
        
    }
        
     public boolean Close(){
        try {
                if (stmt != null) {
                    stmt.close();
                }
            
            
        } catch (SQLException ex) {
            System.err.println("mysql close "+ex);
            return false;
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                return false;
            }//end finally try            
        }
        return true;
     }
     
        
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://10.33.0.161/twitter";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/twitter";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "1234321";
    
    private    Connection conn = null;
    private    Statement stmt = null;

    public JSONObject getAuStateCity(String post_code) {
            int BaseNum=10;
        try {
            ResultSet rs=null;
                
            String country_code = "AU";
            String city_code = null;
            String state_code = null;
            
            String sql="select postcode,suburb,state,latitude,longitude from postcodes_geo where postcode="+post_code;            
            //System.err.println(sql);
            rs = stmt.executeQuery(sql);            
            if(rs.next()) {
                    city_code = rs.getString("suburb");
                    state_code = rs.getString("state");
                    post_code = rs.getString("postcode");                    
            };
            
            rs.close();
            
            JSONObject businesses = new JSONObject();
            businesses.put("country_code", "AU");
            businesses.put("city_name", state_code);
            businesses.put("state_code", city_code);
            businesses.put("post_code", post_code);            
                    
            return businesses;
            
        } catch (SQLException ex) {
            System.err.println("MySQL:getAuStateCity: "+ex);
        }
        return null;
        
    }

}
