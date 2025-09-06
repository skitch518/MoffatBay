<%@ page import="com.moffatbay.util.DatabaseBean" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    // Uses DatabaseBean to connect to the database
    DatabaseBean db = new DatabaseBean();
    boolean isValid = db.validateUser(email, password);
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
