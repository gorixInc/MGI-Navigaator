package graph;

import map.RoadEdge;
import map.RoadVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private HashMap<RoadVertex, LinkedList<RoadEdge>> adjacencyMap;
    private List<RoadVertex> vertices;
    private List<RoadEdge> edges;
    public Graph(){
        edges = new ArrayList<>();
        this.adjacencyMap = new HashMap<>();
        this.vertices = new ArrayList<>();
    }

    public HashMap<RoadVertex, LinkedList<RoadEdge>> getAdjacencyMap() {
        return adjacencyMap;
    }
    public List<RoadVertex> getVertices() {
        return vertices;
    }

    public List<RoadEdge> getEdges() {
        return edges;
    }

    /**
     * Connects origin vertex to destination vertex with given weight, adds vertices to adjMap if needed.
     * @param origin origin
     * @param edge edge to add
     */
    public void addEdge(RoadVertex origin, RoadEdge edge){
        RoadVertex destination = edge.getDestination();
        edges.add(edge);
        if(!adjacencyMap.containsKey(origin)){
            LinkedList<RoadEdge> connectedVertices = new LinkedList<>();
            connectedVertices.add(edge);
            adjacencyMap.put(origin, connectedVertices);
            vertices.add(origin);
        }else {
            adjacencyMap.get(origin).add(edge);
        }
        if (!adjacencyMap.containsKey(destination)){
            adjacencyMap.put(destination, new LinkedList<>());
            vertices.add(destination);
        }
    }

    /**
     * Adds two adds two edges two graph back and forth between two destinations of said edges.
     * @param edge1
     * @param edge2
     */

    public void addNonDirectedEdge(RoadEdge edge1, RoadEdge edge2){
        addEdge(edge2.getDestination(), edge1);
        addEdge(edge1.getDestination(), edge2);
    }


    /**
     * Removes edge pointing to destination from origin from adjacency dict
     * @param origin
     * @param destination
     */
    public void removeEdge(RoadVertex origin, RoadVertex destination){
        LinkedList<RoadEdge> edgesList = adjacencyMap.get(origin);
        for(RoadEdge edge : edgesList){
            if(edge.getDestination() == destination){
                edges.remove(edge);
                edgesList.remove();
                return;
            }
        }
    }

    /**
     * Removes edge pointing to destination from origin from adjacency dict
     * @param v1
     * @param v2
     */
    public void removeNonDirectedEdge(RoadVertex v1, RoadVertex v2){
        removeEdge(v1, v2);
        removeEdge(v2, v1);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(RoadVertex vertex: adjacencyMap.keySet()){
            sb.append(vertex.index).append(" -->");
            for(RoadEdge edge: adjacencyMap.get(vertex)){
                sb.append("\t{").append(edge.getDestination().index).append(", ").append(edge.getBaseWeight()).append("}");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
