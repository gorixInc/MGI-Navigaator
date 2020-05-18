package map;

import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class GraphicalEdge {
    private Line edge;
    private RoadVertex start;
    private RoadVertex end;
    private List<RoadEdge> roadEdges;

    public GraphicalEdge(Line edge, RoadVertex start, RoadVertex end) {
        this.edge = edge;
        this.start = start;
        this.end = end;
        this.roadEdges = new ArrayList<>();
    }
    public void addRoadEdge(RoadEdge re){
        roadEdges.add(re);
    }

    public List<RoadEdge> getRoadEdges() {
        return roadEdges;
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
