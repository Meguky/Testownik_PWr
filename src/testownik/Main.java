package testownik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("testownikMenu.fxml"));
        primaryStage.setTitle("Testownik");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 320, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
