package Back_end;

import java.util.*;

public class Dijkstra {
    Graph graph;
    private HashMap<Vertex, LinkedList<Edge>> adjacencyMap;
    private HashMap<Vertex, PrevVertexAndDistance> prevVertexAndDistanceTable;
    List<Vertex> path;
    List<Vertex> unvisited;
    List<Vertex> allVertices;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        reset();
    }


    private void reset(){
        allVertices = graph.getVertices();
        adjacencyMap = graph.getAdjacencyMap();
        path =  new ArrayList<>();
        unvisited = new ArrayList<>();
        unvisited.addAll(allVertices);
        prevVertexAndDistanceTable =  new HashMap<>();
        for(Vertex v: allVertices){
            prevVertexAndDistanceTable.put(v, new PrevVertexAndDistance(v, Double.MAX_VALUE));
        }
    }

    /**
     * Creates a Back_end.Route object that represents a path between origin and destination vertices
     * @param origin Origin vertex
     * @param destination Destination vertex
     * @return Back_end.Route object
     */
    public Route getRoute(Vertex origin, Vertex destination){
        reset();
        prevVertexAndDistanceTable.put(origin, new PrevVertexAndDistance(origin, 0));
        recursiveDijkstra(origin);
        createPathRecursively(destination);
        double pathLength = prevVertexAndDistanceTable.get(destination).totalWeight;
        if(path.size() == 1 && pathLength > 0){
            return new Route(null, -1);
        }
        Collections.reverse(path);
        return new Route(path, pathLength);
    }

    /**
     * Updates the path List<Back_end.Vertex> recursively from the origin vector,in prevVertexAndDistance to the vector passed
     * in the first step
     *
     * @param vertex
     */
    private void createPathRecursively(Vertex vertex){
        for(Vertex v : prevVertexAndDistanceTable.keySet()){
            if(v == vertex){
                Vertex nextVert = prevVertexAndDistanceTable.get(v).prevVertex;
                path.add(v);
                if(nextVert == vertex){
                    return;
                }
                createPathRecursively(nextVert);
            }
        }
    }

    /**
     * Updates prevVertAndDistance HashMap with distances from the first vertex passed to each vector in the graph.
     * @param v
     */
    private void recursiveDijkstra(Vertex v) {
        double currentDistance = prevVertexAndDistanceTable.get(v).totalWeight;
        unvisited.remove(v);
        for (Edge edge : adjacencyMap.get(v)) {
            Vertex adjVertex = edge.destination;
            double localWeight = edge.weight;
            double newSumWeight = localWeight + currentDistance;
            if (newSumWeight < prevVertexAndDistanceTable.get(adjVertex).totalWeight) {
                prevVertexAndDistanceTable.put(adjVertex, new PrevVertexAndDistance(v, newSumWeight));
            }
        }
        if (unvisited.isEmpty()) {
            return;
        }
        Vertex nextVertex = Collections.min(unvisited, new EdgeComparator(prevVertexAndDistanceTable));
        recursiveDijkstra(nextVertex);
    }

    /**
     * Compares vertices by the shortest distance from origin vertex in prevVertexAndDistance HashMap;
     */
    private static class EdgeComparator implements Comparator<Vertex> {
        private HashMap<Vertex, PrevVertexAndDistance> prevVertexAndDistanceTable;
        EdgeComparator(HashMap<Vertex, PrevVertexAndDistance> prevVertexAndDistanceTable){
            this.prevVertexAndDistanceTable = prevVertexAndDistanceTable;
        }

        public int compare(Vertex v1, Vertex v2){
            double totalWeight1 = prevVertexAndDistanceTable.get(v1).totalWeight;
            double totalWeight2 = prevVertexAndDistanceTable.get(v2).totalWeight;

            if(totalWeight1 == totalWeight2){
                return 0;
            }
            if(totalWeight1 > totalWeight2){
                return 1;
            }else {
                return -1;
            }
        }
    }

    private static class PrevVertexAndDistance{
        Vertex prevVertex;
        double totalWeight;
        public PrevVertexAndDistance(Vertex prevVertex, double totalDistance) {
            this.prevVertex = prevVertex;
            this.totalWeight = totalDistance;
        }
    }
}





