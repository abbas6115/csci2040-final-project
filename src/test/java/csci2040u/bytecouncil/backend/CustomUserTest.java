package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserTest {
    @Test
    void adminRoleHasCorrectAuthority() {
        CustomUser admin = new CustomUser("Admin1", "password", "ADMIN");

        var authorities = admin.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void lowercaseRoleIsUppercased() {
        CustomUser user = new CustomUser("User1", "password", "user");

        var authorities = user.getAuthorities();

        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void allAccountStatusFlagsAreTrue() {
        CustomUser user = new CustomUser("User1", "password", "user");

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
