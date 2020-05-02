import java.io.IOException;
import java.util.ArrayList;

import map.*;
import graph.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage){

        stage.setTitle("GPS");

        stage.setWidth(200);
        stage.setHeight(100);

        stage.setX(300);
        stage.setY(100);

        HBox window = new HBox();
        HBox buttons = new HBox();

        Button editorButton = new Button("Editor");
        Button viewerButton = new Button("Viewer");

        buttons.getChildren().addAll(editorButton, viewerButton);
        window.getChildren().add(buttons);

        Scene scene = new Scene(window);
        stage.setScene(scene);

        editorButton.setOnAction(e -> {
            MapEditor mapEditor = new MapEditor();
            mapEditor.editWindow();
        });

        viewerButton.setOnAction(e -> {
            MapViewer mapViewer = new MapViewer();
            mapViewer.viewWindow();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}