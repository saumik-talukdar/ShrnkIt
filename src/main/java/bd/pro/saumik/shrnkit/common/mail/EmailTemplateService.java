package bd.pro.saumik.shrnkit.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {
    private final SpringTemplateEngine templateEngine;

    public String verificationEmail(
            String firstName,
            String verificationUrl
    ){
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process(
                "email/verify-email",
                context
        );
    }
}
