

import java.io.IOException;
import java.util.ArrayList;

import javafx.stage.FileChooser;
import map.*;

import graph.Dijkstra;
import graph.Graph;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

        stage.setWidth(700);
        stage.setHeight(500);

        stage.setX(300);
        stage.setY(100);

        HBox root = new HBox();
        VBox buttons = new VBox();

        Button editorButton = new Button("Editor");
        Button viewerButton = new Button("Viewer");

        Image image = new Image("http://www.thepluspaper.com/wp-content/uploads/2019/01/1.jpg");

        BackgroundImage background = new BackgroundImage(image, null, null, null, null);

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        buttons.getChildren().addAll(editorButton, viewerButton);


        gc.setFill(Color.WHEAT);
        gc.fillRect(0, 0, 600, 500);

        Pane map = new Pane(canvas);

        root.getChildren().add(buttons);
        root.getChildren().add(map);

        Scene scene1 = new Scene(root);

        root.setBackground(new Background(background));

        stage.setScene(scene1);

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