package bd.pro.saumik.shrnkit.common.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class CookieVisitorService implements VisitorService {

    private static final String COOKIE_NAME = "shrnkit_vid";

    @Override
    public UUID resolveVisitor(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UUID visitorId = extractVisitorId(request);

        if (visitorId != null) {
            return visitorId;
        }

        visitorId = UUID.randomUUID();

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, visitorId.toString())
                .httpOnly(true)
                .secure(false)                 // true in production
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(365))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return visitorId;
    }

    private UUID extractVisitorId(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        // System.out.println("===== Incoming Cookies =====");

        if (cookies == null) {
            // System.out.println("No cookies");
            return null;
        }

        for (Cookie cookie : cookies) {
            // System.out.printf("%s = %s%n", cookie.getName(), cookie.getValue());

            if (!COOKIE_NAME.equals(cookie.getName())) {
                continue;
            }

            try {
                return UUID.fromString(cookie.getValue());
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }

        return null;
    }
}