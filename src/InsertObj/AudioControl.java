package InsertObj;

import Object.AudioTrack;

public class AudioControl extends BasicNode {
    private AudioTrack audio;
    public AudioControl(AudioTrack audio) {
        super("sound_control");
        setHasMin(true);

        this.audio = audio;

        setMinW(audio.getWidth(), true);
        setMinH(audio.getHeight(), true);


        getMain_content().getChildren().add(audio);
    }
}
