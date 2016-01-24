package Main;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.JavaMail.JavaMail;
import java.util.*;

/**
 * Created by German on 27.11.2015.
 * Creates the main window and implements the actual mail checking functionality
 * TODO: taskbar implementation
 */

public class Checker {

    JavaMail connector = new JavaMail();
    VBox mainWindow = new VBox();
    VBox emailsBox = new VBox();

    private int tempCount;
    private boolean firstRun = true;

    //Make stage available for all methods
    private static Stage checkerStage = new Stage();

    public Checker(String email, String pwd){
        //Settings
        int minutes = Settings.getDelay();
        int num = Settings.getCount();

        //Setting the window
        mainWindow.setSpacing(5);
        mainWindow.setPadding(new Insets(0, 10, 10, 10));

        //Stage & scene
        getStage().setResizable(false);
        Scene checkerMainScene = new Scene(mainWindow);
        getStage().setScene(checkerMainScene);
        getStage().setTitle("Total emails: " + getEmailCount(email, pwd));

        //Header (buttons & separator)
        HBox buttons = new HBox();
        Button settings = new Button("Settings");
        Button forceCheck = new Button("Check now");
        VBox header = new VBox(5);
        header.setPadding(new Insets(5, 0, 0, 0));
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(forceCheck, settings);
        Separator separateHeader = new Separator();
        buttons.getChildren().addAll(forceCheck, settings);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        header.getChildren().addAll(buttons, separateHeader);
        mainWindow.getChildren().addAll(header, emailsBox);

        //Button events
        settings.setOnAction(event ->
            new Settings()
        );

        forceCheck.setOnAction(event -> {
            System.out.println("Force check!");
            forceCheckForEmails(email, pwd, num);
        });

        //"Magic"
        checkForEmails(email, pwd, num, minutes);

        //Show the stage
        getStage().show();

        //Proper closing
        getStage().setOnCloseRequest(e -> {
            e.consume();
            getStage().close();
        });
    }

    private void forceCheckForEmails(String email, String pwd, int num) {
        int diff = countDiff(email, pwd, getTempCount());
        if (diff > 0) {
            try {
                emailsBox.getChildren().clear();

                ArrayList<ArrayList<String>> emails = connector.getEmails(email, pwd, num);
                Collections.reverse(emails); //getEmails returns oldest email first, reverse that TODO: make this settings dependant
                for (ArrayList<String> email1 : emails) { // Begin iteration of emails ArrayList

                    HBox singleEmail = new HBox(5); //Setup row for a single email as HBox
                    for (String emailPart : email1) {
                        Label textPart = new Label(emailPart);
                        singleEmail.getChildren().add(textPart);
                    }
                    emailsBox.getChildren().add(singleEmail);//Add the current email to VBox
                }
                setTempCount(getTempCount() + diff);
                setStageTitle("Total emails: " + getTempCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkForEmails(String email, String pwd, int num, int minutes) {
       try {
           System.out.println("Delay: " + Settings.getDelay());
           System.out.println("Count: " + Settings.getCount());
            ArrayList<ArrayList<String>> emails = connector.getEmails(email, pwd, num);
            Collections.reverse(emails); //getEmails returns oldest email first, reverse that TODO: make this settings dependant

            setTempCount(getEmailCount(email, pwd));

           for (ArrayList<String> email1 : emails) { // Begin iteration of emails ArrayList
               HBox singleEmail = new HBox(5); //Setup row for a single email as HBox
               for (String emailPart : email1) {
                   Label textPart = new Label(emailPart);
                   singleEmail.getChildren().add(textPart); //Add date, from and subject to the email/row
               }
               emailsBox.getChildren().add(singleEmail);//Add the current email to VBox
           }
           firstRun = false;

           Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                            try {
                                int diff = countDiff(email, pwd, getTempCount());

                                if (diff == 0 && !firstRun){
                                    System.out.println("No new emails.");
                                    return;
                                } else if (diff > 0 && !firstRun){
                                    emailsBox.getChildren().clear();
                                }
                                setTempCount(getEmailCount(email, pwd) + diff); //Temp?

                                ArrayList<ArrayList<String>> emails = connector.getEmails(email, pwd, num);
                                Collections.reverse(emails); //getEmails returns oldest email first, reverse that TODO: make this settings dependant

                                for (ArrayList<String> email1 : emails) { // Begin iteration of emails ArrayList
                                    HBox singleEmail = new HBox(5); //Setup row for a single email as HBox
                                    for (String emailPart : email1) {
                                        Label textPart = new Label(emailPart);
                                        singleEmail.getChildren().add(textPart); //Add date, from and subject to the email/row
                                    }
                                    emailsBox.getChildren().add(singleEmail);//Add the current email to VBox
                                }
                                setStageTitle("Total emails: " + getTempCount());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    });
                }
            }, 0, minutes * 60000);
        } catch (Exception e) {
            e.printStackTrace();
            new Messenger(e.toString());
        }
    }

    public static Stage getStage() { return checkerStage; }

    private void setStageTitle(String title){
        getStage().setTitle(title);
    }

    private int countDiff(String email, String pwd, int totalCount) {
        return getEmailCount(email, pwd) - totalCount;
    }

    public int getEmailCount(String email, String pwd){
         return connector.getEmailsCount(email, pwd);
    }

    public int getTempCount() {
        return tempCount;
    }

    public void setTempCount(int tempCount) {
        this.tempCount = tempCount;
    }
}