package userStuff;  // your package name, adjust accordingly

import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {
    public static void main(String[] args) {   // <--- this method is required!
        String password = "mySecret123";

        // 1. Hash the password
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Hashed password: " + hashed);

        // 2. Check password against the hash
        boolean matched = BCrypt.checkpw(password, hashed);
        System.out.println("Password match: " + matched);

        // 3. Check with wrong password
        boolean wrongMatch = BCrypt.checkpw("wrongPassword", hashed);
        System.out.println("Wrong password match: " + wrongMatch);
    }
}
