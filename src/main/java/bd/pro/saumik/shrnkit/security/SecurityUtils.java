package bd.pro.saumik.shrnkit.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class SecurityUtils {

    private SecurityUtils() {}

    public static UUID currentUserId(Authentication authentication) {
        return ((AppUserDetails) authentication.getPrincipal()).getId();
    }

    public static AppUserDetails currentUser(Authentication authentication) {
        return (AppUserDetails) authentication.getPrincipal();
    }
}