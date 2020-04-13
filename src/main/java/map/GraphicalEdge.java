package map;

import javafx.scene.shape.Line;

public class GraphicalEdge {
    private Line edge;
    private RoadVertex start;
    private RoadVertex end;

    public GraphicalEdge(Line edge, RoadVertex start, RoadVertex end) {
        this.edge = edge;
        this.start = start;
        this.end = end;
    }

    public Line getEdge() {
        return edge;
    }

    public RoadVertex getStart() {
        return start;
    }

    public RoadVertex getEnd() {
        return end;
    }
}
