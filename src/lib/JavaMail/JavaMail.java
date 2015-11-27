package lib.JavaMail;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Created by German on 25.11.2015.
 * JavaMail is a stateless class and handles connectivity to API (IMAP/POP) services. No input checks are performed here,
 * so the errors thrown should be only connectivity/API related.
 */
public class JavaMail {

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
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";
        Properties properties = getServerProperties(protocol,
                host, port);
        Session session = Session.getDefaultInstance(properties);
        //session.setDebug(true);
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
}
