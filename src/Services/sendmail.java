/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Core.Main;
import Entities.Complaint;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendmail {
    public void send(String too, Complaint C) {

        try {
            String host = "smtp.gmail.com";
            String user = "achref.khemiri@esprit.tn";
            String pass = "A09195334";
            String to = too;
            String from = "achref.khemiri@esprit.tn";
            String messageText = "Suite à votre réclamation  portant sur le sujet de: " + C.getCategory().getName() + "Nous vous assurons de tous nosefforts pour répondre au mieux à nos clients, et vous proposons , à titre de dédommagement.  \n" +
                    "\n" +
                    "En espérant que ce problème ponctuel n'entamera pas l'intérêt que vous portez à nos produits, et en restant à votre disposition pour toute question ou demande de précision, nous vous prions d'agréer, Madame, Monsieur, l'assurance de notre considération. \n Signature : " + Main.user.getFullname() + "";
            String subject = "Réclamation ";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(messageText);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message sent successfully");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
