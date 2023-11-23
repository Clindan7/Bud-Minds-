package com.innovaturelabs.buddymanagement.util;

import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    private static final String MAIL_NOT_SEND = "mail.not.send";

    private static final String MAIL_NOT_SEND_LOG = "mail not send";

    Logger log = LoggerFactory.getLogger(EmailUtil.class);

    @Value("${spring.mail.username}")
    private String mailSender;

    @Value("${spring.mail.password}")
    private String passwordSender;

    @Autowired
    private LanguageUtil languageUtil;

    public void sendMail(String to, String subject, String content, String resetPasswordLink) {
        String senderEmail = mailSender;
        String senderPassword = passwordSender;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(content + "  Reset Password Link  " + resetPasswordLink);
            Transport.send(msg);
        } catch (Exception e) {
            log.error(MAIL_NOT_SEND_LOG);
            throw new BadRequestException(languageUtil.getTranslatedText(MAIL_NOT_SEND, null, "en"), e);
        }
    }

    public void sendEmail(String to, String subject, String content) throws MailSendException, MessagingException{
        String senderEmail = mailSender;
        String senderPassword = passwordSender;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setContent(content,"text/html; charset=UTF-8");
            Transport.send(msg);
        } catch (Exception e) {
            log.error(MAIL_NOT_SEND_LOG);
            throw e;
        }
    }
}
