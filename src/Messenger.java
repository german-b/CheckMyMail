import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by German on 27.11.2015.
 *
 * Displays user messages in popups.
 *
 */
public class Messenger {
    public Messenger(String msg){
        Stage msgStage = new Stage(StageStyle.UNDECORATED);
        msgStage.setResizable(false);
        VBox msgBox = new VBox();
        msgBox.setAlignment(Pos.CENTER);
        Scene msgScene = new Scene(msgBox, 200, 100);

        msgStage.setScene(msgScene);
        Button msgButton= new Button("OK");
        msgBox.getChildren().addAll(new Label(msg), msgButton);

        msgButton.setOnAction(event -> { //TODO: Lambda stuff ?!
            msgStage.close();
        });

        msgStage.show();

    }
}
