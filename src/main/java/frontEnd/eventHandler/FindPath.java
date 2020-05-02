package frontEnd.eventHandler;

import graph.Dijkstra;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.GraphicalEdge;
import map.GraphicalVertex;
import map.RoadVertex;
import map.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindPath implements EventHandler<MouseEvent> {

    private Group canvas;
    private List<GraphicalVertex> graphicalVertices;
    private Dijkstra dijkstra;
    private List<GraphicalEdge> graphicalEdges;

    private List<Circle> routeVertices = new ArrayList<>();
    private List<Line> routeEdges = new ArrayList<>();

    private String roadType;

    private HashMap<String, Integer> roadTypes = new HashMap<String, Integer>() {{
        put("Motorway", 1);
        put("Pedestrian", 2);
        put("Railway", 3);
    }};

    public void setRoadType(String roadType) {
        this.roadType = roadType;
        System.out.println(roadType);
    }

    public FindPath(Group canvas, List<GraphicalVertex> graphicalVertices, List<GraphicalEdge> graphicalEdges, Dijkstra dijkstra, String roadType) {
        this.canvas = canvas;
        this.graphicalVertices = graphicalVertices;
        this.graphicalEdges = graphicalEdges;
        this.dijkstra = dijkstra;
        this.roadType = roadType;
    }

    private boolean first = true;
    private List<RoadVertex> firstVertices;
    private List<RoadVertex> secondVertices;
    private List<Route> routes;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Point2D point = new javafx.geometry.Point2D(mouseEvent.getX(), mouseEvent.getY());

            if (first){
                routes = new ArrayList<Route>();
                firstVertices = firstClick(point);
                first = false;
            } else {
                secondVertices = secondClick(point);
                if (firstVertices.size() == 1){
                    if (secondVertices.size() == 1){
                        Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(0), secondVertices.get(0), new Integer[]{roadTypes.get(roadType)});
                        routes.add(route);
                    }
                    else {
                        for (int i = 0; i < secondVertices.size(); i+=2) {
                            Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(0), secondVertices.get(i+1), new Integer[]{roadTypes.get(roadType)});
                            List<RoadVertex> routeVertices = route.getPathVertices();
                            double routeWeight = route.getTotalWeight();
                            routeVertices.add(secondVertices.get(i));
                            double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(i).posX - secondVertices.get(i+1).posX), 2) + Math.pow((secondVertices.get(i).posY - secondVertices.get(i+1).posY), 2));
                            routeWeight += distanceBetweenVertices;
                            route = new Route(routeVertices, routeWeight);
                            routes.add(route);
                        }
                    }
                } else {
                    for (int i = 0; i < firstVertices.size(); i+=2) {
                        if (secondVertices.size() == 1){
                            Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(i+1), secondVertices.get(0), new Integer[]{roadTypes.get(roadType)});
                            List<RoadVertex> routeVertices = route.getPathVertices();
                            double routeWeight = route.getTotalWeight();
                            routeVertices.add(0, firstVertices.get(i));
                            double distanceBetweenVertices = Math.sqrt(Math.pow((firstVertices.get(i).posX - firstVertices.get(i+1).posX), 2) + Math.pow((firstVertices.get(i).posY - firstVertices.get(i+1).posY), 2));
                            routeWeight += distanceBetweenVertices;
                            route = new Route(routeVertices, routeWeight);
                            routes.add(route);
                        } else {
                            for (int j = 0; j < secondVertices.size(); j+=2) {
                                Route route = dijkstra.getRouteWithRestrictions(firstVertices.get(i+1), secondVertices.get(j+1), new Integer[]{roadTypes.get(roadType)});
                                List<RoadVertex> routeVertices = route.getPathVertices();
                                double routeWeight = route.getTotalWeight();
                                routeVertices.add(secondVertices.get(j));
                                routeVertices.add(0, firstVertices.get(i));
                                double distanceBetweenVertices = Math.sqrt(Math.pow((secondVertices.get(j).posX - secondVertices.get(j).posX), 2) + Math.pow((secondVertices.get(j).posY - secondVertices.get(j+1).posY), 2));
                                distanceBetweenVertices += Math.sqrt(Math.pow((firstVertices.get(i).posX - firstVertices.get(i+1).posX), 2) + Math.pow((firstVertices.get(i).posY - firstVertices.get(i+1).posY), 2));
                                routeWeight += distanceBetweenVertices;
                                route = new Route(routeVertices, routeWeight);
                                routes.add(route);
                            }
                        }
                    }
                }
                first = true;
            }
            Route shortestRoute = null;
            double smallestWeight = Double.MAX_VALUE;
            for (Route route : routes) {
                if (route.getTotalWeight() < smallestWeight){
                    shortestRoute = route;
                    smallestWeight = shortestRoute.getTotalWeight();
                }
            }
            if (shortestRoute != null){
                drawPath(shortestRoute, canvas);
            }
        }
    }

    private List<RoadVertex> firstClick(Point2D point){
        List<RoadVertex> firstVertices = new ArrayList<RoadVertex>();

        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            if (graphicalVertex.getGraphics().contains(point)){
                firstVertices.add(graphicalVertex.getRoadVertex());
                return firstVertices;
            }
        }
        for (GraphicalEdge graphicalEdge : graphicalEdges) {
            if (graphicalEdge.getEdge().contains(point)){
                RoadVertex newVertex = new RoadVertex(-1, point.getX(), point.getY());
                firstVertices.add(newVertex);
                firstVertices.add(graphicalEdge.getEnd());
            }
        }
        return firstVertices;
    }

    private List<RoadVertex> secondClick(Point2D point){
        List<RoadVertex> secondVertices = new ArrayList<RoadVertex>();

        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            if (graphicalVertex.getGraphics().contains(point)){
                secondVertices.add(graphicalVertex.getRoadVertex());
                return secondVertices;
            }
        }
        for (GraphicalEdge graphicalEdge : graphicalEdges) {
            if (graphicalEdge.getEdge().contains(point)){
                RoadVertex newVertex = new RoadVertex(-1, point.getX(), point.getY());
                secondVertices.add(newVertex);
                secondVertices.add(graphicalEdge.getStart());
            }
        }
        return secondVertices;
    }

    private void drawPath(Route route, Group pane) {
        clearPath();
        if (route.getPathVertices() == null) return;
        List<RoadVertex> pathVertices = route.getPathVertices();

        Circle vertex = new Circle(pathVertices.get(0).posX, pathVertices.get(0).posY, 2.5);
        vertex.setFill(Color.RED);
        this.routeVertices.add(vertex);
        vertex = new Circle(pathVertices.get(pathVertices.size() - 1).posX, pathVertices.get(pathVertices.size() - 1).posY, 2.5);
        vertex.setFill(Color.RED);
        this.routeVertices.add(vertex);

        for (int i = 0; i < pathVertices.size() - 1; i++) {
            Line edge = new Line(pathVertices.get(i).posX, pathVertices.get(i).posY, pathVertices.get(i + 1).posX, pathVertices.get(i + 1).posY);
            edge.setStroke(Color.RED);
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
}
