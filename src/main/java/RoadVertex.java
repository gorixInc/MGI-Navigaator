/**
 * Contains coordinate data for each vertex;
 */
public class RoadVertex extends Vertex {
    double posX;
    double posY;

    RoadVertex(int index, String name, double x, double y) {
        super(index, name);
        this.posX = x;
        this.posY = y;
    }
}
