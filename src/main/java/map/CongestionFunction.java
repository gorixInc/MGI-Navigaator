package map;

public abstract class CongestionFunction {
    public abstract double getMultiplierAtTime(double currentTimeMins);
    public abstract Double getMinMultiplier();
    public abstract Double getPeak();
    public abstract Double getWidth();
}
