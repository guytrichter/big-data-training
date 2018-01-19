package com.gazuros.inventory.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by guy on 4/5/17.
 */
public class EmailUtils {

    private static final String HC = "Gazuros123!";  

    public static void sendEmail(String to, String cc, final String from, String subject, String text, String smtpHost) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.debug", "true");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, HC);
                        }
                    });

            Message emailMessage = new MimeMessage(session);
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (null != cc) {
                emailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }
            emailMessage.setFrom(new InternetAddress(from));
            emailMessage.setSubject(subject);
            emailMessage.setText(text);

            session.setDebug(true);

            Transport.send(emailMessage);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//
//        String mailSmtpHost = "smtp.gmail.com";
//        String mailTo = "trichter.guy@gmail.com";
//        String mailFrom = "trichter.guy@gmail.com";
//        String mailSubject = "test";
//        String mailText = "test body";
//
//        sendEmail(mailTo, null, mailFrom, mailSubject, mailText, mailSmtpHost);
//    }
}
