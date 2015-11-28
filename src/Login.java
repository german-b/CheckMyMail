import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.JavaMail.JavaMail;

/**
 * Created by German on 25.11.2015.
 * Creates a login window, validates the input, checks the connectivity and sends the data to DB class.
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
        TextField userEmailInput = new TextField(); //TODO: shrink the width a bit
        userEmailInput.setOnAction(event1 -> {
            System.out.println(Validator.validateEmail(userEmailInput.getText()));
        });
        userEmailInput.setPromptText("user@domain.com");
        PasswordField userPasswordInput = new PasswordField();

        Button loginButton = new Button("Log in");
        loginVBox.getChildren().addAll(new Label("E-mail:"), userEmailInput, new Label("Password:"), userPasswordInput, loginButton);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(5);

        //Show the stage/scene:
        loginStage.setScene(loginScene);
        loginStage.show();

        //Handling the close request properly (prepared for a confirmation dialogue implementation)
        loginStage.setOnCloseRequest(e -> {
            e.consume();
            loginStage.close();
        });

        //Login button action
        //TODO: implement Enter key press as event
        loginButton.setOnAction(event -> {
            String email = userEmailInput.getText();
            String pwd = userPasswordInput.getText();
            if (email != null && !email.isEmpty()){ //http://stackoverflow.com/a/3598792
                if (pwd != null && !pwd.isEmpty()){
                    if (Validator.validateEmail(email)){
                        if (checkLogin(email, pwd)){
                            new Messenger("Connection successful!");
                            //TODO: add the credentials to the DB
                            new Checker(email, pwd);
                            loginStage.close();
                        } else {
                            new Messenger("Authentication failed."); //TODO: handle this from the exception, if possible
                        }
                    } else {
                        new Messenger(email + " is an invalid e-mail");
                    }
                } else {
                    new Messenger("Please fill in the password.");
                }
            } else {
                new Messenger("Please fill in the e-mail.");
            }
        });
    }

    private boolean checkLogin(String email, String pwd) {
            JavaMail connector = new JavaMail();
            return connector.connectToStore(email, pwd);
    }
}