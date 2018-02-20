package com.thomasbisson.meteroriteescape.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Thomas on 06/06/2017.
 */

public class MyPreference {

    private static String BACK_MUSIC = "smallville.mp3";
    private static String HIT_SOUND =  "superman.mp3";

    private static Preferences preferences = Gdx.app.getPreferences("MeteoriteEscape");
    private static Music backSound = Gdx.audio.newMusic(Gdx.files.internal(BACK_MUSIC));
    private static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal(HIT_SOUND));

    public static void playBackMusic() {
        if(preferences.getBoolean("sound",false))
            backSound.play();
    }

    public static void stopBackMusic() {
        if(preferences.getBoolean("sound",false))
            backSound.stop();
    }

    public static void playHitSound() {
        if(preferences.getBoolean("sound",false))
            hitSound.play();
    }

    public static void vibrate() {
        if(preferences.getBoolean("vibrate",false))
            Gdx.input.vibrate(500);
    }

    public static Preferences getPreferences() { return  preferences; }
}
