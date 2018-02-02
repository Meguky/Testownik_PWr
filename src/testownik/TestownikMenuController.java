package testownik;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TestownikMenuController {

    int innerRetryCount, innerInitialCount;
    String innerBaseChoice;
    @FXML
    private TextField retryCount;
    @FXML
    private TextField initialCount;
    @FXML
    private ComboBox<String> baseChoice;
    @FXML
    private Button beginTest;

    @FXML
    public void initialize(){
        beginTest.setOnAction(event -> {
            innerRetryCount = Integer.parseInt(retryCount.getText());
            innerInitialCount = Integer.parseInt(initialCount.getText());
            innerBaseChoice = baseChoice.getValue();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("testownikTest.fxml"));
                Parent root = loader.load();
                Stage testStage = new Stage();
                testStage.setTitle("Test");
                testStage.setScene(new Scene(root,1280,720));
                testStage.show();

                TestownikTestController controller = loader.getController();
                controller.initData(innerInitialCount, innerRetryCount, innerBaseChoice);
            }catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void chooseFolder(ActionEvent event){
        //Directory chooser to determine which question database folder will be in use.
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node)event.getTarget()).getScene().getWindow());
        if(selectedDirectory != null){
            try{
                //Getting path to the questions database from selected directory.
                String path = selectedDirectory.getAbsolutePath();
                String decodedPath = URLDecoder.decode(path,"UTF-8");
                File directory = new File(decodedPath);
                System.out.println(directory.getAbsolutePath());
                //Scanning for sub folders
                FileFilter directoryScanner = (file) -> file.isDirectory();
                File[] directoryFileList = directory.listFiles(directoryScanner);
                try{
                    //Making and populating array with sub folders names
                    List<String> fileNames = new ArrayList<String>(directoryFileList.length);
                    if(directoryFileList.length < 1){
                        //Checking if there are any sub folders
                        new Dialogs("Błąd","Nie znaleziono żadnych folderów z pytaniami.");
                    }
                    for(File file : directoryFileList){
                        fileNames.add(file.getName());
                    }
                    //Transitioning to Observable list for combo box
                    ObservableList<String> options = FXCollections.observableArrayList(fileNames);
                    baseChoice.setItems(options);
                }catch(NullPointerException e){}
            }catch(UnsupportedEncodingException e){}
        }else{
            new Dialogs("Błąd","Nie wybrano żadnego folderu z pytaniami.");
        }
    }
}