import graph.Dijkstra;
import map.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class GraphTest {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        Map map = new Map("322");
        Dijkstra dijkstra = new Dijkstra(map.getGraph());

        RoadVertex v1 = new RoadVertex(1, 0, 0);
        RoadVertex v2 = new RoadVertex(2, 20, 0);
        RoadVertex v3 = new RoadVertex(3, 30, 0);
        RoadVertex v4 = new RoadVertex(4, 40, 0);
        RoadVertex v5 = new RoadVertex(5, 50, 0);
        RoadVertex v6 = new RoadVertex(6, 60, 0);


        map.addTwoWayRoad(v1, v2, new Integer[]{1,2,3});
        map.addTwoWayRoad(v2, v3, new Integer[]{1,2,3});
        map.addTwoWayRoad(v3, v4, new Integer[]{1});
        map.addTwoWayRoad(v3, v5, new Integer[]{2});

        Route r = dijkstra.getRouteWithRestrictions(v1, v5, new Integer[]{2});
        System.out.println(map.toString());
        System.out.println(r.toString());

        //MapFileHandler.saveMap(map, "test.xml");
        Map loadedMap = MapFileHandler.openMap("test.xml");

        Dijkstra newD = new Dijkstra(loadedMap.getGraph());
        System.out.println(loadedMap.toString());
        r = newD.getRouteWithRestrictions(v1, v4, new Integer[]{1});
        System.out.println(r.toString());
    }
}
