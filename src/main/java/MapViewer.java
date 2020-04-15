
import frontEnd.eventHandler.FindPath;
import graph.Dijkstra;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapViewer {
    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
    ArrayList<Line> edgesGraphics = new ArrayList<>();
    Map map;

    public void viewWindow() throws IOException, SAXException, ParserConfigurationException {
        Stage stage = new Stage();
        stage.setTitle("Map Viewer");
        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setX(300);
        stage.setY(100);

        BorderPane window = new BorderPane();

        Menu fileMenu = new Menu("File");
        MenuBar menuBar = new MenuBar();
        MenuItem loadMap = new MenuItem("Load map");
        MenuItem loadImage = new MenuItem("Load image");
        menuBar.getMenus().add(fileMenu);
        fileMenu.getItems().addAll(loadImage, loadMap);

        HBox topToolbar = new HBox();
        VBox topBar = new VBox();

        topBar.getChildren().addAll(menuBar, topToolbar);

        ObservableList<String> roadOptions = FXCollections.observableArrayList(
                "Motorway",
                "Pedestrian",
                "Railway"
        );

        ComboBox roadChoice = new ComboBox(roadOptions);
        roadChoice.setValue("Motorway");

        Button findPathButton = new Button("Find");

        Pane canvas = new Pane();

        VBox buttons = new VBox();

        window.setTop(topBar);
        window.setLeft(buttons);
        window.setCenter(canvas);

        FileChooser loadChooser = new FileChooser();
        loadChooser.setTitle("Load MAP file");
        loadChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MAP", "*.MAP")
        );

        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Load Map Image");
        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );

        loadMap.setOnAction(e -> {
            File file = loadChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    map = MapFileHandler.openMap(file.toString());
                    buttons.getChildren().clear();
                    topToolbar.getChildren().clear();
                    clearMap(canvas);
                    Dijkstra dijkstra = new Dijkstra(map.getGraph());
                    readMap(map, canvas);
                    FindPath findPath = new FindPath(canvas, graphicalVertices, dijkstra, roadChoice.getValue().toString());
                    findPathButton.setOnAction(e1 -> canvas.setOnMouseClicked(findPath));
                    buttons.getChildren().addAll(findPathButton);
                    roadChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) -> findPath.setRoadType(t1));
                    topToolbar.getChildren().add(roadChoice);
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                }
            }
        });


        loadImage.setOnAction(e -> {
            File file = imageChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(480);
                imageView.setFitWidth(960);
                canvas.getChildren().add(imageView);
            }
        });

        stage.setScene(new Scene(window));
        stage.show();


    }

    private void readMap(Map map, Pane canvas) {
        for (RoadVertex vertex : map.getGraph().getVertices()) {
            RoadVertex rVertex = vertex;
            Circle circle = new Circle(rVertex.posX, rVertex.posY, 5);
            GraphicalVertex gVertex = new GraphicalVertex(rVertex, circle);
            graphicalVertices.add(gVertex);
        }
        for (RoadVertex vertex : map.getGraph().getAdjacencyMap().keySet()) {
            for (RoadEdge edge : map.getGraph().getAdjacencyMap().get(vertex)) {
                RoadVertex rVertexStart = vertex;
                RoadVertex rVertexEnd = edge.getDestination();
                Line line = new Line(rVertexStart.posX, rVertexStart.posY, rVertexEnd.posX, rVertexEnd.posY);
                line.setStrokeWidth(2.5);
                edgesGraphics.add(line);
            }
        }
        graphicalVertices.forEach(gv -> canvas.getChildren().add(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().add(edge));
    }

    private void clearMap(Pane canvas){
        graphicalVertices.forEach(gv -> canvas.getChildren().remove(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().remove(edge));
        graphicalVertices.clear();
        edgesGraphics.clear();
    }

}
