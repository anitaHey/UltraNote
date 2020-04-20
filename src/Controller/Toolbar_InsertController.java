package Controller;

import InsertObj.Picture;
import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Toolbar_InsertController {
    private MainController controller = MainController.getInstance();
    private PaperController paper_controller = PaperController.getInstance();
    @FXML
    VBox toolbar_insert_text;
    @FXML
    MenuItem toolbar_insert_phote_file, toolbar_insert_phote_graph;

    @FXML
    public void initialize() {
        toolbar_insert_text.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Text_box tem = new Text_box();
            tem.getStyleClass().add("text_border_focus");
            tem.requestFocus();
            paper_controller.getCurentPaper().addNode(tem);
        });

        toolbar_insert_phote_file.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("選擇圖片");

            FileChooser.ExtensionFilter imageFilter
                    = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.getExtensionFilters().add(imageFilter);

            List<File> photo_list = fileChooser.showOpenMultipleDialog(controller.getStage());

            if(!photo_list.isEmpty()){
                for(File photo : photo_list){
//                    File f = new File(Toolbar_InsertController.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//                    InputStream input = this.getClass().getResourceAsStream(f.toPath().relativize(photo.toPath()).toString());
//                    System.out.println(f.toPath().relativize(photo.toPath()));
                    FileInputStream input = null;
                    try {
                        input = new FileInputStream(photo);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    Picture image = null;
                    try {
                        image = new Picture(input);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    paper_controller.getCurentPaper().addNode(image);
                }
            }
        });
    }
}
