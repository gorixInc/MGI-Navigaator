package map;


import java.util.Objects;

/**
 * Contains coordinate data for each vertex;
 */
public class RoadVertex{
    public Double posX;
    public Double posY;
    public Integer index;


    public RoadVertex(int index, double x, double y) {
        this.index = index;
        this.posX = x;
        this.posY = y;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString(){
        return "RoadVertex with index: " + index.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadVertex that = (RoadVertex) o;
        return Objects.equals(posX, that.posX) &&
                Objects.equals(posY, that.posY) &&
                Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY, index);
    }
}
