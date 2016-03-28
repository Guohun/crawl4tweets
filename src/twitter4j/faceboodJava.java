/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;


import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookBase;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Friend;
import facebook4j.Like;
import facebook4j.PagableList;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.auth.OAuthSupport;
import facebook4j.conf.*;
import java.util.List;

/**
 *
 * @author zgh
 */
public class faceboodJava {
 public static  Configuration createConfiguration() {
    ConfigurationBuilder confBuilder1 = new ConfigurationBuilder();

    confBuilder1.setDebugEnabled(true);
    confBuilder1.setOAuthAppId("573061972819701");
    confBuilder1.setOAuthAppSecret("e278a6516bb997b18c7dbd6986492151");
    confBuilder1.setUseSSL(true);
    confBuilder1.setJSONStoreEnabled(true);
    Configuration configuration = confBuilder1.build();
    return configuration;
}
public static void main(String[] argv) throws FacebookException {
    Configuration configuration =  createConfiguration();
    FacebookFactory facebookFactory = new FacebookFactory(configuration );
    Facebook facebookClient = facebookFactory.getInstance();
   
          
    AccessToken accessToken = null;
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
        facebook4j.ResponseList <Post> feed = facebookClient.getFeed("uniofqld", //187446750783
                new Reading().limit(25));
        
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
            PagableList<Comment> comments = post.getComments();
            System.out.println(date+"\t"+id+":"+name+" Post:"+message);
            for (Comment  x: comments){
                System.out.println("com ent by:"+x.getFrom().getId()+"\t"+x.getFrom().getName()+"|"+x.getMessage());
            }
            
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
     System.exit(0);
     Page friendListData = facebookClient.getPage("/me/friends"); //.Get("/me/friends");
     System.out.println("-------------------------");
     System.out.println(friendListData.getAbout());
        
     ResponseList<Friend> myFriends = facebookClient.getFriends();// friends(); fetchConnection("me/friends", User.class);
     System.out.println("-------------------------");
     System.out.println(myFriends.getCount());
			
				for(Friend user: myFriends)
					System.out.println("/picture\"/></td><td>" + user.getName() +"</td><td>" + user.getId() + "</td></tr>");	
			
}

//User Token	CAAIJMmNSZCvUBAHML99ZBXL4tEqjHvNNlao29mlp2lXGbb2l5tV2S2NvMymKidCZBBZASoQ3onwZCnM35FYaul1woqPW2y48FKyZCrKCiEcLczTL7IZCZAIDyIOQbHaywZBVnxgxMExn9YEl5Ig6ZBjZB63wkwn3oXZAQTOdrRlHVfCikxUk3f5ORRwrDId3eHLO2pFQzuCSwlZAJb3q45aikIycz6cJNVZB6CegUZD

//App Token	573061972819701|7zVUfb8iRUqaF4qBhz95GmqGXYQ

//https://developers.facebook.com/tools/explorer/?method=GET&path=me%3Flocation&version=v2.3&

//http://findfacebookid.com/
}
