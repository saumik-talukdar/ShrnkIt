package bd.pro.saumik.shrnkit.common.http;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

@Service
@RequiredArgsConstructor
public class DefaultUserAgentService
        implements UserAgentService {

    private final Parser parser;

    @Override
    public UserAgentInfo parse(String userAgent) {

        if (userAgent == null || userAgent.isBlank()) {
            return new UserAgentInfo(
                    "Unknown",
                    "Unknown",
                    "Unknown"
            );
        }

        Client client = parser.parse(userAgent);

        String browser = client.userAgent.family;
        String operatingSystem = client.os.family;
        String device = client.device.family;

        return new UserAgentInfo(
                browser != null ? browser : "Unknown",
                operatingSystem != null ? operatingSystem : "Unknown",
                resolveDeviceType(userAgent)
        );
    }

    private String resolveDeviceType(String userAgent) {

        String ua = userAgent.toLowerCase();

        if (ua.contains("tablet") || ua.contains("ipad")) {
            return "Tablet";
        }

        if (ua.contains("mobi")
                || ua.contains("android")
                || ua.contains("iphone")) {
            return "Mobile";
        }

        return "Desktop";
    }
}