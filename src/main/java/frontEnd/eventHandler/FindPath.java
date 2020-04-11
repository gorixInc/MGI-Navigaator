package frontEnd.eventHandler;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.*;
import graph.*;

import java.util.ArrayList;
import java.util.List;


public class FindPath implements EventHandler<MouseEvent> {

    private Pane pane;
    private ArrayList<GraphicalVertex> graphicalVertices;
    private Dijkstra dijkstra;

    private ArrayList<Circle> pathVertices = new ArrayList<>();
    private ArrayList<Line> pathEdges = new ArrayList<>();

    public FindPath(Pane pane, ArrayList<GraphicalVertex> graphicalVertices, Dijkstra dijkstra) {
        this.pane = pane;
        this.graphicalVertices = graphicalVertices;
        this.dijkstra = dijkstra;
    }

    boolean first = true;
    GraphicalVertex firstClick;
    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Point2D point = new javafx.geometry.Point2D(mouseEvent.getX(), mouseEvent.getY());
            if (first) {
                for (GraphicalVertex graphicalVertex : graphicalVertices) {
                    if (graphicalVertex.getGraphics().contains(point)) {
                        firstClick = graphicalVertex;
                        first = false;
                        break;
                    }
                }
            } else {
                for (GraphicalVertex graphicalVertex : graphicalVertices) {
                    if (graphicalVertex.getGraphics().contains(point)) {
                        Route route = dijkstra.getRoute(firstClick.getRoadVertex(), graphicalVertex.getRoadVertex());
                        drawPath(route, pane);
                        first = true;
                        break;
                    }
                }
            }
        }
    }

    private void drawPath(Route route, Pane pane) {
        clearPath();
        if (route.getPathVertices() == null) return;
        List<RoadVertex> pathVertices = route.getPathVertices();

        Circle vertex = new Circle(pathVertices.get(0).posX, pathVertices.get(0).posY, 2.5);
        vertex.setFill(Color.RED);
        this.pathVertices.add(vertex);
        vertex = new Circle(pathVertices.get(pathVertices.size()-1).posX, pathVertices.get(pathVertices.size()-1).posY, 2.5);
        vertex.setFill(Color.RED);
        this.pathVertices.add(vertex);

        for (int i = 0; i < pathVertices.size() - 1; i++) {
            Line edge = new Line(pathVertices.get(i).posX, pathVertices.get(i).posY, pathVertices.get(i+1).posX, pathVertices.get(i+1).posY);
            edge.setStroke(Color.RED);
            pathEdges.add(edge);
        }
        this.pathVertices.forEach(e -> pane.getChildren().add(e));
        pathEdges.forEach(e -> pane.getChildren().add(e));
    }

    private void clearPath(){
        pathVertices.forEach(e -> pane.getChildren().remove(e));
        pathEdges.forEach(e -> pane.getChildren().remove(e));
        pathEdges.clear();
        pathVertices.clear();
    }
}