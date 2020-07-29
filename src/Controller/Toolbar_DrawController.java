package Controller;

import InsertObj.Text_box;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Toolbar_DrawController {
    private MainController controller = MainController.getInstance();
    private PaperController paper_controller = PaperController.getInstance();
    Path path;
    Group root;

    @FXML
    VBox draw_pen_black;

    @FXML
    public void initialize() {
        draw_pen_black.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(draw_pen_black.getStyleClass().contains("toolbar_sm_button_pressed")){
                draw_pen_black.getStyleClass().remove("toolbar_sm_button_pressed");
                paper_controller.getCurentPaper().removeEventHandler(MouseEvent.ANY, mouseHandler);
            }else{
                root = new Group();
                paper_controller.getCurentPaper().addNode(root);
                paper_controller.getCurentPaper().addEventHandler(MouseEvent.ANY, mouseHandler);

                draw_pen_black.getStyleClass().add("toolbar_sm_button_pressed");
            }

        });
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                path = new Path();
                root.getChildren().add(path);
                path.setStrokeWidth(1);
                path.setStroke(Color.BLACK);
                path.getElements()
                        .add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                path.getElements()
                        .add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
            }
        }
    };
}
