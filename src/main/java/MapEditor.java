import frontEnd.eventHandler.Deleter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import map.*;
import frontEnd.eventHandler.AddRoad;
import frontEnd.eventHandler.AddJunction;
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

public class MapEditor {

    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
    ArrayList<GraphicalEdge> edgesGraphics = new ArrayList<>();
    Map map;

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
        rightSlider.setPadding(new Insets(10,10,10,10));
        rightSlider.setMinWidth(80);

        controls.setPadding(new Insets(10,10,10,10));
        controls.setSpacing(10);

        topBar.getChildren().addAll(menuBar, topToolbar);

        menuBar.getMenus().addAll(fileMenu);

        fileMenu.getItems().addAll(newFile, saveFile);

        saveFile.setOnAction(e ->{
            canvas.setOnMousePressed(null);
            canvas.setOnMouseDragged(null);
            canvas.setOnMouseClicked(null);
            File file = saveChooser.showSaveDialog(stage);
            if (file != null){
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

        newFile.setOnAction(e ->{
                File file = imageChooser.showOpenDialog(stage);
                if (file != null){
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
                    AddRoad addRoad = new AddRoad(canvas, graphicalVertices, map, edgesGraphics, twoWay.isSelected(), roadChoice.getValue().toString());
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
                        topToolbar.getChildren().add(roadChoice);
                    });

                    deleteButton.setOnAction(mouseEvent -> {
                        canvas.setOnMousePressed(null);
                        canvas.setOnMouseDragged(null);
                        canvas.setOnMouseClicked(deleter);
                        topToolbar.getChildren().clear();
                    });

                    drag.setOnAction(mouseEvent ->{
                        canvas.setOnMouseClicked(null);
                        canvas.setOnMousePressed(presser ->{
                            canvas.setOnMouseDragged(dragger ->{
                                canvas.setTranslateX(dragger.getX() - presser.getX() + canvas.getTranslateX());
                                canvas.setTranslateY(dragger.getY() - presser.getY() + canvas.getTranslateY());
                            });
                        });
                    });

                    twoWay.setOnAction(checboxEvent -> addRoad.updateCheckbox(twoWay.isSelected()));

                    roadChoice.valueProperty().addListener((ChangeListener<String>) (observableValue, s, t1) -> addRoad.setRoadType(t1));

                    controls.getChildren().addAll(drag, addVertexButton, twoWay, addEdgesButton, deleteButton);
                }
            });



        stage.setScene(new Scene(window));

        stage.show();
    }
}

