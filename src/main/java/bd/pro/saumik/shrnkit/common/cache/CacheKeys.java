package bd.pro.saumik.shrnkit.common.cache;

public final class CacheKeys {

    public static String url(String shortCode) {
        return "url:" + shortCode;
    }

    private CacheKeys() {}
}