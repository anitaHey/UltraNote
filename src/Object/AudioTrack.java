package Object;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioTrack extends HBox {
    private MediaPlayer player;
    private double sound_duration;
    private Image pause, play;

    public AudioTrack(MediaPlayer player) {
        try {
            this.player = player;

            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/AudioControlFxml.fxml"));

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
    Button play_btn, volume_btn;
    @FXML
    ImageView play_pic, volume_pic;
    @FXML
    Slider sound_track;
    @FXML
    Label time_label;

    @FXML
    public void initialize() {
        sound_duration = player.getMedia().getDuration().toMinutes();
        time_label.setText(durationString(sound_duration));

        sound_track.setMax(Math.floor(player.getMedia().getDuration().toSeconds()));

        play_btn.setOnAction(e -> {
            if(player.getStatus() == MediaPlayer.Status.PAUSED || player.getStatus() == MediaPlayer.Status.READY) {
                play_pic.setImage(pause);
                player.play();
            } else {
                play_pic.setImage(play);
                player.pause();
            }
        });

        player.setOnEndOfMedia(() -> {
            play_pic.setImage(play);
            time_label.setText(durationString(sound_duration));
            sound_track.setValue(0);
        });

        player.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            if (!sound_track.isValueChanging()) {
                time_label.setText(durationString(newVal.toMinutes()));
                sound_track.setValue(Math.floor(newVal.toSeconds()));
            }
        });

        sound_track.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!sound_track.isValueChanging()) {
                double currentTime = player.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 1) {
                    player.seek(Duration.seconds(newValue.doubleValue()));
                    time_label.setText(durationString(player.getCurrentTime().toMinutes()));
                }
            }
        });

        sound_track.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal){
                player.seek(Duration.seconds(sound_track.getValue()));
                time_label.setText(durationString(player.getCurrentTime().toMinutes()));
            }
        });
    }

    private String durationString(double duration){
        duration =  Math.round(duration * 100.0)/100.0;
        String[] tem = String.valueOf(duration).split("\\.");
        if(tem[1].length() == 1) tem[1] += "0";
        return tem[0] + ":" + tem[1];
    }
}
