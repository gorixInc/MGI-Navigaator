package graph;

import graph.Vertex;

public class Edge {
    private Vertex destination;
    Double weight;

    public Edge(Vertex destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Double getWeight() {
        return weight;
    }
}
