package frontEnd.eventHandler;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import map.GraphicalEdge;
import map.GraphicalVertex;
import map.Map;
import map.RoadEdge;

import java.util.ArrayList;
import java.util.List;

public class Deleter implements EventHandler<MouseEvent> {

    private List<GraphicalVertex> graphicalVertices;
    private List<GraphicalEdge> edgesGraphics;
    private Map graph;
    private Group canvas;

    public Deleter(List<GraphicalVertex> graphicalVertices, List<GraphicalEdge> edgesGraphics, Map graph, Group canvas) {
        this.graphicalVertices = graphicalVertices;
        this.edgesGraphics = edgesGraphics;
        this.graph = graph;
        this.canvas = canvas;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            for (GraphicalVertex graphicalVertex : graphicalVertices) {
                if(graphicalVertex.getGraphics().contains(point)){
                    graphicalVertices.remove(graphicalVertex);
                    canvas.getChildren().remove(graphicalVertex.getGraphics());
                    ArrayList<GraphicalEdge> removableEdges = new ArrayList<>();
                    edgesGraphics.forEach(e -> {
                        if(e.getEnd().equals(graphicalVertex.getRoadVertex()) || e.getStart().equals(graphicalVertex.getRoadVertex())){
                            removableEdges.add(e);
                        }
                    });
                    removableEdges.forEach(e ->{
                        for (RoadEdge roadEdge : graph.getGraph().getAdjacencyMap().get(e.getStart())) {
                            graph.removeRoad(e.getStart(), roadEdge.getDestination());
                        }
                        edgesGraphics.remove(e);
                        canvas.getChildren().remove(e.getEdge());
                    });
                    break;
                }
            }
            for (GraphicalEdge edgesGraphic : edgesGraphics) {
                if(edgesGraphic.getEdge().contains(point)){
                    edgesGraphics.remove(edgesGraphic);
                    canvas.getChildren().remove(edgesGraphic.getEdge());
                    graph.removeRoad(edgesGraphic.getStart(), edgesGraphic.getEnd());
                    break;
                }
            }
        }
    }
}
