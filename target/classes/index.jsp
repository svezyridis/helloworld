<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta charset="utf-8" />
        <title>signupForm</title>
        
        <!-- The stylesheet -->
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/design/styles.css" />
        <!--[if lt IE 9]>
          <script src="htpt://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        
       
    </head>
    
    <body>

        <div id="main">
        	
        	<h1>Sign up, it's FREE!</h1>
        	
        	<form class="" method="post" action="helloServlet">
        		
        		<div class="row email">
	    			<input type="text" id="email" name="email" placeholder="Email" />
        		</div>
        		
        		<div class="row pass">
        			<input type="password" id="password1" name="password1" placeholder="Password" />
        		</div>
        		
        		<div class="row pass">
        			<input type="password" id="password2" name="password2" placeholder="Password (repeat)"  />
        		</div>
        		
        		<!-- The rotating arrow -->
        		<div class="arrowCap"></div>
        		<div class="arrow"></div>
        		
        		<p class="meterText">Password Meter</p>
        		
        		<input type="submit" value="Register" />
        		
        	</form>
        </div>
        
        <footer>
	        
            <a class="al3xis"> a form with password meter and pass validation </a>
        </footer>
        
        <!-- JavaScript includes - jQuery, the complexify plugin and our own script.js -->
		 
		 <script src="http://code.jquery.com/jquery-1.7.2.min.js"></script>
		 <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/login2.js"></script>

		     
    </body>
</html>
