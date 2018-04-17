<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/design/loginstyle.css" />
</head>
<body>
 <div id="main">
        	
        	<h1>Login</h1>
        	<h2 id="error_text"></h2>
        	<%
		if (session.getAttribute("flash") != null) {
			%>
				<div class="flash">
				<%=session.getAttribute("flash")%>
				</div>
			<%
			session.removeAttribute("flash");
		}
		%>
   	<form class="" method="post" action="helloServlet">
   	
   	<%
		String callback = request.getParameter("callback");
		String system = request.getParameter("system");
		
		if (callback != null && callback != "") {
			%>
			<input type="hidden" name="callback" value="<%=callback%>">
			<%
		}
		%>
	
	<%
		if (system != null && system != "") {
					%>
				<input type="hidden" name="system" value="<%=system%>">
				<%=system%>
				 <%
					}
					%>
        		
        		<div class="row username">
	    			<input type="text" id="username" name="username" placeholder="username" />
        		</div>
        		
        		<div class="row pass">
        			<input type="password" id="password" name="password" placeholder="Password" />
        		</div>
        		
        	
        		<input type="submit" value="Login" />
        
        		
        	</form>
        </div>
        <footer>
	        
            <a class="footer"> &copy; 2018 </a>
        </footer>
</body>
</html>