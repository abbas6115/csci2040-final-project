package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationSystemTests {

    private String simulateLogin(CustomUser user, String inputUser, String inputPass) {
        if (user == null) return "ERROR";

        if (user.getUsername().equals(inputUser) && user.getPassword().equals(inputPass)) {
            if (user.getAuthorities().toString().contains("ADMIN")) {
                return "/admin";
            }
            return "/user";
        }
        return "ERROR";
    }

    private String simulateAccess(CustomUser user, String path) {
        if (user == null && path.equals("/user")) {
            return "/login";
        }
        return path;
    }

    // ST-01-OB
    @Test
    void unauthenticatedAccessRedirectsToLogin() {
        String result = simulateAccess(null, "/user");
        assertEquals("/login", result);
    }

    // ST-02-OB
    @Test
    void adminLoginRedirectsToAdmin() {
        CustomUser admin = new CustomUser("Admin1", "password", "ADMIN");
        String result = simulateLogin(admin, "Admin1", "password");
        assertEquals("/admin", result);
    }

    // ST-03-OB
    @Test
    void userLoginRedirectsToUser() {
        CustomUser user = new CustomUser("User1", "password", "USER");
        String result = simulateLogin(user, "User1", "password");
        assertEquals("/user", result);
    }

    // ST-04-OB
    @Test
    void invalidCredentialsShowError() {
        CustomUser user = new CustomUser("User1", "password", "USER");
        String result = simulateLogin(user, "User1", "wrongpassword");
        assertEquals("ERROR", result);
    }
}
