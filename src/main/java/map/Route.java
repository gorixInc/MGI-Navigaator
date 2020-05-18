package map;


import java.util.List;

public class Route {
    final List<RoadVertex> pathVertices;
    final double totalWeight;

    public Route(List pathVertices, double totalWeight){
        this.pathVertices = pathVertices;
        this.totalWeight = totalWeight;
    }

    public String toString(){
        if(pathVertices == null){
            return "No path!";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Length: ").append(totalWeight).append(" Path: ");
        for (RoadVertex v:pathVertices) {
            sb.append(v.index).append(", ");
        }
        return sb.toString();
    }

    public List<RoadVertex> getPathVertices() {
        return pathVertices;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
}
