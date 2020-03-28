package Back_end;

public class Map {
    Graph mapGraph;
    String name;

    public Map(String name) {
        this.mapGraph = new Graph();
        this.name = name;
    }

    /**
     * Adds a two-way edge to mapGraph with distance calculated from coordinates
     * @param start
     * @param end
     */
    void addTwoWayRoad(RoadVertex start, RoadVertex end){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        mapGraph.addNonDirectedEdge(start, end, distance);
    }

    /**
     * Adds a one-way edge to mapGraph with distance calculated from coordinates
     * @param start
     * @param end
     */
    void addOneWayRoad(RoadVertex start, RoadVertex end){
        double distance = Math.sqrt(Math.pow(Math.abs(start.posY - end.posY), 2) +
                Math.pow(Math.abs(start.posX - end.posX), 2));
        mapGraph.addEdge(start, end, distance);
    }

    /**
     * removes all edges between given vertices
     * @param v1
     * @param v2
     */
    void removerRoad(RoadVertex v1, RoadVertex v2){
        mapGraph.removeNonDirectedEdge(v1,v2);
    }

    //more methods when we need them
}
