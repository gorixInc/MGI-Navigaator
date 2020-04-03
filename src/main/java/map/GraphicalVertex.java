package map;

import javafx.scene.shape.Circle;

public class GraphicalVertex extends RoadVertex {
    public Circle graphics;
    public GraphicalVertex(int index, double x, double y, Circle graphics) {
        super(index, x, y);
        this.graphics = graphics;
    }
}
