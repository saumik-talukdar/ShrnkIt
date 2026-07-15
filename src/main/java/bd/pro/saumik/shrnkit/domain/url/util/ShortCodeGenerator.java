package bd.pro.saumik.shrnkit.domain.url.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {

    private static final String BASE62 =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int LENGTH = 7;

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        StringBuilder builder = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            builder.append(
                    BASE62.charAt(
                            random.nextInt(BASE62.length())
                    )
            );
        }

        return builder.toString();
    }
}