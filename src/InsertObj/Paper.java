package InsertObj;

import Controller.PaperController;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Paper extends Pane {
    private PaperController paper_controller = PaperController.getInstance();
    private boolean isClick = true;
    String background_color = null;

    public Paper() {
        setBackground_color("#fff");
        paper_controller.setCurentPaper(this);
    }

    public Paper(double width, double height) {
        setBackground_color("#fff");
        setInfinatePaper(width, height);
        paper_controller.setCurentPaper(this);

        this.setOnMouseClicked(e -> {
            if (isClick)
                paper_controller.setFocusObject(null);
        });
    }

    public void setClick(boolean input) {
        isClick = input;
    }

    public void setBackground_color(String color) {
        background_color = color;
        this.setStyle("-fx-background-color: " + background_color);
    }

    public void setIndivailPaper(String size, int direction) {
        this.setLayoutX(91);
        this.setLayoutY(47);
        double height = 0;
        double width = 0;
        switch (size) {
            case "A4":
                if (direction == 0) {
                    height = 29.7;
                    width = 21;
                } else {
                    width = 29.7;
                    height = 21;
                }
                break;
            case "A3":
                if (direction == 0) {
                    height = 42;
                    width = 29.7;
                } else {
                    width = 42;
                    height = 29.7;
                }
                break;
            case "B4":
                if (direction == 0) {
                    height = 36.4;
                    width = 25.7;
                } else {
                    width = 36.4;
                    height = 25.7;
                }
                break;

            default:
                String[] split = size.split(",");
                height = Double.parseDouble(split[0]);
                width = Double.parseDouble(split[1]);
        }

        this.setPrefHeight(height);
        this.setPrefWidth(width);
    }

    public void setInfinatePaper(double width, double height) {
        this.setLayoutX(0);
        this.setLayoutY(0);

        this.setMinHeight(height - 100);
        this.setMinWidth(width - 60);
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setPrefWidth(USE_COMPUTED_SIZE);
    }

    public void addNode(Node input) {
        this.getChildren().add(input);
    }

    public void removeNode(Node input) {
        this.getChildren().remove(input);
    }

    public Object[] getAllNode() {
        return this.getChildren().toArray();
    }
}
