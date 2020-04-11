package frontEnd.eventHandler;

import map.GraphicalVertex;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import map.RoadVertex;

import java.util.ArrayList;


public class AddJunction implements EventHandler<MouseEvent> {

    private Pane map;
    private ArrayList<GraphicalVertex> graphicalVertices;

    public AddJunction(Pane map, ArrayList<GraphicalVertex> graphicalVertices){
        this.map = map;
        this.graphicalVertices = graphicalVertices;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            constructVertex(event.getX(), event.getY(), map);
        }
    }

    private void constructVertex(double x, double y, Pane group){
        Circle vertex = drawVertex(x, y);
        if(vertex != null) {
            RoadVertex roadVertex = new RoadVertex(graphicalVertices.size(), x, y);
            GraphicalVertex gv = new GraphicalVertex(roadVertex, vertex);
            graphicalVertices.add(gv);
            group.getChildren().add(gv.getGraphics());
        }
    }

    private Circle drawVertex(double x, double y) {
        boolean vertexExists = false;
        for (GraphicalVertex graphicalVertex : graphicalVertices) {
            Point2D point = new Point2D(x, y);
            if (graphicalVertex.getGraphics().contains(point)){
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
}
