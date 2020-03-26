
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Main extends Application {

    ArrayList<Circle> vertexesGraphics = new ArrayList<>();
    ArrayList<Line> edgesGraphics = new ArrayList<>();
    ArrayList<RoadVertex> vertexes;
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
        VBox nupud = new VBox();

        Button nupp1 = new Button("New route");
        Button nupp2 = new Button("Load Map");
        Button nupp3 = new Button("New Map");
        Button nupp4 = new Button("Clear");

        Image pilt = new Image("http://www.thepluspaper.com/wp-content/uploads/2019/01/1.jpg");

        BackgroundImage taust = new BackgroundImage(pilt, null, null, null, null);

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        nupud.getChildren().add(nupp1);
        nupud.getChildren().add(nupp2);
        nupud.getChildren().add(nupp3);
        nupud.getChildren().add(nupp4);


        gc.setFill(Color.WHEAT);
        gc.fillRect(0, 0, 600, 500);

        Group group = new Group(canvas);

        root.getChildren().add(nupud);
        root.getChildren().add(group);

        Scene scene1 = new Scene(root);

        root.setBackground(new Background(taust));

        stage.setScene(scene1);

        nupp1.setOnAction(e -> {
            dijkstra = new Dijkstra(graph);
            System.out.println("Esimene töötab");
            EventHandler<MouseEvent> findDistance = new EventHandler<MouseEvent>() {
                boolean esimene = true;
                Vertex esimeneClick;
                Vertex teineClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        group.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (esimene) {
                            for (Circle vertexesGraphic : vertexesGraphics) {
                                if (vertexesGraphic.contains(point)) {
                                    esimeneClick = vertexes.get(vertexesGraphics.indexOf(vertexesGraphic));
                                    esimene = false;
                                    break;
                                }
                            }
                        } else {
                            for (Circle vertexesGraphic : vertexesGraphics) {
                                if(vertexesGraphic.contains(point)){
                                    teineClick = vertexes.get(vertexesGraphics.indexOf(vertexesGraphic));
                                    System.out.println(dijkstra.getRoute(esimeneClick, teineClick));
                                    esimene = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            group.addEventFilter(MouseEvent.MOUSE_CLICKED, findDistance);
        });

        nupp2.setOnAction(e -> {
            HashMap<Vertex, LinkedList<Edge>> adjacencyMap = graph.getAdjacencyMap();
            for (RoadVertex vertex : vertexes) {
                drawVertex(vertex.posX, vertex.posY, group);
                for (Edge edge : adjacencyMap.get(vertex)) {
                    drawEdge(vertex.posX, vertex.posY, ((RoadVertex)(edge.destination)).posX, ((RoadVertex)(edge.destination)).posY, group);
                }
            }
            System.out.println("Teine Töötab");
        });

        nupp3.setOnAction(e -> {
            graph = new Graph();
            vertexes = new ArrayList<>();

            EventHandler<MouseEvent> addEdges = new EventHandler<MouseEvent>() {
                boolean esimene = true;
                Circle esimeneClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        group.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        System.out.println(graph.toString());
                        System.out.println(graph.getAdjacencyMap());
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (esimene) {
                            for (Circle vertexesGraphic : vertexesGraphics) {
                                if (vertexesGraphic.contains(point)) {
                                    esimeneClick = vertexesGraphic;
                                    esimene = false;
                                    break;
                                }
                            }
                        } else {
                            for (Circle vertexesGraphic : vertexesGraphics) {
                                if(vertexesGraphic.contains(point) && vertexesGraphic != esimeneClick){
                                    double esimeneX = esimeneClick.getCenterX();
                                    double esimeneY = esimeneClick.getCenterY();
                                    double teineX = vertexesGraphic.getCenterX();
                                    double teineY = vertexesGraphic.getCenterY();
                                    double weight = calculateWeight(esimeneX, esimeneY, teineX, teineY);
                                    graph.addEdge(vertexes.get(vertexesGraphics.indexOf(esimeneClick)), vertexes.get(vertexesGraphics.indexOf(vertexesGraphic)), weight);
                                    drawEdge(esimeneX, esimeneY, teineX, teineY, group);
                                    esimene = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            };

            EventHandler<MouseEvent> addVertexes = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        group.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        group.addEventFilter(MouseEvent.MOUSE_CLICKED, addEdges);
                        for (Vertex vertex : vertexes) {
                            System.out.println(vertex);
                        }
                    } else {
                        RoadVertex vertex = new RoadVertex(vertexes.size() + 1, String.valueOf(vertexes.size() + 1), event.getX(), event.getY());
                        vertexes.add(vertex);
                        drawVertex(event.getX(), event.getY(), group);
                    }
                }
            };

            group.addEventFilter(MouseEvent.MOUSE_CLICKED, addVertexes);
            System.out.println("Kolmas Töötab");
        });

        nupp4.setOnAction(e -> {
            vertexesGraphics.forEach(vertex -> group.getChildren().remove(vertex));
            vertexesGraphics.clear();
            edgesGraphics.forEach(edge -> group.getChildren().remove(edge));
            edgesGraphics.clear();
            System.out.println("Neljas Töötab");
        });

        stage.show();
    }


    private void drawVertex(double x, double y, Group group) {
        boolean olemas = false;
        for (Circle vertex : vertexesGraphics) {
            Point2D point = new Point2D(x, y);
            if (vertex.contains(point)) {
                System.out.println("Vajutasid koha peale");
                olemas = true;
                break;
            }
        }
        if (!olemas) {
            Circle vertex = new Circle(x, y, 5);
            vertexesGraphics.add(vertex);
            group.getChildren().add(vertex);
        }
    }

    private void drawEdge(double x1, double y1, double x2, double y2, Group group){
        Line edge = new Line(x1, y1, x2, y2);
        edge.setStrokeWidth(2);
        edgesGraphics.add(edge);
        group.getChildren().add(edge);
    }

    private double calculateWeight(double x1, double y1, double x2, double y2){
        double yVahe = Math.abs(y1 - y2);
        double xVahe = Math.abs(x1 - x2);
        double weight = Math.sqrt(Math.pow(yVahe, 2) + Math.pow(xVahe, 2));
        return weight;
    }


    public static void main(String[] args) {
        launch(args);
    }
}