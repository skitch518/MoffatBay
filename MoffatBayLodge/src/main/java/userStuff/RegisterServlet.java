package userStuff;

import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read form parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Simple validation example
        if (firstName == null || lastName == null || email == null || password == null || confirmPassword == null ||
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("errorMessage", "Please fill in all required fields.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }
        
     // Password strength validation
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(passwordPattern)) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }


        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Hash password with bcrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create user object and set data
        UserClass user = new UserClass();
        user.setFname(firstName);
        user.setLname(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(hashedPassword);

        // Save user to database
        boolean userSaved = user.saveUser();

        if (userSaved) {
            // Redirect to success page
            response.sendRedirect("loginPage.jsp");
        } else {
            // Forward back to form with error message
            request.setAttribute("errorMessage", "Failed to register. Please try again.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
        }
    }

}
