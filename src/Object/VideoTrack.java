package Object;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

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
    ImageView play_pic, volume_pic;
    @FXML
    Slider slider;
    @FXML
    Label time_label;

    @FXML
    public void initialize() {
        int width = player.getMedia().getWidth();
        int height = player.getMedia().getHeight();
        int tem;
        if(player.getMedia().getWidth() > 600 && player.getMedia().getHeight() > 600){
            tem = width / 600;
            width /= tem;
            height /= tem;
        }

        slider.setPrefWidth(width/2);

        media_view.setFitWidth(width);
        media_view.setFitHeight(height);
        media_view.setMediaPlayer(player);

        time_label.setText(durationString(player.getMedia().getDuration().toSeconds()));

        slider.setMax(Math.floor(player.getMedia().getDuration().toSeconds()));

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
            time_label.setText(durationString(player.getMedia().getDuration().toSeconds()));
            slider.setValue(0);
            player.pause();
        });

        player.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            if (!slider.isValueChanging()) {
                time_label.setText(durationString(newVal.toSeconds()));
                slider.setValue(Math.floor(newVal.toSeconds()));
            }
        });

        slider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!slider.isValueChanging()) {
                double currentTime = player.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 1) {
                    player.seek(Duration.seconds(newValue.doubleValue()));
                    time_label.setText(durationString(player.getCurrentTime().toSeconds()));
                }
            }
        });

        slider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            if(!newVal){
                player.seek(Duration.seconds(slider.getValue()));
                time_label.setText(durationString(player.getCurrentTime().toSeconds()));
            }
        });
    }

    private String durationString(double duration){
        int min = (int) Math.floor(duration / 60);
        duration = duration / 60 - min;
        duration =  Math.round(duration * 100.0)/100.0;
        String[] tem = String.valueOf(duration).split("\\.");
        if(tem[1].length() == 1) tem[1] += "0";
        return min + ":" + tem[1];
    }

}
