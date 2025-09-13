package userStuff;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/ReservationServlet")
public class reservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection info
    private final String jdbcURL = "jdbc:mysql://localhost:3306/moffatbay";
    private final String jdbcUsername = "moffatbay_user";
    private final String jdbcPassword = "Moffat123!";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get form fields
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String checkinStr = request.getParameter("checkin");
        String checkoutStr = request.getParameter("checkout");
        int guests = Integer.parseInt(request.getParameter("guests"));
        String roomType = request.getParameter("roomType");

        try {
            // Parse dates
            LocalDate checkin = LocalDate.parse(checkinStr);
            LocalDate checkout = LocalDate.parse(checkoutStr);
            long lengthOfStay = ChronoUnit.DAYS.between(checkin, checkout);

            if (lengthOfStay <= 0) {
                request.setAttribute("errorMessage", "Check-out date must be after check-in date.");
                request.getRequestDispatcher("reservation.jsp").forward(request, response);
                return;
            }

            // 2. Connect to DB
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {

                // 3. Find suite_id from Suites table by room type
                int suiteId = -1;
                String suiteSql = "SELECT suite_id FROM Suites WHERE suite_name = ?";
                try (PreparedStatement suiteStmt = conn.prepareStatement(suiteSql)) {
                    suiteStmt.setString(1, roomType);
                    try (ResultSet rs = suiteStmt.executeQuery()) {
                        if (rs.next()) {
                            suiteId = rs.getInt("suite_id");
                        } else {
                            request.setAttribute("errorMessage", "Selected room type not found.");
                            request.getRequestDispatcher("reservation.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // 4. Find customer_id by email (must already exist in Customers + CustomerLogin)
                int customerId = -1;
                String custSql = "SELECT c.customer_id FROM Customers c " +
                        "JOIN CustomerLogin cl ON c.customer_id = cl.customer_id " +
                        "WHERE cl.email = ?";
                try (PreparedStatement custStmt = conn.prepareStatement(custSql)) {
                    custStmt.setString(1, email);
                    try (ResultSet rs = custStmt.executeQuery()) {
                        if (rs.next()) {
                            customerId = rs.getInt("customer_id");
                        } else {
                            request.setAttribute("errorMessage", "No customer account found for this email.");
                            request.getRequestDispatcher("reservation.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                // 5. Insert into Reservations table
                String insertSql = "INSERT INTO Reservations (customer_id, suite_id, check_in, check_out, length_of_stay, number_of_guests, status) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setInt(1, customerId);
                    stmt.setInt(2, suiteId);
                    stmt.setDate(3, Date.valueOf(checkin));
                    stmt.setDate(4, Date.valueOf(checkout));
                    stmt.setInt(5, (int) lengthOfStay);
                    stmt.setInt(6, guests);
                    stmt.setString(7, "confirmed");

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        request.setAttribute("successMessage", "Reservation confirmed for " + firstName + " " + lastName);
                        request.getRequestDispatcher("reservationSuccess.jsp").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "Reservation failed.");
                        request.getRequestDispatcher("reservation.jsp").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("reservation.jsp").forward(request, response);
        }
    }
}
