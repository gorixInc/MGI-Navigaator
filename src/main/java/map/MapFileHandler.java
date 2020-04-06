package map;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import graph.Edge;
import graph.Vertex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapFileHandler {
    /**
     * Opens and parses an XML file created by saveMap method and recreates saved Map object, graph adj. dict might be
     * in different order... (should not create problems)
     * @param path path to file
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Map openMap(String path) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();

        Element metadata = (Element) document.getElementsByTagName("metadata").item(0);
        String mapName = metadata.getElementsByTagName("mapname").item(0).getTextContent();
        Map map = new Map(mapName);

        parseForGraph(document, map);
        return map;
    }

    private static void parseForGraph(Document document, Map map){
        Element graph = (Element) document.getElementsByTagName("graph").item(0);
        NodeList vertices = graph.getElementsByTagName("vertex");
        HashMap<Integer, RoadVertex> tempVertexHashMap = new HashMap<>();
        for(int i = 0; i < vertices.getLength(); i++){
            Element thisVertex = (Element) vertices.item(i);
            int thisVertIndex = Integer.parseInt(thisVertex.getAttribute("index"));
            double posX = Double.parseDouble(thisVertex.getElementsByTagName("posX").item(0).getTextContent());
            double posY = Double.parseDouble(thisVertex.getElementsByTagName("posY").item(0).getTextContent());
            tempVertexHashMap.put(thisVertIndex, new RoadVertex(thisVertIndex, posX, posY));
        }

        for(int i = 0; i < vertices.getLength(); i++){
            Element thisVertex = (Element) vertices.item(i);
            int thisVertIndex = Integer.parseInt(thisVertex.getAttribute("index"));
            NodeList edges = thisVertex.getElementsByTagName("edge");
            for(int j = 0; j < edges.getLength(); j++){
                Element thisEdge = (Element) edges.item(j);
                double thisEdgeWeight = Double.parseDouble(thisEdge.getElementsByTagName("weight")
                        .item(0).getTextContent());
                int destinationIndex = Integer.parseInt(thisEdge.getElementsByTagName("destinationIndex")
                        .item(0).getTextContent());
                map.addOneWayRoad(tempVertexHashMap.get(thisVertIndex), tempVertexHashMap.get(destinationIndex)
                        , thisEdgeWeight);
            }
        }
    }

    /**
     * Saves map to XML file.
     * @param map Map object to save
     * @param path Path to file
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void saveMap(Map map, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //Root and metadata
        Element root= document.createElement("map");
        root.appendChild(createMetadata(document, map.name));
        document.appendChild(root);

        //Graph
        Element graph = document.createElement("graph");
        root.appendChild(graph);

        HashMap<Vertex, LinkedList<Edge>> adjacencyMap = map.getGraph().getAdjacencyMap();

        for(Vertex key: adjacencyMap.keySet()){
            graph.appendChild(createVertexNode(document, (RoadVertex) key, adjacencyMap.get(key)));
        }

        //Finalizing
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        StreamResult streamResult = new StreamResult(new File(path));
        transformer.transform(domSource, streamResult);
    }

    private static Node createMetadata(Document doc, String name){
        Element metadata = doc.createElement("metadata");

        Element mapName = doc.createElement("mapname");
        mapName.appendChild(doc.createTextNode(name));
        metadata.appendChild(mapName);

        Element lastEdited = doc.createElement("lastedit");
        lastEdited.appendChild(doc.createTextNode(String.valueOf(new Timestamp(System.currentTimeMillis()))));
        metadata.appendChild(lastEdited);
        return metadata;
    }

    private static Node createVertexNode(Document doc, RoadVertex vertex, LinkedList<Edge> edgeList){
        Element vertexEl = doc.createElement("vertex");
        vertexEl.setAttribute("index", vertex.index.toString());

        Element posX = doc.createElement("posX");
        posX.appendChild(doc.createTextNode(vertex.posX.toString()));

        Element posY = doc.createElement("posY");
        posY.appendChild(doc.createTextNode(vertex.posY.toString()));
        vertexEl.appendChild(posX);
        vertexEl.appendChild(posY);

        for(Edge edge: edgeList){
            Element edgeEl = doc.createElement("edge");
            Element destID = doc.createElement("destinationIndex");
            Element weight = doc.createElement("weight");

            destID.appendChild(doc.createTextNode(edge.getDestination().index.toString()));
            weight.appendChild(doc.createTextNode(edge.getWeight().toString()));

            edgeEl.appendChild(destID);
            edgeEl.appendChild(weight);

            vertexEl.appendChild(edgeEl);

        }
        return vertexEl;
    }
}
