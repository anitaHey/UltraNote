package Controller;

import InsertObj.BasicNode;
import InsertObj.DrawLine;
import InsertObj.DrawPen;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

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
            if (current_pen != null) {
                current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
                paper_controller.getCurrentPaper().removeEventHandler(MouseEvent.ANY, paperDrawHandler);
                current_pen = null;
            }

            if (use_eraser) {
                draw_eraser.getStyleClass().remove("draw_pen_split_btn_pressed");
                for (DrawLine line : all_line) line.setErase(false);
                use_eraser = false;

                paper_controller.getCurrentPaper().setCursor(Cursor.DEFAULT);
                paper_controller.getCurrentPaper().removeEventHandler(MouseEvent.ANY, startEraseHandler);
            }

            for (Object node : paper_controller.getCurrentPaper().getAllNode()) {
                try {
                    ((BasicNode) node).setDrag();
                } catch (Exception e) {
                }
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

    EventHandler<MouseEvent> penHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            paper_controller.setFocusObject(null);

            if (current_pen != null) current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
            if (use_eraser) {
                draw_eraser.getStyleClass().remove("draw_pen_split_btn_pressed");
                for (DrawLine line : all_line) line.setErase(false);
                use_eraser = false;
                paper_controller.getCurrentPaper().setCursor(Cursor.DEFAULT);
                paper_controller.getCurrentPaper().removeEventHandler(MouseEvent.ANY, startEraseHandler);
            }

            paper_controller.getCurrentPaper().addEventHandler(MouseEvent.ANY, paperDrawHandler);
            current_pen = ((DrawPen) mouseEvent.getSource());
            current_pen.getStyleClass().add("draw_pen_split_btn_pressed");

            for (Object node : paper_controller.getCurrentPaper().getAllNode()) {
                try {
                    ((BasicNode) node).cancelDrag();
                } catch (Exception e) {
                }
            }
        }
    };

    EventHandler<MouseEvent> eraserHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            paper_controller.setFocusObject(null);

            if (current_pen != null) current_pen.getStyleClass().remove("draw_pen_split_btn_pressed");
            current_pen = null;
            paper_controller.getCurrentPaper().removeEventHandler(MouseEvent.ANY, paperDrawHandler);
            draw_eraser.getStyleClass().add("draw_pen_split_btn_pressed");
            use_eraser = true;

            for (Object node : paper_controller.getCurrentPaper().getAllNode()) {
                ((BasicNode) node).cancelDrag();
            }

            paper_controller.getCurrentPaper().addEventHandler(MouseEvent.ANY, startEraseHandler);
            paper_controller.getCurrentPaper().setCursor(eraser_cursor);
        }
    };

    EventHandler<MouseEvent> startEraseHandler = mouseEvent -> {
        if (paper_controller.getFocusObject() != null && !paper_controller.getFocusObject().getNodeType().equals("draw")) {
            unsetPen();
        } else {
            if (mouseEvent.getEventType() == MouseEvent.DRAG_DETECTED) {
                paper_controller.getCurrentPaper().startFullDrag();
                for (DrawLine line : all_line) {
                    line.setErase(true);
                }
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                for (DrawLine line : all_line) {
                    line.setErase(false);
                }
            }
        }

    };

    EventHandler<MouseEvent> paperDrawHandler = mouseEvent -> {
        if (paper_controller.getFocusObject() != null && !paper_controller.getFocusObject().getNodeType().equals("draw")) {
            unsetPen();
        } else {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                paper_controller.setFocusObject(null);

                new_draw = new DrawLine();
                all_line.add(new_draw);

                paper_controller.getCurrentPaper().addNode(new_draw.getGroup());
                new_draw.setStroke(current_pen.getPenWidth(), Color.web(current_pen.getPenColor()));
                new_draw.getPath().getElements()
                        .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                new_draw.getPath().getElements()
                        .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                new_draw.setSize();
                paper_controller.getCurrentPaper().removeNode(new_draw.getGroup());
                paper_controller.getCurrentPaper().addNode(new_draw);
            }
        }
    };

    public void deleteDrawPen(DrawPen input) {
        pen_pane.getChildren().remove(input);
    }

    public void deleteDrawLine(DrawLine input) {
        all_line.remove(input);
        paper_controller.getCurrentPaper().removeNode(input);
    }

    public void unsetPen() {
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED,
                1, 2, 3, 4, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null);
        cancel_pen.fireEvent(mouseEvent);
    }
}
