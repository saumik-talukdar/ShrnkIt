package bd.pro.saumik.shrnkit.common.exception;

public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException() {
        super("Short URL not found.");
    }

}