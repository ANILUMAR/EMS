package canteenManagment.main.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Central place for the role checks used by the controllers.
 *
 * Three roles exist in the product:
 * ADMIN    - sees every record in the workspace and is the only role that may
 *            create assignments or manage accounts.
 * EMPLOYEE - developer; only ever sees their own assignments, tasks and hours.
 * TESTER   - same scoping as EMPLOYEE, but lands on the tester board.
 */
public final class AuthContext {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ROLE_TESTER = "ROLE_TESTER";

    private AuthContext() {
    }

    public static boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    public static boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, ROLE_ADMIN);
    }

    /** Email (the JWT subject) of the caller, lowercased for comparisons. */
    public static String email(Authentication authentication) {
        return authentication == null ? "" : String.valueOf(authentication.getName()).toLowerCase();
    }
}
