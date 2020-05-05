import frontEnd.eventHandler.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import map.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapEditor {

    List<GraphicalVertex> graphicalVertices = new ArrayList<>();
    List<GraphicalEdge> edgesGraphics = new ArrayList<>();
    Map map;
    private java.util.Map<String, Integer> roadTypes = new HashMap<String, Integer>() {{
        put("Motorway", 1);
        put("Pedestrian", 2);
        put("Railway", 3);
    }};

    public void editWindow() {
        Stage stage = new Stage();
        stage.setTitle("Map Editor");

        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setX(300);
        stage.setY(100);

        BorderPane window = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        HBox topToolbar = new HBox();
        VBox topBar = new VBox();
        VBox controls = new VBox();
        controls.setStyle("-fx-background-color: #d4d4d4;");
        Pane centerWindow = new Pane();
        Group canvas = new Group();
        VBox rightSlider = new VBox();
        rightSlider.setStyle("-fx-background-color: #d4d4d4;");

        MenuItem newFile = new MenuItem("New");
        MenuItem saveFile = new MenuItem("Save");


        Button drag = new Button("Drag");
        Button addVertexButton = new Button("Add vertices");
        Button addEdgesButton = new Button("Add edges");
        Button deleteButton = new Button("Delete");

        //Scale controls and UI
        VBox scaleDefiner = new VBox();
        HBox scaleHbox = new HBox(10);
        Button setScaleButton = new Button("Define Scale");
        Label km = new Label("km");
        km.setFont(new Font(15));
        Label scaleInfo = new Label();
        TextField scaleRlDist = new TextField();
        scaleRlDist.setMaxWidth(50);
        scaleHbox.getChildren().addAll(setScaleButton, scaleRlDist, km);
        scaleDefiner.getChildren().addAll(scaleHbox, scaleInfo);

        Label speedLimitInfo1 = new Label("Motorway:");
        TextField speedLimit1 = new TextField("90");
        Label speedLimitInfo2 = new Label("Pedestrian:");
        TextField speedLimit2 = new TextField("5");
        Label speedLimitInfo3 = new Label("Railway:");
        TextField speedLimit3 = new TextField("120");

        java.util.Map<Integer, Integer> maxSpeeds = new HashMap<Integer, Integer>() {{
            put(1, 90);
            put(2, 5);
            put(3, 120);
        }};


        Slider zoomSlider = new Slider(0.25, 2, 1);
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setPrefHeight(stage.getHeight() * 0.75);

        Label zoomValue = new Label(zoomSlider.getValue() * 100 + "%");
        zoomSlider.valueProperty().addListener((observableValue, number, t1) -> {
            zoomValue.setText(String.format("%.0f", zoomSlider.getValue() * 100) + "%");
            canvas.setScaleX(zoomSlider.getValue());
            canvas.setScaleY(zoomSlider.getValue());
        });

        ObservableList<String> roadOptions = FXCollections.observableArrayList(
                "Motorway",
                "Pedestrian",
                "Railway"
        );

        ComboBox roadChoice = new ComboBox(roadOptions);
        roadChoice.setValue("Motorway");

        CheckBox twoWay = new CheckBox("Two way?");

        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Open image file");
        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );

        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Save MAP file");
        saveChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MAP", "*.MAP")
        );


        window.setCenter(centerWindow);
        window.setTop(topBar);
        window.setLeft(controls);
        window.setRight(rightSlider);

        centerWindow.getChildren().add(canvas);

        rightSlider.getChildren().addAll(zoomValue, zoomSlider);
        rightSlider.setSpacing(10);
        rightSlider.setPadding(new Insets(10, 10, 10, 10));
        rightSlider.setMinWidth(80);

        controls.setPadding(new Insets(10, 10, 10, 10));
        controls.setSpacing(10);

        topBar.getChildren().addAll(menuBar, topToolbar);

        menuBar.getMenus().addAll(fileMenu);

        fileMenu.getItems().addAll(newFile, saveFile);

        saveFile.setOnAction(e -> {
            canvas.setOnMousePressed(null);
            canvas.setOnMouseDragged(null);
            canvas.setOnMouseClicked(null);
            File file = saveChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    MapFileHandler.saveMap(map, file.toString());
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println(map);
        });

        newFile.setOnAction(e -> {
            File file = imageChooser.showOpenDialog(stage);
            if (file != null) {
                controls.getChildren().clear();
                graphicalVertices.clear();
                edgesGraphics.clear();
                canvas.getChildren().clear();
                map = new Map("nimi");
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(image.getHeight());
                imageView.setFitWidth(image.getWidth());

                canvas.getChildren().add(imageView);


                AddJunction addJunction = new AddJunction(canvas, graphicalVertices);
                AddRoad addRoad = new AddRoad(canvas, graphicalVertices, map, edgesGraphics,
                        twoWay.isSelected(), roadTypes.get(roadChoice.getValue().toString()),
                        maxSpeeds.get(roadTypes.get(roadChoice.getValue().toString())));
                SetScale setScale = new SetScale(canvas, map, scaleInfo);
                Deleter deleter = new Deleter(graphicalVertices, edgesGraphics, map, canvas);

                addVertexButton.setOnAction(mouseEvent -> {
                    canvas.setOnMousePressed(null);
                    canvas.setOnMouseDragged(null);
                    canvas.setOnMouseClicked(addJunction);
                    topToolbar.getChildren().clear();
                });

                addEdgesButton.setOnAction(mouseEvent -> {
                    canvas.setOnMousePressed(null);
                    canvas.setOnMouseDragged(null);
                    canvas.setOnMouseClicked(addRoad);
                    topToolbar.getChildren().clear();
                    topToolbar.getChildren().addAll(roadChoice, speedLimitInfo1, speedLimit1, speedLimitInfo2,
                            speedLimit2, speedLimitInfo3, speedLimit3);
                });

                deleteButton.setOnAction(mouseEvent -> {
                    canvas.setOnMousePressed(null);
                    canvas.setOnMouseDragged(null);
                    canvas.setOnMouseClicked(deleter);
                    topToolbar.getChildren().clear();
                });

                drag.setOnAction(mouseEvent -> {
                    canvas.setOnMouseClicked(null);
                    canvas.setOnMousePressed(presser -> {
                        canvas.setOnMouseDragged(dragger -> {
                            canvas.setTranslateX(dragger.getX() - presser.getX() + canvas.getTranslateX());
                            canvas.setTranslateY(dragger.getY() - presser.getY() + canvas.getTranslateY());
                        });
                    });
                });

                setScaleButton.setOnAction(mouseEvent -> {
                    try {
                        canvas.setOnMousePressed(null);
                        canvas.setOnMouseDragged(null);
                        double newRlDist = Double.parseDouble(scaleRlDist.getText());
                        setScale.setRlDistance(newRlDist);
                        canvas.setOnMouseClicked(setScale);
                    } catch (Exception eb) {
                        scaleInfo.setText("Please enter a number!");
                    }
                });

                twoWay.setOnAction(checboxEvent -> addRoad.updateCheckbox(twoWay.isSelected()));

                speedLimitChangeListener speedLimitChange1  = new speedLimitChangeListener(speedLimit1, maxSpeeds,
                        1, roadTypes.get(roadChoice.getValue().toString()), addRoad);
                speedLimit1.textProperty().addListener(speedLimitChange1);
                speedLimitChangeListener speedLimitChange2  = new speedLimitChangeListener(speedLimit2, maxSpeeds,
                        2, roadTypes.get(roadChoice.getValue().toString()), addRoad);
                speedLimit2.textProperty().addListener(speedLimitChange2);
                speedLimitChangeListener speedLimitChange3  = new speedLimitChangeListener(speedLimit3, maxSpeeds,
                        3, roadTypes.get(roadChoice.getValue().toString()), addRoad);
                speedLimit3.textProperty().addListener(speedLimitChange3);

                roadChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) -> {
                    addRoad.setRoadType(roadTypes.get(t1));
                    addRoad.setMaxSpeed(maxSpeeds.get(roadTypes.get(t1)));
                    speedLimitChange1.setRoadType(roadTypes.get(t1));
                    speedLimitChange2.setRoadType(roadTypes.get(t1));
                    speedLimitChange3.setRoadType(roadTypes.get(t1));
                });




                controls.getChildren().addAll(drag, addVertexButton, twoWay, addEdgesButton, deleteButton,
                        scaleDefiner);
            }
        });

        stage.setScene(new Scene(window));

        stage.show();
    }
}

