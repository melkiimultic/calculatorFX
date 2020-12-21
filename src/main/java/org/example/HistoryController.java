package org.example;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;

public class HistoryController {

    @FXML
    private TextArea text;

    void showHistory(Parent root) {
        Scene scene = new Scene(root, 400, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        getHistory();
    }


    private void getHistory() {
        List<String> expressions = CalculatorDataSource.selectAll();
        for (String s : expressions) {
            text.setText(text.getText() + "\n" + s);
        }

    }
}
