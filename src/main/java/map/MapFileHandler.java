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
    public static Map openMap(String path) throws Exception {
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

    private static void parseForGraph(Document document, Map map) throws Exception {
        Element graph = (Element) document.getElementsByTagName("graph").item(0);

        Double scale = Double.parseDouble(graph.getElementsByTagName("scale").item(0).getTextContent());
        map.updateScale(scale);

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
            NodeList edgeNodes = thisVertex.getElementsByTagName("edge");
            for(int j = 0; j < edgeNodes.getLength(); j++){
                Element thisEdge = (Element) edgeNodes.item(j);
                double thisEdgeSpeed = Double.parseDouble(thisEdge.getElementsByTagName("speed")
                        .item(0).getTextContent());
                int destinationIndex = Integer.parseInt(thisEdge.getElementsByTagName("destinationindex")
                        .item(0).getTextContent());

                NodeList allowedTag = thisEdge.getElementsByTagName("tag");
                Integer tag = Integer.parseInt(allowedTag.item(0).getTextContent());

                Element congFuncNode = (Element) thisEdge.getElementsByTagName("congestionfunction").item(0);
                int congFuncType = Integer.parseInt(congFuncNode.getElementsByTagName("type")
                        .item(0).getTextContent());
                CongestionFunction congestionFunction = new NoCongestion();
                if(congFuncType ==1){
                    Double peakTime = Double.parseDouble(congFuncNode.getElementsByTagName("peaktime")
                            .item(0).getTextContent());
                    Double minMultiplier = Double.parseDouble(congFuncNode.getElementsByTagName("minmultiplier")
                            .item(0).getTextContent());
                    Double width = Double.parseDouble(congFuncNode.getElementsByTagName("width")
                            .item(0).getTextContent());
                    congestionFunction = new SinglePeakCongestion(peakTime, minMultiplier, width);
                }
                map.addOneWayRoad(tempVertexHashMap.get(thisVertIndex), tempVertexHashMap.get(destinationIndex)
                        ,thisEdgeSpeed, tag, congestionFunction);
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

        Element scale =  document.createElement("scale");
        scale.appendChild(document.createTextNode(String.valueOf(map.getScale())));
        graph.appendChild(scale);

        HashMap<RoadVertex, LinkedList<RoadEdge>> adjacencyMap = map.getGraph().getAdjacencyMap();

        for(RoadVertex key: adjacencyMap.keySet()){
            graph.appendChild(createVertexNode(document, key, adjacencyMap.get(key)));
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

    private static Node createVertexNode(Document doc, RoadVertex vertex, LinkedList<RoadEdge> edgeList){
        Element vertexEl = doc.createElement("vertex");
        vertexEl.setAttribute("index", vertex.index.toString());

        Element posX = doc.createElement("posX");
        posX.appendChild(doc.createTextNode(vertex.posX.toString()));

        Element posY = doc.createElement("posY");
        posY.appendChild(doc.createTextNode(vertex.posY.toString()));

        vertexEl.appendChild(posX);
        vertexEl.appendChild(posY);

        for(RoadEdge edge: edgeList){
            vertexEl.appendChild(createEdgeNode(doc, edge));
        }
        return vertexEl;
    }

    private static Node createEdgeNode(Document doc, RoadEdge edge){
        Element edgeEl = doc.createElement("edge");
        Element allowedTagEl = doc.createElement("allowedtags");
        Element destID = doc.createElement("destinationindex");
        Element speedEl = doc.createElement("speed");

        Element congFuncEl = doc.createElement("congestionfunction");
        Element funcType = doc.createElement("type");


        Integer tag = edge.getAllowedTag();
        Element tagEl = doc.createElement("tag");
        tagEl.appendChild(doc.createTextNode(tag.toString()));
        allowedTagEl.appendChild(tagEl);

        congFuncEl.appendChild(funcType);

        if(edge.getCongestionFunction() instanceof NoCongestion){
            funcType.appendChild(doc.createTextNode("0"));
        }
        if(edge.getCongestionFunction() instanceof SinglePeakCongestion){
            funcType.appendChild(doc.createTextNode("1"));

            Element peakTimeEl = doc.createElement("peaktime");
            Element minMultiplierEl = doc.createElement("minmultiplier");
            Element widthEl = doc.createElement("width");

            peakTimeEl.appendChild(doc.createTextNode(((SinglePeakCongestion) edge.getCongestionFunction())
                    .getPeak().toString()));
            minMultiplierEl.appendChild(doc.createTextNode(((SinglePeakCongestion) edge.getCongestionFunction())
                    .getMinMultiplier().toString()));
            widthEl.appendChild(doc.createTextNode(((SinglePeakCongestion) edge.getCongestionFunction())
                    .getWidth().toString()));

            congFuncEl.appendChild(peakTimeEl);
            congFuncEl.appendChild(minMultiplierEl);
            congFuncEl.appendChild(widthEl);
        }

        destID.appendChild(doc.createTextNode(edge.getDestination().index.toString()));
        speedEl.appendChild(doc.createTextNode(edge.getAllowedSpeed().toString()));

        edgeEl.appendChild(destID);
        edgeEl.appendChild(speedEl);
        edgeEl.appendChild(allowedTagEl);
        edgeEl.appendChild(congFuncEl);
        return edgeEl;
    }
}
