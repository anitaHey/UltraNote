package InsertObj;

import Controller.Toolbar_DrawController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class DrawPen extends SplitMenuButton {
    Toolbar_DrawController draw_controll = Toolbar_DrawController.getInstance();
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
    @FXML
    Slider width_slider;
    @FXML
    ColorPicker draw_color;
    @FXML
    HBox delete_pen;
    @FXML
    TextField width_text;

    @FXML
    public void initialize() {
        width_slider.valueProperty().addListener((ov, old_val, new_val) -> {
            setPenWidth(new_val.doubleValue());
            width_text.setText(String.format("%d", new_val.intValue()));
        });

        width_text.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int num = Integer.parseInt(newValue);
                width_slider.setValue(num);
            } catch (Exception e){
            }
        });

        draw_color.valueProperty().addListener((observable, oldValue, newValue) -> {
            setPenColor(toRGBCode(newValue));
        });

        delete_pen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            draw_controll.deleteDrawPen(this);
        });
    }

    public String getPenColor() {
        return penColor;
    }

    public double getPenWidth() {
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

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
