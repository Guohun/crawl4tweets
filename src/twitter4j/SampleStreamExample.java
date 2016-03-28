/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;

import javax.ws.rs.core.MultivaluedHashMap;
        
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import twitter4j.api.TrendsResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class SampleStreamExample {

  public static void run(String consumerKey, String consumerSecret, String token, String secret) throws InterruptedException {
    // Create an appropriately sized blocking queue
    BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

    // Define our endpoint: By default, delimited=length is set (we need this for our processor)
    // and stall warnings are on.
    StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
    endpoint.stallWarnings(false);

    Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
    //Authentication auth = new com.twitter.hbc.httpclient.auth.BasicAuth(username, password);
    System.out.println(Constants.STREAM_HOST);
    
    
    Twitter twitter = TwitterFactory.getSingleton();
    Query query = new Query("source:twitter4j yusukey");
    QueryResult result=null;
      try {
          result = twitter.search(query);
      } catch (TwitterException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
      }
    for (Status status : result.getTweets()) {
        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
    }    
    
    // Create a new BasicClient. By default gzip is enabled.    
    BasicClient client = new ClientBuilder()
            .name("UQ")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(auth)
            .processor(new StringDelimitedProcessor(queue))
            .build();

    // Establish a connection
    client.connect();

    // Do whatever needs to be done with messages
    for (int msgRead = 0; msgRead < 1000; msgRead++) {
      if (client.isDone()) {
        System.out.println("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
        break;
      }

      String msg = queue.poll(5, TimeUnit.SECONDS);
      if (msg == null) {
        System.out.println("Did not receive a message in 5 seconds");
      } else {
        System.out.println(msg);
      }
    }

    client.stop();

    // Print some stats
    System.out.printf("The client read %d messages!\n", client.getStatsTracker().getNumMessages());
  }

  public static void main(String[] args) {
        
      try {
          final String api_key="JYzKS06JDEbYQVs7bBqc1Lmja";
          final String api_secret="LMF5NxW9T5A5vtm4mAEpVNg2HZkWN6dCZxdo3BMEoVS6Zjp6P4";
          final String access_token="3171875106-pRNzG2CRpsRjuTblFbDoz6FIev2sZje2BUu8vEw";
          final String  access_token_secret="iFDFi9xWpq1tFWG0EOxWaNXRvVA3TvQDvRrKPP2S4o3TJ";
          
          // The factory instance is re-useable and thread safe.
          Twitter twitter = TwitterFactory.getSingleton();

          AccessToken accessToken = loadAccessToken(1);
          
          //twitter.setOAuthConsumerKey("[consumer key]", "[consumer secret]");
          twitter.setOAuthConsumer(api_key, api_secret);
          twitter.setOAuthAccessToken(accessToken);
          
          
  /*        
    List<Status> statuses = twitter.getHomeTimeline();
    System.out.println("Showing home timeline.");
    for (Status status : statuses) {
        System.out.println(status.getUser().getName() + ":" +
                           status.getText());
    }     
    System.exit(0);
*/    
          /*    Status status;
          try {
          status = twitter.updateStatus("Yea Tao Massage");
          System.out.println("Successfully updated the status to [" + status.getText() + "].");
          } catch (TwitterException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
          }
          */
          
           
    
//        Status status1 = twitter.showStatus(Long.parseLong("596549919961415680"));//598047680942592001
  //      if (status1 == null) { // 
    //        // don't know if needed - T4J docs are very bad            
     //   } else {
       //     System.out.println("@" + status1.getUser().getScreenName()+":"+status1.getRetweetCount()
         //               + " - " + status1.getText());
        //}          
          //
          //I should tell one of my friends, she always talking with mobile than half hr
        //Why is everyone ranking the iPhone 1 as the best? Respond on Medium with your ranking
          
        Query query = new Query(" UQ"); //Query("Bus");"I love UQ"
        
        double latitude=-27.471;
        double longitude=153.0234;
        double radius=9000;
          GeoLocation location = new GeoLocation(latitude, longitude);
            Query.Unit unit = Query.MILES; // or Query.MILES;
            
        //    query.setGeoCode(location, radius, unit);
            //query.setSince("2015-05-01");
            //query.setUntil("2015-05-08");
          //  query.setLang("en");
//            query.setCount(100); //setRpp(100);
  //        QueryResult result = twitter.search(query);
          
//#darwinnt
    //    ResponseList<Location> locations;
    //    locations = twitter.getAvailableTrends();
       // System.out.println("Showing available trends");
        //for (Location mylocation : locations) {
//            System.out.println(mylocation.getName() + " (woeid:" + mylocation.getWoeid() + ")");
  //      }
/*        Trends trends = twitter.getPlaceTrends(1100661);
        for (int i = 0; i < trends.getTrends().length; i++) {
            System.out.print(trends.getTrends()[i].getName()+"\t");
            System.out.print(trends.getTrends()[i].getQuery()+"\t");
            System.out.println(trends.getTrends()[i].getURL());
        }

        System.out.println();
*/
        //System.exit(0);
        
    //       List<Status> tweets = result.getTweets();
          
                            
          //for (Status status : tweets)            118440437013950465
            //ResponseList<Location> tempP=twitter.trends().getAvailableTrends();
            
 
        

  /*      
        TrendsResources trendsRes = twitter.trends();    
	int remainingCalls = twitter.getRateLimitStatus("trends")
					.get("/trends/place").getRemaining();
        //ResponseList<Location> trendLocations = trendsRes.getAvailableTrends();
        GeoLocation temp=new GeoLocation(-27.497835,153.017472);
        ResponseList<Location> trendLocations = trendsRes.getClosestTrends(temp);   //getAvailableTrends(temp);
            for (Location tempL: trendLocations){
                int myid=tempL.getWoeid();
                System.out.print(myid+"\t");
                Trends tempTSet = twitter.trends().getPlaceTrends(myid);                
                for (Trend trendLocation: tempTSet.getTrends()){
                    System.out.print(trendLocation.getName()+"\t"+trendLocation.getQuery());
                        while (remainingCalls <= 0) {
					int resetTime = twitter.getRateLimitStatus("trends")
							.get("/trends/place").getSecondsUntilReset();
					Thread.sleep((resetTime + 5) * 1000);
					remainingCalls = twitter.getRateLimitStatus("trends")
							.get("/trends/place").getRemaining();

				}
                }
                Trends trends = trendsRes.getPlaceTrends(myid);
                remainingCalls--;
                //for (Trend t : trends.getTrends()) sharedQueue.put(t);                    
                
                System.out.println();
            }
            //Place tempP=twitter.getGeoDetails("00c39537733fa112");
        //    System.out.println(tempP);
            System.exit(0);
    */
            User myuser =twitter.showUser("Resilience_USQ");
            System.out.println("id="+myuser.getId());
             
             int numstatus=myuser.getStatusesCount();
             System.out.println("number="+numstatus);
             Status status =myuser.getStatus();
           //long tempL=Long.parseLong("522568892817080322");   //596480588099547136
           //Status status =twitter.showStatus(tempL);           
             
          {
               System.out.print("Name="+status.getUser().getScreenName());
               System.out.print("ID="+status.getUser().getId());
               String temp2=status.getSource();
               String pattern = "(?i)(<a href=)(.+?)(/\" rel)";
                String updated = temp2.replaceAll(pattern, "$2"); 


               System.out.print("source="+updated+"\t");
               
                if (status.isFavorited())  System.out.print("**");                                              
                Status temp1=status.getRetweetedStatus();
                if (temp1!=null){
                    System.out.println("\tReT:"+temp1.getUser().getScreenName()+":"+temp1.getUser().getId());                    
                }                                    
                Long ReplayTwitter=status.getInReplyToStatusId();
                if (ReplayTwitter>0){
                        Long ReplayUserID=status.getInReplyToUserId();
                        String ReplayScreenName=status.getInReplyToScreenName();
                        System.out.print(status.getId());
                        System.out.println("\tRePlay to: "+ReplayUserID+"   ->"+ReplayScreenName);
                }
                UserMentionEntity[] mentioned = status.getUserMentionEntities();
//                for( UserMentionEntity x: mentioned){
  //                  System.out.println("\tRep--> " + x.getScreenName()+":"+x.getId());                    
    //            }
                
                //System.out.print("      ");
//                Place T_Place=status.getPlace();
  //              if (T_Place!=null)
    //                System.out.print("Loc:"+ status.getPlace());
                
                //twitter.getPlaceTrends(i)
                
                System.out.print("\tname:"+ status.getUser().getName());
                URLEntity[] tempHash=status.getURLEntities();
                for (URLEntity tempx: tempHash)
                    System.out.print(tempx+";");
                
                System.out.print("\tG:"+status.getGeoLocation());
                System.out.print("\tSC:"+ status.getScopes());                
                Place  myplace=status.getPlace();
                if (myplace!=null){
                    System.out.print("\tP:"+myplace.getId());
                    System.out.print("\tP:"+myplace.getCountry());
                    System.out.print("\tP:"+myplace.getCountryCode());                
                    System.out.print("\tPT:"+myplace.getPlaceType());                
                    System.out.print("\tP:"+myplace.getName());                
                    System.out.print("\tP:"+myplace.getURL());                
                    System.out.print("\tS:"+myplace.getStreetAddress());                                
                }
  
                
/*                String tempDate=status.getCreatedAt().toGMTString();
                SimpleDateFormat sdfSource = new  SimpleDateFormat("dd MMM yyyy HH:mm:ss 'GMT'");                
//                sdfSource.setTimeZone(TimeZone.class.get ("GMT"));SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); //
                Date mydate = sdfSource.parse(tempDate);
                System.out.print("\nG_Year:\t"+ (1900+mydate.getYear()));
                System.out.print("G_Month:\t"+ mydate.getMonth());
                System.out.print("G_Day:"+ mydate.getDate());
                System.out.print("G_Day:"+ mydate.getDay());
                System.out.print("G_Hour:"+ mydate.getHours());
                System.out.print("G_Minute:"+ mydate.getMinutes());
                System.out.print("G_Second:"+ mydate.getSeconds());
                System.out.print("\tG_utcoffset:\t"+ mydate.getTimezoneOffset());
                System.out.print("\tUT:"+status.getCreatedAt());                
                System.out.println("\tT:"+status.getUser().getTimeZone());
/*                String []tempS=status.getWithheldInCountries();
                if (tempS!=null) for (String Cs: tempS) System.out.print("\t"+Cs);
                System.out.println("\tLT:"+status.getLang());
                System.out.println("\tLU:"+status.getUser().getLang());
                System.out.println("\tCU:"+status.getUser().getCreatedAt());
                System.out.println("\tDE:"+status.getUser().getDescription());
                System.out.println("\tImageURL:"+status.getUser().getProfileImageURL());
                System.out.println("\tURL:"+status.getUser().getURL());
                System.out.println("\tPO:"+status.getUser().isProtected());
                System.out.println("\tGE:"+status.getUser().isGeoEnabled());
                System.out.println("\tVE:"+status.getUser().isVerified());                
                System.out.println("\tF:"+status.getUser().getFriendsCount());                
                System.out.println("\tT:"+status.getUser().getStatusesCount());                                
*/                   
                
                System.out.println("\t:TX:"+ status.getText());
                
  //              System.out.print("\tUsertimeZone"+ status.getUser().getTimeZone());
    //            System.out.print("\tUserStatus"+status.getUser().getStatus());
      //          System.out.print("\tuserLocation"+ status.getUser().getLocation());
        //        System.out.print("\tnumber_of_rt"+ status.getRetweetCount());
          //      System.out.print("\tisRetweet"+ status.isRetweet()+":"+ status.isRetweeted());
            //    System.out.println("\tPOS"+ status.getWithheldInCountries());


/*              if (status.isRetweeted()){
                  
                  System.out.print("Re:");
              }
*/            
//              GeoLocation  loc=status.getGeoLocation();
  //            if (loc!=null) 
    //              System.out.print(loc.getLatitude()+","+loc.getLongitude()+":");              
              
              //Place myPlace=status.getPlace();
              //if (myPlace!=null) 
                //  System.out.print(myPlace.getName()+":");              
              //System.out.print(status.getSource()+":");              status.getCreatedAt()
//              System.out.println(":@" + status.getUser().getScreenName() + ":" + status.getText());
/*              try{
                long cursor = -1;
                PagableResponseList<User> followers;
                do {
                     followers = twitter.getFollowersList(status.getUser().getScreenName(), cursor);
                    for (User follower : followers) {
                        // TODO: Collect top 10 followers here
                        System.out.println("--->"+follower.getName() + " has " + follower.getFollowersCount() + " follower(s)");
                    }
                } while ((cursor = followers.getNextCursor()) != 0);              
              }catch(TwitterException e){
                 System.err.println("------------------");
              }
*/              
          }
                           System.err.println("------------------");
                           System.exit(0);
                           
            long cursor = -1;
            PagableResponseList<User> followers;
            //do {
                 followers = twitter.getFollowersList("Guohun", cursor);
                //for (User follower : followers) {
                    // TODO: Collect top 10 followers here
                    //System.out.println(follower.getName() + " has " + follower.getFollowersCount() + " follower(s)");
                //}
                                System.err.println("--------------Followers="+followers.size());
                 followers = twitter.getFriendsList("Guohun", cursor);
                for (User follower : followers) {
                    // TODO: Collect top 10 followers here
                    System.out.println(follower.getScreenName() + " has " + follower.getFollowersCount() + " following(s)");
                }
                System.err.println("--------------Following="+followers.size());
            //} while ((cursor = followers.getNextCursor()) != 0);

          
          System.exit(0);
          
          MultivaluedHashMap<String, String> formParams = new MultivaluedHashMap<>();
          formParams.putSingle("sentence", "?京ミッドタウンから国立新美??まで?いて5分で着きます。");
          formParams.putSingle("output", "xml");
          Client client=null;
          
          /*          client = (Client) new ClientBuilder().build();
          
          String results=client
          .target("http://jlp.yahooapis.jp")
          .path("KeyphraseService/V1/extract")
          .request(MediaType.APPLICATION_XML_TYPE)
          .header("User-Agent", "Yahoo AppID:<appid>")
          .post(Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
          System.out.println(results);
          */
          try {
              SampleStreamExample.run(api_key , api_secret, access_token, access_token_secret);
          } catch (InterruptedException ex) {
              Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          /*
          //Instantiate a re-usable and thread-safe factory
          TwitterFactory twitterFactory = new TwitterFactory();
          //Instantiate a new Twitter instance
          Twitter twitter = twitterFactory.getInstance();
          //setup OAuth Consumer Credentials
          twitter.setOAuthConsumer(api_key, api_secret);
          //setup OAuth Access Token
          twitter.setOAuthAccessToken(new AccessToken(access_token, access_token_secret));
          //Instantiate and initialize a new twitter status update
          StatusUpdate statusUpdate = new StatusUpdate(
          //your tweet or status message
          "Remeidial massage  on ");
          Status status=null;
          try {
          //attach any media, if you want to
          statusUpdate.setMedia(
          //title of media
          "Yea Tao Massage"
          , new URL("https://acupressure4toowoomba.wordpress.com/").openStream());
          
          //tweet or update status
          status = twitter.updateStatus(statusUpdate);
          
          } catch (MalformedURLException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
          } catch (TwitterException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
          }
          //response from twitter server
          System.out.println("status.toString() = " + status.toString());
          System.out.println("status.getInReplyToScreenName() = " + status.getInReplyToScreenName());
          System.out.println("status.getSource() = " + status.getSource());
          System.out.println("status.getText() = " + status.getText());
          
          System.out.println("status.getURLEntities() = " + Arrays.toString(status.getURLEntities()));
          System.out.println("status.getUserMentionEntities() = " + Arrays.toString(status.getUserMentionEntities()));
          */
          //SampleStreamExample.run(args[0], args[1], args[2], args[3]);
          
          
      } catch (TwitterException ex) {
          System.out.println("You can't connect to Internet");
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
          
      } //catch (InterruptedException ex) {
//          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
  //    }


/*
  //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();
        //Instantiate a new Twitter instance
        Twitter twitter = twitterFactory.getInstance();
        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(api_key, api_secret);
        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(access_token, access_token_secret));
        //Instantiate and initialize a new twitter status update
        StatusUpdate statusUpdate = new StatusUpdate(
                //your tweet or status message
                "Remeidial massage  on ");
        Status status=null;
      try {
          //attach any media, if you want to
          statusUpdate.setMedia(
                  //title of media
                  "Yea Tao Massage"
                  , new URL("https://acupressure4toowoomba.wordpress.com/").openStream());
          
                  //tweet or update status
        status = twitter.updateStatus(statusUpdate);

      } catch (MalformedURLException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
      } catch (TwitterException ex) {
          Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
      }
        //response from twitter server
        System.out.println("status.toString() = " + status.toString());
        System.out.println("status.getInReplyToScreenName() = " + status.getInReplyToScreenName());
        System.out.println("status.getSource() = " + status.getSource());
        System.out.println("status.getText() = " + status.getText());
        
        System.out.println("status.getURLEntities() = " + Arrays.toString(status.getURLEntities()));
        System.out.println("status.getUserMentionEntities() = " + Arrays.toString(status.getUserMentionEntities()));
*/
      //SampleStreamExample.run(args[0], args[1], args[2], args[3]);
        
      
  }

    private static void storeAccessToken(long id, AccessToken accessToken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static AccessToken loadAccessToken(int parseInt) {
//            String token = // load from a persistent store
  //          String tokenSecret = // load from a persistent store
//            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
//            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";
          final String access_token="3171875106-pRNzG2CRpsRjuTblFbDoz6FIev2sZje2BUu8vEw";
          final String  access_token_secret="iFDFi9xWpq1tFWG0EOxWaNXRvVA3TvQDvRrKPP2S4o3TJ";
                    
            return new AccessToken(access_token, access_token_secret);
    }
}
