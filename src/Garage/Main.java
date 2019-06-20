package Garage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String USER_PATH = "users.usr";

    @Override
    public void start(Stage primaryStage) throws Exception{
        String LOGIN_FXML_LOCATION = "Login\\Login.fxml";
        Parent parent =FXMLLoader.load(getClass().getResource(LOGIN_FXML_LOCATION));
        Scene scene= new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

