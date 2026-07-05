package bd.pro.saumik.shrnkit.common.mail;

public record EmailMessage(
        String to,
        String subject,
        String html
){}
