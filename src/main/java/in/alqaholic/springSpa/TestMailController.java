package in.alqaholic.springSpa;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import java.util.Locale;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestMailController {

    private final JavaMailSender mailSender;
    private final TemplateEngine emailTemplateEngine;

    @PostMapping("/api/mail/test")
    public String testMail(Principal principal, final Locale locale) throws MessagingException, URISyntaxException {
        String subject = "Test subject";
        final Context ctx = new Context(locale);
        ctx.setVariable("name", principal.getName());
        ctx.setVariable("subscriptionDate", new Date());

        final String htmlContent = this.emailTemplateEngine.process("index.html", ctx);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");

        helper.setTo("test@host.com");
        helper.setSubject(subject);

        // use the true flag to indicate the text included is HTML
        helper.setText(htmlContent, true);

        mailSender.send(message);

        return "ok";
    }
}
