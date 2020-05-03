package map;

public class RoadEdge{
    private Integer allowedTag;
    private RoadVertex destination;
    private Double baseWeight;
    private Double scaledWeight;
    private CongestionFunction congestionFunction;

    public RoadEdge(RoadVertex destination, double baseWeight, Integer allowedTag) {
        this.destination = destination;
        this.baseWeight = baseWeight;
        this.allowedTag = allowedTag;
        this.congestionFunction = new NoCongestion();
        this.scaledWeight = baseWeight;
    }

    public RoadEdge(RoadVertex destination, double baseWeight, Integer allowedTag, CongestionFunction congestionFunction) {
        this.destination = destination;
        this.baseWeight = baseWeight;
        this.allowedTag = allowedTag;
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

    public Integer getAllowedTag() {
        return allowedTag;
    }
}
