package bd.pro.saumik.shrnkit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua_parser.Parser;

@Configuration
public class UserAgentConfig {

    @Bean
    public Parser parser() {
        return new Parser();
    }

}