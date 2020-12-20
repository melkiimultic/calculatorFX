package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HistoryController {

    private CalculatorDataSource dataSource = CalculatorDataSource.getInstance();

    @FXML
    private ScrollPane pane;
    @FXML
    private Label text;



    void showHistory(Parent root){
        Scene scene = new Scene(root, 500, 500);
        Stage stage = new Stage();
        text.maxWidthProperty().bind(pane.widthProperty());
        text.maxHeightProperty().bind(pane.heightProperty());
        stage.setScene(scene);
        stage.show();
        getHistory();
    }


    private void getHistory(){
        List<String> expressions = dataSource.selectAll();
        for (String s : expressions) {
            text.setText(text.getText()+"\n"+s);
        }


    }
}
