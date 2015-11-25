package CMM;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by German on 25.11.2015.
 * Creates a login window, validates the input, checks the connectivity and sends the data to DB class.
 */
public class LoginCMM {
    LoginCMM(){
        //Setting the stage
        VBox loginVBox = new VBox();
        Scene loginScene = new Scene(loginVBox, 300, 300);
        Stage loginStage = new Stage();

        //VBox elements
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


    }
}
