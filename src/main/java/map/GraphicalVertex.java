package map;

import javafx.scene.shape.Circle;

public class GraphicalVertex{
    private final Circle graphics;
    private final RoadVertex roadVertex;

    public GraphicalVertex(RoadVertex rVertex, Circle graphics){
        this.roadVertex = rVertex;
        this.graphics = graphics;
    }

    public Circle getGraphics() {
        return graphics;
    }

    public RoadVertex getRoadVertex() {
        return roadVertex;
    }
}
