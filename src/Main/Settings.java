package Main;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lib.DB.DB;

import java.util.HashMap;

/**
 * Created by german on 03/01/16.
 *
 */

public class Settings {
    static DB d = new DB();

    public Settings(){

        //Scene & stage
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        settingsStage.setTitle("Settings");
        settingsStage.setMinWidth(300);
        settingsStage.setMinHeight(150);

        //Content
        VBox settingsLayout = new VBox();
        settingsLayout.setAlignment(Pos.CENTER_LEFT);
        settingsLayout.setPadding(new Insets(10,10,10,10));

        //Inputs
        ChoiceBox<Integer> emailCount = new ChoiceBox<>(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        ));
        emailCount.setValue(d.getCount());
        ChoiceBox<Integer> delayMinutes = new ChoiceBox<>(FXCollections.observableArrayList(
                1, 3, 5, 15, 30, 60
        ));
        delayMinutes.setValue(d.getDelay());
        CheckBox ssl = new CheckBox();
        ssl.setSelected(d.getSSL());


        //Input listeners
        emailCount.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable , oldValue, newValue) -> {
                    System.out.println("Selected: " + newValue);
                    d.setCount(newValue);
        });
        delayMinutes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable , oldValue, newValue) -> {
                    System.out.println("Selected: " + newValue);
                    d.setDelay(newValue);
        });
        ssl.setOnAction((event) -> {
            boolean selected = ssl.isSelected();
            System.out.println("SSL Checkbox selected: " + selected);
            d.setSSL(selected);
        });

        //Labels
        Label emailCountLabel = new Label("Number of e-mails to display: ");
        Label delayMinutesLabel1 = new Label("Refresh emails every ");
        Label delayMinutesLabel2 = new Label("minute(s)");
        Label sslLabel = new Label("Use SSL: ");

        //Input HBoxes / rows
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        HBox row3 = new HBox();
        row1.getChildren().addAll(emailCountLabel, emailCount);
        row2.getChildren().addAll(delayMinutesLabel1, delayMinutes, delayMinutesLabel2);
        row3.getChildren().addAll(sslLabel, ssl);


        //Add layout elements & show
        Scene settingsScene = new Scene(settingsLayout);
        settingsStage.setScene(settingsScene);
        settingsLayout.getChildren().addAll(row1, row2, row3);

        settingsStage.show();
    }

    public static int getCount() { return d.getCount(); }

    public static int getDelay() { return d.getDelay(); }

    public static String getProtocol() { return d.getProtocol(); }

    public static String getHost() { return d.getHost(); }

    public static String getPort() { return Integer.toString(d.getPort()); }

    public static boolean getSSL() { return d.getSSL(); }

    public static void createUser(String email, String pwd) { d.createUser(email, pwd); }

    public static void deleteUser() { d.deleteUser(); }

    public static HashMap getUser() { return d.getUser(); }

}
