package InsertObj;

import Object.VideoTrack;
public class VideoControl extends BasicNode{
    private VideoTrack video;
    public VideoControl(VideoTrack video) {
        super("video_control");
        setHasMin(true);

        this.video = video;

        setMinW(video.getWidth(), true);
        setMinH(video.getHeight(), true);

        getMain_content().getChildren().add(video);
    }
}
