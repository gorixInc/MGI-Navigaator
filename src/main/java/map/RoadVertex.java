package map;

import graph.Vertex;

/**
 * Contains coordinate data for each vertex;
 */
public class RoadVertex extends Vertex {
    public Double posX;
    public Double posY;


    public RoadVertex(int index, double x, double y) {
        super(index);
        this.posX = x;
        this.posY = y;
    }
}
