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
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import static java.lang.Thread.sleep;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author uqgzhu1
 */
public class MonoTest {
    static String array_names[] = {"Lisa","Tim","Brit","Robin","Smith","Lora","Jennifer","Lyla","Victor","Adam"};
     
    static String array_address[][] ={
        {"US", "FL", " Miami"},
        {"US", "FL", " Orlando"},
        {"US", "CA", "San Diego"},
        {"US", "FL", " Orlando"},
        {"US", "FL", " Orlando"},
        {"US", "NY", "New York"},
        {"US", "NY", "Buffalo"},
        {"US", "TX", " Houston"},
        {"US", "CA", "San Diego"},
        {"US", "TX", " Houston"}
    };
  static Twitter twitter=null;
static DBCollection collection=null;
    private static DBCollection Usercollection;

static void threadMessage(String message) {
        String threadName =Thread.currentThread().getName();
        System.out.format("%s: %s%n",threadName,message);
    }

public static void  getTweetByQuery(boolean loadRecords, String keyword, String userName, String password) {

            if (collection != null) {
                final String api_key="JYzKS06JDEbYQVs7bBqc1Lmja";
                final String api_secret="LMF5NxW9T5A5vtm4mAEpVNg2HZkWN6dCZxdo3BMEoVS6Zjp6P4";
                final String access_token="3171875106-pRNzG2CRpsRjuTblFbDoz6FIev2sZje2BUu8vEw";
                final String  access_token_secret="iFDFi9xWpq1tFWG0EOxWaNXRvVA3TvQDvRrKPP2S4o3TJ";
                
                // The factory instance is re-useable and thread safe.
                if (twitter==null){
                    //twitter= TwitterFactory.getSingleton();
                    //AccessToken accessToken = loadAccessToken(1);                   
                    //twitter.setOAuthConsumer(api_key, api_secret);
                    //twitter.setOAuthAccessToken(accessToken);    
                    ConfigurationBuilder cb = new ConfigurationBuilder();
                    cb.setDebugEnabled(false)
                      .setOAuthConsumerKey(api_key)
                      .setOAuthConsumerSecret(api_secret)
                      .setOAuthAccessToken(access_token)
                      .setOAuthAccessTokenSecret(access_token_secret);
                    TwitterFactory tf = new TwitterFactory(cb.build());
                    twitter = tf.getInstance();                    
                }

                threadMessage("Starting MessageLoop thread");
                long startTime = System.currentTimeMillis();                
                Thread t = new Thread(new MonoThread(collection,twitter, Usercollection,userName,password));
                t.start();                  
                threadMessage("Waiting for MessageLoop thread to finish");
                // loop until MessageLoop           // thread exits
                while (t.isAlive()) {
                    try {
                        //threadMessage("Still waiting....................");
                        // Wait maximum of 1 second
                        // for MessageLoop thread
                        // to finish.
                        t.join(28000);
                        
                        t.join(28000);
                        
                        t.join(28000);
                        if (((System.currentTimeMillis() - startTime) >2000*60*60)   // long patience = 1000 * 60 * 60;
                                && t.isAlive()) {
                            startTime=System.currentTimeMillis();
                            threadMessage("Tired of waiting!");
                     //       t.interrupt();
                            // Shouldn't be long now
                            // -- wait indefinitely
                       //     t.join();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MonoTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                threadMessage("Finally!");                
/*                try {
                    Query query = new Query(keyword);
                    query.setCount(150);
                    QueryResult result;
                    result = twitter.search(query);
                           
                    System.out.println("Getting Tweets...");
                    List<Status> tweets = result.getTweets();
                    
                    
                    
                    for (Status tweet : tweets) {

                        BasicDBObject basicObj = new BasicDBObject();
                        basicObj.put("user_Rname", tweet.getUser().getName());
                        basicObj.put("user_name", tweet.getUser().getScreenName());
                        basicObj.put("retweet_count", tweet.getRetweetCount());
                        basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());

                        UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
                        basicObj.put("tweet_mentioned_count", mentioned.length);
                        basicObj.put("tweet_ID", tweet.getId());
                        basicObj.put("tweet_text", tweet.getText());
                        Status temp1=tweet.getRetweetedStatus();
                        if (temp1!=null)  basicObj.put("Re_tweet_ID", temp1.getUser().getId());
                        GeoLocation  loc=tweet.getGeoLocation();
                        if (loc!=null) { basicObj.put("Latitude", loc.getLatitude());
                                         basicObj.put("Longitude", loc.getLongitude());
                                        }
                        basicObj.put("CreateTime", tweet.getCreatedAt());        

                        if (mentioned.length > 0) {
                            basicObj.append("mentions", pickMentions( mentioned));                            
                        }
                        try {
                            //items.insert(basicObj);
                                collection.insert(basicObj);    
                        } catch (Exception e) {
                            System.out.println("MongoDB Connection Error : " + e.getMessage());
//                            loadMenu();
                        }
                    }
                    collection.ensureIndex(new BasicDBObject("tweet_ID", 1), new BasicDBObject("unique", true));
                    // Printing fetched records from DB.
                    if (loadRecords) {
                        //getTweetsRecords();
                            System.out.println("All Persons: "+collection.getCount());
                            //------------------------------------
                            // get all document
                            DBCursor cursor = collection.find();
                            try {
                               while(cursor.hasNext()) {
                                   System.out.println(cursor.next());
                               }
                            } finally {
                               cursor.close();
                            }

                    }

                } catch (TwitterException te) {
                    System.out.println("te.getErrorCode() " + te.getErrorCode());
                    System.out.println("te.getExceptionCode() " + te.getExceptionCode());
                    System.out.println("te.getStatusCode() " + te.getStatusCode());
                    if (te.getStatusCode() == 401) {
                        System.out.println("Twitter Error : \nAuthentication credentials (https://dev.twitter.com/pages/auth) were missing or incorrect.\nEnsure that you have set valid consumer key/secret, access token/secret, and the system clock is in sync.");
                    } else {
                        System.out.println("Twitter Error : " + te.getMessage());
                    }


  //                  loadMenu();
                }
              */  
            } else {
                System.out.println("MongoDB is not Connected! Please check mongoDB intance running..");
            }
                
        }

  public static void main(String[] args) {
  
    try {
   
    // Connect to mongodb
    MongoClient mongo = new MongoClient("localhost", 27017);
  
    // get database
    // if database doesn't exists, mongodb will create it for you
    DB db = mongo.getDB("test");
    String userName=null;
    String mypass=null;
    if (args.length>1)    
        {
            if (args[0].charAt(0)=='u'){
                userName=args[1];
                mypass=args[2];
            }
        }
    // get collection
    // if collection doesn't exists, mongodb will create it for you
    System.out.println(userName+"\t"+mypass);

    collection = db.getCollection("twitter_Stream");
    
    Usercollection = db.getCollection("user_tw_Stream");
    getTweetByQuery(true,"bus",userName,mypass);
    
    System.exit(0);
   
           
           
           // db.accounts.createIndex( { "tax-id": 1 }, { unique: true } )
    /**** Insert ****/
    // create a document to store key and value

    BasicDBObject document ;
    String address[];
    for(int i = 0 ; i < array_names.length ; i++){
        document = new BasicDBObject();
        //value -> String
        document.append("name", array_names[i]);
        //document.containsKey("name");
        // value -> int
        document.append("age", (int)(Math.random()*60));
        // value -> date
        document.append("join", new Date());
        // value -> array
        document.append("friends", pickFriends());
         
        address = pickAddress();
        // value --> document
        document.append("address", new BasicDBObject("country",address[0])
                                    .append("state", address[1])
                                    .append("city", address[2]));
 
        collection.insert(document);
 
    }
     
        //BasicDBObject query2 = new BasicDBObject("name", 1)
          //                      .append("unique", "true");
        //collection.createIndex(query2);    
        collection.ensureIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true));
    //collection.createIndex(new BasicDBObject("name", type).append("unique", true));
     //  collection.ensureIndex(new BasicDBObject("name", 1).append("unique", true));
    
    //BasicDBObject query_key = new BasicDBObject("name", 1)
      //                          .append("unique", "true");
    //collection.createIndex(query_key);
     
    // get count
    System.out.println("All Persons: "+collection.getCount());
    //------------------------------------
    // get all document
    DBCursor cursor = collection.find();
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
    //------------------------------------
 
    // get documents by query
    BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", 40));
 
    cursor = collection.find(query);
    System.out.println("Person with age > 40 --> "+cursor.count());
     
  
    /**** Update ****/
    //update documents found by query "age > 30" with udpateObj "age = 20"
    BasicDBObject newDocument = new BasicDBObject();
    newDocument.put("age", 20);
  
    BasicDBObject updateObj = new BasicDBObject();
    updateObj.put("$set", newDocument);
  
    collection.update(query, updateObj,false,true);
  
    /**** Find and display ****/
    cursor = collection.find(query);
    System.out.println("Person with age > 40 after update --> "+cursor.count());
     
     
    //get all again
    cursor = collection.find();
    try {
       while(cursor.hasNext()) {
           System.out.println(cursor.next());
       }
    } finally {
       cursor.close();
    }
  
    /**** Done ****/
    System.out.println("Done");
  
    } catch (UnknownHostException e) {
    e.printStackTrace();
    } catch (MongoException e) {
    e.printStackTrace();
    }
  
  }
  //----------------------------------------------------
  //These methods are just jused to build random data
  private static String[] pickFriends(){
      int numberOfFriends = (int) (Math.random()* 10);
      LinkedList<String> friends = new LinkedList<String>();
      int random = 0;
      while(friends.size() < numberOfFriends){
          random = (int) (Math.random()*10);
          if(!friends.contains(array_names[random]))
              friends.add(array_names[random]);
               
      }
      String a[] = {};
      return  friends.toArray(a);
  }
  

  private static String[] pickAddress(){
      int random = (int) (Math.random()*10);
      return array_address[random];
  } 

    static private AccessToken loadAccessToken(int i) {
            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";
                    
            return new AccessToken(access_token, access_token_secret);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
