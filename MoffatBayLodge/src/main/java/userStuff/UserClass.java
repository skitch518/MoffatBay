package userStuff;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserClass {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/moffatbay";
    private final String jdbcUsername = "moffatbay_user";
    private final String jdbcPassword = "Moffat123!";

    private Connection connection;

    private String fname;
    private String lname;
    private int customerId;

    public UserClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and setters
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public int getCustomerId() { return customerId; }

    public boolean registerUser(String firstName, String lastName, String email, String phone,
                                String address, String city, String state, String postalCode,
                                String country, String password) {

        try {
            // 1. Check if email exists
            String checkSql = "SELECT COUNT(*) FROM CustomerLogin WHERE email = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Email already exists
                    }
                }
            }

            // 2. Hash password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // 3. Insert into Customers table
            String customerSql = "INSERT INTO Customers (first_name, last_name, phone, address, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int newCustomerId;
            try (PreparedStatement customerStmt = connection.prepareStatement(customerSql, Statement.RETURN_GENERATED_KEYS)) {
                customerStmt.setString(1, firstName);
                customerStmt.setString(2, lastName);
                customerStmt.setString(3, phone);
                customerStmt.setString(4, address);
                customerStmt.setString(5, city);
                customerStmt.setString(6, state);
                customerStmt.setString(7, postalCode);
                customerStmt.setString(8, country);

                int rows = customerStmt.executeUpdate();
                if (rows == 0) return false;

                try (ResultSet generatedKeys = customerStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newCustomerId = generatedKeys.getInt(1);
                    } else return false;
                }
            }

            // 4. Insert into CustomerLogin
            String loginSql = "INSERT INTO CustomerLogin (customer_id, email, password_hash) VALUES (?, ?, ?)";
            try (PreparedStatement loginStmt = connection.prepareStatement(loginSql)) {
                loginStmt.setInt(1, newCustomerId);
                loginStmt.setString(2, email);
                loginStmt.setString(3, hashedPassword);

                int rows = loginStmt.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginCheck(String email, String plainPassword) {
        String sql = "SELECT customer_id, first_name, last_name, password_hash FROM CustomerLogin JOIN Customers ON CustomerLogin.customer_id = Customers.customer_id WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                        this.customerId = rs.getInt("customer_id");
                        this.fname = rs.getString("first_name");
                        this.lname = rs.getString("last_name");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
