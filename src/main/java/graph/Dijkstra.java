package graph;


import map.RoadEdge;
import map.RoadVertex;
import map.Route;

import java.util.*;

public class Dijkstra {
    Graph graph;
    private HashMap<RoadVertex, LinkedList<RoadEdge>> adjacencyMap;
    private HashMap<RoadVertex, PrevVertexAndDistance> prevVertexAndDistanceTable;
    private List<RoadVertex> path;
    private List<RoadVertex> unvisited;
    private List<RoadVertex> allVertices;

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
        for(RoadVertex v: allVertices){
            prevVertexAndDistanceTable.put(v, new PrevVertexAndDistance(v, Double.MAX_VALUE));
        }
    }

    /**
     * Creates a Route object that represents a path between origin and destination vertices
     * @param origin Origin vertex
     * @param destination Destination vertex
     * @return Back_end.Route object
     */
    public Route getRoute(RoadVertex origin, RoadVertex destination){
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
     * Creates a Route object that represents a path between origin and destination vertices.
     * The route is created using only roads whose tags intersect with tags in allowed;
     * @param origin Origin vertex
     * @param destination Destination veretx
     * @param allowed tags, which roads can we use?
     * @return
     */
    public Route getRouteWithRestrictions(RoadVertex origin, RoadVertex destination, Integer[] allowed){
        reset();
        prevVertexAndDistanceTable.put(origin, new PrevVertexAndDistance(origin, 0));
        recursiveDijkstra(origin, Arrays.asList(allowed));
        createPathRecursively(destination);
        double pathLength = prevVertexAndDistanceTable.get(destination).totalWeight;
        if(path.size() == 1 && pathLength > 0){
            return new Route(null, -1);
        }
        Collections.reverse(path);
        return new Route(path, pathLength);
    }

    /**
     * Updates the path List<RoadVertex> recursively from the origin vector,in prevVertexAndDistance to the vector passed
     * in the first step
     *
     * @param vertex
     */
    private void createPathRecursively(RoadVertex vertex){
        for(RoadVertex v : prevVertexAndDistanceTable.keySet()){
            if(v.equals(vertex)){
                RoadVertex nextVert = prevVertexAndDistanceTable.get(v).prevVertex;
                path.add(v);
                if(nextVert.equals(vertex)){
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
    private void recursiveDijkstra(RoadVertex v) {
        double currentDistance = prevVertexAndDistanceTable.get(v).totalWeight;
        unvisited.remove(v);
        for (RoadEdge edge : adjacencyMap.get(v)) {
            RoadVertex adjVertex = edge.getDestination();
            double localWeight = edge.getWeight();
            double newSumWeight = localWeight + currentDistance;
            if (newSumWeight < prevVertexAndDistanceTable.get(adjVertex).totalWeight) {
                prevVertexAndDistanceTable.put(adjVertex, new PrevVertexAndDistance(v, newSumWeight));
            }
        }
        if (unvisited.isEmpty()) {
            return;
        }
        RoadVertex nextVertex = Collections.min(unvisited, new EdgeComparator(prevVertexAndDistanceTable));
        recursiveDijkstra(nextVertex);
    }


    private void recursiveDijkstra(RoadVertex v, List<Integer> allowedTags) {
        double currentDistance = prevVertexAndDistanceTable.get(v).totalWeight;
        unvisited.remove(v);;
        for (RoadEdge edge : adjacencyMap.get(v)) {
            //Checking tags
            boolean valid = false;
            for(Integer tag: edge.getAllowedTags()){
                if(allowedTags.contains(tag)){
                    valid = true;
                    break;
                }
            }
            if(!valid) continue;

            RoadVertex adjVertex = edge.getDestination();
            double localWeight = edge.getWeight();
            double newSumWeight = localWeight + currentDistance;
            if (newSumWeight < prevVertexAndDistanceTable.get(adjVertex).totalWeight) {
                prevVertexAndDistanceTable.put(adjVertex, new PrevVertexAndDistance(v, newSumWeight));
            }
        }
        if (unvisited.isEmpty()) {
            return;
        }
        RoadVertex nextVertex = Collections.min(unvisited, new EdgeComparator(prevVertexAndDistanceTable));
        recursiveDijkstra(nextVertex, allowedTags);
    }

    /**
     * Compares vertices by the shortest distance from origin vertex in prevVertexAndDistance HashMap;
     */
    private static class EdgeComparator implements Comparator<RoadVertex> {
        private HashMap<RoadVertex, PrevVertexAndDistance> prevVertexAndDistanceTable;
        EdgeComparator(HashMap<RoadVertex, PrevVertexAndDistance> prevVertexAndDistanceTable){
            this.prevVertexAndDistanceTable = prevVertexAndDistanceTable;
        }

        public int compare(RoadVertex v1, RoadVertex v2){
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
        RoadVertex prevVertex;
        double totalWeight;
        public PrevVertexAndDistance(RoadVertex prevVertex, double totalDistance) {
            this.prevVertex = prevVertex;
            this.totalWeight = totalDistance;
        }
    }
}





