import Back_end.RoadVertex;
import javafx.scene.shape.Circle;

public class GraphicalVertex extends RoadVertex {
    Circle graphics;
    GraphicalVertex(int index, String name, double x, double y, Circle graphics) {
        super(index, name, x, y);
        this.graphics = graphics;
    }
}
