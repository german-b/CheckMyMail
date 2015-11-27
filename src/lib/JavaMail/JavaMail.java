package lib.JavaMail;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by German on 25.11.2015.
 * JavaMail is a stateless class and handles connectivity to API (IMAP/POP) services. No input checks are performed here,
 * so the errors thrown should be only connectivity/API related.
 *
 * TODO: leave just a single method that returns object Store (instead of connectToStore and getStore
 */
public class JavaMail {
    String protocol = "imap";
    String host = "imap.gmail.com";
    String port = "993";
    private Properties getServerProperties(String protocol,
                                           String host, String port) {
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host",protocol), host);
        properties.put(String.format("mail.%s.port",
                protocol), port);
        properties.setProperty(
                String.format("mail.%s.socketFactory.class",
                        protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback",
                        protocol), "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port",
                        protocol), String.valueOf(port));
        properties.setProperty("mail.imap.ssl.enable", "true");

        return properties;
    }
    public boolean connectToStore(String email, String pwd) {
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
        //session.setDebug(true);
        //Dummy user and pwd for layout & login flow testing without actual connection
        if (email.equals("test") && pwd.equals("test"))
            return true;
        try {
            Store store = session.getStore(protocol);
            store.connect(email, pwd);
            store.close();
            return true;
        }
        catch (AuthenticationFailedException ex){
            System.out.println("Authentication failed.");
            ex.printStackTrace();
        }
        catch (MessagingException ex){
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        return false;
    }
    public Store getStore(String email, String pwd) {
        Store store = null;
            Properties properties = getServerProperties(protocol, host, port);
            Session session = Session.getDefaultInstance(properties);
            try {
                store = session.getStore(protocol);
                store.connect(email, pwd);
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        return store;
    }
    public int getEmailsCount(String email, String pwd){
        int emailsCount = 0;
        Store store = getStore(email, pwd);
        try {
            Folder inbox = store.getFolder("INBOX");
            emailsCount = inbox.getMessageCount();
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return emailsCount;
    }
}
