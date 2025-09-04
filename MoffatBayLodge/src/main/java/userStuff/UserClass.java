package userStuff;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	
	
public class UserClass {
	Connection conn; 
	Statement stmt; 
	private String fname; 
	private String lname;
	private int customerId;
	
	//DB connection stuff
	private static String url = "jdbc:mysql://localhost:3306/moffatbay";
	private static String user= "moffatbay_user";
	private static String pass = "Moffat123!";
			
		public UserClass() { 
				try { 
					Class.forName("com.mysql.cj.jdbc.Driver");
					conn = DriverManager.getConnection(url, user, pass);
					stmt = conn.createStatement();
					System.out.println("connection successful");
				}
				catch (Exception e) {
					System.out.println("Something went wrong");
					System.out.println(e);
				}
		}
		
		public void setFname(String name) {
			this.fname = name;
		}
		
		public String getFname() {
			return this.fname;
			
		}
		
		public void setLname(String name) {
			this.lname = name;
		}
		
		public String getLname() {
			return this.lname;
		}
		
			
		public boolean loginCheck(String email, String pass) {
				ResultSet rs = null;
				boolean success = true; 
				
				try {
					rs = stmt.executeQuery("SELECT * FROM CustomerLogin WHERE email = '" + email + "'");
					
					//check if row exists, if it is, the email is incorrect. 
					if (!rs.next()) { 
						success = false;
						System.out.print("Incorrect email");
					}
					else {
						//check password
						String Userpass = rs.getString("password_hash");
						if (Userpass.equals(pass)) {
							success = true;
							this.customerId = rs.getInt("customer_id");  
							
						} else {
							success =  false;
							System.out.println("Incorrect password");
							
							}
						
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					success =  false;
					System.out.println("SQL ERROR");
				}
				
				
				return success; 
				
		}
		

	
}
