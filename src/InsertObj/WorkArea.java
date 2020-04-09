package InsertObj;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;

public class WorkArea extends Pane {
    private Paper current_paper = null;
    public WorkArea(){
        this.getStyleClass().add("word_area");
        this.getStyleClass().add("no_focus");
    }

    public void setProperty(){
        Bounds bound = current_paper.getBoundsInLocal();
//        System.out.println(bound.getHeight() + " "+bound.getWidth());
        this.setMinHeight(bound.getHeight()+20);
        this.setMinWidth(bound.getWidth()+20);

        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setPrefWidth(USE_COMPUTED_SIZE);
    }

    public void addPaper(Paper paper){
        current_paper = paper;
        this.getChildren().add(paper);
    }

}
