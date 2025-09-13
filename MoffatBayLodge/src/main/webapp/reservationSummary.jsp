<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Moffat Bay Lodge</title>

  <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css2?family=EB+Garamond&family=Roboto&display=swap" rel="stylesheet">

  <!-- Style sheet -->
  <link rel="stylesheet" href="styles.css" />
  <!-- Calendar Widget -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
  <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
</head>
<body>

  <!-- Header -->
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
          <li><a href="#">About us</a></li>
          <li><a href="#">Contact Us</a></li>
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
  
    <!-- Success Message -->
  <section class="register-container">
    <h2 style="text-decoration: underline;">Reservation Summary</h2>

    <p style="font-size: 15px; color: green;">&#x2713 Your Reservation Has Been Confirmed. </p>

    <table border="1" cellpadding="10" cellspacing="0" style="margin-top: 20px;">
      <tr>
        <th>Name: </th>
        <td><%= request.getParameter("firstName") %> <%= request.getParameter("lastName") %></td>
      </tr>
      <tr>
        <th>Email:</th>
        <td><%= request.getParameter("email") %></td>
      </tr>
      <tr>
        <th>Phone:</th>
        <td><%= request.getParameter("phone") %></td>
      </tr>
      <tr>
        <th>Check-In:</th>
        <td><%= request.getParameter("checkin") %></td>
      </tr>
      <tr>
        <th>Check-Out:</th>
        <td><%= request.getParameter("checkout") %></td>
      </tr>
      <tr>
        <th>Guests:</th>
        <td><%= request.getParameter("guests") %></td>
      </tr>
      <tr>
        <th>Room Type:</th>
        <td><%= request.getParameter("roomType") %></td>
      <tr>
        <th>Length of Stay:</th>
        <td><%= request.getParameter("lengthOfStay") %></td>
      </tr>
      <tr>
  		<th>Total Price</th>
  		<td>$<%= request.getAttribute("totalCost") %></td>
      </tr>
      <tr>
  		<th>Reservation Created:</th>
  		<td><%= new java.util.Date() %></td>
	</tr>
    </table>
     <p style="color: #392A16;"><b> Thanks for booking with Moffat Bay Lodge!</b></p>

    <div class="button-wrapper">
      <a href="reservation.jsp" class="btn primary">Book Another Reservation</a>
    </div>
  </section>

      <!-- Footer -->
  <footer>
    <div class="footer-nav">
      <a href="Index.jsp">Home</a>
      <a href="#">FAQS</a>
      <a href="#">Contact Us</a>
      <a href="#">Book Now!</a>
    </div>
    <div class="footer-copy">
      &copy;MoffatBay2025
    </div>
  </footer>

</body>
</html>