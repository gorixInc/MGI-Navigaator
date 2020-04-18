package map;

public class NoCongestion extends CongestionFunction {
    //xml type 0
    @Override
    public double getMultiplier(double currentTimeMins) {
        return 1;
    }
}
