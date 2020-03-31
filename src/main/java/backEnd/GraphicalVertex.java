package backEnd;

import backEnd.RoadVertex;
import javafx.scene.shape.Circle;

public class GraphicalVertex extends RoadVertex {
    public Circle graphics;
    public GraphicalVertex(int index, String name, double x, double y, Circle graphics) {
        super(index, name, x, y);
        this.graphics = graphics;
    }
}
