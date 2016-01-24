package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.JavaMail.JavaMail;
import java.util.HashMap;

/**
 * Created by German on 25.11.2015.
 * Creates a login window, validates the input, checks the connectivity and sends the data to DB class.
 */
public class Login {

    TextField userEmailInput = new TextField();
    PasswordField userPasswordInput = new PasswordField();
    CheckBox rememberMe = new CheckBox("Remember me");
    JavaMail connector = new JavaMail();

    public Login() {
        //Setting the stage
        VBox loginVBox = new VBox();
        Scene loginScene = new Scene(loginVBox, 300, 170);
        Stage loginStage = new Stage();
        loginStage.setTitle("Log in to CheckMyMail");
        loginStage.setResizable(false);

        //VBox elements
        //TODO: add an optional IMAP server input
        userEmailInput.setMaxWidth(200);
        userEmailInput.setPromptText("user@domain.com");
        userPasswordInput.setPromptText("password");
        userPasswordInput.setMaxWidth(200);
        rememberMe.setAlignment(Pos.BOTTOM_LEFT);

        Button loginButton = new Button("Log in");
        loginVBox.getChildren().addAll(new Label("E-mail:"), userEmailInput, new Label("Password:"), userPasswordInput, rememberMe, loginButton);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(5);
        loginVBox.setPadding(new Insets(5, 10, 5, 10));

        //Show the stage/scene:
        loginStage.setScene(loginScene);
        loginStage.show();

        //Handling the close request properly (prepared for a confirmation dialogue implementation)
        loginStage.setOnCloseRequest(e -> {
            e.consume();
            loginStage.close();
        });

        //Login button action
        loginButton.setOnAction(event -> {
            String email = userEmailInput.getText();
            String pwd = userPasswordInput.getText();

            if (!email.isEmpty()) { //http://stackoverflow.com/a/3598792
                if (!pwd.isEmpty()) {
                    if (Validator.validateEmail(email)) {
                        checkLogin(email, pwd);
                        new Checker(email, pwd);
                        loginStage.close();
                    } else {
                        new Messenger(email + " is an invalid e-mail");
                        userEmailInput.clear();
                    }
                } else {
                    new Messenger("Please fill in the password.");
                }
            } else {
                new Messenger("Please fill in the e-mail.");
            }
        });
        //Allow enter key to submit the form
        userEmailInput.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });
        userPasswordInput.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });

        //Populate the login form if user exists in DB
        HashMap user = Settings.getUser();
        if (user.containsKey("username") && user.containsKey("password")) {
            this.userEmailInput.setText(user.get("username").toString());
            this.userPasswordInput.setText(user.get("password").toString());
        }
    }

    private void checkLogin(String email, String pwd) {
        if (connector.connectToStore(email, pwd)) {
            if (rememberMe.isSelected()) {
                //Add working user to DB
                Settings.createUser(email, pwd);
            } else {
                //Not remembering user, dropping already existing one too if needed
                Settings.deleteUser();
            }
        }
    }
}