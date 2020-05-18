package frontEnd.eventHandler;

import graph.Dijkstra;
import graph.Graph;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindPath implements EventHandler<MouseEvent> {

    private Group canvas;
    private List<GraphicalVertex> graphicalVertices;
    private Dijkstra dijkstra;
    private List<GraphicalEdge> graphicalEdges;
    private Graph graph;

    private List<Circle> routeVertices = new ArrayList<>();
    private List<Line> routeEdges = new ArrayList<>();
    private double time;
    private Integer travellerTag;
    private Label travelTimeLabel;

    public void setRoadType(Integer travellerTag) {
        this.travellerTag = travellerTag;
    }

    public FindPath(Group canvas, List<GraphicalVertex> graphicalVertices, List<GraphicalEdge> graphicalEdges,
                    Dijkstra dijkstra, Integer travellerTag, Graph graph, double time, Label travelTimeLabel) {
        this.canvas = canvas;
        this.graphicalVertices = graphicalVertices;
        this.graphicalEdges = graphicalEdges;
        this.dijkstra = dijkstra;
        this.travellerTag = travellerTag;
        this.graph = graph;
        this.time = time;
        this.travelTimeLabel = travelTimeLabel;
    }

    private boolean first = true;
    private List<RoadVertex> firstVertices;
    private List<Route> routes;
    Point2D pointFirst = null;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Point2D point = new javafx.geometry.Point2D(mouseEvent.getX(), mouseEvent.getY());

            if (first) {
                routes = new ArrayList<Route>();
                firstVertices = firstClick(point);
                first = false;
                pointFirst = point;
            } else {
                List<RoadVertex> secondVertices = secondClick(point, pointFirst);
                if (firstVertices.size() == 1) {
                    if (secondVertices.size() == 1) {
                        Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(0), secondVertices.get(0),travellerTag, time
                        );
                        routes.add(route);
                    } else {
                        for (int i = 0; i < secondVertices.size(); i += 2) {
                            if (secondVertices.get(i).index.equals(secondVertices.get(i + 1).index)) {
                                List<RoadVertex> routeVertices = new ArrayList<RoadVertex>();
                                routeVertices.add(secondVertices.get(i));
                                routeVertices.add(secondVertices.get(i + 1));
                                double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(i).posX - secondVertices.get(i + 1).posX), 2) + Math.pow((secondVertices.get(i).posY - secondVertices.get(i + 1).posY), 2));
                                Route route = new Route(routeVertices, distanceBetweenVertices);
                                routes.add(route);
                            } else {
                                Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(0), secondVertices.get(i + 1), travellerTag,time);
                                List<RoadVertex> routeVertices = route.getPathVertices();
                                double routeWeight = route.getTotalWeight();
                                routeVertices.add(secondVertices.get(i));
                                double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(i).posX - secondVertices.get(i + 1).posX), 2) + Math.pow((secondVertices.get(i).posY - secondVertices.get(i + 1).posY), 2));
                                routeWeight += distanceBetweenVertices;
                                route = new Route(routeVertices, routeWeight);
                                routes.add(route);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < firstVertices.size(); i += 2) {
                        if (secondVertices.size() == 1) {
                            Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(i + 1), secondVertices.get(0), travellerTag,time);
                            List<RoadVertex> routeVertices = route.getPathVertices();
                            double routeWeight = route.getTotalWeight();
                            routeVertices.add(0, firstVertices.get(i));
                            double distanceBetweenVertices = Math.sqrt(Math.pow((firstVertices.get(i).posX - firstVertices.get(i + 1).posX), 2) + Math.pow((firstVertices.get(i).posY - firstVertices.get(i + 1).posY), 2));
                            routeWeight += distanceBetweenVertices;
                            route = new Route(routeVertices, routeWeight);
                            routes.add(route);
                        } else {
                            for (int j = 0; j < secondVertices.size(); j += 2) {
                                if (secondVertices.get(j).index.equals(secondVertices.get(j + 1).index)) {
                                    List<RoadVertex> routeVertices = new ArrayList<RoadVertex>();
                                    routeVertices.add(secondVertices.get(j + 1));
                                    routeVertices.add(secondVertices.get(j));
                                    double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(j).posX - secondVertices.get(j + 1).posX), 2) + Math.pow((secondVertices.get(j).posY - secondVertices.get(j + 1).posY), 2));
                                    Route route = new Route(routeVertices, distanceBetweenVertices);
                                    routes.add(route);
                                } else {
                                    Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(i + 1), secondVertices.get(j + 1), travellerTag,time);
                                    List<RoadVertex> routeVertices = route.getPathVertices();
                                    double routeWeight = route.getTotalWeight();
                                    routeVertices.add(secondVertices.get(j));
                                    routeVertices.add(0, firstVertices.get(i));
                                    double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(j).posX - secondVertices.get(j + 1).posX), 2) + Math.pow((secondVertices.get(j).posY - secondVertices.get(j + 1).posY), 2));
                                    distanceBetweenVertices += Math.sqrt(Math.pow((firstVertices.get(i).posX - firstVertices.get(i + 1).posX), 2) + Math.pow((firstVertices.get(i).posY - firstVertices.get(i + 1).posY), 2));
                                    routeWeight += distanceBetweenVertices;
                                    route = new Route(routeVertices, routeWeight);
                                    routes.add(route);
                                }
                            }
                        }
                    }
                }
                first = true;
            }
            Route shortestRoute = null;
            double smallestWeight = Double.MAX_VALUE;
            for (Route route : routes) {
                if (route.getTotalWeight() < smallestWeight) {
                    shortestRoute = route;
                    smallestWeight = shortestRoute.getTotalWeight();
                }
            }
            if (shortestRoute != null) {
                double travelTimeMins = shortestRoute.getTotalWeight()*60;

                travelTimeLabel.setText("Travel time: "+ TimeConverter.getHoursWithUnit(travelTimeMins) +
                        TimeConverter.getMinsWithUnit(travelTimeMins));
                drawPath(shortestRoute, canvas);
            }
        }
    }

    private List<RoadVertex> firstClick(Point2D point) {
        List<RoadVertex> firstVertices = new ArrayList<RoadVertex>();

        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            if (graphicalVertex.getGraphics().contains(point)) {
                firstVertices.add(graphicalVertex.getRoadVertex());
                return firstVertices;
            }
        }
        for (GraphicalEdge graphicalEdge : graphicalEdges) {
            if (graphicalEdge.getEdge().contains(point)) {
                loop:
                for (RoadEdge roadEdge : graph.getAdjacencyMap().get(graphicalEdge.getStart())) {
                    if (roadEdge.getDestination().equals(graphicalEdge.getEnd())) {
                        Integer allowedTag = roadEdge.getAllowedTag();
                        if (allowedTag.equals(travellerTag)) {
                            RoadVertex newVertex = new RoadVertex(-1, point.getX(), point.getY());
                            firstVertices.add(newVertex);
                            firstVertices.add(graphicalEdge.getStart());
                            break loop;
                        }
                    }
                }
            }
        }
        return firstVertices;
    }

    private List<RoadVertex> secondClick(Point2D point, Point2D pointFirst) {
        List<RoadVertex> secondVertices = new ArrayList<RoadVertex>();

        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            if (graphicalVertex.getGraphics().contains(point)) {
                secondVertices.add(graphicalVertex.getRoadVertex());
                return secondVertices;
            }
        }
        for (GraphicalEdge graphicalEdge : graphicalEdges) {
            if (graphicalEdge.getEdge().contains(point)) {
                loop:
                for (RoadEdge roadEdge : graph.getAdjacencyMap().get(graphicalEdge.getStart())) {
                    if (roadEdge.getDestination().equals(graphicalEdge.getEnd())) {
                        Integer allowedTag = roadEdge.getAllowedTag();
                        if (allowedTag.equals(travellerTag)) {
                            RoadVertex newVertex = new RoadVertex(-1, point.getX(), point.getY());
                            secondVertices.add(newVertex);
                            secondVertices.add(graphicalEdge.getEnd());
                            if (graphicalEdge.getEdge().contains(pointFirst)) {
                                secondVertices.add(newVertex);
                                newVertex = new RoadVertex(-1, pointFirst.getX(), pointFirst.getY());
                                secondVertices.add(newVertex);
                            }
                            break loop;
                        }
                    }
                }
            }
        }
        return secondVertices;
    }

    private void drawPath(Route route, Group pane) {
        clearPath();
        if (route.getPathVertices() == null) return;
        List<RoadVertex> pathVertices = route.getPathVertices();

        Circle vertex = new Circle(pathVertices.get(0).posX, pathVertices.get(0).posY, 10);
        vertex.setFill(Color.GREEN);
        this.routeVertices.add(vertex);
        vertex = new Circle(pathVertices.get(pathVertices.size() - 1).posX, pathVertices.get(pathVertices.size() - 1).posY, 10);
        vertex.setFill(Color.GREEN);
        this.routeVertices.add(vertex);

        for (int i = 0; i < pathVertices.size() - 1; i++) {
            Line edge = new Line(pathVertices.get(i).posX, pathVertices.get(i).posY, pathVertices.get(i + 1).posX, pathVertices.get(i + 1).posY);
            edge.setStroke(Color.GREEN);
            edge.setStrokeWidth(8);
            routeEdges.add(edge);
        }
        this.routeVertices.forEach(e -> pane.getChildren().add(e));
        routeEdges.forEach(e -> pane.getChildren().add(e));
    }

    private void clearPath() {
        routeVertices.forEach(e -> canvas.getChildren().remove(e));
        routeEdges.forEach(e -> canvas.getChildren().remove(e));
        routeEdges.clear();
        routeVertices.clear();
    }
    public void setTime(double time){
        this.time =time;
    }
}
