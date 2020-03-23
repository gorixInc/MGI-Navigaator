import java.util.*;

public class Dijkstra {
    Graph graph;
    private HashMap<Vertex, LinkedList<Edge>> adjacencyMap;
    private HashMap<Vertex, Edge> prevVertexAndDistance;
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
        path =  new ArrayList<Vertex>();
        unvisited = new ArrayList<Vertex>();
        unvisited.addAll(allVertices);
        prevVertexAndDistance =  new HashMap<Vertex, Edge>();
        for(Vertex v: allVertices){
            prevVertexAndDistance.put(v, new Edge(v, Double.MAX_VALUE));
        }
    }

    /**
     * Creates a Route object that represents a path between origin and destination vertices
     * @param origin
     * @param destination
     * @return
     */
    public Route getRoute(Vertex origin, Vertex destination){
        reset();
        prevVertexAndDistance.put(origin, new Edge(origin, 0));
        recursiveDijkstra(origin);
        createPathRecursively(destination);
        double pathLength = prevVertexAndDistance.get(destination).weight;
        if(path.size() == 1 && pathLength > 0){
            return new Route(null, -1);
        }
        Collections.reverse(path);
        return new Route(path, pathLength);
    }

    /**
     * Updates the path List<Vertex> recursively from the origin vector,in prevVertexAndDistance to the vector passed
     * in the first step
     *
     * @param vertex
     */
    private void createPathRecursively(Vertex vertex){
        for(Vertex v : prevVertexAndDistance.keySet()){
            if(v == vertex){
                Vertex nextVert = prevVertexAndDistance.get(v).destination;
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
        double currentDistance = prevVertexAndDistance.get(v).weight;
        unvisited.remove(v);
        for (Edge edge : adjacencyMap.get(v)) {
            Vertex adjVertex = edge.destination;
            double localWeight = edge.weight;
            double newSumWeight = localWeight + currentDistance;
            if (newSumWeight < prevVertexAndDistance.get(adjVertex).weight) {
                prevVertexAndDistance.put(adjVertex, new Edge(v, newSumWeight));
            }
        }
        if (unvisited.isEmpty()) {
            return;
        }
        Vertex nextVertex = Collections.min(unvisited, new EdgeComparator(prevVertexAndDistance));
        recursiveDijkstra(nextVertex);
    }

    /**
     * Compares vertices by the shortest distance from origin vertex in prevVertexAndDistance HashMap;
     */
    private class EdgeComparator implements Comparator<Vertex> {
        private HashMap<Vertex, Edge> prevVertexAndDistance;
        EdgeComparator(HashMap<Vertex, Edge> prevVertexAndDistance){
            this.prevVertexAndDistance = prevVertexAndDistance;
        }

        public int compare(Vertex v1, Vertex v2){
            double totalWeight1 = prevVertexAndDistance.get(v1).weight;
            double totalWeight2 = prevVertexAndDistance.get(v2).weight;

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
}





