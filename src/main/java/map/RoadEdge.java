package map;

public class RoadEdge{
    private Integer[] allowedTags;
    private RoadVertex destination;
    private Double baseWeight;
    private Double scaledWeight;
    private CongestionFunction congestionFunction;

    public RoadEdge(RoadVertex destination, double baseWeight, Integer[] allowedTags) {
        this.destination = destination;
        this.baseWeight = baseWeight;
        this.allowedTags = allowedTags;
        this.congestionFunction = new NoCongestion();
        this.scaledWeight = baseWeight;
    }

    public RoadEdge(RoadVertex destination, double baseWeight, Integer[] allowedTags, CongestionFunction congestionFunction) {
        this.destination = destination;
        this.baseWeight = baseWeight;
        this.allowedTags = allowedTags;
        this.congestionFunction = congestionFunction;
        this.scaledWeight = baseWeight;
    }

    public void updateWeight(double minsSinceMidnight){
        this.scaledWeight = congestionFunction.getMultiplier(minsSinceMidnight) * baseWeight;
    }
    public double getScaledWeight(double minsSinceMidnight){
        return congestionFunction.getMultiplier(minsSinceMidnight) * baseWeight;
    }
    public CongestionFunction getCongestionFunction() {
        return congestionFunction;
    }

    public RoadVertex getDestination() {
        return destination;
    }

    public Double getBaseWeight() {
        return baseWeight;
    }

    public Integer[] getAllowedTags() {
        return allowedTags;
    }
}
