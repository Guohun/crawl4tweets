package twitter_fix;

//import cmu.arktweetnlp.Tagger;
//import cmu.arktweetnlp.Tagger.TaggedToken;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tweetfbnlp.SocialBaseClass;
import util.MyQuickSort;
import util.MyStringArray;
import static util.MyStringArray.isNoisy;
import util.Stopwords;
//import util.Stopwords;

//import Common.Stopwords;
//import edu.mit.jwi.item.Word;


public class tweetClass extends SocialBaseClass {
    Long MytweetId;

    public tweetClass(Long userid, String tweetid, String text) {
        super(userid, tweetid, text,SocialBaseClass.Social_Type.TW);
        this.MytweetId=Long.valueOf(tweetid);
    }

     public tweetClass(Long userid, Long tweetid, String text) {
         
          super(userid, String.valueOf(tweetid), text,SocialBaseClass.Social_Type.TW);
          this.MytweetId=tweetid;
    }

    

}
