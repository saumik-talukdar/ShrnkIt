package bd.pro.saumik.shrnkit.domain.auth.dto.response;

public record AuthResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn
) {}