/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/*

lookup the use name from the user id

*/
/**
 *
 * @author uqgzhu1
 */
public class MongoRepair   {
    
    
  public static void main(String[] args) {

            
            final String api_key="5xi95sDq8ZEfoy48j7TKOOLBD";
            final String api_secret="COZ38TtQOnlben9s7ecTCkRYE9wGWvLX7vqGrMa0Jm739bPRUZ";
            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";
            // The factory instance is re-useable and thread safe.
            Twitter twitter = TwitterFactory.getSingleton();
            
            
            AccessToken accessToken = loadAccessToken(1);
            
            //twitter.setOAuthConsumerKey("[consumer key]", "[consumer secret]");
            twitter.setOAuthConsumer(api_key, api_secret);
            twitter.setOAuthAccessToken(accessToken);
            
            
            
            
            try {
                
                // Connect to mongodb
                MongoClient mongo = new MongoClient("localhost", 27017);
                
                // get database
                // if database doesn't exists, mongodb will create it for you
                DB db = mongo.getDB("test");
                
                // get collection
                // if collection doesn't exists, mongodb will create it for you
                DBCollection collection = db.getCollection("twitter");
                
                DBCursor cursor;
                BasicDBObject query;
                //------------------------------------
                // ( 1 ) collection.find() --> get all document
                cursor = collection.find();
                System.out.println("find() all documents");
                System.out.println("results --> "+cursor.count());
                
                try {
                    long Mynumber=0;
                    Status status=null;
                   while(cursor.hasNext()) {
                       //     System.out.println(cursor.next());
                            DBObject oldData=cursor.next();
                            
                            //System.out.println(user.getDescription());
                            long oldTweet_ID=(long)oldData.get("tweet_ID");
                            
                            String oldRe_user_screenName=(String)oldData.get("Re_user_screenName");
                            if (oldRe_user_screenName!=null) continue;
                            
                            System.out.println(oldData.get("tweet_text"));
                            
                            try{
                                status = twitter.showStatus(oldTweet_ID);
                            }catch( TwitterException e){
                                System.out.println(e);
                                if (e.getStatusCode()==88) return;
                                continue;
                            }
                            
                            Object Oold_retweet_count=oldData.get("FavoriteCount");
                            if (Oold_retweet_count!=null){
                                    int old_favoriate_count=(Integer)Oold_retweet_count;
                                    int new_favoriate_count=status.getFavoriteCount();
                                    if (old_favoriate_count!=new_favoriate_count)
                                        System.out.println("@"+status.getId()+"favis is +"+(new_favoriate_count-old_favoriate_count));    
                                    //User user = twitter.showUser((long));
                                    //System.out.println("<h3>Displaying user id of user "+ user.getScreenName() + "</h3><br/>");    
                            }
                            if (status == null) { //
                                // don't know if needed - T4J docs are very bad
                            } else {                
                                    BasicDBObject jo=GetMongoRecord(status);                                    
                                    System.out.println("update:" +oldTweet_ID+"-->"+status.getId()+" : "+ jo.getString("Re_user_screenName")+":"+jo.getString("tweet_text"));
                                    collection.update(new BasicDBObject("tweet_ID", oldTweet_ID), jo); // set the document in the DB to the new document for Jo                     
                            }
                            
                            Mynumber++;
                        //    if (Mynumber>3) break;
                
                    }
                } finally {
                        cursor.close();
                }
                
                System.out.println("---------------------------------");
                
                
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (MongoException e) {
                e.printStackTrace();
            }
            
        } 
       

    private static BasicDBObject  GetMongoRecord(Status tweet) {
        
                BasicDBObject basicObj = new BasicDBObject();
                basicObj.put("user_Rname", tweet.getUser().getName());
                basicObj.put("user_name", tweet.getUser().getScreenName());
                basicObj.put("retweet_count", tweet.getRetweetCount());
                basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());
                
                UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
                basicObj.put("tweet_mentioned_count", mentioned.length);
                basicObj.put("tweet_ID", tweet.getId());
                basicObj.put("tweet_text", tweet.getText());                
                Status temp1=tweet.getRetweetedStatus(); if (temp1!=null) {  
                    basicObj.put("Re_user_ID", temp1.getUser().getId());
                    basicObj.put("Re_user_screenName", temp1.getUser().getScreenName());
                    basicObj.put("Re_tweet_ID", temp1.getId());
                }
                GeoLocation  loc=tweet.getGeoLocation();
                if (loc!=null) { basicObj.put("Latitude", loc.getLatitude());                basicObj.put("Longitude", loc.getLongitude());                }
                basicObj.put("CreateTime", tweet.getCreatedAt());
                basicObj.put("FavoriteCount", tweet.getFavoriteCount());
                basicObj.put("user_Id", tweet.getUser().getId());
                
                if (tweet.getUser().getTimeZone()!=null) basicObj.put("UsertimeZone", tweet.getUser().getTimeZone());
                if (tweet.getUser().getStatus()!=null)  basicObj.put("UserStatus", tweet.getUser().getStatus());
                //basicObj.put("tweetLocation", tweet.getPlace().getGeometryCoordinates());
                String U_Loc=tweet.getUser().getLocation();                if (U_Loc!=null) basicObj.put("userLocation", U_Loc);
                basicObj.put("number_of_rt", tweet.getRetweetCount());
                //basicObj.put("isRetweet", tweet.getPlace().getGeometryCoordinates());                
                //basicObj.put("POS", tweet.getWithheldInCountries());
                if (mentioned.length > 0) {
                    basicObj.append("mentions", pickMentions( mentioned));
                }
                
                return basicObj;

    }

    private static AccessToken loadAccessToken(int i) {
            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";
                    
            return new AccessToken(access_token, access_token_secret);
    }

    private static Object pickMentions(UserMentionEntity[] mentioned) {
           LinkedList<String> friends = new LinkedList<String>();
      
      for (UserMentionEntity  x: mentioned ) {                    
              //String  y=x.getName();// Long.toString(x.getId());
              friends.add(x.getScreenName());               
      }
      String a[] = {};
      return  friends.toArray(a);

    }
}
