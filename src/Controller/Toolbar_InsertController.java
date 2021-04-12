package Controller;

import InsertObj.*;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import Object.AudioTrack;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Toolbar_InsertController {
    private MainController controller = MainController.getInstance();
    private PaperController paper_controller = PaperController.getInstance();
    private static Toolbar_InsertController instance;

    public static Toolbar_InsertController getInstance() {
        if (instance == null) {
            instance = new Toolbar_InsertController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_InsertController newInstance) {
        instance = newInstance;
    }

    @FXML
    VBox toolbar_insert_text, toolbar_insert_code, toolbar_insert_list, toolbar_insert_vbox, toolbar_insert_hbox;
    @FXML
    MenuItem toolbar_insert_phote_file, toolbar_insert_phote_graph, toolbar_insert_sound_file, toolbar_insert_sound_record;

    @FXML
    public void initialize() {
        setInstance(this);

        toolbar_insert_text.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Text_box tem = new Text_box();
            tem.getStyleClass().add("text_border_focus");
            tem.requestFocus();
            paper_controller.getCurrentPaper().addNode(tem);
        });

        toolbar_insert_phote_file.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("選擇圖片");

            FileChooser.ExtensionFilter imageFilter
                    = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
            fileChooser.getExtensionFilters().add(imageFilter);

            List<File> photo_list = fileChooser.showOpenMultipleDialog(controller.getStage());

            if (!photo_list.isEmpty()) {
                for (File photo : photo_list) {
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
                    paper_controller.getCurrentPaper().addNode(image);
                }
            }
        });

        toolbar_insert_code.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            CodeBlock codeBlock = new CodeBlock();
            codeBlock.requestFocus();
            paper_controller.getCurrentPaper().addNode(codeBlock);
        });

        toolbar_insert_list.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            CheckList checkList = new CheckList();
            checkList.requestFocus();
            paper_controller.getCurrentPaper().addNode(checkList);
        });

        toolbar_insert_vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Layout_VBox layout_vbox = new Layout_VBox();
            layout_vbox.requestFocus();
            paper_controller.getCurrentPaper().addNode(layout_vbox);
        });

        toolbar_insert_hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Layout_HBox layout_hbox = new Layout_HBox();
            layout_hbox.requestFocus();
            paper_controller.getCurrentPaper().addNode(layout_hbox);
        });

        toolbar_insert_sound_file.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("選擇音訊");

            FileChooser.ExtensionFilter soundFilter
                    = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac");
            fileChooser.getExtensionFilters().add(soundFilter);

            List<File> sound_list = fileChooser.showOpenMultipleDialog(controller.getStage());

            if (!sound_list.isEmpty()) {
                for (File sound : sound_list) {
                    Media media = new Media(sound.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);

                    mediaPlayer.setOnReady(() -> {
                        AudioTrack track = new AudioTrack(mediaPlayer);
                        paper_controller.getCurrentPaper().addNode(new AudioControl(track));
                    });
                }
            }
        });
    }
}
