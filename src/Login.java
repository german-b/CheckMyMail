import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.JavaMail.JavaMail;

/**
 * Created by German on 25.11.2015.
 * Creates a login window, validates the input, checks the connectivity and sends the data to DB class.
 *
 * TODO: implement a class that handles message output to the user (Messenger)
 *
 */
public class Login {
    public Login(){
        //Setting the stage
        VBox loginVBox = new VBox();
        Scene loginScene = new Scene(loginVBox, 300, 150);
        Stage loginStage = new Stage();
        loginStage.setTitle("Log in to CheckMyMail");
        loginStage.setResizable(false);

        //VBox elements
        //TODO: add an optional IMAP server input
        TextField userEmailInput = new TextField();
        userEmailInput.setPromptText("user@domain.com");
        PasswordField userPasswordInput = new PasswordField();

        Button loginButton = new Button("Log in");
        loginVBox.getChildren().addAll(new Label("E-mail:"), userEmailInput, new Label("Password:"), userPasswordInput, loginButton);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(5);

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
                        //TODO: open the "Main" window of the application (Checker.java)
                        System.out.println("Checked and connected!");
                        new Checker(email, pwd);
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
