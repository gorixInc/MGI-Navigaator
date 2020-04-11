import map.*;
import frontEnd.eventHandler.AddRoad;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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

        final HBox hBox = new HBox();
        VBox vBox = new VBox();
        Pane group = new Pane();
        hBox.getChildren().addAll(vBox, group);

        AddJunction addJunction = new AddJunction(group, graphicalVertices);
        addVertexButton.setOnAction(e -> group.setOnMouseClicked(addJunction));

        AddRoad addRoad = new AddRoad(group, graphicalVertices, map, edgesGraphics, twoWay.isSelected());
        addEdgesButton.setOnAction(e -> group.setOnMouseClicked(addRoad));

        twoWay.setOnAction(e->addRoad.updateCheckbox(twoWay.isSelected()));

        saveButton.setOnAction(e ->{
            group.setOnMouseClicked(null);
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


        openButton.setOnAction(
                actionEvent -> {
                    File file = imageChooser.showOpenDialog(stage);
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

