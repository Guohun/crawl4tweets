/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter4j;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Friend;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
/**
 *
 * @author zgh
 */
public class FacebookSign {
    public static void main1(String argv[]){
             Facebook facebook = new FacebookFactory().getInstance();
        //request.getSession().setAttribute("facebook", facebook);
        //StringBuffer callbackURL = request.getRequestURL();
      //  int index = callbackURL.lastIndexOf("/");
    //    callbackURL.replace(index, callbackURL.length(), "").append("/callback");
  //      response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackURL.toString()));
    }
    public static void main(String[] args) throws FacebookException {
        // Make the configuration builder 
        ConfigurationBuilder confBuilder = new ConfigurationBuilder(); 
        confBuilder.setDebugEnabled(true); 
        // Set application id, secret key and access token 
        
        confBuilder.setOAuthAppId("573061972819701"); 
        confBuilder.setOAuthAppSecret("a453c6eeac338a267ef790cbbef8388b"); 
//        confBuilder.setOAuthAccessToken("2jskjdbjbdjb"); // Set permission 
            confBuilder.setOAuthAccessToken("CAAIJMmNSZCvUBAI2cg1RxkYtZA6OpRc0h0Iuw4eBd5X14ntLOfZCZAZBvoZCZB4WGfut4ZCUpb1jVpoaxRnxpJpfeq4VRPyeyq8SLCNYcUUKPjYExZByZAyzM0lMjZAFeIUgW1mERNTt3dbYLuRbyRRiGqxAzhWIx39kZCzutywbQRGjocjyQobRZCZC1RmwBkb1XOIWXF5x27rGcgyBrWJtxNeCQuAlLi6NClAXgZD");
        
        confBuilder.setOAuthPermissions("email,publish_stream, id, name, first_name, last_name, generic"); 
        confBuilder.setUseSSL(true); 
        confBuilder.setJSONStoreEnabled(true); 
// Create configuration object 
        Configuration   configuration = confBuilder.build(); // Create facebook instance 
        FacebookFactory ff = new FacebookFactory(configuration); 
        Facebook facebook = ff.getInstance(); 

             //Page friendListData = facebook.getPage("/me/taggable_friends"); //.Get("/me/friends");
        
       // facebook4j.ResponseList<Post>  friendListData = facebook.getFeed("/me/taggable_friends"); //.Get("/me/friends");
     //System.out.println("-------------------------");
     
     //System.out.println(friendListData.size());

   String query = "select message FROM stream where post_id IN (select post_id FROM like WHERE post_id IN (select post_id from stream where source_id ='your_page_id' and likes.count > 0 limit 200) AND user_id IN (SELECT uid2 FROM friend WHERE uid1 = me() ))";
                //execute query
                facebook4j.internal.org.json.JSONArray jsonArray = facebook.executeFQL(query);
              
           //parse the json returned  
           for (int i = 0; i < jsonArray.length(); i++)
               {
                    facebook4j.internal.org.json.JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                System.out.println(jsonObject.get("message"));
            } catch (facebook4j.internal.org.json.JSONException ex) {
                Logger.getLogger(FacebookSign.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
                }
           
     facebook4j.ResponseList<Friend> myFriends = facebook.getFriends();// friends(); fetchConnection("me/friends", User.class);
     System.out.println("-------------------------");
     System.out.println(myFriends.getCount());
			
				for(Friend user: myFriends)
					System.out.println("/picture\"/></td><td>" + user.getName() +"</td><td>" + user.getId() + "</td></tr>");	

        
        // Get facebook posts 
            String results = getFacebookPostes(facebook); 
            String responce = stringToJson(results); // Create file and write to the file 
            System.out.println(results); 
            System.out.println(responce); 
            System.out.println("..........Writing complete"); 
    } 

public static String getFacebookPostes(Facebook facebook) throws FacebookException { 
// Get posts for a particular search 
        facebook4j.ResponseList<Post> results = facebook.getPosts("Reebok"); 
        return results.toString(); 
} 
public static String stringToJson(String data) { 
        try {
            //JSONObject jsonObject=null;
            JSONObject jsonObject = new JSONObject(data);         //"{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
            //jsonObject = JSONObject.fromObject(data);
            JSONArray message = (JSONArray) jsonObject.get("message");
            System.out.println("Message : "+message); 
            return "Done";
        } catch (twitter4j.JSONException ex) {
            Logger.getLogger(FacebookSign.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "100005817192892";
}
//https://graph.facebook.com//100005817192892/?access_token=CAAIJMmNSZCvUBAHML99ZBXL4tEqjHvNNlao29mlp2lXGbb2l5tV2S2NvMymKidCZBBZASoQ3onwZCnM35FYaul1woqPW2y48FKyZCrKCiEcLczTL7IZCZAIDyIOQbHaywZBVnxgxMExn9YEl5Ig6ZBjZB63wkwn3oXZAQTOdrRlHVfCikxUk3f5ORRwrDId3eHLO2pFQzuCSwlZAJb3q45aikIycz6cJNVZB6CegUZD
//Read more: http://mrbool.com/how-to-integrate-facebook-api-in-java/29175#ixzz3Ym5g4qrH
/*
App ID	573061972819701 : healthconsultant
User ID
840958345996243 : Guohun Zhu
User last installed this app via API v2.x
CAAIJMmNSZCvUBAI2cg1RxkYtZA6OpRc0h0Iuw4eBd5X14ntLOfZCZAZBvoZCZB4WGfut4ZCUpb1jVpoaxRnxpJpfeq4VRPyeyq8SLCNYcUUKPjYExZByZAyzM0lMjZAFeIUgW1mERNTt3dbYLuRbyRRiGqxAzhWIx39kZCzutywbQRGjocjyQobRZCZC1RmwBkb1XOIWXF5x27rGcgyBrWJtxNeCQuAlLi6NClAXgZD

*/

}
