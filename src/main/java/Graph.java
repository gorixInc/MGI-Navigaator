import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private HashMap<Vertex, LinkedList<Edge>> adjacencyMap;
    private List<Vertex> vertices;
    Graph(){
        this.adjacencyMap = new HashMap<Vertex, LinkedList<Edge>>();
        this.vertices = new ArrayList<Vertex>();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Connects origin vertex to destination vertex with given weight, adds vertices to adjMap if needed.
     * @param origin
     * @param destination
     * @param weight
     */
    void addEdge(Vertex origin, Vertex destination, double weight){
        if(!adjacencyMap.containsKey(origin)){
            LinkedList<Edge> connectedVertices = new LinkedList<Edge>();
            connectedVertices.add(new Edge(destination,weight));
            adjacencyMap.put(origin, connectedVertices);
            vertices.add(origin);
        }else {
            adjacencyMap.get(origin).add(new Edge(destination, weight));
        }
        if (!adjacencyMap.containsKey(destination)){
            adjacencyMap.put(destination, new LinkedList<Edge>());
            vertices.add(destination);
        }
    }

    public HashMap<Vertex, LinkedList<Edge>> getAdjacencyMap() {
        return adjacencyMap;
    }

    /**
     * Adds and edge from v1 to v2, and from v2 to v1
     * @param v1
     * @param v2
     * @param weight
     */
    void addNonDirectedEdge(Vertex v1, Vertex v2, double weight){
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Vertex vertex: adjacencyMap.keySet()){
            sb.append(vertex.name).append(" -->");
            for(Edge edge: adjacencyMap.get(vertex)){
                sb.append("\t{").append(edge.destination.name).append(", ").append(edge.weight).append("}");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
