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

    // getters and setters
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public int getCustomerId() { return customerId; }
    public Connection getConnection() { return connection; }

    // Register user
    public boolean registerUser(String firstName, String lastName, String email, String phone,
                                String address, String city, String state,
                                String postalCode, String country, String password) {

        String checkEmail = "SELECT login_id FROM CustomerLogin WHERE email = ?";
        String insertCustomer = "INSERT INTO Customers (first_name,last_name,phone,address,city,state,postal_code,country) VALUES (?,?,?,?,?,?,?,?)";
        String insertLogin = "INSERT INTO CustomerLogin (customer_id,email,password_hash) VALUES (?,?,?)";

        try (PreparedStatement psCheck = connection.prepareStatement(checkEmail)) {
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next()) {
                return false; // email already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        int generatedId = -1;
        try (PreparedStatement psCustomer = connection.prepareStatement(insertCustomer, Statement.RETURN_GENERATED_KEYS)) {
            psCustomer.setString(1, firstName);
            psCustomer.setString(2, lastName);
            psCustomer.setString(3, phone);
            psCustomer.setString(4, address);
            psCustomer.setString(5, city);
            psCustomer.setString(6, state);
            psCustomer.setString(7, postalCode);
            psCustomer.setString(8, country);

            int rows = psCustomer.executeUpdate();
            if (rows == 0) return false;

            ResultSet keys = psCustomer.getGeneratedKeys();
            if (keys.next()) {
                generatedId = keys.getInt(1);
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        try (PreparedStatement psLogin = connection.prepareStatement(insertLogin)) {
            psLogin.setInt(1, generatedId);
            psLogin.setString(2, email);
            psLogin.setString(3, hashed);
            int rows = psLogin.executeUpdate();
            if (rows == 0) return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        this.customerId = generatedId;
        this.fname = firstName;
        this.lname = lastName;
        return true;
    }

    // Login check
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
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Close connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
