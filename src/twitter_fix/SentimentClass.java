/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter_fix;

import cmu.arktweetnlp.Tagger;
import static cmu.arktweetnlp.Twokenize.tokenizeRawTweetText;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.TfIdf;

/**
 *
 * @author uqgzhu1
 */
public class SentimentClass {

    String base ="/home/uqgzhu1/datasets/data/"; //System.getProperty("user.dir") + "/data/";
    private String[] FileLists = {"words/positive.txt", "words/negative.txt", "negationWords.txt"};

    HashSet<String> negativeList = null;
    HashSet<String> positiveList = null;
    HashSet<String> negationList = null;
    char OpionFlag='X';
    
    public SentimentClass() {
        positiveList = new HashSet<String>();
        negativeList = new HashSet<String>();
        negationList = new HashSet<String>();

        try {
            for (int i = 0; i < 3; i++) {
                FileReader fr = new FileReader(base + FileLists[i]);
                BufferedReader textReader = new BufferedReader(fr);
                String aLine = null;
                while ((aLine = textReader.readLine()) != null) {
                    if (i == 0) {
                        positiveList.add(aLine);
                    } else if (i == 1) {
                        negativeList.add(aLine);
                    } else {

                        negationList.add(aLine);

                    }
                }//end while		
                textReader.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SentimentClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SentimentClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int totalWordOcurance(String word, char Flag) {

        int wordCounter = 0;
        if (word.isEmpty()) return 0;
        if (word.charAt(0) == '#' || word.charAt(0) == '@') {
            word = word.substring(1);
        }
        
        //     System.err.println(word);
        if (Flag == 'P') {
            if (this.positiveList.contains(word)) {
                wordCounter++;
            }

        } else if (this.negativeList.contains(word)) {
            wordCounter++;
        }
        return wordCounter;
    }//end method
    //https://support.skype.com/en/faq/FA12330/what-is-the-full-list-of-emoticons
 public  static String[] SadSymbols = {":(", ":=(", ":-(", ":((", "8-x", "X(", "???", "???", "???", "???", ";(", ";-(", ";=("};
 public  static String[] HappySymbols = {":)", ":=)", ":-)", "???", "???", "???", "???", ":D", ":=D", ":-D", ":d", ":=d", ":-d", "8=)", "8-)", "B=)", "B-)", "(`v`)", "(-v-)"};
 public  static String[] HappyVerb={"??"};

    public double[] textSentiment(List<String> textToken, char[] tagFlags, double returnData[]) {
        int percentOfAllText = 0;
        int PpercentText = 0;
        int NpercentText = 0;
        int _percentText = 0;
        boolean uq_flag=false;
        boolean researcher=false;
        char oldOpinon[]=new char [tagFlags.length];
        L1:        
        for (int i = 0; i < textToken.size(); i++) {
            String tempWord = textToken.get(i);
            if (tempWord==null) continue;
            if (tempWord.isEmpty()) continue;
            if (tagFlags[i] == ',') {
                
                if (tempWord.charAt(0) == '?') {
                    percentOfAllText = 1;
                    PpercentText = 0;
                    NpercentText = 0;
                    _percentText = 1;
                    oldOpinon[i]='X';
                }
                continue;
            }
            if (tagFlags[i] == 'P') {
                if (i<textToken.size()-1) { if (tagFlags[i+1] == 'S') { _percentText++; oldOpinon[i]='X'; i++; continue; } 
                        if (tagFlags[i+1] == 'N'&&textToken.get(i+1).contains("drought")) { _percentText++; i++; oldOpinon[i]='X'; continue;} 
                }
            }     
            if (tagFlags[i] == '^')        if (tempWord.contains("#uq")) { uq_flag=true; continue;}
            if (tagFlags[i] == 'N')        if (tempWord.contains("researcher")) { researcher=true; continue;}
            if (tagFlags[i] == 'U')        continue;
            if (tagFlags[i] == 'V'||tagFlags[i] == 'A') {
                    if (tagFlags[i] == 'V') for (String x : HappyVerb) {
                        if (tempWord.contains(x)) {
                            oldOpinon[i]='P';
                            PpercentText += 2;
                            continue L1;
                        }
                    }   
                    if (i<tagFlags.length-1){                        
                        if (tagFlags[i+1]=='$'){    //rank $1
                            if (tempWord.contains("rank"))  { PpercentText += 2; oldOpinon[i]='P';}
                            else if (tempWord.contains("close")) { _percentText++; oldOpinon[i]='X';}
                            i++;                            
                            continue L1;                            
                        }
                    }
            }
            
            //
            if (tagFlags[i] == 'E' || tagFlags[i] == 'G'|| tagFlags[i] == '!') {
                    if (tempWord.isEmpty()) continue;
                    for (String x : SadSymbols) {
                        if (tempWord.contains(x)) {
                            NpercentText += 2;
                            oldOpinon[i]='N';
                            continue L1;
                        }
                    }
                    for (String x : HappySymbols) {
                        if (tempWord.contains(x)) {
                            //System.err.println(tempWord);
                            PpercentText += 2;
                            oldOpinon[i]='P';
                            continue L1;
                        }
                    }
            }
            
            percentOfAllText++;
            boolean deny = false;
            if (i > 0) { //prevWord used to check for negation words                
                deny = isNegationWord(textToken.get(i - 1));
                percentOfAllText--;
            }//end if
            
            int positive = totalWordOcurance(tempWord, 'P');
            int negative = totalWordOcurance(tempWord, 'N');
            if (positive == 0 && negative == 0) {
                oldOpinon[i]='X';
                if (deny)   _percentText++;  
            }else{
                if (negative>0&&!deny) { // fight against cancer 
                    if (tempWord.contains("#drought")){                        
                        if (i<tagFlags.length-1) if (textToken.get(i+1).contains("adaptation")){ _percentText++; continue;}
                    }else  if (tempWord.contains("cancer")&&i>2){
                        for (int j=i-1;j>=0;j--){
                            if ((tagFlags[j]=='N')&&(oldOpinon[j]=='N')) { oldOpinon[j]='P'; oldOpinon[i]='P'; 
                                 PpercentText+=2; NpercentText-=2;
                                continue L1;
                            }else if((tagFlags[j]=='V')&&(oldOpinon[j]!='N')){
                                if (textToken.get(j).contains("monitor")) {
                                    oldOpinon[j]='X'; oldOpinon[i]='X'; 
                                    _percentText+=2; 
                                continue L1;
                                }
                            }
                        }
                    }
                }                
                if (deny) {
                    PpercentText -= positive;
                    NpercentText -= negative;
                    if (positive>negative) oldOpinon[i]='N';
                    else if (positive<negative) oldOpinon[i]='P';
                } else {
                    PpercentText += positive;
                    NpercentText += negative;
                    if (positive>negative) oldOpinon[i]='P';
                    else if (positive<negative) oldOpinon[i]='N';                    
                }                
                
            }
        //     System.err.println(tempWord+":"+PpercentText+":"+NpercentText+":"+_percentText);    
        }//end for

        returnData[0] = (double) PpercentText / (double) percentOfAllText;
        returnData[1] = (double) NpercentText / (double) percentOfAllText;
        returnData[2] = (double) _percentText / (double) percentOfAllText;
        
        if (PpercentText>NpercentText&&PpercentText>=_percentText)
            this.OpionFlag='P';
        else if (PpercentText<NpercentText&&NpercentText>=_percentText){
            if (uq_flag&&researcher) this.OpionFlag='X'; else this.OpionFlag='N';
        }
        else 
            this.OpionFlag='X';
        
        return returnData;
    }//end method	
public  char getOpion(){
    return this.OpionFlag;
}
    /*
     * Check to see if a word is a negation word
     * Receives the word to check
     * Returns a boolean value indicating if the word is a negation word or not
     */
    private boolean isNegationWord(String word) {
        if (this.negationList.contains(word)) {
            return true;
        } else {
            return false;
        }//end if
    }//end if

    public static void main(String args[]) {
        String inputStr2 = "@yenohs_ear told bus driver told 205 and 84 were closed in both directions so I imagine he's in the move from the airport";
        String inputStr = "I?? bills. @emmasq can you tell me much bout hugh Beasley emma. Ay ayosss! :((( exam in less than 6 hours and 45minutes . may Allah ease . bismillah ??? (@ The University of Queensland - @uq_news) https://t.co/EdsXgRsihi";
        String inputStr1 = "@emmasq can you tell me much bout hugh Beasley emma ? Rookie listed for us set to probably debut on sunday for the lions";
        String inputStr3 = "Oh jeez , you hear about Colt Brennan 's serious car accident ? I remember being in Hawaii a few years back when Hawaii 's coach nearly died too";
        String inputStr4 = "Since everyone #loves #burgers #grassfed from #Grilld (Grill'd) really good #burger and #fries. Great?? https://t.co/CmpYZg4lKS";
        String inputStr5 = "party?????";
        String inputStr6 = "If she texts you \"3am\" start the car, you about to get some fam?????????";
        String inputStr10 = "I'm at QUT law School in Brisbane, QLD https://t.co/cCCcWwSUJB";
        String inputStr7 = "orientation week for semester two (@ QUT Gardens Point in Brisbane, QLD) https://t.co/An4W9jRqMW http://t.co/OSnUxUrO1V";
        String inputStr8 = "View from the Milton citicat stop this morning. Chilly. http://t.co/eg8btQCDup";
        String inputStr9 = "good night twitter (`v`)";
        String inputStr11 = "Band drives ????????? #dorks @ ??? if you wanna be my lovaa ??? https://t.co/C9oC3LmiPf";
        String inputStr12 = "Acah-acah candid la kononnya ???";
        String inputStr13 = "#Nike #AirMax #jalanjalan #AmsyarInPerth #Northbridge #Perth #WA @?? https://t.co/t6aNS9gsLb";        
        String inputStr14 = "Working! (@ James Cook University in Townsville  Queensland) https://t.co/R3DKhcLQBV";
        String inputStr15 = "RT @ECDC_UQ: Welcome to the official Instagram's page for the University of Queensland's  Early Cognitive Development? https://t.co/bc4NebHa70";
        String inputStr16 = "follows #UQ restored @customshousebne to its full grandeur. The lovely building's #history, @_GreatSouthEast, Sunday 5.30pm. http://t.co/lKZs8jNqph";
        String inputStr17 = "RT @uqlibrary: The #Fryer Lecture in Australian Literature has been postponed. We are hoping to reschedule though. Thanks to everyone who h?";
        String inputStr18 = "Happy Canada Day! Here??s the chemistry of the country??s greatest export: http://t.co/f812sdpOzX";
        String inputStr19 = "RT @compoundchem: Happy Canada Day! Here??s the chemistry of the country??s greatest export: http://t.co/f812sdpOzX";
        String inputStr20 = "@marybansaron I'm okay it's not the end of the world ?????????";
        String inputStr21 = "RT @stevesayers1: SNP call #Corbyn a #Tory oh ffs! ????????? seems if you are #socialist and critique SNP u r Tory #SNPOut 2 say Tory=Evil http:/??";
        String inputStr22 = "Congratulations to all @griffith_uni students walking across the GCCEC stage graduating today! ???????? https://instagram.com/p/5gK8I1l2Yo/ ";
        String inputStr23 ="@BondUniversity and @Griffith_Uni have their open days this weekend; find out what's on: http://t.co/1TRmY1HV0M";
        String inputStr24 ="RT @moregoldcoast: @BondUniversity and @Griffith_Uni have their open days this weekend; find out what's on: http://t.co/1TRmY1HV0M";
        String inputStr25 =   "GOLD COAST: Uni open days music to students?? ears - http://t.co/G3xOBQarey via @moregoldcoast";
        String inputStr26 =   "There are no spots free at the Edward St / Queen St station #1.";
        String inputStr27 =   "Station #12 at Albert St Mall / Adelaide St now has plenty of bikes for hire.";
        String inputStr28 =   "Station #146 at Brisbane St / Glen Rd now has plenty of bikes for hire.";
        String inputStr29 =   "Wind 3.6 km/h NE. Barometer 1009.6 mb, Rising slowly. Temperature 30.9 °C. Rain today 0.3 mm. Humidity 10%";
        String inputStr30 =   "#UQ St Lucia Open Day is just over a week away - you can plan your day here: http://t.co/1dlG8F5H3I http://t.co/K1NhQVrrvv";
        String inputStr31 =   "Our amazing Adjunct Prof Virginia San Fratello won @IIDA_HQ Educator of the year! Our students loved her MArch crits. http://bit.ly/1h24j4H";
        String inputStr32 =   "I'm at Z Block - Creative Industries in Kelvin Grove, QLD https://t.co/iRX3lylDpO";
        
        SentimentClass w = new SentimentClass();
        TfIdf tempTf=new TfIdf();
        
            //w.SentenceDetect();
        List<String> tokens = new ArrayList<String>();
        tweetClass tempTweet = new tweetClass(Long.valueOf(10000), Long.valueOf(10000), inputStr22);
        char[] v_tweet_Array = tempTweet.getTokenTagList(tokens);
        if (v_tweet_Array==null) return;
        for (int i = 0; i < tokens.size(); i++) 
            System.out.printf("%s\t%s\n", v_tweet_Array[i], tokens.get(i));
//        double entropy=calculatePermutationEntropy(v_tweet_Array,4);
        tempTf.Add("40721", tokens, v_tweet_Array);
        
        tokens.clear();
        
         tempTweet = new tweetClass(Long.valueOf(10000), Long.valueOf(10000), inputStr27);         
        v_tweet_Array = tempTweet.getTokenTagList(tokens);
        if (v_tweet_Array==null) return;
        for (int i = 0; i < tokens.size(); i++) 
            System.out.printf("%s\t%s\n", v_tweet_Array[i], tokens.get(i));
        //double entropy1=calculatePermutationEntropy(v_tweet_Array,4);                
        //System.out.printf("En=%6.4f\tEn1=%6.4f\n", entropy,entropy1);
        tempTf.Add("40722", tokens, v_tweet_Array);
        tokens.clear();
        
        System.out.println("----------------------");
                   
        
        String nag2=  "RT @timdunneAPR2P: Radicals and Reactionaries in 20C International Thought.Great new ed bk by @DrIanHall @OrtiOricellari @POLSISEngage  htt??";
        
        String nag3=  "descobri HOJE que ONTEM teve algum evento com MUITAS CABRAS na uq E EU NAO SABIAA A A A A  A AAA A A A";
        String nag4=  "@resourcerules SMI University of Queensland for an interesting PhD confirmation related 2 humanrightslaw & #mining. http://t.co/Guu9Wu49w6";
        String nag1=  "Thanks to all who have supported #UQ. Your generous gifts help researchers & students create change in the world:";
        tempTweet = new tweetClass(Long.valueOf(10000), Long.valueOf(10000), nag1);         
        v_tweet_Array = tempTweet.getTokenTagList(tokens);
        if (v_tweet_Array==null) return;
        for (int i = 0; i < tokens.size(); i++) 
            System.out.printf("%s\t%s\n", v_tweet_Array[i], tokens.get(i));
/*        tempTf.Add("40723", tokens, v_tweet_Array);
        String returnStr[] = {"", "", "",""};
       
        StringBuffer tempBuf=tempTweet.getOnlyKeyText(',');
        System.out.println(tempBuf);
        String Location[]=tempBuf.toString().split(",");//  {"student","walking","world"};
        double sortD[]=tempTf.tfIdfCalculatorNoSave_new(Location);
        for (int i=0;i<sortD.length;i++){
            System.out.printf("%s\t%6.4f\n",Location[i],sortD[i]);
        }
*/        
        double returnData[] = {0, 0, 0};        
        double[] temp = w.textSentiment(tokens, v_tweet_Array, returnData);
        System.out.println("opinion=\t"+w.getOpion());
        System.exit(0);
        
                
        
     //   int times[] = tempTweet.getHighTF(returnStr);
       // System.out.printf("P=%s:%d\tN=%s:%d\tX=%s:%d\n", returnStr[0], times[0], returnStr[1], times[1], returnStr[2], times[2]);

        System.out.printf("P=%4.3f,N=%4.3f X=%4.3f\n", returnData[0], returnData[1], returnData[2]);

        // w.chunk(inputStr);
        System.out.println("------------------------------------------------");
          //  w.Parse(inputStr);

    }

    public char findSentiment(String old_text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
