package testownik;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private MenuBar menuBar;
    @FXML
    private Menu menu;
    @FXML
    private MenuItem settings;
    @FXML
    private MenuItem about;
    @FXML
    private MenuItem exit;

    @FXML
    public void settingsHandle(){
        try {
            //Initializing new windows with test
            FXMLLoader loader = new FXMLLoader(getClass().getResource("testownikSettings.fxml"));
            Parent root = loader.load();
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.setScene(new Scene(root,360,150));
            settingsStage.setResizable(false);
            settingsStage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void aboutHandle(){
        String aboutText = "Program stworzony do pomocy przy nauce pytań na testy PWr,\n" +
                "Kompatybilny ze starymi bazami pytań (pytania numerowane od 000 do 999)\n" +
                "Stworzony przez: Krzysztof Razik";
        new Dialog("O Programie", aboutText,Alert.AlertType.INFORMATION);
    }

    @FXML
    public void exitHandle(){
        Platform.exit();
    }

    @FXML
    public void initialize(){
        beginTest.setFocusTraversable(true);
    }

    @FXML
    public void beginTest(){
        boolean errorFlag = false;
        //Checking if innerRetryCount is Integer (0-INT_MAX)
        try{
            innerRetryCount = Integer.parseInt(retryCount.getText());
            if(innerRetryCount < 0){
                new Dialog("Błąd", "Ilość początkowych wystąpnień pytania -> Liczba musi być równa lub większa niż zero.", Alert.AlertType.ERROR);
                errorFlag = true;
            }
        }catch(NumberFormatException e){
            new Dialog("Błąd","Ilość początkowych wystąpnień pytania -> Wpisana wartość nie jest liczbą, lub jest za duża.", Alert.AlertType.ERROR);
            errorFlag = true;
        }
        //Checking if innerInitialCount is Integer (1-INT_MAX)
        try{
            innerInitialCount = Integer.parseInt(initialCount.getText());
            if(innerInitialCount <= 0){
                new Dialog("Błąd", "Ilość powtórzeń w razie błędu -> Liczba musi być większa niż zero.", Alert.AlertType.ERROR);
                errorFlag = true;
            }
        }catch(NumberFormatException e){
            new Dialog("Błąd","Ilość powtórzeń w razie błędu -> Wpisana wartość nie jest liczbą, lub jest za duża.", Alert.AlertType.ERROR);
            errorFlag = true;
        }
        //Cheking if any questions database is selected
        innerBaseChoice += "\\" + baseChoice.getValue();
        if(baseChoice.getValue() == null){
            new Dialog("Błąd","Nie wybrano żadnej bazy pytań", Alert.AlertType.ERROR);
            errorFlag = true;
        }
        if(!errorFlag) {
            try(BufferedReader database = new BufferedReader(new FileReader(new File(innerBaseChoice + "\\" + "baza.txt")))){
                try {
                    //Initializing new windows with test
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("testownikTest.fxml"));
                    Parent root = loader.load();
                    Stage testStage = new Stage();
                    testStage.setTitle("Test");
                    testStage.setResizable(false);
                    testStage.setScene(new Scene(root,1280,720));
                    testStage.show();
                    //Passing parameters to test window
                    TestownikTestController controller = loader.<TestownikTestController>getController();
                    controller.initData(innerInitialCount, innerRetryCount, innerBaseChoice);
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }catch(IOException e){
                new Dialog("Błąd", "Nie wykryto bazy pytań, dodaj plik baza.txt do folderu z pytaniami", Alert.AlertType.ERROR);
            }
        }
        errorFlag = false;
    }

    public void chooseFolder(ActionEvent event){
        //Directory chooser to determine which question database folder will be in use.
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node)event.getTarget()).getScene().getWindow());
        if(selectedDirectory != null){
            //Getting path to the questions database from selected directory.
            try{
                String path = URLDecoder.decode(selectedDirectory.getAbsolutePath(),"UTF-8");
                innerBaseChoice = path;
                populateComboBox(path);
            }catch(UnsupportedEncodingException e){}
        }else{
            new Dialog("Błąd","Nie wybrano żadnego folderu z pytaniami.", Alert.AlertType.ERROR);
        }
    }

    void initData(String pPathToQuestionBase){
        innerBaseChoice = pPathToQuestionBase;
        System.out.println(pPathToQuestionBase);
        populateComboBox(pPathToQuestionBase);
    }

    private void populateComboBox(String pathToDirectory){
        File directory = new File(pathToDirectory);
        //Scanning for sub folders
        FileFilter directoryScanner = (file) -> file.isDirectory();
        File[] directoryFileList = directory.listFiles(directoryScanner);
        try{
            //Making and populating array with sub folders names
            List<String> fileNames = new ArrayList<>(directoryFileList.length);
            if(directoryFileList.length < 1){
                //Checking if there are any sub folders
                new Dialog("Błąd","Nie znaleziono żadnych folderów z pytaniami.", Alert.AlertType.ERROR);
            }
            for(File file : directoryFileList){
                fileNames.add(file.getName());
            }
            //Transitioning to Observable list for combo box
            ObservableList<String> options = FXCollections.observableArrayList(fileNames);
            baseChoice.setItems(options);
        }catch(NullPointerException e){}
    }
}
