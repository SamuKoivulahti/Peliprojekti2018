package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Essi Supponen on 19/04/2018.
 */

public class SoundManager {
    private static Sound changeSelectionSound = Gdx.audio.newSound(Gdx.files.internal("audio/pew.wav"));
    private static Music selectionBarSound = Gdx.audio.newMusic(Gdx.files.internal("audio/timer.wav"));
    private static Sound timerSound = Gdx.audio.newSound(Gdx.files.internal("audio/timer.wav"));
    private static Sound rightAnswerSound = Gdx.audio.newSound(Gdx.files.internal("audio/right.wav"));
    private static Sound wrongAnswerSound = Gdx.audio.newSound(Gdx.files.internal("audio/wrong.wav"));

    private static Sound buttonPushSound = Gdx.audio.newSound(Gdx.files.internal("audio/buttonpush.wav"));
    private static Sound buttonUpSound = Gdx.audio.newSound(Gdx.files.internal("audio/buttonpush.wav"));
    private static Sound clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/pew.wav"));

    private static Music menuMusic;
    private static Music ingameMusic;

    public static void playChangeSelectionSound() {
        //changeSelectionSound.play();
    }

    public static void playSelectionBarSound() {
        selectionBarSound.setLooping(true);
        selectionBarSound.play();
    }

    public static boolean isSelectionBarSoundPlaying() {
        return selectionBarSound.isPlaying();
    }

    public static void stopSelectionBarSound() {
        selectionBarSound.stop();
    }

    public static void playTimerSound() {
        timerSound.loop();
    }

    public static void stopTimerSound() {
        timerSound.stop();
    }

    public static void playRightAnswerSound() {
        rightAnswerSound.play();
    }

    public static void playWrongAnswerSound() {
        wrongAnswerSound.play();
    }

    public static void playButtonPushSound() {
        buttonPushSound.play();
    }

    public static void playButtonUpSound() {
        buttonUpSound.play();
    }

    public static void playClickSound() {
        clickSound.play();
    }
}
