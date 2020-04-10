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
    public Graph(){
        this.adjacencyMap = new HashMap<>();
        this.vertices = new ArrayList<>();
    }

    public HashMap<RoadVertex, LinkedList<RoadEdge>> getAdjacencyMap() {
        return adjacencyMap;
    }
    public List<RoadVertex> getVertices() {
        return vertices;
    }
    /**
     * Connects origin vertex to destination vertex with given weight, adds vertices to adjMap if needed.
     * @param origin
     * @param destination
     * @param weight
     */
    public void addEdge(RoadVertex origin, RoadVertex destination, double weight, Integer[] allowedTags){
        RoadEdge edge = new RoadEdge(destination, weight, allowedTags);
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
     * Adds and edge from v1 to v2, and from v2 to v1
     * @param v1
     * @param v2
     * @param weight
     */

    public void addNonDirectedEdge(RoadVertex v1, RoadVertex v2, double weight, Integer[] allowedTags){
        addEdge(v1, v2, weight, allowedTags);
        addEdge(v2, v1, weight, allowedTags);
    }


    /**
     * Removes edge pointing to destination from origin from adjacency dict
     * @param origin
     * @param destination
     */
    public void removeEdge(RoadVertex origin, RoadVertex destination){
        LinkedList<RoadEdge> edges = adjacencyMap.get(origin);
        for(RoadEdge edge : edges){
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
    public void removeNonDirectedEdge(RoadVertex v1, RoadVertex v2){
        removeEdge(v1, v2);
        removeEdge(v2, v1);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(RoadVertex vertex: adjacencyMap.keySet()){
            sb.append(vertex.index).append(" -->");
            for(RoadEdge edge: adjacencyMap.get(vertex)){
                sb.append("\t{").append(edge.getDestination().index).append(", ").append(edge.getWeight()).append("}");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
