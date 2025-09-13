<%@ page contentType="text/html;charset=UTF-8" language="java" import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Moffat Bay Lodge - Registration</title>
  <link href="https://fonts.googleapis.com/css2?family=EB+Garamond&family=Roboto&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="styles.css">
</head>
<body>
<header class="header-container">
  <div class="header-left">
    <img src="images/MoffatBayLogo3.png" alt="Moffat Bay Lodge Logo" class="logo-img">
  </div>

  <div class="header-center">
    <h1 class="welcome-message">Moffat Bay Lodge</h1>
    <nav class="main-nav">
      <ul>
        <li><a href="Index.jsp">Home</a></li>
        <li><a href="#">Suites</a></li>
        <li><a href="#">Attractions</a></li>
        <li><a href="about.jsp">About us</a></li>
        <li><a href="#">Book a room!</a></li>
      </ul>
    </nav>
  </div>

  <div class="header-right">
    <div class="auth-links">
      <img src="images/profileicon.png" alt="Profile Icon" class="profile-icon">
      <a href="loginPage.jsp">Login</a>
      <a href="registration.jsp">Sign Up</a>
    </div>
  </div>
</header>

<h2>Create Your Account</h2>

<section class="register-container">
  <form action="<%= request.getContextPath() %>/Register" method="post">
    <div class="form-row name-fields">
      
      <!-- Registration Form -->
      <label>First Name:</label>
      <input type="text" name="firstName" required value="<%= request.getParameter("firstName") != null ? request.getParameter("firstName") : "" %>">
      
      <label>Last Name:</label>
      <input type="text" name="lastName" required value="<%= request.getParameter("lastName") != null ? request.getParameter("lastName") : "" %>">
      <br>

      <label>Email:</label>
      <input type="email" name="email" required value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
      <br>

      <label>Phone:</label>
      <input type="text" name="phone" value="<%= request.getParameter("phone") != null ? request.getParameter("phone") : "" %>">
      <br>

      <label>Password:</label>
      <input type="password" name="password" required>
      <br>
      <label>Confirm Password:</label>
      <input type="password" name="confirmPassword" required>
      <br>

      <label>Address:</label>
      <input type="text" name="address" style="width:400px" required value="<%= request.getParameter("address") != null ? request.getParameter("address") : "" %>">
      <br>

      <label>City:</label>
      <input type="text" name="city" required value="<%= request.getParameter("city") != null ? request.getParameter("city") : "" %>">
      
      <label>State:</label>
      <input type="text" name="state" required value="<%= request.getParameter("state") != null ? request.getParameter("state") : "" %>">
      <br>

      <label>Zip Code:</label>
      <input type="text" name="postalCode" required value="<%= request.getParameter("postalCode") != null ? request.getParameter("postalCode") : "" %>">
      
      <label>Country:</label>
      <input type="text" name="country" required value="<%= request.getParameter("country") != null ? request.getParameter("country") : "" %>">
      <br>

      <!-- Updates -->
      <input type="checkbox" name="emailUpdates" <%= request.getParameter("emailUpdates") != null ? "checked" : "" %>> I allow Moffat Bay to email me updates.
      <br>
      <input type="checkbox" name="smsUpdates" <%= request.getParameter("smsUpdates") != null ? "checked" : "" %>> I allow Moffat Bay to text me updates.
      <br>

      <div class="button-wrapper">
        <button type="submit" class="btn primary">Create Account</button>
      </div>
    </div>

    <!-- Error message -->
    <%
      String errorMessage = (String) request.getAttribute("errorMessage");
      if (errorMessage != null) {
    %>
        <p style="color:red;"><%= errorMessage %></p>
    <%
      }
    %>

  </form>
</section>

<footer>
  <div class="footer-nav">
    <a href="Index.jsp">Home</a>
    <a href="#">FAQS</a>
    <a href="#">Contact Us</a>
    <a href="#">Book Now!</a>
  </div>
  <div class="footer-copy">&copy;MoffatBay2025</div>
</footer>

</body>
</html>
