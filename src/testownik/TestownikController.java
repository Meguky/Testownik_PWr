package testownik;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TestownikController {

    int innerRetryCount, innerInitialCount;
    String innerBaseChoice;
    @FXML
    private TextField retryCount;
    @FXML
    private TextField initialCount;
    @FXML
    private ComboBox<String> baseChoice;

    @FXML
    public void initialize(){

    }

    @FXML
    public void beginTest(ActionEvent event){
        innerRetryCount = Integer.parseInt(retryCount.getText());
        innerInitialCount = Integer.parseInt(initialCount.getText());
        innerBaseChoice = baseChoice.getValue();

        JOptionPane.showMessageDialog(null,""+innerRetryCount+innerInitialCount+innerBaseChoice);
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
                    if(fileNames.size() < 1){
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
