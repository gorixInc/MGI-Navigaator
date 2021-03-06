package frontEnd.eventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import map.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SetScale implements EventHandler<MouseEvent> {
    private final Group canvas;
    private final Map map;
    double rlDistance;
    private final Scale sp;
    private final Label infoLabel;

    public SetScale(Group canvas, Map map, Label infoLabel) {
        this.canvas = canvas;
        this.map = map;
        this.sp = new Scale();
        rlDistance = 1;
        this.infoLabel = infoLabel;
    }

    public void setRlDistance(double rlDistance) {
        this.rlDistance = rlDistance;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(!sp.gotFirst) {
            canvas.getChildren().remove(sp.lineGraphics);
            sp.x[0] = mouseEvent.getX();
            sp.y[0] = mouseEvent.getY();
            sp.gotFirst = true;
        }else{
            sp.x[1] = mouseEvent.getX();
            sp.y[1] = mouseEvent.getY();
            Line line = new Line(sp.x[0], sp.y[0], sp.x[1],sp.y[1]);
            line.setStrokeWidth(5);
            line.getStrokeDashArray().addAll(10d);
            canvas.getChildren().add(line);
            sp.lineGraphics = line;
            double pixDistance = Math.sqrt(Math.pow((sp.x[1] - sp.x[0]),2) + Math.pow((sp.y[1]-sp.y[0]),2));
            map.updateScale(rlDistance/pixDistance);
            sp.gotFirst = false;
            infoLabel.setText("Scale set!");
        }
    }

    private static class Scale {
        final double[] x;
        final double[] y;
        boolean gotFirst;
        Line lineGraphics;

        public Scale() {
            this.x = new double[2];
            this.y = new double[2];
            gotFirst = false;
        }
    }
}
