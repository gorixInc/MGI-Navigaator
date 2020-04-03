import map.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class GraphTest {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        Map map = new Map("322");

        RoadVertex v1 = new RoadVertex(1, 10, 20);
        RoadVertex v2 = new RoadVertex(2, 20, 10);
        RoadVertex v3 = new RoadVertex(3, 30, 20);
        RoadVertex v4 = new RoadVertex(4, 40, 250);


        map.addTwoWayRoad(v1, v2);
        map.addTwoWayRoad(v2, v3);
        map.addOneWayRoad(v4, v1);
        map.addTwoWayRoad(v4, v2);


        System.out.println(map.getGraph().toString());

        MapFileHandler.saveMap(map, "test1.xml");

        Map loadedMap = MapFileHandler.openMap("test1.xml");

        System.out.println(loadedMap.getGraph().toString());

        MapFileHandler.saveMap(loadedMap, "test2.xml");

        Map loadedMap2 = MapFileHandler.openMap("test2.xml");

        System.out.println(loadedMap2.getGraph().toString());
    }
}
