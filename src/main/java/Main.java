
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

public class Main extends Application {

    ArrayList<Line> edgesGraphics = new ArrayList<>();
    ArrayList<GraphicalVertex> graphicalVertices = new ArrayList<>();
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

        Group kaart = new Group(canvas);

        root.getChildren().add(nupud);
        root.getChildren().add(kaart);

        Scene scene1 = new Scene(root);

        root.setBackground(new Background(taust));

        stage.setScene(scene1);

        nupp1.setOnAction(e -> {
            dijkstra = new Dijkstra(graph);
            System.out.println("Esimene töötab");
            EventHandler<MouseEvent> findDistance = new EventHandler<MouseEvent>() {
                boolean esimene = true;
                Vertex esimeneClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        kaart.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (esimene) {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point)){
                                    esimeneClick = graphicalVertex;
                                    esimene = false;
                                    break;
                                }
                            }
                        } else {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point)){
                                    System.out.println(dijkstra.getRoute(esimeneClick, graphicalVertex));
                                    esimene = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            kaart.addEventFilter(MouseEvent.MOUSE_CLICKED, findDistance);
        });

        nupp2.setOnAction(e -> {
            clearCanvas(kaart);
            populateCanvas(kaart);
            System.out.println("Teine Töötab");
        });

        nupp3.setOnAction(e -> {
            clearCanvas(kaart);
            //vertexesGraphics.clear();
            edgesGraphics.clear();
            graph = new Graph();
            //vertexes = new ArrayList<>();
            //vertexesGraphics = new ArrayList<>();
            graphicalVertices = new ArrayList<>();

            EventHandler<MouseEvent> addEdges = new EventHandler<MouseEvent>() {
                boolean esimene = true;
                GraphicalVertex esimeneClick;
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        kaart.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        System.out.println(graph.toString());
                        System.out.println(graph.getAdjacencyMap());
                    } else {
                        Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                        if (esimene) {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if(graphicalVertex.graphics.contains(point)){
                                    esimeneClick = graphicalVertex;
                                    esimene = false;
                                    break;
                                }
                            }
                        } else {
                            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                                if (graphicalVertex.graphics.contains(point) && graphicalVertex != esimeneClick){
                                    double esimeneX = esimeneClick.posX;
                                    double esimeneY = esimeneClick.posY;
                                    double teineX = graphicalVertex.posX;
                                    double teineY = graphicalVertex.posY;
                                    graph.addEdge(esimeneClick, graphicalVertex);
                                    drawEdge(esimeneX, esimeneY, teineX, teineY, kaart);
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
                        kaart.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        kaart.addEventFilter(MouseEvent.MOUSE_CLICKED, addEdges);
                        for (GraphicalVertex graphicalVertex : graphicalVertices) {
                            System.out.println(graphicalVertex);
                        }
                    } else {
                        constructVertex(event.getX(), event.getY(), kaart);
                    }
                }
            };

            kaart.addEventFilter(MouseEvent.MOUSE_CLICKED, addVertexes);
            System.out.println("Kolmas Töötab");
        });

        nupp4.setOnAction(e -> {
            clearCanvas(kaart);
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

    private void clearCanvas(Group group){
        graphicalVertices.forEach(vertex -> group.getChildren().remove(vertex.graphics));
        edgesGraphics.forEach(edge -> group.getChildren().remove(edge));
    }

    private void populateCanvas(Group group){
        graphicalVertices.forEach(vertex -> group.getChildren().add(vertex.graphics));
        edgesGraphics.forEach(edge -> group.getChildren().add(edge));
    }


    private Circle drawVertex(double x, double y) {
        boolean olemas = false;
        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            Point2D point = new Point2D(x, y);
            if (graphicalVertex.graphics.contains(point)){
                System.out.println("Vajutasid koha peale");
                olemas = true;
                break;
            }
        }
        if (!olemas) {
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