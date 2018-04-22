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

    public static void playChangeSelectionSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            //changeSelectionSound.play();
        }
    }

    public static void playSelectionBarSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            selectionBarSound.setLooping(true);
            selectionBarSound.play();
        }
    }

    public static boolean isSelectionBarSoundPlaying() {
        return selectionBarSound.isPlaying();
    }

    public static void stopSelectionBarSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            selectionBarSound.stop();
        }
    }

    public static void playTimerSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            timerSound.loop();
        }
    }

    public static void stopTimerSound() {
        timerSound.stop();
    }

    public static void playRightAnswerSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            rightAnswerSound.play();
        }
    }

    public static void playWrongAnswerSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            wrongAnswerSound.play();
        }
    }

    public static void playButtonPushSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            buttonPushSound.play();
        }
    }

    public static void playButtonUpSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            buttonUpSound.play();
        }
    }

    public static void playClickSound(boolean soundEffectsOn) {
        if (soundEffectsOn) {
            clickSound.play();
        }
    }
}
