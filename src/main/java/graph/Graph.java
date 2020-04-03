package graph;

import map.RoadVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private HashMap<Vertex, LinkedList<Edge>> adjacencyMap;
    private List<Vertex> vertices;
    public Graph(){
        this.adjacencyMap = new HashMap<>();
        this.vertices = new ArrayList<>();
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
    public void addEdge(Vertex origin, Vertex destination, double weight){
        if(!adjacencyMap.containsKey(origin)){
            LinkedList<Edge> connectedVertices = new LinkedList<>();
            connectedVertices.add(new Edge(destination,weight));
            adjacencyMap.put(origin, connectedVertices);
            vertices.add(origin);
        }else {
            adjacencyMap.get(origin).add(new Edge(destination, weight));
        }
        if (!adjacencyMap.containsKey(destination)){
            adjacencyMap.put(destination, new LinkedList<>());
            vertices.add(destination);
        }
    }

    /**
     * addEdge for RoadVertices, calculates weight from coordinates.
     * @param origin
     * @param destination
     */
    public void addEdge(RoadVertex origin, RoadVertex destination){
        double weight = Math.sqrt(Math.pow(Math.abs(origin.posY - destination.posY), 2) +
                Math.pow(Math.abs(origin.posX - destination.posX), 2));
        addEdge(origin, destination, weight);
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
    public void addNonDirectedEdge(Vertex v1, Vertex v2, double weight){
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /**
     * Removes edge pointing to destination from origin from adjacency dict
     * @param origin
     * @param destination
     */
    public void removeEdge(Vertex origin, Vertex destination){
        LinkedList<Edge> edges = adjacencyMap.get(origin);
        for(Edge edge : edges){
            if(edge.getDestination() == destination){
                edges.remove(edge);
                return;
            }
        }
    }

    /**
     * Removes edge pointing to destination from origin from adjacency dict
     * @param v1
     * @param v2
     */
    public void removeNonDirectedEdge(Vertex v1, Vertex v2){
        removeEdge(v1, v2);
        removeEdge(v2, v1);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Vertex vertex: adjacencyMap.keySet()){
            sb.append(vertex.index).append(" -->");
            for(Edge edge: adjacencyMap.get(vertex)){
                sb.append("\t{").append(edge.getDestination().index).append(", ").append(edge.weight).append("}");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
