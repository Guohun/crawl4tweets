/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

2016.4.3  

Add NLP.init on 
        MonoThread(DBCollection collection, Twitter twitter, DBCollection Usercollection, String userName, String password, DBCollection SearchCollection) {

 */
package twitter4j;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import static twitter4j.MonoTest.twitter;
import twitter_fix.NLP;
import twitter_fix.SentimentClass;
import twitter_fix.map_class;
import twitter_fix.tweetClass;
import util.ConnectionDB;
import util.FoursquareAPI;
import static util.StringUtils.getHttpLinkStr;

/**
 *
 * @author uqgzhu1
 */
public class MonoThread implements Runnable {

    private DBCollection collection = null;
    private DBCollection Usercollection = null;
    private Twitter twitter = null;
    FoursquareAPI foursquareApi = null;
    //private static String base = System.getProperty("user.dir") + "/datasets/";
    private static String base = "/home/uqgzhu1/datasets/";
    private static String[] FileLists = {"twitter_name_only.csv"};
    //private Map <String, String>  post_code_Map=null;
    private Map<String, String> post_code_Map = null;
    private Map<String, String> Company_Map = null;
    private List<map_class> tweet_List=new ArrayList <map_class>(); 
    // Display a message, preceded by
    // the name of the current thread
    private final DBCollection SearchCollection;
    private SentimentClass G_SentimentClass= new SentimentClass();

    MonoThread(DBCollection collection, Twitter twitter, DBCollection Usercollection, String userName, String password, DBCollection SearchCollection) {
        this.collection = collection;
        this.twitter = twitter;
        this.Usercollection = Usercollection;
        this.SearchCollection=SearchCollection;
        NLP.init();
//        foursquareApi = new FoursquareAPI(userName, password);
        foursquareApi = new FoursquareAPI();                
        post_code_Map = new HashMap<String, String>();
        Company_Map=new HashMap<String, String>();
        for (int i = 0; i < FileLists.length; i++) {
            FileReader fr = null;
            try {
                fr = new FileReader(base + FileLists[i]);
                BufferedReader textReader = new BufferedReader(fr);
                String aLine = null;
                aLine = textReader.readLine();//skip one
                while ((aLine = textReader.readLine()) != null) {
                    String StrArray[] = aLine.split("\t");
                    if (StrArray.length < 5) {
                        System.out.println("CSV error on\t "+aLine);
                        continue;
                    }
                    if (StrArray[3].isEmpty()) continue;
                    if (StrArray[3].charAt(0)=='*') continue;
                    if (StrArray[2].length() > 2) {
                        this.post_code_Map.put(StrArray[2], StrArray[0]);
                            Company_Map.put(StrArray[2], StrArray[4]);
                    }
                }//end while
                textReader.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("total postcode="+this.post_code_Map.size());
    }

    @Override
    public void run() {
        String keyword = "university of queensland";
        double latitude = -27.497835;
        double longitude = 153.017472;
        double radius = 100;

        GeoLocation myLoc = new GeoLocation(latitude, longitude);
        GeoLocation GattonLoc = new GeoLocation(-27.56, 152.34);
        GeoLocation PerthLoc = new GeoLocation(-31.9505596161, 115.8438415527);
        GeoLocation TokyoLoc = new GeoLocation(35.699, 139.707);
        GeoLocation SanDiegoLoc = new GeoLocation(-32.4254, -117.0945);
        GeoLocation SanFranciscoorNewYorkDiegoLoc = new GeoLocation(-122.75368, -121.75378);
        GeoLocation NewYorkDiegoLoc = new GeoLocation(-74.40, -73.41);
        GeoLocation MontrealLoc = new GeoLocation(45.495619, -73.553446);
        GeoLocation LongdonLoc = new GeoLocation(51.51, -0.131);
        GeoLocation ShanghaiLoc = new GeoLocation(31.12, 121.30);
        GeoLocation DarwinLoc = new GeoLocation(-12.4613304138, 130.8419036865);
        Map <String, Integer> saveNumber=new HashMap<String, Integer> ();
//        -122.75,36.8,-121.75,37.8	San Francisco
//-74,40,-73,41	New York City
        while (true) {
            String KeywordsSet[][] = {
                {"ENGG1100", "Engineering Design"},
                {"ENGG1200", "Engineering Modelling & Problem Solving"},
                {"MATH1051", "Calculus & Linear Algebra I [1]"},
                {"MATH1052", "Multivariate Calculus & Ordinary Differential Equations"},
                {"ENGG1300", "Introduction to Electrical Systems"},
                {"ENGG1400", "Engineering Mechanics: Statics & Dynamics"},
                {"ENGG1500", "Engineering Thermodynamics"},
                {"BIOL1040", "Cells to Organisms"},
                {"CHEE1001", "Principles of Biological Engineering"},
                {"CHEM1100", ""}, //Chemistry 1
                {"CSSE1001", "Introduction to Software Engineering"},
                {"ENGG1600", "Introduction to Research Practices - The Big Issues"},
                {"ERTH1501", "Earth Processes & Geological Materials for Engineers"},
                {"PHYS1002", "Electromagnetism and Modern Physics"},
                {"CHEM1090", "Introductory Chemistry [2]"},
                {"MATH1050", "Mathematical Foundations [1] [3]"}};
            String UniKeySet[][] = {
                {"UQ", "University of Queensland"},
                {"QUT", "Queensland University of Technology"},
                {"USQ", "University of Southern Queensland"},
                {"GRIFFITH", "Griffith University"},
                {"ACU", "Australian Catholic University"},
                {"JCU", "James Cook University"},
                {"USC", "University of the Sunshine Coast"},
                {"SCU", "Southern Cross University"},
                {"BondUniversity", "Bond University"},
                {"CQU", "Central Queensland University"}
            };

            String SpecialKeySet[][] = {
                {"University of Queensland", "Marketing", "Market"},
                {"uq clean energy", "CleanEnergy", "CreateChange"},
                {"uq solar energy", "SolarEnergy", "CreateChange"},
                {"uq create change", "CreateChange", "CreateChange"},
                {"uq addressing gender", "GenderinEquity", "CreateChange"},
                {"uq inequality", "GenderinEquity", "CreateChange"},
                {"uq gender bias", "GenderBias", "CreateChange"},
                {"uq GenderEquity", "GenderEquity", "CreateChange"},
                {"uq academia", "Academia", "CreateChange"},
                {"uq womeninstem", "Womeninstem", "CreateChange"},
                {"uq gender inequality", "GenderinEquity", "CreateChange"},
                {"social entrepreneurship", "SocialEntrepreneurship", "CreateChange"},
                {"bspa", "bspa", "CreateChange"},
                {"flighting superbugs", "Superbugs", "CreateChange"},
                {"uq superbugs", "Superbugs", "CreateChange"},
                {"uq CEPA", "CEPA", "CreateChange"},
                {"changemarkers", "ChangeMarkers", "ChangeMakers"},
                {"prof Polly Parker", "PollyParker", "ChangeMakers"},
                {"uq Polly Parker", "PollyParker", "ChangeMakers"},
                {"prof Alastair McEwan", "AlastairMcEwan", "ChangeMakers"},
                {"uq Alastair McEwan", "AlastairMcEwan", "ChangeMakers"},
                {"cervical cancer vaccine", "CancerVaccine", "ChangeMakers"},
                {"magnetic resonance imaging", "MRI", "ChangeMakers"},
                {"uq MRI", "MRI", "ChangeMakers"},
                {"UniQuest", "UniQuest", "ChangeMakers"},
                {"Prof Ian Frazer", "IanFrazer", "ChangeMakers"},
                {"uq Ian Frazer", "IanFrazer", "ChangeMakers"},
                {"ResApp Diagnostics", "IanFrazer", "ChangeMakers"},
                {"Nexgen Plants", "NexgenPlants", "ChangeMakers"},
                {"UQ Open Day", "Living", "Market"},
                {"Lucia campus", "LuciaCampus", "Market"},
                {"Gatton campus", "GattonCampus", "Market"},
                {"uq discovery tour", "DiscoveryTour", "Market"},
                {"central Walkway", "CentralWalkway", "Market"},
                {"myAdvisor", "myAdvisor", "Market"},
                {"@oneinbillion", "Staff", "VC"}, //Max LU                
                {"uq business school", "BEL", "School"},
                {"uq engineering school", "ITEE", "School"},
                {"uq BEL", "BEL", "School"},
                {"uq ITEE", "ITEE", "School"},
                {"uq Marketing", "Marketing", "Market"}, //                {"xue li", "Staff", "ITEE"},                                                                
            //              {"Shazia Sadiq", "Staff", "ITEE"},
            //            {"Heng Tao", "Staff", "ITEE"},                
            };

            try {

/*                getNewTweets("Sydney violence", "AU", "Event", "Crime", "Melbourne");
                sleep(19000);
                
                getNewTweets("Melbourne shooting", "AU", "Event", "Crime", "Melbourne");
                sleep(19000);

                getNewTweets("Sydney shooting", "AU", "Event", "Crime", "Melbourne");
                sleep(19000);

                getNewTweets("#QLDTraffic", "AU", "Event", "Traffic", "QLD");
                sleep(19000);

                getNewTweets("#bnetraffic", "AU", "Event", "Traffic", "QLD");
                sleep(19000);
                
                getNewTweets("#brisbane traffic", "AU", "Event", "Traffic", "Brisbane");
                sleep(19000);
                
                getNewTweets("Melbourne violence", "AU", "Event", "Crime", "Melbourne");
                sleep(19000);
                


                
                getNewTweets("#studybrisbane", "AU", "Study", "QUT", "UQ");
                sleep(19000);
                        
                getNewTweets("#TRAFFICALERT #brisbane", "AU", "Event", "Traffic", "Brisbane");
                sleep(19000);
*/                                        
//                getNewTweets("#TaiwanElection", "TW", "Election", "Taiwan", "Postive");
                //sleep(19000);
                        
//                getNewTweets("#HappyNewYear", "YY", "Happy", "Celebration", "Postive");
//                sleep(19000);

//                getNewTweets("#NewYearsEve", "YY", "Happy", "Celebration", "Postive");
//                sleep(19000);


                        
                        
                System.out.println("catch special user time-------------------------");
                Connection Conn = ConnectionDB.openMySqlConnect();
                Long createTime=null;

                    this.tweet_List.clear();
                    
                    int num1=this.tweet_List.size();                    
                    
                    
                    //String[] FunjiKey={"FujitsuRecruit","FujitsuAU","fujitsugeneral","fujitsu","airconditioning","air condition","air_condition","fujitsu_ac","Fujitsu_Global",
                      //      "#cooling","#HVAC","reverse cycle split system"};
                    //String[] FunjiKey={"Fujitsu air condition","Fujitsu air-condition","Fujitsu aircondition"};
                    //https://twitter.com/AtYourServicecy/status/695662311764176896
                    //https://twitter.com/AggeliesKerkyra/status/676691813839716352
                    //https://twitter.com/JaniceOsborne10/status/673089697824964609
                    //https://twitter.com/thessalonian70/status/
                    String[] FunjiID={"695662311764176896","685389132089053184","676691813839716352","673089697824964609","626558195000045568","617334422187175937",
                            "563731626711318528","502716232085147648","462304226082029568","459499574579986432",
                            "420824056225472512","375133597377646592","355817678050570240","352062523572355072",
                            "228480856434552833","223957761925455872","459080488640983040","358557830531383297","325110360992538626","155078097421602816",
                            "715229864924680197","711766351761645568","714432448902393856"}; 
                    //https://twitter.com/mcgoldrick25/status/685389132089053184
                    //https://twitter.com/QldSportsFan/status/155078097421602816
                    //https://twitter.com/timsterne/status/358557830531383297https://twitter.com/UTEster750/status/325110360992538626
                    for (int i=0;i<FunjiID.length;i++){
                        String fkeyword1= FunjiID[i];                                            
                        //getNewTweets(fkeyword1, "FUJI", "Conditioning", "Airecondition", "FUJITSU");
                        System.out.println("user can not find:\t "+fkeyword1);
                        getTweetsStatus(Long.valueOf(fkeyword1), "FUJI", "Conditioning", "Airecondition", "FUJITSU");
                        sleep(19000);                            
                    }

                    String[] FunjiKey={"Fujitsu air conditioning","Fujitsu air conditioner","Fujitsu airconditioning","Fujitsu air condition"};
                    String[] FunjiPostcode={null,null,null};
                    for (int i=0;i<FunjiKey.length;i++){
                        System.out.println("user can not find:\t "+FunjiKey[i]);
                        getNewTweets(FunjiKey[i], "FUJI", "Conditioning", "Airecondition", "FUJITSU");
                        sleep(19000);                                                    
                    }
                        
                        createTime=this.tweet_List.get(0).LongCreate_Time;                    
                        System.out.println( "\t Post="+tweet_List.size() +"\t"+new Date(createTime));                                                
                        Write2MyEventRecord(Conn, null, "FUJIA", createTime, tweet_List.size(),"fujitsu,tritechairconditioning");

                        
            if (post_code_Map.size()>0) return;
                    
                     
                    

                this.tweet_List.clear();
                for (String tempUname : this.post_code_Map.keySet()) {                    
                    String postCode=post_code_Map.get(tempUname);
                    User myuser=null;
                    try {
                        myuser = twitter.showUser(tempUname);
                    } catch (TwitterException ex) {
                        //Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("user can not find:\t "+tempUname);
                        sleep(19000);
                        continue;
                    }
                    int numstatus=myuser.getStatusesCount();
                    Integer mytime=saveNumber.get(tempUname);
                    if (mytime!=null){
                        if (mytime==numstatus) {
                            sleep(19000);
                            continue;
                        }
                    }
                    
                    saveNumber.put(tempUname, numstatus);
                    num1=this.tweet_List.size();
                    getNewTweets(tempUname, "AU", null, null, null);
                    int num=this.tweet_List.size()-num1; //after insert
                    if (num==0) continue;
                    String company=Company_Map.get(tempUname);
                    if (company.compareToIgnoreCase("UQ")==0){
                        System.out.println("write UQ Post="+num +"\t"+tempUname);                        
                        createTime=this.tweet_List.get(num1).LongCreate_Time;                    
                        Write2MyEventRecord(Conn, postCode, "UQ_Post", createTime, num,"uq,university");                    
                        
                    }else if (company.compareToIgnoreCase("QUT")==0){
                        System.out.println("write QUT Post="+num +"\t"+tempUname);                        
                        createTime=this.tweet_List.get(num1).LongCreate_Time;                    
                        Write2MyEventRecord(Conn, postCode, "QUT_Post", createTime, num,"qut");                    
                        
                    }

                    sleep(19000);
                }
                
                ConnectionDB.closeMySqlConnect();
                
                
                getNewTweets("storm", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);
                
                getNewTweets("bluecology", "AU", "Chris Brown", "Staff", "GRIFFITH");
                sleep(19000);
                getNewTweets("ProfTerryHughes", "AU", "Terry Hughes", "Staff", "JCU");
                sleep(19000);
                
                getNewTweets("#DalyRiver", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);

                getNewTweets("#floods", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);
                


                getNewTweets("#flooding", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);


             //   getNewTweets("TonyAbbottMHR", "AU", "Tony Abbott", "Minister", "GOV");
//                sleep(19000);

                getNewTweets("oveHG", "AU", "Ove Hoegh-Guldberg", "Staff", "UQ");
                sleep(19000);

                getNewTweets("#UQMarketDay", "AU", "Event", "Market", "UQ");
                sleep(19000);

                getNewTweets("#UQ Market Day", "AU", "Market Day", "Market", "UQ");
                sleep(19000);

                getNewTweets("UQMSA", "AU", "Association", "Student", "UQ");
                sleep(19000);

                getNewTweets("frankBioNano", "AU", "AIBN", "Staff", "UQ");
                sleep(19000);
                getNewTweets("lucia campus", "AU", "Campus", "Living", "UQ");
                sleep(19000);
                getNewTweets("gatton campus", "AU", "Campus", "Living", "UQ");
                sleep(19000);
                getNewTweets("toowoomba campus", "AU", "Campus", "Living", "USQ");
                sleep(19000);
                getNewTweets("ipswich campus", "AU", "Campus", "Living", "UQ");
                sleep(19000);

                for (int i = 0; i < SpecialKeySet.length; i++) {
                    if (SpecialKeySet[i][0].length() > 2) {
                        getNewTweets(SpecialKeySet[i][0], "AU", SpecialKeySet[i][1], SpecialKeySet[i][2], "UQ");
                        sleep(19000);
                    }
                }

                getNewTweets("go.Women", "AU", "Lucia Campus", "Event", "UQ");
                sleep(19000);

//                   
                for (int i = 0; i < UniKeySet.length; i++) {
                    for (int j = 0; j < UniKeySet[0].length; j++) {
                        getNewTweets(UniKeySet[i][j], "AU", UniKeySet[i][0], "University", "QLD");
                        sleep(19000);
                    }
                }

                getNewTweets("UQ_News", "AU", "News", "UQ", "University");
                sleep(19000);
                getNewTweets("USQNews", "AU", "News", "USQ", "University");
                sleep(19000);
                getNewTweets("qutnews", "AU", "News", "QUT", "University");
                sleep(19000);
                getNewTweets("griffithmedia", "AU", "News", "QUT", "University");
                sleep(19000);
                getNewTweets("ACUmedia", "AU", "News", "ACU", "University");
                sleep(19000);
                getNewTweets("JCU_medicine", "AU", "News", "JCU", "University");
                sleep(19000);
                getNewTweets("usceduau", "AU", "News", "USC", "University");
                sleep(19000);

                getNewTweets("scunews", "AU", "News", "SCU", "University");
                sleep(19000);

                getNewTweets("CQUni", "AU", "News", "CQU", "University");
                sleep(19000);

                for (int i = 0; i < KeywordsSet.length; i++) {
                    for (int j = 0; j < KeywordsSet[0].length; j++) {
                        if (KeywordsSet[i][j].length() > 2) {
                            getNewTweets(KeywordsSet[i][j], "AU", "Engineering", "Course", "UQ");
                            sleep(19000);
                        }
                    }
                }

                System.out.println("catch Darwin location-------------------------");
                getNewTweets("hurricane", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);
                
                getNewTweets("flood", "XX", "Tragic", "Weather", "Climate");
                sleep(19000);
                
  //              getNewTweets(DarwinLoc, 100, "AU");
                //            sleep(19000);

//                getNewTweets("brisbane","AU", "", "Course");
                //              sleep(19000);
                //            getNewTweets("university queensland","AU", "", "Course");
                //          sleep(19000);
//                System.out.println("catch Perth location-------------------------");
                //              getNewTweets(PerthLoc, 100, "AU");
                //            sleep(19000);
                //      System.out.println("catch Gatton location-------------------------");
                //        getNewTweets(GattonLoc, 100, "AU");
                //          sleep(19000);
//                System.out.println("catch UQ location-------------------------");
                //              getNewTweets(myLoc, radius, "AU");
                //            sleep(19000);

                /*                System.out.println("catch Tokyo location-------------------------");
                 getNewTweets(TokyoLoc, 300, "JP");
                 sleep(19000);


                 System.out.println("catch SanDiego location-------------------------");
                 getNewTweets(SanDiegoLoc, 300, "USA");
                 sleep(19000);

                 //                getNewTweets("gatton campus");
                 sleep(19000);

                 System.out.println("catch Montreal location-------------------------");
                 getNewTweets(MontrealLoc, 300, "CA");
                 sleep(19000);


                 System.out.println("catch Longdon location-------------------------");
                 getNewTweets(LongdonLoc, 300, "UK");
                 sleep(19000);

                 //                getNewTweets("qld students");
                 sleep(19000);

                 System.out.println("catch Shanghai location-------------------------");
                 getNewTweets(ShanghaiLoc, 300, "CN");
                 sleep(19000);
                 */
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(MonoTest.class.getName()).log(Level.SEVERE, null, ex);
            }         }

    }

    /**
     *
     * @param keyword the value of keyword
     * @param districtCode the value of districtCode
     * @param KeywordsSet1 the value of KeywordsSet1
     * @param course the value of course
     */
    void getNewTweets(String keyword, String districtCode, String subTopic2, String subTopic1, String Topic) {
        try {
            Query query = new Query(keyword);
            query.setCount(200);
            if (subTopic2 != null) {
                query.setLang("en");
            }
            QueryResult result;
            collection.ensureIndex(new BasicDBObject("tweet_ID", 1), new BasicDBObject("unique", true));
            Usercollection.ensureIndex(new BasicDBObject("user_Id", 1), new BasicDBObject("unique", true));

            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                System.out.println("find:\t" + keyword + ":\t" + result.getCount() + "\t searched");
                for (Status tweet : tweets) {
                    GeoLocation loc = tweet.getGeoLocation();
                    String screenuserName = tweet.getUser().getScreenName();

                    org.json.JSONObject businesses = null;
                    String city_code = null;
                    String state_code = null;
                    String post_code = null;
                    String country_code = null;
                    boolean userUq=false;
                    if (loc != null) {
                        Double v_Latitude = loc.getLatitude();
                        Double v_Longtitude = loc.getLongitude();
                        
                        businesses = this.foursquareApi.getAuStateCity(v_Latitude, v_Longtitude);
                        
                    }
                    post_code = this.post_code_Map.get(screenuserName);                        
                    if (post_code!=null )         userUq=true;
                    if (businesses != null) {
                        String old_post_code=null;
                        try{
                            country_code = (String) businesses.get("country_code");
                            city_code = (String) businesses.get("city_name");
                            state_code = (String) businesses.get("state_code");
                            old_post_code = (String) businesses.get("post_code");
                        }catch(org.json.JSONException ex){
                            System.err.println("find businesses without postcode " + businesses);
                        }
                        
                        if (old_post_code!=null)  post_code=old_post_code;
                        else{
                            if (country_code==null&&post_code!=null) country_code="AU";
                        }
                        districtCode=country_code;
                    } else {
                        if (post_code != null) {                            
                            businesses = this.foursquareApi.getAuStateCity(post_code);                        
                            String old_country_code=null;
                            if (businesses != null) {
                                    try{
                                    old_country_code = (String) businesses.get("country_code");
                                    city_code = (String) businesses.get("city_name");
                                    state_code = (String) businesses.get("state_code");
//                                    post_code = (String) businesses.get("post_code");                                
                                    }catch(org.json.JSONException ex){
                                                System.err.println("find businesses with postcode " +post_code+"\t"+ businesses);
                                    }                                    
                            }
                            if (old_country_code==null) country_code ="AU";                            
                            districtCode=country_code;
                        }
                    }
                    BasicDBObject basicObj = this.GetTweetRecord(tweet, districtCode, country_code, city_code, state_code, post_code, "R", subTopic2, subTopic1, Topic);
                    if (basicObj == null) {
                        System.out.println("Error on insert Tweet records or repeat insert");
                        continue;
                    }
//                   if (userUq)
//                        this.GetSearchFbRecord(tweet, districtCode, country_code, city_code, state_code, post_code, "R", subTopic2, subTopic1, Topic);
                    
                    try {
                        //items.insert(basicObj);
                        collection.insert(basicObj);

                        /**
                         * ** Update ***
                         */
                        Long v_User_ID = tweet.getUser().getId();
                        BasicDBObject existObj = new BasicDBObject("user_Id", v_User_ID);
                        BasicDBObject UserObj = GetUserRecord(tweet, v_User_ID, districtCode, country_code, city_code, state_code, post_code, "R", subTopic2, subTopic1, Topic);
                        if (UserObj == null) {
                            System.out.println("Error on insert User records");
                            continue;
                        }

                        DBCursor userCourse = Usercollection.find(existObj);
                        if (userCourse.hasNext()) {
                            DBObject TempData = userCourse.next();
                            String tempPlace = (String) TempData.get("R_Post_Code");
                            if (tempPlace == null) {
                                Usercollection.update(existObj, UserObj);
                            } else {
                                if (post_code.compareTo(tempPlace) != 0) {
                                    System.out.println("User from " + tempPlace + " -\\->" + post_code);
                                    UserObj.append("$set", new BasicDBObject().append("MoveCity", tempPlace));
                                    Usercollection.update(existObj, UserObj);
                                } else {
                                    //                              String tempName = (String) TempData.get("Place_Name");
//                                String userLocfromDB= (String) TempData.get("user_location");                                
                                    //the same post_code so do nothing or check user hide their place
                                }
                            }
                        } else {
                            Usercollection.insert(UserObj);
                        }
                        userCourse.close();
                        UserObj = null;
                        existObj = null;
                        Thread.sleep(300);
                    } catch (Exception e) {
                    }
                    basicObj = null;

                    Thread.sleep(500);

                }

                //result=result.nextQuery();                     
                Thread.sleep(10000);
            } while ((query = result.nextQuery()) != null);

        } catch (TwitterException ex) {
            java.util.logging.Logger.getLogger(MonoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

    void getTweetsStatus(Long tweet_ID, String districtCode, String subTopic2, String subTopic1, String Topic) {
        try {
            collection.ensureIndex(new BasicDBObject("tweet_ID", 1), new BasicDBObject("unique", true));
            Usercollection.ensureIndex(new BasicDBObject("user_Id", 1), new BasicDBObject("unique", true));

//            do {
                Status tweet = twitter.showStatus(tweet_ID);                
                
                {
                    GeoLocation loc = tweet.getGeoLocation();
                    org.json.JSONObject businesses = null;
                    String city_code = null;
                    String state_code = null;
                    String post_code = null;
                    String country_code = null;
                    boolean userUq=false;
                    if (loc != null) {
                        Double v_Latitude = loc.getLatitude();
                        Double v_Longtitude = loc.getLongitude();                        
                        businesses = this.foursquareApi.getAuStateCity(v_Latitude, v_Longtitude);                        
                    }
                    if (businesses != null) {
                        String old_post_code=null;
                        try{
                            country_code = (String) businesses.get("country_code");
                            city_code = (String) businesses.get("city_name");
                            state_code = (String) businesses.get("state_code");
                            old_post_code = (String) businesses.get("post_code");
                        }catch(org.json.JSONException ex){
                            System.err.println("find businesses without postcode " + businesses);
                        }
                        
                        if (old_post_code!=null)  post_code=old_post_code;
                        else{
                            if (country_code==null&&post_code!=null) country_code="AU";
                        }
                        districtCode=country_code;
                    } 
                    
                    BasicDBObject basicObj = this.GetTweetRecord(tweet, districtCode, country_code, city_code, state_code, post_code, "R", subTopic2, subTopic1, Topic);                    
                    if (basicObj == null) {
                        System.out.println("Error on insert Tweet records or repeat insert");
                        return ;
                    }
                    System.out.println(tweet.getRetweetCount()+ " - " + tweet.getText());                 
                    try {
                        collection.insert(basicObj);

/*                        Long v_User_ID = tweet.getUser().getId();
                        BasicDBObject existObj = new BasicDBObject("user_Id", v_User_ID);
                        BasicDBObject UserObj = GetUserRecord(tweet, v_User_ID, districtCode, country_code, city_code, state_code, post_code, "R", subTopic2, subTopic1, Topic);
                        if (UserObj == null) {
                            System.out.println("Error on insert User records");
                            return;
                        }

                        DBCursor userCourse = Usercollection.find(existObj);
                        if (userCourse.hasNext()) {
                            DBObject TempData = userCourse.next();
                            String tempPlace = (String) TempData.get("R_Post_Code");
                            if (tempPlace == null) {
                                Usercollection.update(existObj, UserObj);
                            } else {
                                if (post_code.compareTo(tempPlace) != 0) {
                                    System.out.println("User from " + tempPlace + " -\\->" + post_code);
                                    UserObj.append("$set", new BasicDBObject().append("MoveCity", tempPlace));
                                    Usercollection.update(existObj, UserObj);
                                } else {
                                    //                              String tempName = (String) TempData.get("Place_Name");
//                                String userLocfromDB= (String) TempData.get("user_location");                                
                                    //the same post_code so do nothing or check user hide their place
                                }
                            }
                        } else {
                            Usercollection.insert(UserObj);
                        }
                        userCourse.close();
                        UserObj = null;

                        existObj = null;
                        Thread.sleep(300);
*/                                                
                    } catch (Exception e) {
                    }
                    basicObj = null;

                    Thread.sleep(500);

                }

                //result=result.nextQuery();                     
                Thread.sleep(10000);
  //          } while ((query = result.nextQuery()) != null);

        } catch (TwitterException ex) {
            java.util.logging.Logger.getLogger(MonoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
    
    void getNewTweets(GeoLocation myLoc, double radius, String districtCode) {
        try {
            Query query = new Query();
            Query.Unit unit = Query.MILES;
            query.setGeoCode(myLoc, radius, unit);
            if (radius > 200) {
                query.setCount(400);
            } else {
                query.setCount(200);
            }
            collection.ensureIndex(new BasicDBObject("tweet_ID", 1), new BasicDBObject("unique", true));
            Usercollection.ensureIndex(new BasicDBObject("user_Id", 1), new BasicDBObject("unique", true));

            QueryResult result;
            do {
                result = this.twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    GeoLocation loc = tweet.getGeoLocation();
                    String screenuserName = tweet.getUser().getScreenName();
                    
                    org.json.JSONObject businesses = null;
                    String city_code = null;
                    String state_code = null;
                    String post_code = null;
                    String country_code = null;
                    if (loc != null) {
                        Double v_Latitude = loc.getLatitude();
                        Double v_Longtitude = loc.getLongitude();
                         businesses = this.foursquareApi.getAuStateCity(v_Latitude, v_Longtitude);
                    }
                    if (businesses != null) {
                        country_code = (String) businesses.get("country_code");
                        city_code = (String) businesses.get("city_name");
                        state_code = (String) businesses.get("state_code");
                        post_code = (String) businesses.get("post_code");
                    }else{
                        post_code = this.post_code_Map.get(screenuserName);
                      //  System.err.println(screenuserName + " code to find postcode" + post_code);
                        if (post_code != null) {
                            businesses = this.foursquareApi.getAuStateCity(post_code);                        
                            if (businesses != null) {
                                country_code = (String) businesses.get("country_code");
                                city_code = (String) businesses.get("city_name");
                                state_code = (String) businesses.get("state_code");
                                post_code = (String) businesses.get("post_code");
                            }
                        }                        
                    }
                    BasicDBObject basicObj = this.GetTweetRecord(tweet, districtCode, country_code, city_code, state_code, post_code, "R", null, null, null);
                    if (basicObj == null) {
                        System.out.println("Error on insert Tweet records or repeat insert");
                        continue;
                    }

                    try {
                        //items.insert(basicObj);
                        collection.insert(basicObj);

                        /**
                         * ** Update ***
                         */
                        Long v_User_ID = tweet.getUser().getId();
                        BasicDBObject existObj = new BasicDBObject("user_Id", v_User_ID);
                        BasicDBObject UserObj = GetUserRecord(tweet, v_User_ID, districtCode, country_code, city_code, state_code, post_code, "R", null, null, null);
                        if (UserObj == null) {
                            System.out.println("Error on insert User records");
                            continue;
                        }

                        DBCursor userCourse = Usercollection.find(existObj);
                        if (userCourse.hasNext()) {
                            DBObject TempData = userCourse.next();
                            String tempPlace = (String) TempData.get("R_Post_Code");
                            if (tempPlace == null) {
                                Usercollection.update(existObj, UserObj);
                            } else {
                                if (post_code.compareTo(tempPlace) != 0) {
                                    System.out.println("User from " + tempPlace + " -\\->" + post_code);
                                    UserObj.append("$set", new BasicDBObject().append("MoveCity", tempPlace));
                                    Usercollection.update(existObj, UserObj);
                                } else {
                                    //                              String tempName = (String) TempData.get("Place_Name");
//                                String userLocfromDB= (String) TempData.get("user_location");                                
                                    //the same post_code so do nothing or check user hide their place
                                }
                            }
                        } else {
                            Usercollection.insert(UserObj);
                        }
                        userCourse.close();
                        UserObj = null;
                        existObj = null;

                    } catch (Exception e) {
                    }
                    basicObj = null;

                    Thread.sleep(300);

                }

                //result=result.nextQuery();                     
                Thread.sleep(10000);
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException ex) {
            java.util.logging.Logger.getLogger(MonoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private BasicDBObject GetUserRecord(Status status, Long v_User_ID, String districtCode, String country_code, String city_code, String state_code, String post_code, String InsertMethod, String subTopic2, String subTopic1, String Topic) {
        try {
            BasicDBObject basicObj = new BasicDBObject();

            String v_user_screenName = status.getUser().getScreenName();

            GeoLocation loc = status.getGeoLocation();

            basicObj.put("user_Id", v_User_ID);
            basicObj.put("user_screen_name", v_user_screenName);

            if (loc != null) {
                basicObj.put("Latitude", loc.getLatitude());
                basicObj.put("Longitude", loc.getLongitude());
            }
            Place myplace = status.getPlace();
            if (myplace != null) {
                basicObj.put("Place_ID", myplace.getId());
                basicObj.put("Place_Country", myplace.getCountry());
                basicObj.put("Place_Country_Code", myplace.getCountryCode());
                basicObj.put("Place_Type", myplace.getPlaceType());
                basicObj.put("Place_Name", myplace.getName());

                basicObj.put("Place_URL", myplace.getURL());
                basicObj.put("Place_Street", myplace.getStreetAddress());

            }

            String mylocation = status.getUser().getLocation();
            if (mylocation != null) {
                basicObj.put("user_location", mylocation);
            }

            basicObj.put("follows_count", status.getUser().getFollowersCount());
            basicObj.put("favorite_count", status.getUser().getFavouritesCount());
            basicObj.put("fiends_count", status.getUser().getFriendsCount());
            basicObj.put("status_count", status.getUser().getStatusesCount());

            String v_UsertimeZone = status.getUser().getTimeZone();
            if (v_UsertimeZone != null) {
                basicObj.put("UsertimeZone", v_UsertimeZone);
            }

            basicObj.put("user_Lan", status.getUser().getLang());
            basicObj.put("user_CreateTime", status.getUser().getCreatedAt());
            basicObj.put("user_Description", status.getUser().getDescription());
            basicObj.put("user_ImageURL", status.getUser().getProfileImageURL());
            basicObj.put("user_URL", status.getUser().getURL());
            basicObj.put("user_protected", status.getUser().isProtected());
            basicObj.put("user_geoEnabled", status.getUser().isGeoEnabled());
            basicObj.put("user_verified", status.getUser().isVerified());

            basicObj.put("update_date", status.getCreatedAt());

            String tempDate = status.getCreatedAt().toGMTString();
            SimpleDateFormat sdfSource = new SimpleDateFormat("dd MMM yyyy HH:mm:ss 'GMT'");
            Date mydate = sdfSource.parse(tempDate);
            long integer_date = mydate.getTime();
            basicObj.put("integer_Date", integer_date);

//            basicObj.put("GMT_Year", (1900 + mydate.getYear()));
            //          basicObj.put("GMT_Month", mydate.getMonth());
            //        basicObj.put("GMT_Day", mydate.getDay());
            //      basicObj.put("GMT_Date", mydate.getDate());
            //    basicObj.put("GMT_Hour", mydate.getHours());
            //  basicObj.put("GMT_Minute", mydate.getMinutes());
            //basicObj.put("GMT_Second", mydate.getSeconds());
            basicObj.put("Record_Loc", districtCode);
            basicObj.put("Record_Method", InsertMethod);

            if (country_code != null) {
                basicObj.put("R_Country_Code", country_code);
                basicObj.put("R_State_Code", state_code);
                basicObj.put("R_City", city_code);
                basicObj.put("R_Post_Code", post_code);
            }
            if (subTopic2 != null) {
                basicObj.put("Topic3", subTopic2);
                basicObj.put("Topic2", subTopic1);
                basicObj.put("Topic1", Topic);
            }

//                String []tempS=status.getWithheldInCountries();
//                if (tempS!=null) for (String Cs: tempS) System.out.print("\t"+Cs);
//                System.out.println("\tLT:"+status.getLang());
//                System.out.println("\tCU:"+status.getUser().getCreatedAt());
            return basicObj;
        } catch (ParseException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BasicDBObject GetTweetRecord(Status tweet, String districtCode, String country_code, String city_code, String state_code, String post_code, String InsertMethod, String subTopic2, String subTopic1, String Topic) {
        try {
            //, String district, String kw, boolean withimage) {
            long tweet_Id=tweet.getId();
            
            
//            for (map_class tempmap: this.tweet_List)
  //                  if (tempmap.tweet_ID==tweet_Id) return null;
            
            map_class tempmap=new map_class ();
            
            tempmap.tweet_ID=tweet_Id;                        
            tempmap.user_Name=tweet.getUser().getScreenName();
            tempmap.user_Id=tweet.getUser().getId();
            tempmap.old_text=tweet.getText();
            tempmap.lang=tweet.getLang();
            tempmap.Topics=Topic;
            
            System.out.println("begin write one time "+tempmap.tweet_ID);
            
            BasicDBObject basicObj = new BasicDBObject();
            basicObj.put("user_Rname", tweet.getUser().getName());            
            GeoLocation loc = tweet.getGeoLocation();            
            if (loc != null) {
                tempmap.lati=loc.getLatitude();
                tempmap.logi=loc.getLongitude();
                basicObj.put("Latitude", tempmap.lati);
                basicObj.put("Longitude", tempmap.logi);
            }
            basicObj.put("CreateTime", tweet.getCreatedAt());
            String tempDate = tweet.getCreatedAt().toGMTString();
            SimpleDateFormat sdfSource = new SimpleDateFormat("dd MMM yyyy HH:mm:ss 'GMT'");
            Date mydate = sdfSource.parse(tempDate);
            long integer_date = mydate.getTime();
            tempmap.LongCreate_Time=integer_date;
            tempmap.post_code=post_code;
            
            basicObj.put("integer_Date", integer_date);

            
            basicObj.put("tweet_text", tempmap.old_text);
            basicObj.put("user_name", tempmap.user_Name);
            basicObj.put("tweet_ID",tempmap.tweet_ID );
            basicObj.put("user_Id",tempmap.user_Id);
            basicObj.put("retweet_count", tweet.getRetweetCount());
            basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());

            UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
            basicObj.put("tweet_mentioned_count", mentioned.length);
            
            basicObj.put("tweet_text", tweet.getText());
            String tempSource = getHttpLinkStr(tweet.getSource());
            if (tempSource != null) {
                basicObj.put("tweet_Source", tempSource);
            } else {
                basicObj.put("tweet_Source", tweet.getSource());
            }

            Status temp1 = tweet.getRetweetedStatus();
            if (temp1 != null) {
                basicObj.put("Re_user_ID", temp1.getUser().getId());
                basicObj.put("Re_user_screenName", temp1.getUser().getScreenName());
                basicObj.put("Re_tweet_ID", temp1.getId());
                tempmap.ReP_ReT='T';
            }
            Long ReplayTwitter = tweet.getInReplyToStatusId();
            if (ReplayTwitter > 0) {
                basicObj.put("ReP_user_ID", tweet.getInReplyToUserId());
                basicObj.put("ReP_user_screenName", tweet.getInReplyToScreenName());
                basicObj.put("ReP_tweet_ID", ReplayTwitter);
                tempmap.ReP_ReT='P';
            }

            basicObj.put("FavoriteCount", tweet.getFavoriteCount());
            
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

            MediaEntity[] mev = tweet.getMediaEntities();
            if (mev.length > 0) {
                basicObj.append("meida", pickMedias(mev));
            }

            Place temPL = tweet.getPlace();
            if (temPL != null) {
                String mystr = temPL.getStreetAddress();
                if (mystr != null) {
                    basicObj.put("tweet_Street", mystr);
                }
                basicObj.put("tweet_Country", temPL.getCountry());
                //basicObj.put("tweet_Country", temPL.getBoundingBoxCoordinates());                    
                basicObj.put("tweet_Place_ID", temPL.getId());
                basicObj.put("tweet_Country_Code", temPL.getCountryCode());
                basicObj.put("tweet_Place_Type", temPL.getPlaceType());
                basicObj.put("tweet_Place_Name", temPL.getName());
                basicObj.put("tweet_Place_URL", temPL.getURL());
            }
            basicObj.put("tweet_Lan", tempmap.lang);
            basicObj.put("user_Lan", tweet.getUser().getLang());
//        basicObj.put("user_CreateTime", tweet.getUser().getCreatedAt());
            basicObj.put("Record_Loc", districtCode);
            basicObj.put("Record_Method", InsertMethod);
            if (country_code != null) {
                basicObj.put("R_Country_Code", country_code);
                basicObj.put("R_State_Code", state_code);
                basicObj.put("R_City", city_code);
                basicObj.put("R_Post_Code", post_code);
            }
            if (subTopic2 != null) {
                basicObj.put("Topic3", subTopic2);
                basicObj.put("Topic2", subTopic1);
                basicObj.put("Topic1", Topic);
            }
            this.tweet_List.add(tempmap);
//        		imagelink = tpp.GetImageLink(twittertext.trim());
//			String trendwords = tpp.extractTrendWords(twittertext.trim());
//			cstmt.setString(16, district);
//				cstmt.setString(17, kw);            
            return basicObj;
        } catch (ParseException ex) {
            //Logger.getLogger(TwitterManager.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error on" + ex);
        }
        return null;
    }

    private boolean Write2MyEventRecord(Connection Conn, String postCode, String event_Names, long createTime, int num, String keywords) {
        int total=this.tweet_List.size()-1;
        if (total<1) return false;
        String ArrayKey[]=keywords.split(",");
        List < Long>Cluster_Tweets=new  ArrayList<>();
        try {
            //, String district, String kw, boolean withimage) {
  //first wite to tw_list to search_list
            
            
            List<String> tokens=new ArrayList<String> ();
            int numRep=0;
            for (int i=0;i<num;i++){
                   map_class temp=this.tweet_List.get(total--);
                   String tempinputStr=temp.old_text.toLowerCase();
        //           boolean found=false;
//                   for (String tempStr1: ArrayKey){
  //                      if (tempinputStr.contains(tempStr1))  {found=true; break;}
    //               }
      //             if (!found) continue;
                  tweetClass myTweetClass=new tweetClass(temp.user_Id, temp.tweet_ID, temp.old_text);
                   temp.text=myTweetClass.getOnlyKeyText(',').toString();                  
                   
                    double opinionCount[] = {0, 0, 0};
                    tokens.clear();
                    char v_tweet_Array[]=myTweetClass.getTokenTagList(tokens);                    
                    G_SentimentClass.textSentiment(tokens, v_tweet_Array, opinionCount);
                    temp.opinion ='X';
                    //if (!Tony_uq_tw)   
                        temp.opinion=G_SentimentClass.getOpion();
                    if (temp.ReP_ReT=='T'||temp.ReP_ReT=='P')
                        numRep++;
                    char x = NLP.findSentiment(temp.old_text);                        
                    if (temp.opinion=='N') if (x=='N')  temp.opinion='N'; else temp.opinion='X';
                    
//                    else  { String tempS = (String) data.get("opintion");  myOpinon=tempS.charAt(0); }
                    myTweetClass.setOpionFlag(temp.opinion);
                   
                    BasicDBObject basicObj =new BasicDBObject(); //GetTweetRecord(tweet, country_code, city_code, state_code, post_code, InsertMethod);
                    basicObj.put("tweet_ID", temp.tweet_ID);
                    basicObj.put("user_Id", temp.tweet_ID);
                    basicObj.put("user_name", temp.user_Name);                    
                    basicObj.put("Latitude", temp.lati);
                    basicObj.put("Longitude", temp.logi);
                    basicObj.put("R_Post_Code", temp.post_code);
                    basicObj.put("LongCreateTime", temp.LongCreate_Time);
                    basicObj.put("opinion", temp.opinion);                    
                    basicObj.put("tweet_text",temp.text);
                    basicObj.put("old_text",temp.old_text);
                    if (temp.Company_Name!=null) {
                        basicObj.put("Company",temp.Company_Name);
                        if (temp.Topics!=null)
                            basicObj.put("Topics",temp.Topics);
                    }
                    
                    SearchCollection.ensureIndex(new BasicDBObject("tweet_ID", 1), new BasicDBObject("unique", true));

                    try {
                        SearchCollection.insert(basicObj);
                        Cluster_Tweets.add(temp.tweet_ID);
                    } catch (com.mongodb.MongoException ex) {
                        System.err.println("tweet_Id=" + temp.tweet_ID + "\t" + ex);
                    }
                    
            }
            // get Cluster_Tweets
            if (Cluster_Tweets.size()<1) return false;
            
            StringBuffer myIdBuffer=new StringBuffer();
            for (Long temp1: Cluster_Tweets){
                myIdBuffer.append(temp1);
                myIdBuffer.append(',');                
            }
            myIdBuffer.setLength(myIdBuffer.length()-1);
        // the mysql insert statement
            String query = " insert into eventTable (postcode, Names, Begin_Time,num_of_Posts, Cluster_Tweets,num_of_Retweets)"
              + " values (?, ?, ?, ?, ?,?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setString (1, postCode);
            preparedStmt.setString (2, event_Names);            
            preparedStmt.setLong(3, createTime);
            preparedStmt.setInt(4, num);
            preparedStmt.setString(5,myIdBuffer.toString());
            preparedStmt.setInt(6, numRep);
            // execute the preparedstatement
            preparedStmt.execute();
       
            
            //Conn.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MonoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private String[] pickMentions(UserMentionEntity[] mentioned) {

        LinkedList<String> friends = new LinkedList<String>();

        for (UserMentionEntity x : mentioned) {
            //String  y=x.getName();// Long.toString(x.getId());
            friends.add(x.getScreenName());
        }
        String a[] = {};
        return friends.toArray(a);
    }

    private String[] pickMedias(MediaEntity[] mev) {
        LinkedList<String> friends = new LinkedList<String>();

        for (MediaEntity me : mev) {
            //String  y=x.getName();// Long.toString(x.getId());
            String imageurl = null;
            if (me.getType().equals("photo")) {
                imageurl = me.getMediaURL();
            } else {
                imageurl = me.getText();
            }
            friends.add(me.getType());
            friends.add(imageurl);
        }
        String a[] = {};
        return friends.toArray(a);

    }

 
}
