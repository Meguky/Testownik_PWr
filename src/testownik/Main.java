package testownik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    String pathToBase;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loadConfig();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("testownikMenu.fxml"));
        Parent root = loader.load();
        Stage testStage = new Stage();
        primaryStage.setTitle("Testownik");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 320, 480));
        primaryStage.show();
        if(pathToBase != null){
            TestownikMenuController controller = loader.<TestownikMenuController>getController();
            controller.initData(pathToBase);
        }

    }

    void loadConfig(){
        String pathToConfig = System.getProperty("user.dir");
        try (BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( pathToConfig + "\\settings"), "utf-8"))){
            pathToBase = reader.readLine();
        }catch(IOException e) {
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
