package pl.zakrzewow.totallyepicapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 650);
        stage.setTitle("Totalnie epicka aplikacja");
        stage.setScene(scene);
        scene.getStylesheets().add(String.valueOf(App.class.getResource("style.css")));
        stage.getIcons().add(new Image(String.valueOf(App.class.getResource("icon.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}