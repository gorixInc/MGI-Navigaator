package frontEnd.eventHandler;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.Map;

public class speedLimitChangeListener implements ChangeListener<String>{

    public speedLimitChangeListener(TextField speedLimit, Map<Integer, Integer> maxSpeeds, int index, int roadType, AddRoad addRoad) {
        this.speedLimit = speedLimit;
        this.maxSpeeds = maxSpeeds;
        this.index = index;
        this.roadType = roadType;
        this.addRoad = addRoad;
    }


    public void setRoadType(int roadType) {
        this.roadType = roadType;
    }

    private TextField speedLimit;
    private Map<Integer, Integer> maxSpeeds;
    private int index;
    private int roadType;
    private int arv;
    private AddRoad addRoad;

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
        if (!t1.matches("\\d*")) {
            t1 = t1.replaceAll("[^\\d]", "");
            speedLimit.setText(t1);
        }
        if (t1.equals("")) {
            arv = 0;
        } else {
            arv = Integer.parseInt(t1);
        }
        maxSpeeds.put(index, arv);
        if (roadType == index){
            addRoad.setMaxSpeed(arv);
        }
    }
}

