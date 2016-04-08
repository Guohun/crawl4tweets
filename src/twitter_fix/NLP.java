package twitter_fix;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
        //Properties props = new Properties();
         //props.put("annotators", "tokenize, ssplit, parse, sentiment");
        //pipeline = new StanfordCoreNLP(props);
        pipeline = new StanfordCoreNLP("twitter_fix/MyPropFile.properties");
    }

    public static char findSentiment(String tweet)
    {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0)
        {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
            {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                
                String partText = sentence.toString();
              //  System.out.println(sentiment+"\t"+partText);
                if (partText.length() > longest) 
                {
                    
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        //return mainSentiment;
        switch (mainSentiment) {
        case 0:
            return 'N';
        case 1:
            return 'N';
        case 2:
            return 'X';
        case 3:
            return 'P';
        case 4:
            return 'P';
        default:
            return 'X';
        }
        
    }
    
    public static void testgraph(String tweet){
     // create an empty Annotation just with the given text
        Annotation document = new Annotation(tweet);
    SemanticGraph dependencies=null;
        // run all Annotators on this text
        pipeline.annotate(document);        
     List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
          // traversing the words in the current sentence
          // a CoreLabel is a CoreMap with additional token-specific methods
          for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class);
          }

          // this is the parse tree of the current sentence
          Tree tree = sentence.get(TreeAnnotation.class);

          // this is the Stanford dependency graph of the current sentence
           dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            String dep_type = "CollapsedDependenciesAnnotation";
            System.out.println(dep_type+" ===>>");
         System.out.println("Sentence: "+sentence.toString());
         System.out.println("DEPENDENCIES: "+dependencies.toList());
         System.out.println("DEPENDENCIES SIZE: "+dependencies.size());
          
        }
       List<SemanticGraphEdge> edge_set1 = dependencies.getAllEdges(IndexedWord.NO_WORD, IndexedWord.NO_WORD);// .getEdgeSet();
      int j=0;
        for(SemanticGraphEdge edge : edge_set1)
        {
                  j++;
                  System.out.println("------EDGE DEPENDENCY: "+j);
                  Iterator<SemanticGraphEdge> it = edge_set1.iterator();
                  IndexedWord dep = edge.getDependent();
                  String dependent = dep.word();
                  int dependent_index = dep.index();
                  IndexedWord gov = edge.getGovernor();
                  String governor = gov.word();
                  int governor_index = gov.index();
                GrammaticalRelation relation = edge.getRelation();
               System.out.println("No:"+j+" Relation: "+relation.toString()+" Dependent ID: "+dependent.intern()+" Dependent: "+dependent.toString()+" Governor ID: "+governor.intern()+" Governor: "+governor.toString());

        }//end of for
    }
    
    // test the module
    public static void main(String args[])
    {
        //String tweets="@sberg27 Hi Shelby. Cool poster! :) my group works on wheat/barley roots @ UQ. I'm interested to find out more. Who is you primary advisor?";
        
        String tweets1="Good to meet Minister for Science at Aus-China Symp Neuroscience hearing her inspiring speech @karenandrewsmp http://t.co/2bpclBTrDu";
        String tweets2="i really wish that im studying in UQ XDDDD";
        String tweets3="@UQ_News  it may be too late if you have not studied hard when the Jacarandas blossom - but don't panic: better to be late than never";
        String tweets="1PM cancer causing worm may have healing powers, the health of our waterways & Brisbane celebrates its diversity... 98.1FM via @radio4eb";
    	init();
    	char x = findSentiment(tweets);
        System.out.println("result=\t"+x);
        //testgraph(tweets);
    	
    }
}