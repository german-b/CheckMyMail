import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.DB.DB;
import lib.JavaMail.JavaMail;

/**
 * Created by German on 25.11.2015.
 * Creates a login window, validates the input, checks the connectivity and sends the data to DB class.
 */
public class LoginCMM {
    public LoginCMM(){
        //Setting the stage
        VBox loginVBox = new VBox();
        Scene loginScene = new Scene(loginVBox, 300, 300);
        Stage loginStage = new Stage();

        //VBox elements
        //TODO: add an optional IMAP server input
        Label userEmailLabel = new Label("E-mail:");
        TextField userEmailInput = new TextField();
        userEmailInput.setPromptText("user@domain.com");

        Label userPasswordLabel = new Label("Password:");
        PasswordField userPasswordInput = new PasswordField();

        Button loginButton = new Button("Log in");
        loginVBox.getChildren().addAll(userEmailLabel, userEmailInput, userPasswordLabel, userPasswordInput, loginButton);

        //Show the stage/scene:
        loginStage.setScene(loginScene);
        loginStage.show();

        //Login button action
        loginButton.setOnAction(event -> {
            String email = userEmailInput.getText();
            String pwd = userPasswordInput.getText();
            if (email != null && !email.isEmpty()){ //http://stackoverflow.com/a/3598792
                if (pwd != null && !pwd.isEmpty()){
                    if (checkLogin(email, pwd)){
                        //close the login window?
                        //TODO: open the "Main" window of the application
                        System.out.println("Checked and connected!");
                    }
                } else {
                    System.out.println("Please fill in the password.");
                }
            } else {
                System.out.println("Please fill in the e-mail.");
            }
        }); // Login button event end
    }

    private boolean checkLogin(String email, String pwd) {
        //TODO: regexp check for e-mail and check the connection
        //Validator.regexpEmail(email);
        JavaMail connector = new JavaMail();
        if (connector.connectToStore(email, pwd)){
            return true;
        }
        return false;
    }
}
