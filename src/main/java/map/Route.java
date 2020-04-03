package map;

import graph.Vertex;

import java.util.List;

public class Route {
    List<Vertex> pathVertices;
    double totalWeight;

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
        for (Vertex v:pathVertices) {
            sb.append(v.index).append(", ");
        }
        return sb.toString();
    }

    public List<Vertex> getPathVertices() {
        return pathVertices;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
}
