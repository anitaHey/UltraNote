package Controller;

import InsertObj.BasicNode;
import InsertObj.DrawLine;
import InsertObj.DrawPen;
import InsertObj.Paper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Toolbar_DrawController {
    private static Toolbar_DrawController instance;
    private PaperController paper_controller = PaperController.getInstance();
    private ArrayList<DrawLine> all_line = new ArrayList<>();
    private DrawPen current_pen = null;
    private boolean use_eraser;
    DrawLine new_draw;
    private Cursor eraser_cursor;

    public static Toolbar_DrawController getInstance() {
        if (instance == null) {
            instance = new Toolbar_DrawController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_DrawController newInstance) {
        instance = newInstance;
    }

    @FXML
    VBox cancel_pen;
    @FXML
    FlowPane pen_pane;
    @FXML
    MenuItem add_pen_normal, add_pen_light;
    @FXML
    Slider earser_width_slide;
    @FXML
    TextField earser_width_text;
    @FXML
    ToggleGroup eraser;
    @FXML
    SplitMenuButton draw_eraser;

    @FXML
    public void initialize() {
        setInstance(this);

        Image cursor_img = new Image("pic/eraser_cursor.png");
        eraser_cursor = new ImageCursor(cursor_img, cursor_img.getWidth() / 4, cursor_img.getHeight() / 4);

        DrawPen init = new DrawPen("#000", 1);
        pen_pane.getChildren().add(init);
        init.addEventHandler(MouseEvent.MOUSE_CLICKED, penHandler);
        draw_eraser.addEventHandler(MouseEvent.MOUSE_CLICKED, eraserHandler);

        cancel_pen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (current_pen != null) current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
            current_pen = null;

            if (use_eraser) {
                draw_eraser.getStyleClass().remove("draw_pen_split_btn_pressed");
                for (DrawLine line : all_line) line.setErase(false);
                use_eraser = false;
                paper_controller.getCurentPaper().setCursor(Cursor.DEFAULT);
            }

            paper_controller.getCurentPaper().removeEventHandler(MouseEvent.ANY, paperDrawHandler);

            for (Object node : paper_controller.getCurentPaper().getAllNode()) {
                ((BasicNode) node).setDrag();
            }
        });

        add_pen_normal.setOnAction(event -> {
            DrawPen tem = new DrawPen("#000", 1);
            tem.addEventHandler(MouseEvent.MOUSE_CLICKED, penHandler);

            pen_pane.getChildren().add(tem);
        });

        earser_width_slide.valueProperty().addListener((ov, old_val, new_val) -> {
            earser_width_text.setText(String.format("%d", new_val.intValue()));
        });

        earser_width_text.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int num = Integer.parseInt(newValue);
                earser_width_slide.setValue(num);
            } catch (Exception e) {
            }
        });

//        eraser.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
//
//        });
    }

    EventHandler<MouseEvent> penHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (current_pen != null) current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
            if (use_eraser) {
                draw_eraser.getStyleClass().remove("draw_pen_split_btn_pressed");
                for (DrawLine line : all_line) line.setErase(false);
                use_eraser = false;
                paper_controller.getCurentPaper().setCursor(Cursor.DEFAULT);
            }

            paper_controller.getCurentPaper().addEventHandler(MouseEvent.ANY, paperDrawHandler);
            current_pen = ((DrawPen) mouseEvent.getSource());
            current_pen.getStyleClass().add("draw_pen_split_btn_pressed");

            for (Object node : paper_controller.getCurentPaper().getAllNode()) {
                ((BasicNode) node).cancelDrag();
            }
        }
    };

    EventHandler<MouseEvent> eraserHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (current_pen != null) current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
            current_pen = null;
            paper_controller.getCurentPaper().removeEventHandler(MouseEvent.ANY, paperDrawHandler);
            draw_eraser.getStyleClass().add("draw_pen_split_btn_pressed");
            use_eraser = true;

            for (Object node : paper_controller.getCurentPaper().getAllNode()) {
                ((BasicNode) node).cancelDrag();
            }

            paper_controller.getCurentPaper().addEventHandler(MouseEvent.ANY, startEraseHandler);
            paper_controller.getCurentPaper().setCursor(eraser_cursor);
        }
    };

    EventHandler<MouseEvent> startEraseHandler = mouseEvent -> {
        if(mouseEvent.getEventType() == MouseEvent.DRAG_DETECTED){
            paper_controller.getCurentPaper().startFullDrag();
            for (DrawLine line : all_line) {
                line.setErase(true);
            }
        } else if(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED){
            for (DrawLine line : all_line) {
                line.setErase(false);
            }
        }
    };

    EventHandler<MouseEvent> paperDrawHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                paper_controller.setFocusObject(null);

                new_draw = new DrawLine();
                all_line.add(new_draw);

                paper_controller.getCurentPaper().addNode(new_draw.getGroup());
                new_draw.setStroke(current_pen.getPenWidth(), Color.web(current_pen.getPenColor()));
                new_draw.getPath().getElements()
                        .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                new_draw.getPath().getElements()
                        .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                new_draw.setSize();
                paper_controller.getCurentPaper().removeNode(new_draw.getGroup());
                paper_controller.getCurentPaper().addNode(new_draw);
            }
        }
    };

    public void deleteDrawPen(DrawPen input) {
        pen_pane.getChildren().remove(input);
    }

    public void deleteDrawLine(DrawLine input){
        all_line.remove(input);
        paper_controller.getCurentPaper().removeNode(input);
    }
}
