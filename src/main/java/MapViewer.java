import graph.Edge;
import graph.Vertex;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import map.GraphicalVertex;
import map.Map;
import map.MapFileHandler;
import map.RoadVertex;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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
        map = MapFileHandler.openMap("map.XML");

        System.out.println(map.getGraph().toString());
        for (Vertex vertex : map.getGraph().getVertices()) {
            RoadVertex rVertex = ((RoadVertex) vertex);
            Circle circle = new Circle(rVertex.posX, rVertex.posY, 5);
            GraphicalVertex gVertex = new GraphicalVertex(rVertex, circle);
            graphicalVertices.add(gVertex);
        }
        for (Vertex vertex : map.getGraph().getAdjacencyMap().keySet()) {
            for (Edge edge : map.getGraph().getAdjacencyMap().get(vertex)) {
                RoadVertex rVertexStart = ((RoadVertex) vertex);
                RoadVertex rVertexEnd = ((RoadVertex) edge.getDestination());
                Line line = new Line(rVertexStart.posX, rVertexStart.posY, rVertexEnd.posX, rVertexEnd.posY);
                edgesGraphics.add(line);
            }
        }

        Pane group = new Pane();
        graphicalVertices.forEach(gv -> group.getChildren().add(gv.graphics));
        edgesGraphics.forEach(edge -> group.getChildren().add(edge));

        stage.setScene(new Scene(group));
        stage.show();


    }
}
