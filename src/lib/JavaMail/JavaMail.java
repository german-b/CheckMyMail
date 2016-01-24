package lib.JavaMail;

import Main.Messenger;
import Main.Settings;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//import org.jsoup.Jsoup;
//import javax.mail.internet.MimeMultipart;

/**
 * Created by German on 25.11.2015.
 * JavaMail is a stateless class and handles connectivity to API (IMAP/POP) services. No input checks are performed here,
 * so the errors thrown should be only connectivity/API related.
 *
 * TODO: leave just a single method that returns object Store (instead of connectToStore and getStore
 * TODO: write a constructor so that we would not have to pass email&pw to every single method
 */

public class JavaMail {
    String protocol = Settings.getProtocol();
    String host = Settings.getHost();
    String port = Settings.getPort();
    boolean ssl = Settings.getSSL();

    private Properties getServerProperties(String protocol, String host, String port) {
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

        String sslEnabled = "true"; //default
        if (!ssl) sslEnabled = "false";
        properties.setProperty("mail.imap.ssl.enable", sslEnabled);

        return properties;
    }

    public boolean connectToStore(String email, String pwd) {
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
        //session.setDebug(true);
        try {
            Store store = session.getStore(protocol);
            store.connect(email, pwd);
            store.close();
            return true;
        } catch (AuthenticationFailedException ex) {
            new Main.Messenger("Error: Authentication failed.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store. Check connectivity and settings!");
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
        } catch (AuthenticationFailedException ex) {
            new Main.Messenger("Error: Authentication failed.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            new Messenger("Error: Could not connect to the message store.");
            ex.printStackTrace();
        }
        return store;
    }

    public int getEmailsCount(String email, String pwd) {
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

    public ArrayList<ArrayList<String>> getEmails(String email, String pwd, int num) throws Exception {
        ArrayList<ArrayList<String>> emails = new ArrayList<>();
        Message[] messages;
        try {
            Store store = getStore(email, pwd);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            int count = inbox.getMessageCount();
            int arrayCount = 0;
            messages = inbox.getMessages(count - num + 1, count);
            for (Message message : messages) {
                emails.add(new ArrayList<>());
                Address[] fromAddresses = message.getFrom();
                //http://stackoverflow.com/a/5215224
                String fromAddress = fromAddresses == null ? null : ((InternetAddress) fromAddresses[0]).getAddress();

                emails.get(arrayCount).add(message.getSubject()); //Subject:
                emails.get(arrayCount).add(fromAddress); //From:
                emails.get(arrayCount).add(formatDate(message.getSentDate())); // TODO: refine the date format and make it dependent on the settings

                arrayCount++;
                /*try {
                    emails.add(getBodyFromMessage(message));
                } catch (Exception e) {
                    System.out.println("Error reading e-mail.");
                    e.printStackTrace();
                }*/
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return emails;
    }//TODO: handle the different mime types gracefully, not that easy with the below method...scrap for now.

    private String formatDate(Date sentDate) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        return new SimpleDateFormat(dateFormat).format(sentDate);
    }
    /*private String getBodyFromMessage(Message message) throws Exception { //http://stackoverflow.com/a/31877854
        if (message.isMimeType("text/plain")){
            return message.getContent().toString();
        }else if (message.isMimeType("multipart/*")) {
            String result = "";
            MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i ++){
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")){
                    result = result + "\n" + bodyPart.getContent();
                    break;  //without break same text appears twice in my tests
                } else if (bodyPart.isMimeType("text/html")){
                    String html = (String) bodyPart.getContent();
                    result = result + "\n" + Jsoup.parse(html).text();
                }
            }
            return result;
        }
        return "";
    }*/
}