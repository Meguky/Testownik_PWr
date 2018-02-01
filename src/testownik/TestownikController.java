package testownik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node)event.getTarget()).getScene().getWindow());
        if(selectedDirectory == null){
            //TODO make not directory selected label
        }else{
            try{
                String path = selectedDirectory.getAbsolutePath();
                String decodedPath = URLDecoder.decode(path,"UTF-8");
                File directory = new File(decodedPath);

                System.out.println(directory.getAbsolutePath());
                FileFilter directoryScanner = (file) -> file.isDirectory();
                File[] directoryFileList = directory.listFiles(directoryScanner);
                try{
                    List<String> fileNames = new ArrayList<String>(directoryFileList.length);
                    for(File file : directoryFileList){
                        fileNames.add(file.getName());
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(fileNames);
                    baseChoice.setItems(options);
                }catch(NullPointerException e){
                    System.out.println(e.getMessage()); //TODO better exception handling
                }
            }catch(UnsupportedEncodingException e){
                //TODO Make exception handling
            }
        }


    }



}
