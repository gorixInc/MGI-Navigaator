public class GraphTest {
    public static void main(String[] args) {
        Graph graph = new Graph();
        Vertex A = new Vertex(1, "A");
        Vertex B = new Vertex(2, "B");
        Vertex C = new Vertex(3, "C");
        Vertex D = new Vertex(4, "D");
        Vertex E = new Vertex(5, "E");
        Vertex F = new Vertex(6, "F");

        graph.addNonDirectedEdge(A, B, 4);
        graph.addNonDirectedEdge(A, D, 30);
        graph.addNonDirectedEdge(A, C, 5);
        graph.addNonDirectedEdge(C, D, 10);
        graph.addNonDirectedEdge(B, D, 13);
        graph.addEdge(D, E, 1);
        graph.addEdge(E, F, 7);


        System.out.println(graph.toString());

        Dijkstra dijkstra = new Dijkstra(graph);
        Route r = dijkstra.getRoute(A,D);
        System.out.println(r.toString());

        r = dijkstra.getRoute(B,C);
        System.out.println(r.toString());

        r = dijkstra.getRoute(E,A);
        System.out.println(r.toString());

    }
}
