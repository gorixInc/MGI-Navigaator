package map;

public class RoadEdge{
    private Integer allowedTag;
    private RoadVertex destination;
    private Double pixLength;
    private Double realLength;
    private Double allowedSpeed;
    private Double timeNoCong;
    private Double timeWithCong;
    private CongestionFunction congestionFunction;
    private boolean hasCongestion;

    public RoadEdge(RoadVertex destination, double pixLength, double scale, double allowedSpeed, Integer allowedTag) {
        this.destination = destination;
        this.pixLength = pixLength;
        this.allowedTag = allowedTag;
        this.congestionFunction = new NoCongestion();
        this.allowedSpeed = allowedSpeed;
        this.realLength = pixLength * scale;
        this.timeNoCong = realLength/allowedSpeed;
        this.timeWithCong = timeNoCong;
        this.hasCongestion = false;
    }


    public RoadEdge(RoadVertex destination, double pixLength, double scale, double allowedSpeed, Integer allowedTag,
                    CongestionFunction congestionFunction) {
        this.destination = destination;
        this.pixLength = pixLength;
        this.allowedTag = allowedTag;
        this.congestionFunction = congestionFunction;
        this.allowedSpeed = allowedSpeed;
        this.realLength = pixLength * scale;
        this.timeNoCong = realLength/allowedSpeed;
        this.timeWithCong = timeNoCong;
        this.hasCongestion = true;
    }

    public void updateScale(Double newScale){
        this.realLength = pixLength * newScale;
        this.timeNoCong = realLength/allowedSpeed;
    }
    public void updateSpeed(Double newSpeed){
        this.allowedSpeed = newSpeed;
        this.timeNoCong = realLength/allowedSpeed;
    }

    public void updateCongTime(double minsSinceMidnight){
        this.timeWithCong = congestionFunction.getMultiplierAtTime(minsSinceMidnight) * timeNoCong;
    }
    public double getWeightAtTime(double minsSinceMidnight){
        return congestionFunction.getMultiplierAtTime(minsSinceMidnight) * timeNoCong;
    }
    public CongestionFunction getCongestionFunction() {
        return congestionFunction;
    }

    public RoadVertex getDestination() {
        return destination;
    }

    public Double getTimeWithCong() {
        return timeWithCong;
    }

    public Double getTimeNoCong(){return timeNoCong;}

    public Double getRealLength(){return realLength;}

    public Double getPixLength() {
        return pixLength;
    }

    public Integer getAllowedTag() {
        return allowedTag;
    }

    public Double getAllowedSpeed() {
        return allowedSpeed;
    }
    public boolean hasCongestion() {
        return hasCongestion;
    }

}
