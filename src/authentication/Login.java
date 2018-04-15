package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

import crypto.Encryption;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	public static String getMyIdentifier() {
		return "SKAUTH2";
	}
	private static final long serialVersionUID = 1L;
	 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 static final String DB_URL = "jdbc:mysql://localhost/USERS";

	    //  Database credentials
	 static final String USER = "savvas";
	 static final String PASS = "root";
	
	Connection conn=null;
	PreparedStatement stmt=null;
	
	public  Map getSystem(String id) {
		
		String identifier = null;
		String keybase64 = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		     
		  	String sqlString = "SELECT IDENTIFIER, KEYBASE64 FROM SERVICES" +
	                " WHERE IDENTIFIER = ? ";
		      stmt = conn.prepareStatement(sqlString);
		      stmt.setString(1, id);
		      ResultSet rs = stmt.executeQuery(sqlString);
		      while (rs.next()) {
		    		 identifier = rs.getString("IDENTIFIER");
		    		 keybase64 = rs.getString("KEYBASE64");	
		    	}
		   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			      //TODO duplicate entry message
			    
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
	      		
		Map<String,String> system = null;

		system = new HashMap<String,String>();
		system.put("identifier",identifier );
		system.put("keybase64", keybase64);
		if (identifier!=null) {
			return system;
		}
		return null;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		
		try {
			;

				String systemIdentifier = request.getParameter("system");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				

				// TODO handle missing system, username or password
				if (systemIdentifier == null || username == null || password == null || systemIdentifier.equals("") || username.equals("") || password.equals("") ) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Missing system, username or password");
					response.sendRedirect("login.jsp");
					return;
				}
				
				Map system = this.getSystem(systemIdentifier);
				if (system == null) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Unknown system");
					response.sendRedirect("login.jsp?");
					return;
				}
				
				Map userData = this.validateUser(username, password);
				if (userData == null) {
					response.setContentType("text/html;charset=UTF-8");
					session.setAttribute("flash", "Wrong username or password");
					response.sendRedirect("login.jsp?system="+systemIdentifier);
					return;
				}
				
				
				JSONObject dataJsonObj = new JSONObject();
				out.println("creating jsosssn \n");
				
				dataJsonObj.put("AUTHID", this.getMyIdentifier());
				dataJsonObj.put("SID", systemIdentifier);
				dataJsonObj.put("userid", username+"@"+this.getMyIdentifier());
				dataJsonObj.put("validtill", (int)(System.currentTimeMillis()/1000)) ;
				JSONObject usermeta = new JSONObject();
				usermeta.put("name", userData.get("name").toString()) ;
				usermeta.put("nick", userData.get("nick").toString()) ;
				usermeta.put("email", userData.get("email").toString()) ;
				dataJsonObj.put("usermeta", usermeta);

				String sharedKeyBase64 = system.get("keybase64").toString(); 
				byte[] sharedKey = Base64.getDecoder().decode(sharedKeyBase64);
				String cipher = "AES/CBC/PKCS5Padding";

				String crypted = Encryption.encrypt(dataJsonObj.toString(), sharedKey, cipher);
				
				JSONObject tokenJsonObj = new JSONObject();
				tokenJsonObj.put("error", "");
				tokenJsonObj.put("issuer", this.getMyIdentifier());
				tokenJsonObj.put("crypted", crypted);

				String token = tokenJsonObj.toString();
				

				
				String finalResponse = token.toString();
			
				if (callback == null || callback.equals("")) {
					response.setContentType("text/plain;charset=UTF-8");
					out.println(finalResponse);
				}
				else {
					if (redirectmethod.equals("NONE")) {
						response.setContentType("text/plain;charset=UTF-8");
						out.println(finalResponse);
						out.println("\n");
						out.println("Would redirect to "+callback);
					}
					if (redirectmethod.equals("GET")) {
						response.setContentType("text/plain;charset=UTF-8");
						String url = callback+"?token="+URLEncoder.encode(finalResponse,"UTF-8");
						response.sendRedirect(url);
						return;
					}
					if (redirectmethod.equals("POST")) {
						response.setContentType("text/html;charset=UTF-8");
						
						out.println("	<html>");
						out.println("		<body>");
						out.println("			<form action='"+callback+"' method='POST' name='callbackform'>");
						out.println("			<input type='hidden' name='token' value='"+token+"'>");
						out.println("			</form>");
						out.println("			<script>");
						out.println("				window.onload = function(){");
						out.println("				  document.forms['callbackform'].submit();");
						out.println("				}");
						out.println("			</script>");
						out.println("		</body>");
						out.println("	</html>");
						
					}
				}
				
			} 
			catch (GeneralSecurityException ex) {
				response.setContentType("text/html;charset=UTF-8");
				session.setAttribute("flash", "GeneralSecurityException");
				response.sendRedirect("login.jsp");
				return;
			} 
			catch (java.io.UnsupportedEncodingException ex) {
				response.setContentType("text/html;charset=UTF-8");
				session.setAttribute("flash", "UnsupportedEncodingException");
				response.sendRedirect("login.jsp");
				return;
			} 
			finally {
				out.close();  // Always close the output writer
			}
		}
		
		public Map validateUser(String username, String password) {
			Map userData = this.getUser(username);
			if (userData == null) {
				return null;
			}
			
			String pwdhash = userData.get("pwdhash").toString();
			//if (BCrypt.checkpw(password, pwdhash)) {
			if(true) {
				userData.remove("pwdhash");
				return userData;
			}
			
			return null;

		}
		
	}


