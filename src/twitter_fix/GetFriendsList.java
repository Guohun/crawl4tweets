/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter_fix;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 *
 * @author uqgzhu1
 */
public class GetFriendsList {
static                HashMap  <String, HashSet <Long> > FirstGroupUser=new HashMap  <String, HashSet <Long> > ();
static                HashMap  <String, HashSet <Long> > FirstGroupTweet=new HashMap  <String, HashSet <Long> > ();
static                HashMap  <String, HashSet <Double> > FirstGroupLat=new HashMap  <String, HashSet <Double> > ();

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
                
                // Connect to mongodb
                MongoClient mongo = new MongoClient("localhost", 27017);

                // get database
                // if database doesn't exists, mongodb will create it for you
                DB db = mongo.getDB("test");

                // get collection
                // if collection doesn't exists, mongodb will create it for you
                //DBCollection collection = db.getCollection("facebook");

                DBCursor cursor;
                DBCollection twcollection = db.getCollection("twitter_Stream");
                DBCollection TwUsercollection = db.getCollection("user_tw_Stream");
                DBCollection TwFriendscollection = db.getCollection("user_tw_friends");
                
                DBObject clause1 = null;
                DBObject clause2 = null;
                DBObject clause3 = null;
                DBObject clause4 = null;
                DBObject clause5 = null;
                
                clause1 = new BasicDBObject("Topic1", "UQ" );//
                clause4 = new BasicDBObject("Topic2", "UQ" );//
                clause5 = new BasicDBObject("Topic3", "UQ" );//
                clause2 = new BasicDBObject("R_Post_Code", "4072");//new BasicDBObject("$exists", true));
                  
                BasicDBList Orquery = new BasicDBList();
                Orquery.add(clause1);
                Orquery.add(clause2);
                Orquery.add(clause4);
                Orquery.add(clause5);
                DBObject query1 = new BasicDBObject("$or", Orquery);
                
//                clause2 = new BasicDBObject("user_Id", new BasicDBObject("$ne","2248628622"));//new BasicDBObject("$exists", true));
                clause3 = new BasicDBObject("R_Country_Code", "AU");//new BasicDBObject("$regex", "uq"));//
                //DBObject clause7 = new BasicDBObject("Record_Loc", "AU");//new BasicDBObject("$exists", true));                
                BasicDBList or  = new BasicDBList();                                
                or.add(clause3);                
//                or.add(clause2);
               // or.add(clause3);
                //or.add(clause7);
                DBObject query = new BasicDBObject("$and", or);
    //QueryBuilder builder = new QueryBuilder();
                //builder = QueryBuilder.start();
                 //builder.or(new BasicDBObject(mongoKey, mongoValue));
                cursor = TwUsercollection.find(query);
                System.out.println("( 1 ) .find()");
                System.out.println("results --> " + cursor.count());
                
                try {
                    while (cursor.hasNext()) {
                        DBObject data = cursor.next();
                        //Long Tweet_Id = (Long) data.get("tweet_ID");
                        String  screen_name=(String)data.get("user_screen_name");
                        Long v_user_Id = (Long) data.get("user_Id");
                        String text = (String) data.get("user_Description");                                                
                        String Topic2 = (String) data.get("Topic2");
                        String Topic3 = (String) data.get("Topic3");
                        Double Lat=(Double) data.get("Latitude");
                        Double Longi=(Double) data.get("Longitude");        
                        
    //                    if (v_user_Id.compareTo(Long.valueOf("2248628622"))==0) continue;
//                        if (text.contains("#HollywoodChoice")) continue;
  //                      if (text.contains("@Entrepreneur")) continue;
                 //       if (text.contains("coal production down 40% since 2010 and clean energy")) continue;
                        
//                        BasicDBList report = (BasicDBList) data.get("comments");
//                        String post_Name=(String) data.get("user_name");
//                        String ReuserName=(String) data.get("Re_user_screenName");
                                //System.out.println((String) x.get("pub_id") + "\t" + post_userId);
                        //text.replace("\n", " ");
                        //text.replace("\r", " ");
                        if (v_user_Id==null) continue;
                        if (text!=null){
                        char []temp=text.toCharArray();
                        for (int i=0;i<temp.length;i++){
                            if (temp[i]=='\r'||temp[i]=='\n'||temp[i]=='\f'||temp[i]=='\''||temp[i]==','||temp[i]=='\"')
                                temp[i]=' ';
                        }
                        text=String.valueOf(temp);                        
                        }
                        System.out.println(v_user_Id+"\t"+Lat+"\t"+Longi+"\t"+text);  
                        long tw_cursor = -1;
                        PagableResponseList<User> followers;
                        HashMap <Long, Long>  User_IdMap=new HashMap <Long, Long>  ();
                        try {
                            followers = twitter.getFriendsList(screen_name, tw_cursor);
                            for (User follower : followers) {
                                // TODO: Collect top 10 followers here
                               //     User_IdMap.put(v_user_Id, v_user_Id)
                                    follower.getId();
                            }
                            System.err.println("--------------Following="+followers.size());
                        } catch (TwitterException ex) {
                            Logger.getLogger(GetFriendsList.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        
/*                        HashSet <Long> tempSet=FirstGroupUser.get(Topic2);
                        if (tempSet==null) tempSet=new HashSet <Long>();
                        tempSet.add(v_user_Id);
                        FirstGroupUser.put(Topic2, tempSet);
                        
                        tempSet=FirstGroupTweet.get(Topic2);
                        if (tempSet==null) tempSet=new HashSet <Long>();
                        tempSet.add(Tweet_Id);
                        FirstGroupTweet.put(Topic2, tempSet);

                        if (Lat!=null){
                                HashSet <Double> latSet=FirstGroupLat.get(Topic2);
                                if (latSet==null) latSet=new HashSet <Double>();
                                latSet.add(Lat);
                                FirstGroupLat.put(Topic2, latSet);                      
                        }
                        
                        
*/                        
                //        System.out.println(Tweet_Id+ "," +v_user_Id+","+Lat+","+Longi+","+Topic3+","+text);                        
//                                if (CheckSaveList.contains(ReuserName)){
  //                                  System.err.println("Tw Re repeat"+ReuserName);
    //                            }else{
      //                              TwSaveList.add(ReuserName);
        //                        }
                        
                        }
                    
                } finally {
                    cursor.close();
                }
  /*              System.out.println("Subclass\t"+"Num_Tweets\t"+"Num_Users\t"+"Num_Geo");                
                for (String tempS: FirstGroupTweet.keySet()){
                        HashSet <Long> tempSetU=FirstGroupUser.get(tempS);
                        HashSet <Long> tempSetT=FirstGroupTweet.get(tempS);
                        HashSet <Double> tempSetL=FirstGroupLat.get(tempS);
                        int LatNum=0;
                        if (tempSetL!=null) LatNum=tempSetL.size();
                        System.out.println(tempS+"\t"+tempSetT.size()+"\t"+tempSetU.size()+"\t"+LatNum);
                }
*/              
                System.exit(0);
                //clause1 = new BasicDBObject("user_name", new BasicDBObject("$regex", "UQ"));
                clause2 = new BasicDBObject("ReP_user_ID", new BasicDBObject("$exists", true));
                
                or.clear();
                or.add(clause1);
                or.add(clause2);
                query = new BasicDBObject("$and", or);

                cursor = twcollection.find(query);
                System.out.println("( 3 ) .find(ReP)");
                System.out.println("results --> " + cursor.count());
                try {
                    while (cursor.hasNext()) {
                        DBObject data = cursor.next();
//                        BasicDBList report = (BasicDBList) data.get("comments");
                        String post_Name=(String) data.get("user_name");
                        String ReuserName=(String) data.get("ReP_user_screenName");
                                //System.out.println((String) x.get("pub_id") + "\t" + post_userId);
                        System.out.println(post_Name+ "\t" + ReuserName);                        
          //                      if (CheckSaveList.contains(ReuserName)){
            //                        System.err.println("Tw rePly repeat"+ReuserName);
              //                  }else{
                //                    TwSaveList.add(ReuserName);
                  //              }
                        
                        }
                    
                } finally {
                    cursor.close();
                }
               
                
                //System.out.println(CheckSaveList.size()+":"+TwSaveList.size());
                System.exit(0);
                System.out.println("---------------------------------");
                //------------------------------------
                // ( 2 ) collection.find({"age" : {"$gt" : 10}})) --> get documents by query
                String[] comparison_operators = {"$gt", "$lt", "$gte", "$lte", "$ne"};
                //for(int p = 0 ; p < comparison_operators.length; p++)

                {
                    int p = 0;
                    //query = new BasicDBObject("post_date", new BasicDBObject(comparison_operators[p], "Mon May 11 11:55:24 EST 2015")); //2015-05-12T15:15:31Z
                    query = new BasicDBObject("post_date", "Mon May 11 11:55:24 EST 2015"); //2015-05-12T15:15:31Z
                    cursor = twcollection.find(query);

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
                cursor = twcollection.find(query);
                System.out.println("( 5 ) .find({\"$or\" :[{\"name\":\"John\"},{\"age\":20}]})");
                System.out.println("results --> " + cursor.count());
                System.out.println("---------------------------------");
                //-----------------------------------
                // ( 6 ) collection.find({"friends" : ["John","Tim"]},{"name" : 1, "freinds" : 1,"_id":0})) --> get documents by query
                String[] friends = {"John", "Tim"};
                query = new BasicDBObject("comments", new BasicDBObject("$ne", "[]"));    //new BasicDBObject("$all",friends)
                BasicDBObject keys = new BasicDBObject("post_name", 1).append("friends", 1).append("_id", 0);
                cursor = twcollection.find(query);    //,keys
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
                cursor = twcollection.find(query);//,keys
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
                cursor = twcollection.find(query, keys);
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

            } catch (UnknownHostException ex) {
                Logger.getLogger(GetFriendsList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    private static AccessToken loadAccessToken(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    }
