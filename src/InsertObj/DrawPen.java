package InsertObj;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class DrawPen extends VBox {
    private String penColor = "#000";
    private double penWidth = 1;

    public DrawPen(String color, double width) {
        this.penWidth = width;
        this.penColor = color;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/DrawPenFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    SVGPath pen_color1, pen_color2;

    public String getPenColor(){
        return penColor;
    }

    public double getPenWidth(){
        return penWidth;
    }

    public void setPenColor(String newColor) {
        penColor = newColor;
        pen_color1.setFill(Color.web(getPenColor()));
        pen_color2.setFill(Color.web(getPenColor()));
    }

    public void setPenWidth(double newWidth) {
        penWidth = newWidth;
    }
}
