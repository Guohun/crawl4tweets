package util;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {
	private static Connection mysqlconn;
        private DBCollection collection=null;
        private DBCollection Usercollection=null;
	private static String url = "jdbc:mysql://localhost:3306/twitter?characterEncoding=utf-8";
	//jdbc:mysql://localhost:3306/twitter?zeroDateTimeBehavior=convertToNull
	private static String uname = "root";
	private static String pwd = "1234321";
	
	public void openConnect(){
        //  	conn = null;
		try{
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection(url,uname,pwd);
                        
                        MongoClient mongo = new MongoClient("localhost", 27017);
                        DB db = mongo.getDB("test");
                        collection = db.getCollection("twitter_stream");
                        Usercollection = db.getCollection("user_tws");
                        
		}
		catch (UnknownHostException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
    }

    public static Connection openMySqlConnect(){
            if (mysqlconn!=null) return mysqlconn;
            try {
                mysqlconn= null;
                
                Class.forName("com.mysql.jdbc.Driver");
                mysqlconn = DriverManager.getConnection(url,uname,pwd);
                
                return mysqlconn;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
    }
        
    
    public void closeMySqlConnect(){
        try{            
            mysqlconn.close();            
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }        
    }


    /**
     * @return the collection
     */
    public DBCollection getCollection() {
        return collection;
    }

    /**
     * @return the Usercollection
     */
    public DBCollection getUsercollection() {
        return Usercollection;
    }
}
