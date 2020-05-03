import graph.Dijkstra;
import map.*;
public class GraphTest {
    public static void main(String[] args) throws Exception {
        Map map = new Map("322");
        Dijkstra dijkstra = new Dijkstra(map.getGraph());

        RoadVertex v1 = new RoadVertex(1, 0, 0);
        RoadVertex v2 = new RoadVertex(2, 20, 0);
        RoadVertex v3 = new RoadVertex(3, 40, 0);
        RoadVertex v4 = new RoadVertex(4, 40, 0);
        RoadVertex v5 = new RoadVertex(5, 40, 0);
        RoadVertex v6 = new RoadVertex(6, 60, 0);


        map.addTwoWayRoad(v1, v2, 1);
        map.addTwoWayRoad(v2, v3, 1);
        map.addTwoWayRoad(v3, v4, 1);
        map.addTwoWayRoad(v3, v5, 1);
        map.addTwoWayRoad(v4, v6, 1, new SinglePeakCongestion(10.21235, 0.4235235123, 40));
        map.addTwoWayRoad(v5, v6, 1, new SinglePeakCongestion(45.23234,0.4235235, 40));

        Route r1 = dijkstra.getRouteWithRestrictions(v3, v6, 1, 10);
        Route r2 = dijkstra.getRouteWithRestrictions(v3, v6, 1, 40);
        System.out.println(r1);
        System.out.println(r2);
        MapFileHandler.saveMap(map, "test.xml");


        Map loadedMap = MapFileHandler.openMap("test.xml");
        Dijkstra newD = new Dijkstra(loadedMap.getGraph());

        Route r21 = newD.getRouteWithRestrictions(v3, v6,1, 10);
        Route r22 = newD.getRouteWithRestrictions(v3, v6, 1, 45);

        System.out.println(r21);
        System.out.println(r22);
    }
}
