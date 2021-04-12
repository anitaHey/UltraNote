package Object;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoTrack extends VBox {
    private MediaPlayer player;
    private double sound_duration;
    private Image pause, play;

    public VideoTrack(MediaPlayer player) {
        try {
            this.player = player;

            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/VideoControlFxml.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            pause = new Image(getClass().getResource("../pic/pause.png").toString(), true);
            play = new Image(getClass().getResource("../pic/play.png").toString(), true);
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    MediaView media_view;
    @FXML
    Button play_btn, volume_btn;
    @FXML
    Slider slider;

    @FXML
    public void initialize() {
        media_view.setFitWidth(player.getMedia().getWidth());
        media_view.setFitHeight(player.getMedia().getHeight());
        media_view.setMediaPlayer(player);
    }

}
