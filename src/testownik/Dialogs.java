package testownik;

import javafx.scene.control.Alert;

public class Dialogs {

    Dialogs(String pTitle, String pMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(pTitle);
        alert.setHeaderText(null);
        alert.setContentText(pMessage);
        alert.showAndWait();
    }
}
