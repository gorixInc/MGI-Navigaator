
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapViewer {
    final List<GraphicalVertex> graphicalVertices = new ArrayList<>();
    final List<GraphicalEdge> edgesGraphics = new ArrayList<>();
    Map map;
    Integer currentTag = 0;

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

        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10,5,0,10));
        controls.setStyle("-fx-background-color: #d4d4d4;");

        Slider timeSlider = new Slider(0, 1439, 1);
        timeSlider.setOrientation(Orientation.HORIZONTAL);

        Label travelTimeLabel = new Label("Travel time: ");

        VBox rightSlider = new VBox(10);
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
                            tagChoiceNameToIndex.get(tagChoice.getValue()), map.getGraph(), timeSlider.getValue(),travelTimeLabel);
                    findPathButton.setOnAction(e1 -> {
                        canvas.setOnMousePressed(null);
                        canvas.setOnMouseDragged(null);
                        canvas.setOnMouseClicked(findPath);
                    });

                    Label timeValue = new Label(TimeConverter.hhmm(timeSlider.getValue()));
                    timeValue.setFont(Font.font(16));
                    timeSlider.valueProperty().addListener((observableValue, number, t1) -> {
                        timeValue.setText(TimeConverter.hhmm(timeSlider.getValue()));
                        findPath.setTime(timeSlider.getValue());
                        updateCongestion(timeSlider.getValue());
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
                    controls.getChildren().addAll(timeValue,timeSlider, tagChoice, findPathButton, travelTimeLabel);
                    tagChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) ->
                    {
                        currentTag = tagChoiceNameToIndex.get(t1);
                        findPath.setRoadType(currentTag);
                        updateCongestion(timeSlider.getValue());

                    });
                    rightSlider.getChildren().addAll(zoomValue, zoomSlider, dragButton);
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
            Circle circle = new Circle(vertex.posX, vertex.posY, 9);
            GraphicalVertex gVertex = new GraphicalVertex(vertex, circle);
            graphicalVertices.add(gVertex);
        }
        for (RoadVertex vertex : map.getGraph().getAdjacencyMap().keySet()) {
           edge: for (RoadEdge edge : map.getGraph().getAdjacencyMap().get(vertex)) {
               RoadVertex rVertexEnd = edge.getDestination();
                for(GraphicalEdge ge: edgesGraphics){
                    if(ge.getStart().equals(vertex) && ge.getEnd().equals(rVertexEnd)||
                        ge.getStart().equals(rVertexEnd) && ge.getEnd().equals(vertex)){
                        ge.addRoadEdge(edge);
                        continue edge;
                    }
                }
                Line line = new Line(vertex.posX, vertex.posY, rVertexEnd.posX, rVertexEnd.posY);
                line.setStrokeWidth(7);
                GraphicalEdge graphicalEdge = new GraphicalEdge(line, vertex, rVertexEnd);
                edgesGraphics.add(graphicalEdge);
            }
        }
        graphicalVertices.forEach(gv -> canvas.getChildren().add(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().add(edge.getEdge()));
    }

    private void clearMap(Group canvas) {
        graphicalVertices.forEach(gv -> canvas.getChildren().remove(gv.getGraphics()));
        edgesGraphics.forEach(edge -> canvas.getChildren().remove(edge));
        graphicalVertices.clear();
        edgesGraphics.clear();
        Rectangle rect = new Rectangle();
        rect.setX(-10000);
        rect.setY(-10000);
        rect.setWidth(20000);
        rect.setHeight(20000);
        rect.setFill(Color.WHITE);
        canvas.getChildren().add(rect);
    }


    private void updateCongestion(double time){
        for(GraphicalEdge ge : edgesGraphics){
            double multiplier = 0;
            for(RoadEdge re: ge.getRoadEdges()){
                if(re.getAllowedTag().equals(currentTag)){
                    if(re.hasCongestion()){
                        multiplier = re.getCongestionFunction().getMultiplierAtTime(time);
                        ge.getEdge().setStroke(Color.rgb((int) (
                                255-255/multiplier),0,0));
                        break;
                    }
                }
                ge.getEdge().setStroke(Color.BLACK);
            }
        }
    }
}
