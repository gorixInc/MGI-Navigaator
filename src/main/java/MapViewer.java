
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapViewer {
    List<GraphicalVertex> graphicalVertices = new ArrayList<>();
    List<GraphicalEdge> edgesGraphics = new ArrayList<>();
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

        ObservableList<String> tags = FXCollections.observableArrayList();
        HashMap<String, Integer> tagChoiceNameToIndex= new HashMap<>();
        for(int i = 0; i < 6; i++){
            String presetName = "Tag " + (i + 1);
            tags.add(presetName);
            tagChoiceNameToIndex.put(presetName, i);
        }
        ComboBox tagChoice = new ComboBox(tags);
        tagChoice.setValue("Tag 1");

        Button findPathButton = new Button("Find");
        Button dragButton = new Button("Drag");


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
                    FindPath findPath = new FindPath(canvas, graphicalVertices, edgesGraphics, dijkstra,
                            tagChoiceNameToIndex.get(tagChoice.getValue()), map.getGraph());
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
                    tagChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) ->
                            findPath.setRoadType(tagChoiceNameToIndex.get(t1)));
                    topToolbar.getChildren().add(tagChoice);
                    rightSlider.getChildren().addAll(zoomValue, zoomSlider);
                    rightSlider.setSpacing(10);
                    rightSlider.setPadding(new Insets(10,10,10,10));
                    rightSlider.setMinWidth(80);
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
            Circle circle = new Circle(rVertex.posX, rVertex.posY, 9);
            GraphicalVertex gVertex = new GraphicalVertex(rVertex, circle);
            graphicalVertices.add(gVertex);
        }
        for (RoadVertex vertex : map.getGraph().getAdjacencyMap().keySet()) {
            for (RoadEdge edge : map.getGraph().getAdjacencyMap().get(vertex)) {
                RoadVertex rVertexStart = vertex;
                RoadVertex rVertexEnd = edge.getDestination();
                Line line = new Line(rVertexStart.posX, rVertexStart.posY, rVertexEnd.posX, rVertexEnd.posY);
                line.setStrokeWidth(7);
                GraphicalEdge graphicalEdge = new GraphicalEdge(line, rVertexStart, rVertexEnd);
                edgesGraphics.add(graphicalEdge);
            }
        }
        graphicalVertices.forEach(gv -> canvas.getChildren().add(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().add(edge.getEdge()));
    }

    private void clearMap(Group canvas){
        graphicalVertices.forEach(gv -> canvas.getChildren().remove(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().remove(edge));
        graphicalVertices.clear();
        edgesGraphics.clear();
    }
}
