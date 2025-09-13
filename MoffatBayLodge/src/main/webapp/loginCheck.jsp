<%@ page import="userStuff.UserClass" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String email = request.getParameter("email");
    String password = request.getParameter("password");

<<<<<<< HEAD
    // Userclass to connect to the database
=======
    // Uses DatabaseBean to connect to the database
>>>>>>> 3f3b714a197c5575c2c6db9b3b185a20f0bc1668
    UserClass db = new UserClass();
    boolean isValid = db.loginCheck(email, password);
    db.close(); 

    // Checks to see it the users email and password are valid
    if (isValid) {
        out.println("<h2>Login successful!</h2>");
        session.setAttribute("userEmail", email);
        
    } else {
        out.println("<h2>Login failed. Invalid email or password.</h2>");
        out.println("<a href='loginPage.jsp'>Try again</a>");
    }
%>