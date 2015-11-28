import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by German on 25.11.2015.
 * Contains methods that validate various inputs from the user.
 */
public class Validator {

    public static boolean validateEmail(String email) {
        boolean isValid = false;

        try {
            InternetAddress emailaddress = new InternetAddress(email);
            emailaddress.validate();
            isValid = true;
        } catch (AddressException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}