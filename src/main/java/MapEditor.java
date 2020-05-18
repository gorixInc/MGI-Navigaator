import frontEnd.eventHandler.*;
import javafx.beans.value.ChangeListener;
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
    final List<GraphicalVertex> graphicalVertices = new ArrayList<>();
    final List<GraphicalEdge> edgesGraphics = new ArrayList<>();
    Map map;
    UiRoadPreset currentPreset;

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

        CheckBox twoWay = new CheckBox("Two way?");

        //CHANGES
        //Bottom panel layout

        final double padding = 5;
        final double entryWidth = 40;
        HBox bottomPanel =  new HBox(padding);

        final int numberOfTags = 5;
        VBox roadTagSettingsV = new VBox(padding); //Main for road tags
        CheckBox[] tagCheckboxes = new CheckBox[numberOfTags];
        TextField[] tagSpeedEntries = new TextField[numberOfTags];
        CheckBox[] tagHasCongestion = new CheckBox[numberOfTags];
        TextField[] tagCongPeak = new TextField[numberOfTags];
        TextField[] tagCongMult = new TextField[numberOfTags];
        TextField[] tagCongWidth = new TextField[numberOfTags];
        Label[] tagErrorMessages = new Label[numberOfTags];

        for(int i = 0; i < numberOfTags; i ++){
            CheckBox tagCheckBox = new CheckBox();
            TextField tagSpeedEntry = new TextField();
            CheckBox congCheck = new CheckBox();
            TextField congPeak = new TextField();
            TextField congMult = new TextField();
            TextField congWidth = new TextField();
            Label errorMessage = new Label();
            tagSpeedEntry.maxWidthProperty().setValue(entryWidth);
            congPeak.maxWidthProperty().setValue(entryWidth);
            congMult.maxWidthProperty().setValue(entryWidth);
            congWidth.maxWidthProperty().setValue(entryWidth);

            HBox tagHbox = new HBox(padding);
            Label tagLabel = new Label("Tag: " + (i+1));
            tagLabel.setPadding(new Insets(0,0,0,200));
            tagHbox.getChildren().addAll(tagLabel, tagCheckBox,  new Label("speed: "),
                    tagSpeedEntry, new Label("km/h"), new Label("Congestion?"), congCheck,
                    new Label("Congestion\npeak time"), congPeak, new Label("Congestion\nmultiplier"), congMult,
                    new Label("Congestion\nwidth"), congWidth, errorMessage);

            roadTagSettingsV.getChildren().add(tagHbox);
            tagCheckboxes[i] = tagCheckBox;
            tagSpeedEntries[i] = tagSpeedEntry;
            tagHasCongestion[i] = congCheck;
            tagCongPeak[i] = congPeak;
            tagCongMult[i] = congMult;
            tagCongWidth[i] = congWidth;
            tagErrorMessages[i] = errorMessage;
        }


        Button updatePresetButton = new Button("Update preset");

        bottomPanel.setStyle("-fx-background-color: #d4d4d4;");

        // bottom panel function
        final int numberOfPresets = 6;
        HashMap<String, Integer> presetNameToIndex = new HashMap<>();
        HashMap<Integer, UiRoadPreset> presetIndexToPreset = new HashMap<>();
        ObservableList<String> roadPresets = FXCollections.observableArrayList();
        for(int i = 0; i < numberOfPresets; i++){
            String presetName = "Road preset " + (i + 1);
            roadPresets.add(presetName);
            presetNameToIndex.put(presetName, i);
            presetIndexToPreset.put(i, new UiRoadPreset(numberOfTags));
        }
        ComboBox<String> roadChoice = new ComboBox<>(roadPresets);
        roadChoice.setValue("Road preset 1");
        bottomPanel.getChildren().addAll(roadTagSettingsV, roadChoice, updatePresetButton);

        for(int i = 0; i < 5; i++){
            presetIndexToPreset.put(i, new UiRoadPreset(numberOfTags));
        }
        //End of bottom panel

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
                } catch (ParserConfigurationException | TransformerException ignored) {}
            }
            System.out.println(map);
        });

        newFile.setOnAction(e -> {
            File file = imageChooser.showOpenDialog(stage);
            if (file != null) {
                window.setBottom(bottomPanel);
                window.setRight(rightSlider);
                controls.getChildren().clear();
                addEdgesButton.setDisable(true);
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
                        twoWay.isSelected(), numberOfPresets);
                SetScale setScale = new SetScale(canvas, map, scaleInfo);
                Deleter deleter = new Deleter(graphicalVertices, edgesGraphics, map, canvas);

                addVertexButton.setOnAction(mouseEvent -> {
                    canvas.setOnMousePressed(null);
                    canvas.setOnMouseDragged(null);
                    canvas.setOnMouseClicked(addJunction);
                    topToolbar.getChildren().clear();
                });

                addEdgesButton.setOnAction(mouseEvent -> {
                    int roadPresetIndex = presetNameToIndex.get(roadChoice.getValue());
                    UiRoadPreset currentRoadPreset = presetIndexToPreset.get(roadPresetIndex);
                    addRoad.setCurrentPreset(currentRoadPreset);
                    canvas.setOnMousePressed(null);
                    canvas.setOnMouseDragged(null);
                    canvas.setOnMouseClicked(addRoad);
                    topToolbar.getChildren().clear();
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

                roadChoice.valueProperty().addListener((observableValue, s, t1) -> {
                    int roadPresetIndex = presetNameToIndex.get(t1);
                    currentPreset = presetIndexToPreset.get(roadPresetIndex);
                    addRoad.setCurrentPreset(currentPreset);

                    //updating bottom panel
                    for(Label error : tagErrorMessages){
                        error.setText("");
                    }

                    for(int i = 0; i < numberOfTags; i++){
                        if(currentPreset.getTags().contains(i)){
                            tagCheckboxes[i].setSelected(true);
                            tagSpeedEntries[i].setText(String.valueOf(currentPreset.getMaxSpeed()[i]));
                            if(currentPreset.hasCongestion()[i]){
                                tagHasCongestion[i].setSelected(true);
                                tagCongMult[i].setText(String.valueOf(currentPreset.getCongestionFunction()[i].getMinMultiplier()));
                                tagCongPeak[i].setText(String.valueOf(currentPreset.getCongestionFunction()[i].getPeak()));
                                tagCongWidth[i].setText(String.valueOf(currentPreset.getCongestionFunction()[i].getWidth()));
                            }else {
                                tagCongMult[i].setText("");
                                tagCongPeak[i].setText("");
                                tagCongWidth[i].setText("");
                            }
                        }else{
                            tagCheckboxes[i].setSelected(false);
                            tagCongMult[i].setText("");
                            tagCongPeak[i].setText("");
                            tagCongWidth[i].setText("");
                            tagHasCongestion[i].setSelected(false);
                            tagSpeedEntries[i].setText("");
                        }
                    }
                });
                controls.getChildren().addAll(drag, addVertexButton, twoWay, addEdgesButton, deleteButton,
                        scaleDefiner);
            }
        });

        updatePresetButton.setOnAction(actionEvent-> {
            int currentPresetIndex = presetNameToIndex.get(roadChoice.getValue());
            UiRoadPreset updatedPreset = new UiRoadPreset(numberOfTags);
            List<Integer> newAllowedTags = new ArrayList<>();
            for(Label error : tagErrorMessages){
                error.setText("");
            }
            for(int i = 0; i < numberOfTags; i++){
                if(tagCheckboxes[i].isSelected()){
                    newAllowedTags.add(i);
                    try{
                        double speed = Double.parseDouble(tagSpeedEntries[i].getText());
                        if(speed == 0){
                            tagErrorMessages[i].setText("Speed limit cannot be 0!");
                            return;
                        }
                        updatedPreset.getMaxSpeed()[i] = speed;
                    }catch (Exception e){
                        tagErrorMessages[i].setText("Error in speed field!");
                        return;
                    }
                    CongestionFunction newCongFunc;
                    if(tagHasCongestion[i].isSelected()){
                        updatedPreset.hasCongestion()[i] = true;
                        try {
                            newCongFunc = new SinglePeakCongestion(Double.parseDouble(tagCongPeak[i].getText()),
                                    Double.parseDouble(tagCongMult[i].getText()), Double.parseDouble(tagCongWidth[i].getText()));
                        } catch (Exception e) {
                            tagErrorMessages[i].setText("Congestion function error!");
                            return;
                        }
                        updatedPreset.getCongestionFunction()[i] = newCongFunc;
                    }else {
                        updatedPreset.hasCongestion()[i] = false;
                        updatedPreset.setCongestionFunction(new NoCongestion(), i);
                    }
                }
                tagErrorMessages[i].setText("Updated successfully!");
            }
            updatedPreset.setTags(newAllowedTags);
            currentPreset = updatedPreset;
            presetIndexToPreset.put(currentPresetIndex, updatedPreset);
            addEdgesButton.setDisable(false);
        });

        stage.setScene(new Scene(window));

        stage.show();
    }

}

