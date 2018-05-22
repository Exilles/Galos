package dc.galos.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import dc.galos.R;

public class Sound extends Service {
    MediaPlayer player;
    static boolean volume = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.background_music);
        player.setLooping(true); // зацикливаем
    }

    @Override
    public void onDestroy() {
        player.stop();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        player.start();
    }

    public static boolean isVolume() {
        return volume;
    }

    public static void setVolume(boolean volume) {
        Sound.volume = volume;
    }
}
