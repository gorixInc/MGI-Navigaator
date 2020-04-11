
import frontEnd.eventHandler.FindPath;
import graph.Dijkstra;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        Button loadButton = new Button("Load");
        Button findPathButton = new Button("Find");

        VBox buttons = new VBox();

        FileChooser loadChooser = new FileChooser();
        loadChooser.setTitle("Save MAP file");
        loadChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MAP", "*.MAP")
        );

        Pane group = new Pane();

        loadButton.setOnAction(e -> {
            File file = loadChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    map = MapFileHandler.openMap(file.toString());
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                }
                Dijkstra dijkstra = new Dijkstra(map.getGraph());
                readMap(map, group);
                FindPath findPath = new FindPath(group, graphicalVertices, dijkstra);
                findPathButton.setOnAction(e1 -> group.setOnMouseClicked(findPath));
                buttons.getChildren().addAll(findPathButton);
            }
        });


        buttons.getChildren().addAll(loadButton);
        group.getChildren().addAll(buttons);

        stage.setScene(new Scene(group));
        stage.show();


    }

    private void readMap(Map map, Pane group) {
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
        graphicalVertices.forEach(gv -> group.getChildren().add(gv.getGraphics()));
        edgesGraphics.forEach(edge -> group.getChildren().add(edge));
    }

}
