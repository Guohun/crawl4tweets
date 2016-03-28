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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
public class YelpAPI {
      private static final String API_HOST = "api.yelp.com";
  //private static final String DEFAULT_TERM = "dinner";
  //private static final String DEFAULT_LOCATION = "San Francisco, CA";
  private static final int SEARCH_LIMIT = 3;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";

  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */

  private static final String CONSUMER_KEY = "N6vP9RuRtxUV2vg3gkirxw";
  private static final String CONSUMER_SECRET = "dXUK1UVIe4U4jHZ8Ije04IChObw";
  private static final String TOKEN = "kDIobgBul37VZzHy9Z_KC-Pah5qG_cN3";
  private static final String TOKEN_SECRET = "K6MEkdLh2K1SlTdyC4DlDU3N8Ko";

  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   * 
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service =        new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
            .apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }
  public YelpAPI() {
      
  //  this.service =new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
    //        .apiSecret(CONSUMER_SECRET).build();
    //this.accessToken = new Token(TOKEN, TOKEN_SECRET);
  }
      
  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
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
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
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
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   * 
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
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
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  /**
   * Queries the Search API based on the command line arguments and takes the first result to query
   * the Business API.
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
 public String search(String term, double latitude, double longitude) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
   request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));    
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

public JSONObject  search4Yelp(String term, double latitude, double longitude) {
        try {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
       request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));    
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();

            boolean Begin_City = false;
            int number = 0;
            JSONObject list1 = new JSONObject();
            String input = null;
            
            BufferedReader br
                    = new BufferedReader(
                            new InputStreamReader(response.getStream()));
            
            while ((input = br.readLine()) != null) {
//                System.out.println(input);
                String[] tempS = input.split(",");
                for (int index=0;index<tempS.length;index++){
                    String [] IntempS= tempS[index].split(":");                    
                    if (IntempS.length<2) continue;
                    char [] SaveStringArry=null;
                    
                    if (IntempS[0].contains("location")&&IntempS.length>2){
                        IntempS[0]=IntempS[1];
                        SaveStringArry=IntempS[2].toCharArray();
                    }else{
                        SaveStringArry=IntempS[1].toCharArray();
                    }
                    
                    for (int i=0;i<SaveStringArry.length;i++){
                        if (SaveStringArry[i]=='\"') SaveStringArry[i]=' ';
                        else if (SaveStringArry[i]=='[') SaveStringArry[i]=' ';
                        else if (SaveStringArry[i]==']') SaveStringArry[i]=' ';
                        else if (SaveStringArry[i]=='{') SaveStringArry[i]=' ';
                        else if (SaveStringArry[i]=='}') SaveStringArry[i]=' ';
                    }
                    String SaveString=String.copyValueOf(SaveStringArry).trim();
                        
                    if (IntempS[0].contains("postal_code")){
                            list1.put("post_code", SaveString);
                            number++;
                        } else 
                            if (IntempS[0].contains("state_code")) {
                                list1.put("state_code", SaveString);
                                number++;
                            } else if (IntempS[0].contains("country_code")) {
                                list1.put("country_code", SaveString);
                                number++;
                            } else if (IntempS[0].contains("city")) {
                                list1.put("city_name", SaveString);
                                number++;
                            }else if (IntempS[0].contains("address")) {
                                list1.put("address", SaveString);
                                number++;
                            }else if (IntempS[0].contains("display_address")) {
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
 
 //https://api.foursquare.com/v2/venues/search?client_id=SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ&client_secret=DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK&v=20130815&ll=-36.4575,148.262222
 
  /**
   * Main entry for sample Yelp API requests.
   * <p>
   * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to run this example.
   */
  public static void main(String[] args) {
  //  YelpAPICLI yelpApiCli = new YelpAPICLI();
    //new JCommander(yelpApiCli, args);

      YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
      //String searchResponseJSON = yelpApi.search("city", -27.56, 152.34);
  //    String searchResponseJSON = yelpApi.search("city", -28.636765,153.637283);//-27.497835,153.017472);
      
      JSONObject searchResponseJSON = yelpApi.search4Yelp("city", -27.497835,153.017472);
        String country_code = (String) searchResponseJSON.get("country_code");
        String city_code = (String) searchResponseJSON.get("city_name");
        String state_code = (String) searchResponseJSON.get("state_code");
        String post_code = (String) searchResponseJSON.get("post_code");

        System.out.println(String.format("post_code= \"%s\" :", post_code));
        System.out.println(String.format("city_code= \"%s\" :", city_code));
        System.out.println(String.format("state_code= \"%s\" :", state_code));
        System.out.println(String.format("country_code= \"%s\" :", country_code));
        System.out.println(searchResponseJSON.toJSONString());
      
     //System.out.println(response);
/*      
        JSONParser parser = new JSONParser();
        JSONObject response=null;
        try {
              response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.err.println("Error: could not parse JSON response:");
            System.err.println(searchResponseJSON);
            System.exit(1);
        }
        
        System.out.println(response);
        JSONArray businesses = (JSONArray) response.get("businesses");
        //JSONArray businesses = (JSONArray) response.get("region");
        //JSONObject Location = (JSONObject) response.get("location");        
        JSONObject firstBusiness = (JSONObject) businesses.get(0);        
        JSONObject Location =  (JSONObject)firstBusiness.get("location");
        String country_code=(String) Location.get("country_code");
        String city_code=(String) Location.get("city");
        String state_code=(String) Location.get("state_code");
        String post_code=(String) Location.get("postal_code");
        
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
*/        
    //queryAPI(yelpApi, yelpApiCli);
  }
}
