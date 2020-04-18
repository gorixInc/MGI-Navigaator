import java.io.IOException;
import java.util.ArrayList;

import map.*;
import graph.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class Main extends Application {

    ArrayList<Line> edgesGraphics = new ArrayList<>();
    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
    ArrayList<Line> routeEdges = new ArrayList<>();
    ArrayList<Circle> routeVertices = new ArrayList<>();
    Graph graph;
    Dijkstra dijkstra;


    @Override
    public void start(Stage stage) throws Exception {

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
            try {
                mapViewer.viewWindow();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SAXException ex) {
                ex.printStackTrace();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}