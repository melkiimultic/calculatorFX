package org.example;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

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
        String history = expressions.stream().collect(Collectors.joining("\n"));
        text.setText("\n" + history);
    }
}
