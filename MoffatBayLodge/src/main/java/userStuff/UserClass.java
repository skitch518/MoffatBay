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

    // getters and setters for fname, lname, customerId
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public Connection getConnection() {
        return connection;
    }


    // Validates user by email and plain password using bcrypt hash check
    public boolean loginCheck(String email, String plainPassword) {
        String sql = "SELECT customer_id, fname, lname, password_hash FROM CustomerLogin WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                        this.customerId = rs.getInt("customer_id");
                        this.fname = rs.getString("fname");
                        this.lname = rs.getString("lname");
                        return true;
                    } else {
                        System.out.println("Incorrect password");
                        return false;
                    }
                } else {
                    System.out.println("Email not found");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Closes connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
