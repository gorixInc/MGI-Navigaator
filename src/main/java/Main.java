

import java.util.ArrayList;

import backEnd.*;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class Main extends Application {

    ArrayList<Line> edgesGraphics = new ArrayList<>();
    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
    ArrayList<Line> routeEdges = new ArrayList<>();
    ArrayList<Circle> routeVertices = new ArrayList<>();
    Graph graph;
    Dijkstra dijkstra;


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("GPS");

        stage.setWidth(700);
        stage.setHeight(500);

        stage.setX(300);
        stage.setY(100);

        HBox root = new HBox();
        VBox buttons = new VBox();

        Button button1 = new Button("New route");
        Button button2 = new Button("Load Map");
        Button button3 = new Button("New Map");
        Button button4 = new Button("Clear");

        Image image = new Image("http://www.thepluspaper.com/wp-content/uploads/2019/01/1.jpg");

        BackgroundImage background = new BackgroundImage(image, null, null, null, null);

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        buttons.getChildren().add(button1);
        buttons.getChildren().add(button2);
        buttons.getChildren().add(button3);
        buttons.getChildren().add(button4);


        gc.setFill(Color.WHEAT);
        gc.fillRect(0, 0, 600, 500);

        Group map = new Group(canvas);

        root.getChildren().add(buttons);
        root.getChildren().add(map);

        Scene scene1 = new Scene(root);

        root.setBackground(new Background(background));

        stage.setScene(scene1);

        button1.setOnAction(e -> {
            removePath(map);
            dijkstra = new Dijkstra(graph);
            System.out.println("Esimene töötab");
            EventHandler<MouseEvent> findDistance = new EventHandler<MouseEvent>() {
                boolean first = true;
                Vertex firstClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        map.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (first) {
                            removePath(map);
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point)){
                                    firstClick = graphicalVertex;
                                    first = false;
                                    break;
                                }
                            }
                        } else {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point)){
                                    Route teekond = dijkstra.getRoute(firstClick, graphicalVertex);
                                    drawPath(teekond, map);
                                    System.out.println(teekond);
                                    first = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            map.addEventFilter(MouseEvent.MOUSE_CLICKED, findDistance);
        });

        button2.setOnAction(e -> {
            clearCanvas(map);
            populateCanvas(map);
            System.out.println("Teine Töötab");
        });

        button3.setOnAction(e -> {
            clearCanvas(map);
            edgesGraphics.clear();
            graph = new Graph();
            graphicalVertices = new ArrayList<>();

            EventHandler<MouseEvent> addEdges = new EventHandler<MouseEvent>() {
                boolean first = true;
                RoadVertex firstClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        map.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        System.out.println(graph.toString());
                        System.out.println(graph.getAdjacencyMap());
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (first) {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if(graphicalVertex.graphics.contains(point)){
                                    firstClick = graphicalVertex;
                                    first = false;
                                    break;
                                }
                            }
                        } else {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point) && graphicalVertex != firstClick){
                                    double firstX = firstClick.posX;
                                    double firstY = firstClick.posY;
                                    double secondX = graphicalVertex.posX;
                                    double secondY = graphicalVertex.posY;
                                    graph.addEdge(firstClick, graphicalVertex);
                                    drawEdge(firstX, firstY, secondX, secondY, map);
                                    first = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            };

            EventHandler<MouseEvent> addVertices = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        map.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        map.addEventFilter(MouseEvent.MOUSE_CLICKED, addEdges);
                        for (GraphicalVertex graphicalVertex : graphicalVertices) {
                            System.out.println(graphicalVertex);
                        }
                    } else {
                        constructVertex(event.getX(), event.getY(), map);
                    }
                }
            };

            map.addEventFilter(MouseEvent.MOUSE_CLICKED, addVertices);
            System.out.println("Kolmas Töötab");
        });

        button4.setOnAction(e -> {
            clearCanvas(map);
            System.out.println("Neljas Töötab");
        });

        stage.show();
    }

    private void constructVertex(double x, double y, Group group){
        Circle vertex = drawVertex(x, y);
        if(vertex != null) {
            GraphicalVertex gv = new GraphicalVertex(graphicalVertices.size(), String.valueOf(graphicalVertices.size()), x, y, vertex);
            graphicalVertices.add(gv);
            group.getChildren().add(gv.graphics);
        }
    }

    private void removePath(Group group){
        routeEdges.forEach(edge -> group.getChildren().remove(edge));
        routeVertices.forEach(vertex -> group.getChildren().remove(vertex));
        routeEdges.clear();
        routeVertices.clear();
    }

    private void drawPath(Route route, Group group){
        if (route.getPathVertices() == null) return;
        for (Vertex pathVertex : route.getPathVertices()) {
            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                if (graphicalVertex.index == pathVertex.index){
                    Circle routeVertex = new Circle(graphicalVertex.posX, graphicalVertex.posY, 2.5);
                    routeVertex.setFill(Color.RED);
                    routeVertices.add(routeVertex);
                }
            }
        }
        for (int i = 0; i < routeVertices.size()-1; i++) {
            Line routeEdge = new Line(routeVertices.get(i).getCenterX(), routeVertices.get(i).getCenterY(), routeVertices.get(i+1).getCenterX(), routeVertices.get(i+1).getCenterY());
            routeEdge.setStroke(Color.RED);
            routeEdges.add(routeEdge);
        }
        routeVertices.forEach(vertex -> group.getChildren().add(vertex));
        routeEdges.forEach(edge -> group.getChildren().add(edge));
    }

    private void clearCanvas(Group group){
        graphicalVertices.forEach(vertex -> group.getChildren().remove(vertex.graphics));
        edgesGraphics.forEach(edge -> group.getChildren().remove(edge));
        removePath(group);
    }

    private void populateCanvas(Group group){
        graphicalVertices.forEach(vertex -> group.getChildren().add(vertex.graphics));
        edgesGraphics.forEach(edge -> group.getChildren().add(edge));
    }


    private Circle drawVertex(double x, double y) {
        boolean vertexExists = false;
        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            Point2D point = new Point2D(x, y);
            if (graphicalVertex.graphics.contains(point)){
                System.out.println("A vertex already exists at that point.");
                vertexExists = true;
                break;
            }
        }
        if (!vertexExists) {
            return new Circle(x, y, 5);
        }
        return null;
    }

    private void drawEdge(double x1, double y1, double x2, double y2, Group group){
        Line edge = new Line(x1, y1, x2, y2);
        edge.setStrokeWidth(2);
        edgesGraphics.add(edge);
        group.getChildren().add(edge);
    }



    public static void main(String[] args) {
        launch(args);
    }
}