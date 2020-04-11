package frontEnd.eventHandler;

import map.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class AddRoad implements EventHandler<MouseEvent> {

    private Pane map;
    private ArrayList<GraphicalVertex> graphicalVertices;
    private Map graph;
    private ArrayList<Line> edgesGraphics;
    private boolean twoWay;

    public AddRoad(Pane map, ArrayList<GraphicalVertex> graphicalVertices, Map graph, ArrayList<Line> edgesGraphics, boolean twoWay) {
        this.map = map;
        this.graphicalVertices = graphicalVertices;
        this.graph = graph;
        this.edgesGraphics = edgesGraphics;
        this.twoWay = twoWay;
    }

    boolean first = true;
    RoadVertex firstClick;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            if (first) {
                for (GraphicalVertex graphicalVertex : graphicalVertices) {
                    if (graphicalVertex.getGraphics().contains(point)) {
                        firstClick = graphicalVertex.getRoadVertex();
                        first = false;
                        break;
                    }
                }
            } else {
                for (GraphicalVertex graphicalVertex : graphicalVertices) {
                    if (graphicalVertex.getGraphics().contains(point) && graphicalVertex.getRoadVertex() != firstClick) {
                        double firstX = firstClick.posX;
                        double firstY = firstClick.posY;
                        double secondX = graphicalVertex.getRoadVertex().posX;
                        double secondY = graphicalVertex.getRoadVertex().posY;
                        if (twoWay) {
                            graph.addTwoWayRoad(firstClick, graphicalVertex.getRoadVertex(), new Integer[]{1});
                        } else {
                            graph.addOneWayRoad(firstClick, graphicalVertex.getRoadVertex(), new Integer[]{1});
                        }
                        drawEdge(firstX, firstY, secondX, secondY, map);
                        first = true;
                        break;
                    }
                }
            }
        }
    }

    public void updateCheckbox(boolean twoWay){
        this.twoWay = twoWay;
    }

    private void drawEdge(double x1, double y1, double x2, double y2, Pane group) {
        Line edge = new Line(x1, y1, x2, y2);
        edge.setStrokeWidth(2);
        edgesGraphics.add(edge);
        group.getChildren().add(edge);
    }
}
