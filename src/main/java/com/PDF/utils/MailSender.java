package com.PDF.utils;

import com.sun.xml.internal.ws.util.ByteArrayDataSource;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by martanase on 6/25/2017.
 */
public class MailSender {

    private static final String emailAddressGo4PDF = "go4pdf@gmail.com";
    private static final String passwordGo4PDF = "Licenta2017";
    private static final Properties mailProperties;

    static {
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailProperties.put("mail.smtp.port", "587");
    }

    public static boolean sendMail(String receiverEmail, String attachmentPath){
        
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

        Session session = Session.getDefaultInstance(mailProperties, null);

        InternetAddress[] recipientAddress = null;
        try {
            recipientAddress = InternetAddress.parse(receiverEmail);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }

        try {
            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(emailAddressGo4PDF));
            mailMessage.addRecipients(Message.RecipientType.TO, recipientAddress);
            mailMessage.setSubject("PDF conversion");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hello,/n/n" +
                    "You requested a conversion from Go4PDF./n" +
                    "Thank you for using our services./n/n" +
                    "If you need to get into contact with us, please do not hesitate to reach us at inquiry@Go4PDF.com/n" +
                    "Yours sincerely,/nGo4PDF Team.");

            Multipart multipartMessage = new MimeMultipart();
            multipartMessage.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            DataSource attachment = null;
            try {
                attachment = new ByteArrayDataSource(Files.readAllBytes(Paths.get(attachmentPath)), "application/pdf");
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageBodyPart.setDataHandler(new DataHandler(attachment));
            messageBodyPart.setFileName(Paths.get(attachmentPath).getFileName().toString());
            multipartMessage.addBodyPart(messageBodyPart);
            mailMessage.setContent(multipartMessage);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", emailAddressGo4PDF, passwordGo4PDF);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            transport.close();
            System.out.println("Done,mail sent to " + receiverEmail);
            return true;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
