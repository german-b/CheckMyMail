public class App {

    public static void main(String[] args){

        APISample bean=new APISample();
        bean.getNewEmails("imap", "imap.gmail.com", "993", "user@gmail.com", "password");
    }

}