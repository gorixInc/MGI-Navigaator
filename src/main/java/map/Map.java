package map;

import graph.Graph;
public class Map {
    Graph mapGraph;
    public String name;
    private double scale;

    public Map(String name) {
        this.mapGraph = new Graph();
        this.name = name;
        this.scale = 1;
    }

    /**
     * Adds a two-way road from start to end vertex, allowedTags allow the filtering of who can use the road.
     * @param start start vertex
     * @param end end vertex
     * @param allowedTags tags, who can use the road.
     */
    public void addTwoWayRoad(RoadVertex start, RoadVertex end, double maxSpeed, Integer allowedTags){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        RoadEdge edge1 = new RoadEdge(end, distance, scale, maxSpeed, allowedTags);
        RoadEdge edge2 = new RoadEdge(start, distance, scale, maxSpeed, allowedTags);
        mapGraph.addNonDirectedEdge(edge1, edge2);
    }
    /**
     * Adds a two-way road from start to end vertex, allowedTags allow the filtering of who can use the road,
     * allows to specify a congestion function.
     * @param start start vertex
     * @param end end vertex
     * @param allowedTags tags, who can use the road.
     * @param congestionFunction congestion function
     */
    public void addTwoWayRoad(RoadVertex start, RoadVertex end, double maxSpeed,  Integer allowedTags,
                              CongestionFunction congestionFunction){

        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));

        RoadEdge edge1 = new RoadEdge(end, distance, scale, maxSpeed, allowedTags, congestionFunction);
        RoadEdge edge2 = new RoadEdge(start, distance, scale, maxSpeed,  allowedTags, congestionFunction);
        mapGraph.addNonDirectedEdge(edge1, edge2);
    }

    /**
     * Adds a two-way road from start to end vertex, allowedTags allow the filtering of who can use the road,
     * allows to specify different congestion functions for forwards and backwards traversal.
     * @param start start vertex
     * @param end end vertex
     * @param allowedTags tags, who can use the road.
     * @param congestionFunctionForward congestion function from start to end
     * @param congestionFunctionBackward congestion function from end to start
     */
    public void addTwoWayRoad(RoadVertex start, RoadVertex end, double maxSpeed, Integer allowedTags,
                              CongestionFunction congestionFunctionForward, CongestionFunction congestionFunctionBackward){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));

        RoadEdge edge1 = new RoadEdge(end, distance, scale, maxSpeed, allowedTags, congestionFunctionForward);
        RoadEdge edge2 = new RoadEdge(start, distance, scale, maxSpeed, allowedTags, congestionFunctionBackward);
        mapGraph.addNonDirectedEdge(edge1, edge2);
    }

    public void addOneWayRoad(RoadVertex start, RoadVertex end, double maxSpeed, Integer allowedTag){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        RoadEdge roadEdge = new RoadEdge(end,distance, scale, maxSpeed, allowedTag );
        mapGraph.addEdge(start, roadEdge);

    }

    public void addOneWayRoad(RoadVertex start, RoadVertex end, double maxSpeed, Integer allowedTag, CongestionFunction
                              congestionFunction){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        RoadEdge roadEdge = new RoadEdge(end, distance, scale, maxSpeed, allowedTag, congestionFunction);
        mapGraph.addEdge(start, roadEdge);
    }

    /**
     * removes all edges between given vertices
     * @param v1 vertex1
     * @param v2 vertex2
     */
    public void removeRoad(RoadVertex v1, RoadVertex v2){
        mapGraph.removeNonDirectedEdge(v1,v2);
    }

    /**
     * Updates scaledWeight value for all edges in mapGraph. Probably useful for visualisation.
     * @param timeMins time of day in minutes since midnight
     */
    public void updateCongestion(double timeMins){
        for(RoadEdge edge: mapGraph.getEdges()){
            edge.updateCongTime(timeMins);
        }
    }

    public void updateScale(double newScale){
        for(RoadEdge edge: mapGraph.getEdges()){
            edge.updateScale(newScale);
        }
        this.scale = newScale;
    }

    public double getScale() {
        return scale;
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
