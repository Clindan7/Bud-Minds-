package com.buddymanagement.user.reg.batch.util;

import com.buddymanagement.user.reg.batch.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailServiceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceUtil.class);

    private static final String MAIL_NOT_SEND_LOG = "mail not send";
    @Value("${spring.mail.password}")
    private String passwordSender;
    @Value("${spring.mail.username}")
    private String mailSender;

    public void sendEmail(String to, String subject, String content) throws BadRequestException,MailSendException,Exception{
        LOGGER.info("Sending mail:{}", to);
        final String senderEmail = mailSender;
        final String senderPassword = passwordSender;
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
        }catch (MailSendException me) {
            LOGGER.error(MAIL_NOT_SEND_LOG, me);
            throw new BadRequestException("1933-Mail sending failed");
        }catch (MessagingException me) {
            LOGGER.error("Unexpected error occurred", me);
            throw me;
        }catch (Exception e){
            LOGGER.error("Unexpected error occurred", e);
            throw e;
        }

    }
}
