package testownik;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URLDecoder;

public class TestownikSettings {

    private String pathToBase;

    @FXML
    Label pathLabel;
    @FXML
    Button chooseFolderButton;
    @FXML
    Button saveConfigButton;
    @FXML
    VBox vb;

    @FXML
    void initialize(){
        loadConfig();
        if(pathToBase != null){
            pathLabel.setText(pathToBase);
        }
    }

    public void chooseFolder(ActionEvent event){
        //Directory chooser to determine which question database folder will be in use.
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node)event.getTarget()).getScene().getWindow());
        if(selectedDirectory != null){
            //Getting path to the questions database from selected directory.
            try{
                pathToBase = URLDecoder.decode(selectedDirectory.getAbsolutePath(),"UTF-8");
                pathLabel.setText(pathToBase);
            }catch(UnsupportedEncodingException e){
                e.getStackTrace();
            }
        }else{
            new Dialog("Błąd","Nie wybrano żadnego folderu z pytaniami.", Alert.AlertType.ERROR);
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

    public void saveConfig(){
        String pathToConfig = System.getProperty("user.dir");
        System.out.println(pathToConfig);
        try (Writer writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( pathToConfig + "\\settings"), "utf-8"))){
            writer.write(pathToBase);
        }catch(IOException e){
            e.getStackTrace();
        }
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.close();
    }
}
