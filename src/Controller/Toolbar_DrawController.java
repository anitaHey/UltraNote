package Controller;

import InsertObj.BasicNode;
import InsertObj.DrawPen;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;

public class Toolbar_DrawController {
    private static Toolbar_DrawController instance;
    private PaperController paper_controller = PaperController.getInstance();
    private ArrayList<DrawPen> create_pen = new ArrayList<>();
    private DrawPen current_pen = null;
    Path path;
    BasicNode root;

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
    HBox pen_hbox;

    @FXML
    public void initialize() {
        setInstance(this);
        DrawPen init = new DrawPen("#000", 1);
        create_pen.add(init);
        pen_hbox.getChildren().addAll(create_pen);
        System.out.println(create_pen);

        init.addEventHandler(MouseEvent.MOUSE_CLICKED, penHandler);

        cancel_pen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (current_pen != null) current_pen.getStyleClass().remove("toolbar_sm_button_pressed");
            current_pen = null;
            paper_controller.getCurentPaper().removeEventHandler(MouseEvent.ANY, paperDrawHandler);

            for (Object node : paper_controller.getCurentPaper().getAllNode()) {
                ((BasicNode) node).setDrag();
            }
        });
    }

    EventHandler<MouseEvent> penHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (current_pen != null) current_pen.getStyleClass().remove("toolbar_sm_button_pressed");
            paper_controller.getCurentPaper().addEventHandler(MouseEvent.ANY, paperDrawHandler);
            current_pen = ((DrawPen) mouseEvent.getSource());
            current_pen.getStyleClass().add("toolbar_sm_button_pressed");

            for (Object node : paper_controller.getCurentPaper().getAllNode()) {
                ((BasicNode) node).cancelDrag();
            }
        }
    };

    EventHandler<MouseEvent> paperDrawHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                root = new BasicNode("draw");
                root.cancelDrag();
                paper_controller.setFocusObject(null);

                path = new Path();
                paper_controller.getCurentPaper().addNode(root);

                root.getMain_content().getChildren().add(path);
                path.setStrokeWidth(current_pen.getPenWidth());
                path.setStroke(Color.web(current_pen.getPenColor()));
                path.getElements()
                        .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                path.getElements()
                        .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {

            }
        }
    };
}
