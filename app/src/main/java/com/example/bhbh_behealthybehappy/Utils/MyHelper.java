package com.example.bhbh_behealthybehappy.Utils;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


public class MyHelper {

    // Variables
    private final int VIBRATE_DURATION = 400;

    private static MyHelper instance;
    private Context appContext;


    private MediaPlayer mp;

    public static MyHelper getInstance() {
        return instance;
    }

    private MyHelper(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    // Singleton
    public static void init(Context appContext) {
        if (instance == null) {
            instance = new MyHelper(appContext);
        }
    }

    // Vibrates phone for a set time (VIBRATE_DURATION)
    public void vibrate() {
        Vibrator v = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        Log.d("pttt", "Vibrating");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(VIBRATE_DURATION);
        }
    }


    // Allows to play audio files
    public void playAudio(int rawId) {
        stopAudio();
        Log.d("pttt", "Starting audio");
        mp = MediaPlayer.create(this.appContext,rawId);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp=null;
            }
        });
        mp.start();
    }

    // Stops playing audio
    public void stopAudio() {
        if (mp != null) {
            try {
                Log.d("pttt", "Stopping audio");
                mp.reset();
                mp.prepare();
                mp.stop();
                mp.release();
                mp=null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Shows a bubble message on screen
    public void toast(String message) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }

}
