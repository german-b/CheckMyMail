import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lib.JavaMail.JavaMail;

import java.util.ArrayList;

/**
 * Created by German on 27.11.2015.
 * Creates the main window (+taskbar stuff) and implements the actual mail checking functionality
 *
 */
public class Checker {
    int emailsCount;
    String email;
    String pwd;

    public Checker(String email, String pwd){
        //Email count check method for testing
        JavaMail connector = new JavaMail();
        emailsCount = connector.getEmailsCount(email, pwd);

        //Setting the grid
        GridPane emailsGrid = new GridPane();
        emailsGrid.setHgap(10);
        emailsGrid.setVgap(10);
        emailsGrid.setPadding(new Insets(0, 10, 0, 10));

        //Stage & scene
        Stage checkerStage = new Stage();
        checkerStage.setResizable(false);
        Scene checkerMainScene = new Scene(emailsGrid, 400, 200);
        checkerStage.setScene(checkerMainScene);

        Label sampleLabel = new Label(Integer.toString(emailsCount));

        try {
            ArrayList emails = connector.getEmails(email, pwd, 1);
            int count = emails.size(); //Every e-mail consists of sender, subject and date (for now)
            for (int i = 0; i < count; ++i) {
                //Iterate through the ArrayList and create nodes + add to GridPane
                System.out.println(emails.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Messenger(e.toString());
        }
        //Add elements and show
        emailsGrid.add(sampleLabel, 0, 0);
        checkerStage.show();
    }
/*    private checkerTimer(int minutes){
        //Loop the e-mail check every 'minutes' minutes

    }*/
}
