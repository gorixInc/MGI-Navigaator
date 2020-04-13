package map;

import graph.Graph;

public class Map {
    Graph mapGraph;
    public String name;

    public Map(String name) {
        this.mapGraph = new Graph();
        this.name = name;
    }

    /**
     * Adds a two-way road from start to end vertex, allowedTags allow the filtering of who can use the road.
     * @param start start vertex
     * @param end end vertex
     * @param allowedTags tags, who can use the road.
     */
    public void addTwoWayRoad(RoadVertex start, RoadVertex end, Integer[] allowedTags){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        mapGraph.addNonDirectedEdge(start, end, distance, allowedTags);
    }

    /**
     * Adds a two-way road from start to end vertex, allowedTags allow the filtering of who can use the road.
     * @param start start vertex
     * @param end end vertex
     * @param allowedTags tags, who can use the road.
     */
    public void addOneWayRoad(RoadVertex start, RoadVertex end, Integer[] allowedTags){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        mapGraph.addEdge(start, end, distance, allowedTags);
    }
    /**
     * Adds a two-way road from start to end vertex with given weight, allowedTags allow the filtering of who can use the road.
     * @param start start vertex
     * @param end end vertex
     * @param weight weight override
     * @param allowedTags tags, who can use the road.
     */
    public void addOneWayRoad(RoadVertex start, RoadVertex end, Double weight, Integer[] allowedTags){
        mapGraph.addEdge(start, end, weight, allowedTags);
    }

    /**
     * removes all edges between given vertices
     * @param v1
     * @param v2
     */
    public void removeRoad(RoadVertex v1, RoadVertex v2){
        mapGraph.removeNonDirectedEdge(v1,v2);
    }

    @Override
    public String toString() {
        return mapGraph.toString();
    }

    //more methods when we need them

    public Graph getGraph() {
        return mapGraph;
    }
}
