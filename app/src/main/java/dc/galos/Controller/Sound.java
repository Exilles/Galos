package dc.galos.Controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Sound {

    private MediaPlayer mediaPlayer;

    private static boolean volume = true;

    public void mediaPlay(Context context, int resid) {
        mediaPlayer = MediaPlayer.create(context, resid);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public static boolean isVolume() {
        return volume;
    }

    public static void setVolume(boolean volume) {
        Sound.volume = volume;
    }
}
