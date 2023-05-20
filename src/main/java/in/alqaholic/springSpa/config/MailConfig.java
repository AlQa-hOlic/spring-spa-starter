package in.alqaholic.springSpa.config;

import java.util.Collections;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;

    public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        // javaMailSender.setUsername(username);
        // javaMailSender.setPassword(password);

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");

        // properties.put("mail.smtp.auth", "true");
        // properties.put("mail.smtp.starttls.enable", "true");
        // properties.put("mail.debug", "true");

        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for TEXT emails
        // templateEngine.addTemplateResolver(textTemplateResolver());
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        // Resolver for HTML editable emails (which will be treated as a String)
        templateEngine.addTemplateResolver(stringTemplateResolver());
        // // Message source, internationalization config
        // templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }

    // private ITemplateResolver textTemplateResolver() {
    // final ClassLoaderTemplateResolver templateResolver = new
    // ClassLoaderTemplateResolver();
    // templateResolver.setOrder(Integer.valueOf(1));
    // templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
    // templateResolver.setPrefix("/mailTemplates/");
    // templateResolver.setSuffix(".txt");
    // templateResolver.setTemplateMode(TemplateMode.TEXT);
    // templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
    // templateResolver.setCacheable(false);
    // return templateResolver;
    // }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        templateResolver.setResolvablePatterns(Collections.singleton("*"));
        templateResolver.setPrefix("/mailTemplates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private ITemplateResolver stringTemplateResolver() {
        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(3));
        // No resolvable pattern, will simply process as a String template everything
        // not previously matched
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

}
