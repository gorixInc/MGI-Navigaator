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


        map.addTwoWayRoad(v1, v2, new Integer[]{1,2,3});
        map.addTwoWayRoad(v2, v3, new Integer[]{1,2,3});
        map.addTwoWayRoad(v3, v4, new Integer[]{1,2,3});
        map.addTwoWayRoad(v3, v5, new Integer[]{1,2,3});
        map.addTwoWayRoad(v4, v6, new Integer[]{1,2,3}, new SinglePeakCongestion(10, 0.4, 40));
        map.addTwoWayRoad(v5, v6, new Integer[]{1,2,3}, new SinglePeakCongestion(45,0.4, 40));

        Route r1 = dijkstra.getRouteWithRestrictions(v3, v6, new Integer[]{1}, 10);
        Route r2 = dijkstra.getRouteWithRestrictions(v3, v6, new Integer[]{1}, 40);

        MapFileHandler.saveMap(map, "test.xml");
        System.out.println(r1);
        System.out.println(r2);

        Map loadedMap = MapFileHandler.openMap("test.xml");
        Dijkstra newD = new Dijkstra(loadedMap.getGraph());

        Route r21 = newD.getRouteWithRestrictions(v3, v6, new Integer[]{1}, 10);
        Route r22 = newD.getRouteWithRestrictions(v3, v6, new Integer[]{1}, 40);

        System.out.println(r21);
        System.out.println(r22);
    }
}
