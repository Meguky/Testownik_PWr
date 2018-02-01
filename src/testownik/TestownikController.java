package testownik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
        baseChoice.setItems(initComboBox());
    }

    @FXML
    public void beginTest(ActionEvent event){
        innerRetryCount = Integer.parseInt(retryCount.getText());
        innerInitialCount = Integer.parseInt(initialCount.getText());
        innerBaseChoice = baseChoice.getValue();

        JOptionPane.showMessageDialog(null,""+innerRetryCount+innerInitialCount+innerBaseChoice);
    }

    private ObservableList<String> initComboBox(){

        try{
            String path = this.getClass().getResource(".").getPath();
            String decodedPath = URLDecoder.decode(path,"UTF-8");
            File directory = new File(decodedPath + "\\bazy");

            System.out.println(directory.getAbsolutePath());
            FileFilter directoryScanner = (file) -> file.isDirectory();
            File[] directoryFileList = directory.listFiles(directoryScanner);
            try{
                List<String> fileNames = new ArrayList<String>(directoryFileList.length);
                for(File file : directoryFileList){
                    fileNames.add(file.getName());
                }
                ObservableList<String> options = FXCollections.observableArrayList(fileNames);
                return options;
            }catch(NullPointerException e){
                System.out.println(e.getMessage()); //TODO better exception handling
            }
        }catch(UnsupportedEncodingException e){
            //TODO Make exception handling
        }
        ObservableList<String> nullOptions = FXCollections.observableArrayList();
        return nullOptions;
    }


}
