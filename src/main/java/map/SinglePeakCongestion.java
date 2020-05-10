package map;

public class SinglePeakCongestion extends CongestionFunction {
    //xml type 1
    private Double peakTime;
    private Double minMultiplier;
    private Double width;

    public SinglePeakCongestion(double peakTime, double minMultiplier, double width) throws Exception {
        if (peakTime > 1439 || peakTime < -1 || minMultiplier <= 0 || minMultiplier > 1 || width < 0) {
            throw new Exception("Inappropriate parameters, must be: -1 < peakTime < 1439, 0 <= minMultiplier < 1, width < 0");
        }

        this.peakTime = peakTime;
        this.minMultiplier = minMultiplier;
        this.width = width;
    }

    public double getMultiplierAtTime(double timeMins){
        double multiplier = (width*10*(1/minMultiplier-1))/(Math.pow((timeMins-peakTime), 2) + width * 10) + 1; //https://www.desmos.com/calculator/jibvt5akbp
        if(multiplier < 1.1){
            multiplier = 1;
        }
        return multiplier;
    }

    public Double getPeak() {
        return peakTime;
    }
    public Double getMinMultiplier() {
        return minMultiplier;
    }
    public Double getWidth() {
        return width;
    }
}
