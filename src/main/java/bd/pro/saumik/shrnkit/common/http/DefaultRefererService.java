package bd.pro.saumik.shrnkit.common.http;

import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class DefaultRefererService implements RefererService {

    @Override
    public RefererInfo parse(String referer) {

        if (referer == null || referer.isBlank()) {
            return new RefererInfo("Direct");
        }

        try {

            URI uri = URI.create(referer);

            String host = uri.getHost();

            if (host == null) {
                return new RefererInfo("Unknown");
            }

            return new RefererInfo(
                    normalizeHost(host)
            );

        } catch (Exception e) {

            return new RefererInfo("Unknown");

        }

    }

    private String normalizeHost(String host) {

        host = host.toLowerCase();

        if (host.startsWith("www.")) {
            host = host.substring(4);
        }

        if (host.startsWith("m.")) {
            host = host.substring(2);
        }

        return host;
    }

}