package authentication;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String username= "savvas";
	private final String password= "root";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/USERS";

    //  Database credentials
    static final String USER = "savvas";
    static final String PASS = "root";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
		PreparedStatement stmt=null;
		request.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		String password = request.getParameter("password1");
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
	
		try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		  
		      //STEP 4: Execute a query
		      System.out.println("Inserting records into the table...");
		  	String insertString = "INSERT INTO USERS"
					+ "(EMAIL,PASSWORD) VALUES"
					+ "(?,?)";
		      stmt = conn.prepareStatement(insertString);
		      stmt.setString(1, email);
		      stmt.setString(2, hashed);
		      stmt.executeUpdate();
		      System.out.println("Inserted records into the table...");
		      response.setContentType("text/html;charset=UTF-8");
		      session.setAttribute("flash", "Account created succesfully");
		      response.sendRedirect("login.jsp");
		      return;

		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      //TODO duplicate entry message
		      if (se.getErrorCode()==1062) {
		    	  response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "User with the same email allready exists");
					response.sendRedirect("signup.jsp");
					return;
		      }
		    
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		      
		   }//end try
	
		
	}
	}


