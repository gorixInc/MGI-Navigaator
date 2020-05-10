package map;

public class NoCongestion extends CongestionFunction {
    //xml type 0
    @Override
    public double getMultiplierAtTime(double currentTimeMins) {
        return 1;
    }

    @Override
    public Double getMinMultiplier() {
        return 0.0;
    }

    @Override
    public Double getPeak() {
        return 0.0;
    }

    @Override
    public Double getWidth() {
        return 0.0;
    }
}
