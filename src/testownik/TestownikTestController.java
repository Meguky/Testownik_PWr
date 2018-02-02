package testownik;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class TestownikTestController {
    private int testInitialCount, testRetryCount;
    private String testBaseChoice;

    @FXML
    Label label1;
    @FXML
    Label label2;
    @FXML
    Label label3;

    void initData(int pInitial, int pRetry, String pBase){
        testInitialCount = pInitial;
        testRetryCount = pRetry;
        testBaseChoice = pBase;
    }

    @FXML
    public void setLabels(ActionEvent event){
        label1.setText(testBaseChoice);
        label2.setText(Integer.toString(testRetryCount));
        label3.setText(Integer.toString(testInitialCount));
    }
}
