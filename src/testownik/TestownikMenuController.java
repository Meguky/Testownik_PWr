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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TestownikMenuController {

    private int innerRetryCount, innerInitialCount;
    private String innerBaseChoice;
    @FXML
    private TextField retryCount;
    @FXML
    private TextField initialCount;
    @FXML
    private ComboBox<String> baseChoice;
    @FXML
    private Button beginTest;



    @FXML
    public void beginTest(){
        beginTest.setOnAction(event -> {
            //Checking if innerRetryCount is Integer
            try{
                innerRetryCount = Integer.parseInt(retryCount.getText());
            }catch(NumberFormatException e){
                new Dialog("Błąd","Wpisz liczbę w polu od ilości powtórzeń w razie błędu");
            }
            //Checking if innerInitialCount is Integer
            try{
                innerInitialCount = Integer.parseInt(initialCount.getText());
            }catch(NumberFormatException e){
                new Dialog("Błąd","Wpisz liczbę w polu od ilości początkowych wystąpień pytania");
            }
            //Cheking if any questions database is selected
            innerBaseChoice += "\\" + baseChoice.getValue();
            if(baseChoice.getValue() == null){
                new Dialog("Błąd","Nie wybrano żadnej bazy");
            }else{
                try(BufferedReader database = new BufferedReader(new FileReader(new File(innerBaseChoice + "\\" + "baza.txt")))){
                    try {
                        //Initializing new windows with test
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("testownikTest.fxml"));
                        Parent root = loader.load();
                        Stage testStage = new Stage();
                        testStage.setTitle("Test");
                        testStage.setScene(new Scene(root,1280,720));
                        testStage.show();
                        //Passing parameters to test window
                        TestownikTestController controller = loader.<TestownikTestController>getController();
                        controller.initData(innerInitialCount, innerRetryCount, innerBaseChoice);
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                }catch(IOException e){
                    new Dialog("Error", "Folder nie zawiera bazy pytań.");
                }
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
                String path = URLDecoder.decode(selectedDirectory.getAbsolutePath(),"UTF-8");
                innerBaseChoice = path;
                File directory = new File(path);
                System.out.println(directory.getAbsolutePath());
                //Scanning for sub folders
                FileFilter directoryScanner = (file) -> file.isDirectory();
                File[] directoryFileList = directory.listFiles(directoryScanner);
                try{
                    //Making and populating array with sub folders names
                    List<String> fileNames = new ArrayList<>(directoryFileList.length);
                    if(directoryFileList.length < 1){
                        //Checking if there are any sub folders
                        new Dialog("Błąd","Nie znaleziono żadnych folderów z pytaniami.");
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
            new Dialog("Błąd","Nie wybrano żadnego folderu z pytaniami.");
        }
    }
}
