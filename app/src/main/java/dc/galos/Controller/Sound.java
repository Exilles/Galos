package dc.galos.Controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Sound {

    private static MediaPlayer mediaPlayer;

    private static boolean volume = true;

    public void initialization(Context context, int resid) {
        mediaPlayer = MediaPlayer.create(context, resid);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
    }

    public void mediaStart() {
        mediaPlayer.start();
    }

    public void mediaStop() {
        mediaPlayer.stop();
    }

    public static boolean isVolume() {
        return volume;
    }

    public static void setVolume(boolean volume) {
        Sound.volume = volume;
    }
}
