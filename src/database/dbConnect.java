package database;

import java.sql.Connection;
import java.sql.DriverManager;


public class dbConnect {
	static Connection con=null;
	
	
public static Connection getConnection(String db_name,String user_name,String password)
{
    try
    {
        Class.forName("com.mysql.jdbc.Driver");
        con=DriverManager.getConnection("jdbc:mysql://localhost/"+db_name+"?user="+user_name+"&password="+password);
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }

  
    return con;        
}
}