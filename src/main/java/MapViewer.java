
import frontEnd.eventHandler.FindPath;
import graph.Dijkstra;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
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

    public void viewWindow(){
        Stage stage = new Stage();
        stage.setTitle("Map Viewer");
        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setX(300);
        stage.setY(100);

        BorderPane window = new BorderPane();

        VBox topBar = new VBox();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem loadMap = new MenuItem("Load map");
        MenuItem loadImage = new MenuItem("Load image");
        fileMenu.getItems().addAll(loadImage, loadMap);
        menuBar.getMenus().add(fileMenu);


        HBox topToolbar = new HBox();

        ObservableList<String> roadOptions = FXCollections.observableArrayList(
                "Motorway",
                "Pedestrian",
                "Railway"
        );
        ComboBox roadChoice = new ComboBox(roadOptions);
        roadChoice.setValue("Motorway");

        Button findPathButton = new Button("Find");
        Button dragButton = new Button("Drag");

        Button scaleButton = new Button("Set scale");

        Pane centerWindow = new Pane();
        Group canvas = new Group();

        VBox controls = new VBox();
        controls.setStyle("-fx-background-color: #d4d4d4;");

        VBox rightSlider = new VBox();
        rightSlider.setStyle("-fx-background-color: #d4d4d4;");

        Slider zoomSlider = new Slider(0.25, 2, 1);
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setPrefHeight(stage.getHeight()*0.75);

        Label zoomValue = new Label(zoomSlider.getValue() * 100 + "%");
        zoomSlider.valueProperty().addListener((observableValue, number, t1) -> {
            zoomValue.setText(String.format("%.0f",zoomSlider.getValue() * 100) + "%");
            canvas.setScaleX(zoomSlider.getValue());
            canvas.setScaleY(zoomSlider.getValue());
        });

        window.setCenter(centerWindow);
        window.setTop(topBar);
        window.setLeft(controls);
        window.setRight(rightSlider);

        centerWindow.getChildren().add(canvas);


        topBar.getChildren().addAll(menuBar, topToolbar);

        stage.setScene(new Scene(window));

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
                    controls.getChildren().clear();
                    topToolbar.getChildren().clear();
                    rightSlider.getChildren().clear();
                    clearMap(canvas);
                    Dijkstra dijkstra = new Dijkstra(map.getGraph());
                    readMap(map, canvas);
                    FindPath findPath = new FindPath(canvas, graphicalVertices, dijkstra, roadChoice.getValue().toString());
                    findPathButton.setOnAction(e1 -> {
                        canvas.setOnMousePressed(null);
                        canvas.setOnMouseDragged(null);
                        canvas.setOnMouseClicked(findPath);
                    });
                    dragButton.setOnAction(mouseEvent ->{
                        canvas.setOnMouseClicked(null);
                        canvas.setOnMousePressed(presser ->{
                            canvas.setOnMouseDragged(dragger ->{
                                canvas.setTranslateX(dragger.getX() - presser.getX() + canvas.getTranslateX());
                                canvas.setTranslateY(dragger.getY() - presser.getY() + canvas.getTranslateY());
                            });
                        });
                    });
                    controls.getChildren().addAll(dragButton, findPathButton);
                    roadChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) -> findPath.setRoadType(t1));
                    topToolbar.getChildren().add(roadChoice);
                    rightSlider.getChildren().addAll(zoomValue, zoomSlider);
                    rightSlider.setSpacing(10);
                    rightSlider.setPadding(new Insets(10,10,10,10));
                    rightSlider.setMinWidth(80);
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
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
                imageView.setFitHeight(image.getHeight());
                imageView.setFitWidth(image.getWidth());
                canvas.getChildren().add(imageView);
            }
        });

        stage.show();
    }

    private void readMap(Map map, Group canvas) {
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

    private void clearMap(Group canvas){
        graphicalVertices.forEach(gv -> canvas.getChildren().remove(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().remove(edge));
        graphicalVertices.clear();
        edgesGraphics.clear();
    }
}
