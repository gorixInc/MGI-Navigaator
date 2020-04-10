package map;

public class RoadEdge{
    private Integer[] allowedTags;
    private RoadVertex destination;
    private Double weight;

    public RoadEdge(RoadVertex destination, double weight, Integer[] allowedTags) {
        this.destination = destination;
        this.weight = weight;
        this.allowedTags = allowedTags;
    }

    public RoadVertex getDestination() {
        return destination;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer[] getAllowedTags() {
        return allowedTags;
    }
}
