package testownik;

import javafx.scene.control.Alert;

public class Dialog {

    Dialog(String pTitle, String pMessage, Alert.AlertType pType){
        Alert alert = new Alert(pType);
        alert.setTitle(pTitle);
        alert.setHeaderText(null);
        alert.setContentText(pMessage);
        alert.showAndWait();
    }
}
