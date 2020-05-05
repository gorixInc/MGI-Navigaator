package frontEnd.eventHandler;

import javafx.scene.Group;
import map.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.List;

public class AddRoad implements EventHandler<MouseEvent> {

    private Group canvas;
    private List<GraphicalVertex> graphicalVertices;
    private Map graph;
    private List<GraphicalEdge> edgesGraphics;
    private boolean twoWay;
    private Integer roadType;
    private int maxSpeed;

    public void setRoadType(Integer roadType) {
        this.roadType = roadType;
        System.out.println(roadType);
    }

    public void setMaxSpeed(int maxSpeed){
        this.maxSpeed = maxSpeed;
    }

    public AddRoad(Group canvas, List<GraphicalVertex> graphicalVertices, Map graph, List<GraphicalEdge> edgesGraphics,
                   boolean twoWay, Integer roadType, int maxSpeed) {
        this.canvas = canvas;
        this.graphicalVertices = graphicalVertices;
        this.graph = graph;
        this.edgesGraphics = edgesGraphics;
        this.twoWay = twoWay;
        this.roadType = roadType;
        this.maxSpeed = maxSpeed;
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
                    if (graphicalVertex.getGraphics().contains(point) && graphicalVertex.getRoadVertex() != firstClick){
                        double firstX = firstClick.posX;
                        double firstY = firstClick.posY;
                        double secondX = graphicalVertex.getRoadVertex().posX;
                        double secondY = graphicalVertex.getRoadVertex().posY;
                        System.out.println(maxSpeed);
                                ////// ADD SPEED HERE!
                        if (twoWay) {
                            graph.addTwoWayRoad(firstClick, graphicalVertex.getRoadVertex(), maxSpeed,
                                    roadType);
                        } else {
                            graph.addOneWayRoad(firstClick, graphicalVertex.getRoadVertex(), maxSpeed,
                                    roadType);
                        }
                        GraphicalEdge gEdge = new GraphicalEdge(drawEdge(firstX, firstY, secondX, secondY, canvas),
                                firstClick, graphicalVertex.getRoadVertex());
                        edgesGraphics.add(gEdge);
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

    private Line drawEdge(double x1, double y1, double x2, double y2, Group group) {
        Line edge = new Line(x1, y1, x2, y2);
        edge.setStrokeWidth(2);
        group.getChildren().add(edge);
        return edge;
    }
}

