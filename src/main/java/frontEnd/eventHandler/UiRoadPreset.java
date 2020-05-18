package frontEnd.eventHandler;

import map.CongestionFunction;

import java.util.ArrayList;
import java.util.List;

public class UiRoadPreset {
    List<Integer> allowedTags;
    final double[] maxSpeed;
    final CongestionFunction[] congestionFunction;
    final boolean[] hasCongestion;
    public UiRoadPreset(int numberOfTags){
        this.allowedTags = new ArrayList<>();
        this.maxSpeed = new double[numberOfTags];
        this.congestionFunction = new CongestionFunction[numberOfTags];
        this.hasCongestion = new boolean[numberOfTags];
    }

    public List<Integer> getTags() {
        return allowedTags;
    }
    public void setTags(List<Integer> allowedTags){
        this.allowedTags = allowedTags;
    }
    public double[] getMaxSpeed() {
        return maxSpeed;
    }

    public CongestionFunction[] getCongestionFunction() {
        return congestionFunction;
    }

    public boolean[] hasCongestion() {
        return hasCongestion;
    }

    public void setCongestionFunction(CongestionFunction congestionFunction, int n) {
        this.congestionFunction[n] = congestionFunction;
    }
}
