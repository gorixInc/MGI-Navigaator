package frontEnd.eventHandler;

import map.*;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;

public class AddRoad implements EventHandler<MouseEvent> {

    private Pane map;
    private ArrayList<GraphicalVertex> graphicalVertices;
    private Map graph;
    private ArrayList<GraphicalEdge> edgesGraphics;
    private boolean twoWay;
    private String roadType;
    private HashMap<String, Integer> roadTypes = new HashMap<String, Integer>(){{
        put("Motorway", 1);
        put("Pedestrian", 2);
        put("Railway", 3);
    }};

    public void setRoadType(String roadType) {
        this.roadType = roadType;
        System.out.println(roadType);
    }

    public AddRoad(Pane map, ArrayList<GraphicalVertex> graphicalVertices, Map graph, ArrayList<GraphicalEdge> edgesGraphics, boolean twoWay, String roadType) {
        this.map = map;
        this.graphicalVertices = graphicalVertices;
        this.graph = graph;
        this.edgesGraphics = edgesGraphics;
        this.twoWay = twoWay;
        this.roadType = roadType;
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
                            graph.addTwoWayRoad(firstClick, graphicalVertex.getRoadVertex(), new Integer[]{roadTypes.get(roadType)});
                        } else {
                            graph.addOneWayRoad(firstClick, graphicalVertex.getRoadVertex(), new Integer[]{roadTypes.get(roadType)});
                        }
                        GraphicalEdge gEdge = new GraphicalEdge(drawEdge(firstX, firstY, secondX, secondY, map),firstClick, graphicalVertex.getRoadVertex());
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

    private Line drawEdge(double x1, double y1, double x2, double y2, Pane group) {
        Line edge = new Line(x1, y1, x2, y2);
        edge.setStrokeWidth(2);
        group.getChildren().add(edge);
        return edge;
    }
}

