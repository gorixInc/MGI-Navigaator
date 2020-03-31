import backEnd.*;
import frontEnd.eventHandler.AddOneWayRoad;
import frontEnd.eventHandler.AddJunction;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MapEditor {

    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
    ArrayList<Line> edgesGraphics = new ArrayList<>();
    Map map;

    public void editWindow() {
        Stage stage = new Stage();
        stage.setTitle("Map Editor");

        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setX(300);
        stage.setY(100);

        map = new Map("nimi");

        Button openButton = new Button("Open a picture");
        Button addVertexButton = new Button("Add vertices");
        Button addEdgesButton = new Button("Add edges");
        Button saveButton = new Button("Save");

        CheckBox twoWay = new CheckBox("Two way?");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open image file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        final HBox hBox = new HBox();
        VBox vBox = new VBox();
        Pane group = new Pane();
        hBox.getChildren().addAll(vBox, group);

        AddJunction addJunction = new AddJunction(group, graphicalVertices);
        addVertexButton.setOnAction(e -> group.setOnMouseClicked(addJunction));

        AddOneWayRoad addOneWayRoad = new AddOneWayRoad(group, graphicalVertices, map, edgesGraphics, twoWay.isSelected());
        addEdgesButton.setOnAction(e -> group.setOnMouseClicked(addOneWayRoad));

        twoWay.setOnAction(e->addOneWayRoad.updateCheckbox(twoWay.isSelected()));

        saveButton.setOnAction(e ->{
            group.setOnMouseClicked(null);
            System.out.println(map);
        });



        openButton.setOnAction(
                actionEvent -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null){
                        Image image = new Image(file.toURI().toString());
                        ImageView imageView = new ImageView(image);
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(480);
                        imageView.setFitWidth(960);

                        group.getChildren().add(imageView);
                    }
                }
        );




        vBox.getChildren().addAll(openButton, addVertexButton, addEdgesButton, saveButton, twoWay);

        stage.setScene(new Scene(hBox));

        stage.show();
    }
}

