package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage)  {
        try {
            final URL resource = getClass().getResource("calculator.fxml");
            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            //выводить окошко все пошло не так
        }
    }

    public static void main(String[] args) {
        launch();
    }

}