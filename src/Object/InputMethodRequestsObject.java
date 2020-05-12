package Object;

import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodRequests;

public class InputMethodRequestsObject implements InputMethodRequests {
    double position_x = 0;
    double position_y = 0;

    @Override
    public Point2D getTextLocation(int i) {
        return new Point2D(position_x, position_y);
    }

    @Override
    public int getLocationOffset(int i, int i1) {
        return 0;
    }

    @Override
    public void cancelLatestCommittedText() {

    }

    @Override
    public String getSelectedText() {
        return "";
    }

    public void setPosition_x(double x) {
        this.position_x = x;
    }

    public void setPosition_y(double y) {
        this.position_y = y;
    }
}
