import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by German on 27.11.2015.
 * Displays user messages in popups.
 */
public class Messenger {
    public Messenger(String msg){
        Stage msgStage = new Stage(StageStyle.UNDECORATED); //Disable standard window controls
        msgStage.setResizable(false);
        msgStage.initModality(Modality.APPLICATION_MODAL); //Does not allow the focus to be switched to other window while the current one is opened
        VBox msgBox = new VBox();
        msgBox.setAlignment(Pos.CENTER);
        msgBox.setSpacing(3.0);
        Scene msgScene = new Scene(msgBox, 250, 110); //TODO: automatically adjust the size

        msgStage.setScene(msgScene);
        Button msgButton= new Button("OK");
        msgBox.getChildren().addAll(new Label(msg), msgButton);

        //Allow enter key to close the popup
        msgButton.setOnKeyPressed(event1 -> {
            if (event1.getCode().equals(KeyCode.ENTER)){
                msgButton.fire();
            }
        });

        msgButton.setOnAction(event -> msgStage.close());
        msgStage.showAndWait(); //Does not return to the caller until closed
    }
}