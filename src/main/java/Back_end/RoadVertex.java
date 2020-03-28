package Back_end;

/**
 * Contains coordinate data for each vertex;
 */
public class RoadVertex extends Vertex {
    public double posX;
    public double posY;


    public RoadVertex(int index, String name, double x, double y) {
        super(index, name);
        this.posX = x;
        this.posY = y;
    }
}
