/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.examples.basic.Crawler;
//import edu.uci.ics.crawler4j.examples.basic.WriteFile;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.auth.OAuthSupport;
import facebook4j.conf.Configuration;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import twitter4j.auth.AccessToken;
import static twitter4j.faceboodJava.createConfiguration;

/**
 *
 * @author zgh
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ButtonTwitter = new javax.swing.JButton();
        ButtonFacebook = new javax.swing.JButton();
        ButtonCrawler = new javax.swing.JButton();
        Combox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jOutPutText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Save to");

        ButtonTwitter.setText("GetTwitter");
        ButtonTwitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTwitterActionPerformed(evt);
            }
        });

        ButtonFacebook.setText("GetFaceBook");
        ButtonFacebook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFacebookActionPerformed(evt);
            }
        });

        ButtonCrawler.setText("Crawler");
        ButtonCrawler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCrawlerActionPerformed(evt);
            }
        });

        Combox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UQ", "QUT", "BUS", " " }));

        jLabel2.setText("Input Key words");

        jTextField2.setText("/home/zgh/octave");

        jOutPutText.setColumns(20);
        jOutPutText.setRows(5);
        jScrollPane1.setViewportView(jOutPutText);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonTwitter, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 15, Short.MAX_VALUE)
                        .addComponent(ButtonFacebook)
                        .addGap(27, 27, 27)
                        .addComponent(ButtonCrawler)
                        .addContainerGap())
                    .addComponent(jTextField2)
                    .addComponent(Combox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonTwitter)
                    .addComponent(ButtonFacebook)
                    .addComponent(ButtonCrawler))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField2))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Combox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonFacebookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFacebookActionPerformed
    try{                                               
        // TODO add your handling code here:
        Configuration configuration =  createConfiguration();
        FacebookFactory facebookFactory = new FacebookFactory(configuration );
        Facebook facebookClient = facebookFactory.getInstance();
        
        
        facebook4j.auth.AccessToken accessToken = null;
        try{
            OAuthSupport oAuthSupport = new OAuthAuthorization(configuration );
            accessToken = oAuthSupport.getOAuthAppAccessToken();
            
        }catch (FacebookException e) {
            System.err.println("Error while creating access token " + e.getLocalizedMessage());
        }
        facebookClient.setOAuthAccessToken(accessToken);
        
//results in an error says {An active access token must be used to query information about the current user}
        // facebookClient.postStatusMessage("Hello World from Facebook4J."); 
        //facebook4j.ResponseList<Post> feed = facebookClient.getFeed("usq");    //eclipse.org
            String Url =(String)Combox.getSelectedItem();  //perentUrl.nextLine();
            if (Url.compareTo("UQ")==0) {
                Url="uniofqld";
            }
            if (Url.compareTo("QUT")==0) {
                Url="qutguild";
            }
            if (Url.compareTo("BUS")==0) {
                Url="LinkTransfers";
            }
            
            

        facebook4j.ResponseList <Post> feed = facebookClient.getFeed(Url, //187446750783
                new Reading());
        
        // For all 25 feeds...
        for (int i = 0; i < feed.size(); i++) {
            // Get post.
            Post post = feed.get(i);
            // Get (string) message.
            String message = post.getMessage();
            // Print out the message.
            //System.out.println(message);

            // Get more stuff...
            
            String date = post.getCreatedTime().toString();
            String name = post.getFrom().getName();
            String id = post.getId();
            this.jOutPutText.setLineWrap(true);
            this.jOutPutText.setWrapStyleWord(true);

            PagableList<Comment> comments = post.getComments();
            //StringBuffer outText =new StringBuffer();
            Document doc=jOutPutText.getDocument();
            doc.insertString(0,"---------------------------------------------------\n",null);
            for (Comment  x: comments){
                
                    doc.insertString(0,"com ent by:"+x.getFrom().getId()+"\t"+x.getFrom().getName()+"|"+x.getMessage()+"\n",null);
               
            }
            doc.insertString(0, date+"\t"+id+":"+name+" Post:"+message+"\n",null);
            
            //setText(outText.toString());
            //PagableList<Like> fetchedLikes = post.getLikes();
            /*
            Paging<Like> paging=null;
            
            do {
            for (Like like : fetchedLikes)
            postLikes.add(like);
            paging = fetchedLikes.getPaging();
            } while ((paging != null)	&& ((fetchedLikes = facebook.fetchNext(paging)) != null));
            int count = postLikes.size();
            */
        }
        System.out.println("--Finish-------------------");        
    }catch (FacebookException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }   catch (BadLocationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButtonFacebookActionPerformed

    private void ButtonTwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonTwitterActionPerformed

        try {
            final String api_key="5xi95sDq8ZEfoy48j7TKOOLBD";
            final String api_secret="COZ38TtQOnlben9s7ecTCkRYE9wGWvLX7vqGrMa0Jm739bPRUZ";
            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";
            // The factory instance is re-useable and thread safe.
            Twitter twitter = TwitterFactory.getSingleton();
            
            if (RunTwitter==false){
                    this.jOutPutText.setLineWrap(true);
                    this.jOutPutText.setWrapStyleWord(true);            
                    AccessToken accessToken = loadAccessToken(1);

                    //twitter.setOAuthConsumerKey("[consumer key]", "[consumer secret]");
                    twitter.setOAuthConsumer(api_key, api_secret);
                    twitter.setOAuthAccessToken(accessToken);
                    RunTwitter=true;
            }
            /*    Status status;
            try {
            status = twitter.updateStatus("Yea Tao Massage");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            } catch (TwitterException ex) {
            Logger.getLogger(SampleStreamExample.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
            
            String Url =(String)Combox.getSelectedItem();  //perentUrl.nextLine();
            if (Url.compareTo("UQ")==0) {
                Url="UQ";
            }
            if (Url.compareTo("QUT")==0) {
                Url="QUT";
            }
            if (Url.compareTo("BUS")==0) {
                Url="BUS brisbane";
            }
            
            Query query = new Query(Url);
            QueryResult result = twitter.search(query);
            //StringBuffer str=new StringBuffer();
            Document doc=jOutPutText.getDocument();
            for (Status status : result.getTweets()) {
                    doc.insertString(0,"@" + status.getUser().getScreenName() + ":" + status.getText()+"\n",null);
            }
            
            if (Url.compareTo("UQ")==0) {
                Url="University of Queensland";
            }
            if (Url.compareTo("QUT")==0) {
                Url="Queensland University Technology ";
            }
            if (Url.compareTo("BUS")==0) {
                Url="bus garden city";
            }
            
            query.query(Url);
            result = twitter.search(query);
            //StringBuffer str=new StringBuffer();
            
            for (Status status : result.getTweets()) {
                    doc.insertString(0,"@" + status.getUser().getScreenName() + ":" + status.getText()+"\n",null);
            }

            
             doc.insertString(0,"---------------------------------------------------\n",null);
        } catch (TwitterException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
          
          

        
    }//GEN-LAST:event_ButtonTwitterActionPerformed

    private void ButtonCrawlerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCrawlerActionPerformed
        // TODO add your handling code here:
   try {
            // TODO add your handling code here:
            
            RobotstxtConfig robotstxtConfig2 = new RobotstxtConfig();
            
            String crawlStorageFolder = this.jTextField2.getText();//"/home/zgh/octave";
            int numberOfCrawlers = 1;
            
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            
            config.setMaxDepthOfCrawling(21);
            config.setMaxPagesToFetch(24);
            
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
            
            
            //Scanner perentUrl = new Scanner(System.in);            
            //System.out.println("Enter full perant Url... example. http://www.domain.co.uk/");
            
            String Url =(String)Combox.getSelectedItem();  //perentUrl.nextLine();
            if (Url.compareTo("UQ")==0) {
                Url="https://groups.google.com/forum/#!forum/colleagues-at-uq-list";
            }
            if (Url.compareTo("QUT")==0) {
                Url="https://groups.google.com/forum/#!search/Queensland$20University$20of$20Technology";
            }
            
            //Scanner keyword = new Scanner(System.in,"utf-8");
            //System.out.println("Enter search term... example. Pies");
            String Search ="student"; //keyword.nextLine();
            
            
            
            this.jOutPutText.setText("Searching domain :" + Url+"\n"+"Keyword:" + Search+"\n");
            //System.out.println("Input Keyword:" + new String(Search.getBytes("utf-8")));
            
            ArrayList<String> DomainsToInv = new ArrayList<String>();
            ArrayList<String> SearchTerms = new ArrayList<String>();
            ArrayList<String> UrlHits = new ArrayList<String>();
            
            DomainsToInv.add(Url);
            SearchTerms.add(Search);
            
            controller.addSeed(Url);
            
            controller.setCustomData(DomainsToInv);
            controller.setCustomData(SearchTerms);
            
            controller.start(Crawler.class, numberOfCrawlers);
            
        /*    WriteFile f = new WriteFile();
            f.openfile(Search);
            f.StartHtml();
            f.addUrl(UrlHits);
            f.EndHtml();        
            f.closeFile();
            */
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_ButtonCrawlerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCrawler;
    private javax.swing.JButton ButtonFacebook;
    private javax.swing.JButton ButtonTwitter;
    private javax.swing.JComboBox Combox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextArea jOutPutText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    private AccessToken loadAccessToken(int i) {
            final String access_token="306033723-ZDjAly4t3LQSZQZuAZcvmkeh8T9deFHeq0PALmSm";
            final String  access_token_secret="NnyDqn9m4bs08ynxTwJvDbcVtbOZ8lVrsVQcP3ih06dLD";                    
            return new AccessToken(access_token, access_token_secret);

    }
    private  boolean RunTwitter=false;
}
