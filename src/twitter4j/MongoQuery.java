/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import twitter4j.auth.AccessToken;

/**
 *
 * @author uqgzhu1
 */
public class MongoQuery {

    public static void main(String[] args) {
        final String api_key = "JYzKS06JDEbYQVs7bBqc1Lmja";
        final String api_secret = "LMF5NxW9T5A5vtm4mAEpVNg2HZkWN6dCZxdo3BMEoVS6Zjp6P4";
        final String access_token = "3171875106-pRNzG2CRpsRjuTblFbDoz6FIev2sZje2BUu8vEw";
        final String access_token_secret = "iFDFi9xWpq1tFWG0EOxWaNXRvVA3TvQDvRrKPP2S4o3TJ";
        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();

        AccessToken accessToken = loadAccessToken(1);

        //twitter.setOAuthConsumerKey("[consumer key]", "[consumer secret]");
        twitter.setOAuthConsumer(api_key, api_secret);
        twitter.setOAuthAccessToken(accessToken);

        try {

            // Connect to mongodb
            MongoClient mongo = new MongoClient("10.33.2.142", 27017);

            // get database
            // if database doesn't exists, mongodb will create it for you
            DB db = mongo.getDB("test");

            // get collection
            // if collection doesn't exists, mongodb will create it for you
            DBCollection collection = db.getCollection("user_tw_Stream");

            DBCursor cursor;
            BasicDBObject query;
            //{  "Topic3":{    "$regex":"ENG"  }}
            
//            collection.find({"Topic3" : {"$regex" :"ENG"}});
            query = new BasicDBObject("Topic3", new BasicDBObject("$regex", "ENG"));            
            //------------------------------------            
            // ( 1 ) collection.find() --> get all document
            
       //     collection.remove(query);
            cursor = collection.find(query);    
            System.out.println("( 1 ) .get the user id and information()");
            System.out.println("results --> " + cursor.count());
            cursor.close();
            System.exit(0);
            
            try {
                while (cursor.hasNext()) {
                    DBObject data = cursor.next();
                    String v_user_Name = (String) data.get("user_name");
                    Long v_twitte_id = (Long) data.get("tweet_ID");
                    String v_twitte_text = (String) data.get("tweet_text");

                    Long v_user_Id = (Long) data.get("user_Id");

                    if (v_user_Id == null) {
                        System.out.print("update:" +v_user_Name+"/status/"+ v_twitte_id);
                        try{
                            Status status = twitter.showStatus(v_twitte_id);
                            BasicDBObject jo = GetMongoRecord(status);
                            System.out.println( "-->" + status.getId() + " : " + jo.getString("Re_user_screenName") + ":" + jo.getString("tweet_text"));
                            collection.update(new BasicDBObject("tweet_ID", v_twitte_id), jo); // set the document in the DB to the new document for Jo                     
                        }catch (TwitterException ex){
                            if (ex.getStatusCode()==144) continue;
                        }
                        
                    }else{
                        
                    }
                        
                    
/*                  Long v_Retwitte_user_id = (Long) data.get("Re_user_ID");
                    String v_Retwitte_user_Name = (String) data.get("Re_user_screenName");
                    Long v_Retwitte_id = (Long) data.get("Re_tweet_ID");

                    Long v_Rptwitte_user_id = (Long) data.get("ReP_user_ID");
                    String v_Rptwitte_user_Name = (String) data.get("ReP_user_screenName");
                    Long v_Rptwitte_id = (Long) data.get("ReP_tweet_ID");

                    Integer v_Retweet_count = (Integer) data.get("retweet_count");
                    Integer v_tweet_followers_count = (Integer) data.get("tweet_followers_count");
                    Integer v_FavoriteCount = (Integer) data.get("FavoriteCount");
                    Integer v_tweet_mentioned_count = (Integer) data.get("tweet_mentioned_count");

                    Date v_CreateTime = (Date) data.get("CreateTime");
                    String v_UsertimeZone = (String) data.get("UsertimeZone");
                    String v_userLocation = (String) data.get("userLocation");

                    Double v_Lat = (Double) data.get("Latitude");
                    Double v_Long = (Double) data.get("Longitude");

                    BasicDBList report = (BasicDBList) data.get("mentions");
*/
                }
            }  finally {
                cursor.close();
            }

            System.out.println("---------------------------------");
            System.exit(0);
            //------------------------------------
            // ( 2 ) collection.find({"age" : {"$gt" : 10}})) --> get documents by query
            String[] comparison_operators = {"$gt", "$lt", "$gte", "$lte", "$ne"};
            //for(int p = 0 ; p < comparison_operators.length; p++)

            {
                int p = 0;
                //query = new BasicDBObject("post_date", new BasicDBObject(comparison_operators[p], "Mon May 11 11:55:24 EST 2015")); //2015-05-12T15:15:31Z
                query = new BasicDBObject("post_date", "Mon May 11 11:55:24 EST 2015"); //2015-05-12T15:15:31Z
                cursor = collection.find(query);

                System.out.println(query);
                System.out.println("( 2." + (p + 1) + " ) .find({\"The University of Queensland post \" : {\"" + comparison_operators[p] + "\" : null}})");
                System.out.println("results --> " + cursor.count());
                try {
                    while (cursor.hasNext()) {
                        DBObject doc = cursor.next();
                        //collection.remove(doc);                   
                        System.out.println(doc.get("post_date"));
                    }
                } finally {
                    cursor.close();
                }

            }
            System.out.println("---------------------------------");
            System.exit(0);

    //------------------------------------
            // ( 3 ) collection.find({"name" : /j/}, {"name" : 1, "age" : 1,"_id":0}) --> get documents with some keys
/*    Pattern j = Pattern.compile("j", Pattern.CASE_INSENSITIVE);
             query = new BasicDBObject("post_name", j);
             BasicDBObject keys = new BasicDBObject("post_name", 1);//.append("age", 1).append("_id", 0);
             cursor = collection.find(query, keys);
             System.out.println("( 3 ) .find({\"name\" : /j/i}, {\"name\" : 1, \"age\" : 1,\"_id\":0})");
             System.out.println("results --> "+cursor.count());
             try {
             while(cursor.hasNext()) {
             System.out.println(cursor.next());
             }
             } finally {
             cursor.close();
             }
             System.out.println("---------------------------------");
             //-----------------------------------  

             // ( 4 ) collection.find({"age" : {"$in" : [10,20,30]}})) --> get documents by query
             String[] or_operators = {"$in","$nin"};
             int[] age_array = {10,20,30};
             for(int p = 0 ; p < or_operators.length; p++){
             query = new BasicDBObject("age", new BasicDBObject(or_operators[p], age_array));
             cursor = collection.find(query);
             System.out.println("( 4."+(p+1)+" ) .find({\"age\" : {\""+or_operators[p]+"\" : [10,20,30]}})");
             System.out.println("results --> "+cursor.count());
             }
             System.out.println("---------------------------------");
             */
            //-----------------------------------  
            // ( 5 ) collection.find({"$or" : [ {"name":"John" },{"age":20}]}) --> get documents by query
            BasicDBObject or_conditions[] = {new BasicDBObject("post_name", "John"), new BasicDBObject("post_message", "UQ")};
            query = new BasicDBObject("$or", or_conditions);
            cursor = collection.find(query);
            System.out.println("( 5 ) .find({\"$or\" :[{\"name\":\"John\"},{\"age\":20}]})");
            System.out.println("results --> " + cursor.count());
            System.out.println("---------------------------------");
            //-----------------------------------  
            // ( 6 ) collection.find({"friends" : ["John","Tim"]},{"name" : 1, "freinds" : 1,"_id":0})) --> get documents by query
            String[] friends = {"John", "Tim"};
            query = new BasicDBObject("comments", new BasicDBObject("$ne", "[]"));    //new BasicDBObject("$all",friends) 
            BasicDBObject keys = new BasicDBObject("post_name", 1).append("friends", 1).append("_id", 0);
            cursor = collection.find(query);    //,keys
            System.out.println("( 6 ) .find({\"friends\" : {$all:[\"John\",\"Tim\"]}},{\"name\" : 1, \"freinds\" : 1,\"_id\":0})");
            System.out.println("results --> " + cursor.count());
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }
            System.out.println("---------------------------------");

            //------------------------------------
            // ( 7 ) collection.find({"address" : {"country":"US","state":"NY","city":"Buffalo"}},{"name" : 1, "address" : 1,"_id":0})) --> get documents by query
//"comments" : [ { "pub_id" : "676362572496642" , "screenName" : "Bonnie Rose" , "message" : "Is there still some left..?"} , { "pub_id" : "10153831110983761" , "screenName" : "Alessandro Souza" , "message" : "I wo    
            //Pattern p_S = Pattern.compile("^I");
            Pattern p_S = Pattern.compile("Is*", Pattern.CASE_INSENSITIVE);

	  //BasicDBObject regexQuery = new BasicDBObject();
            //regexQuery.put("name",new BasicDBObject("$regex", "Mky.*-[1-3]").append("$options", "i"));
            //System.out.println(regexQuery.toString());
            query = new BasicDBObject("comments", new BasicDBObject("pub_id", "676362572496642").append("screenName", "Bonnie Rose").append("message", new BasicDBObject("$gt", "I")));//.append("message" , "Is there still some left..?"));//.append("message","well"));10153388659860555
            //query.append("number", new BasicDBObject("$ne", 4));
            System.out.println(query.toString());
            keys = new BasicDBObject("post_name", "The University of Queensland"); //.append("post_shared", 1).append("post_message", 0);
            cursor = collection.find(query);//,keys
            System.out.println("( 7 ) .find({\"address\" : {\"country\":\"US\",\"state\":\"NY\",\"city\":\"Buffalo\"}},{\"name\" : 1, \"address\" : 1,\"_id\":0})");
            System.out.println("results --> " + cursor.count());
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }
            System.out.println("---------------------------------");
            System.exit(0);
            //------------------------------------
            // ( 8 ) collection.find({"address.state" : "NY"},{"name" : 1, "address" : 1,"_id":0})) --> get documents by query
            query = new BasicDBObject("address.state", "NY");
            keys = new BasicDBObject("name", 1).append("address", 1).append("_id", 0);
            cursor = collection.find(query, keys);
            System.out.println("( 8 ) .find({\"address.state\" : \"NY\"},{\"name\" : 1, \"address\" : 1,\"_id\":0})");
            System.out.println("results --> " + cursor.count());
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
            } finally {
                cursor.close();
            }
            System.out.println("---------------------------------");
            /**
             * ** Done ***
             */
            System.out.println("Done");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }

    }

    private static AccessToken loadAccessToken(int i) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        final String access_token = "3171875106-pRNzG2CRpsRjuTblFbDoz6FIev2sZje2BUu8vEw";
        final String access_token_secret = "iFDFi9xWpq1tFWG0EOxWaNXRvVA3TvQDvRrKPP2S4o3TJ";

        return new AccessToken(access_token, access_token_secret);

    }

    private static BasicDBObject GetMongoRecord(Status tweet) {
        BasicDBObject basicObj = new BasicDBObject();
        basicObj.put("user_Rname", tweet.getUser().getName());
        basicObj.put("user_name", tweet.getUser().getScreenName());
        basicObj.put("retweet_count", tweet.getRetweetCount());
        basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());

        UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
        basicObj.put("tweet_mentioned_count", mentioned.length);
        basicObj.put("tweet_ID", tweet.getId());
        basicObj.put("tweet_text", tweet.getText());
        Status temp1 = tweet.getRetweetedStatus();
        if (temp1 != null) {
            basicObj.put("Re_user_ID", temp1.getUser().getId());
            basicObj.put("Re_user_screenName", temp1.getUser().getScreenName());
            basicObj.put("Re_tweet_ID", temp1.getId());
        }
        Long ReplayTwitter = tweet.getInReplyToStatusId();
        if (ReplayTwitter > 0) {
            basicObj.put("ReP_user_ID", tweet.getInReplyToUserId());
            basicObj.put("ReP_user_screenName", tweet.getInReplyToScreenName());
            basicObj.put("ReP_tweet_ID", ReplayTwitter);
        }

        GeoLocation loc = tweet.getGeoLocation();
        if (loc != null) {
            basicObj.put("Latitude", loc.getLatitude());
            basicObj.put("Longitude", loc.getLongitude());
        }
        basicObj.put("CreateTime", tweet.getCreatedAt());
        basicObj.put("FavoriteCount", tweet.getFavoriteCount());
        basicObj.put("user_Id", tweet.getUser().getId());
        String mylocation = tweet.getUser().getLocation();
        if (mylocation != null) {
            basicObj.put("user_location", mylocation);
        }

        if (tweet.getUser().getTimeZone() != null) {
            basicObj.put("UsertimeZone", tweet.getUser().getTimeZone());
        }
        if (tweet.getUser().getStatus() != null) {
            basicObj.put("UserStatus", tweet.getUser().getStatus());
        }
                //basicObj.put("tweetLocation", tweet.getPlace().getGeometryCoordinates());
        //String U_Loc=tweet.getUser().getLocation();                if (U_Loc!=null) basicObj.put("userLocation", U_Loc);
        basicObj.put("number_of_rt", tweet.getRetweetCount());
                //basicObj.put("isRetweet", tweet.getPlace().getGeometryCoordinates());                
        //basicObj.put("POS", tweet.getWithheldInCountries());
        if (mentioned.length > 0) {
            basicObj.append("mentions", pickMentions(mentioned));
        }

        Place temPL = tweet.getPlace();
        if (temPL != null) {
            basicObj.put("tweet_Street", temPL.getStreetAddress());
            basicObj.put("tweet_Country", temPL.getCountry());
            //basicObj.put("tweet_Country", temPL.getBoundingBoxCoordinates());                    
        }
        basicObj.put("tweet_Lan", tweet.getLang());
        basicObj.put("user_Lan", tweet.getUser().getLang());
        basicObj.put("user_CreateTime", tweet.getUser().getCreatedAt());

        return basicObj;

    }

    private static Object pickMentions(UserMentionEntity[] mentioned) {
        LinkedList<String> friends = new LinkedList<String>();

        for (UserMentionEntity x : mentioned) {
            //String  y=x.getName();// Long.toString(x.getId());
            friends.add(x.getScreenName());
        }
        String a[] = {};
        return friends.toArray(a);

    }
}
