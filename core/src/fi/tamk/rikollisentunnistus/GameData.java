package fi.tamk.rikollisentunnistus;

/**
 * Created by Samu Koivulahti on 25.3.2018.
 */

public class GameData {
    private int points;
    private boolean win;
    private int level;
    private Face chosenCriminal;
    private Face correctCriminal;
    private boolean stillLeaning;

    public GameData() {
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean getWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getStillLeaning() {
        return stillLeaning;
    }

    public void setStillLeaning(boolean stillLeaning) {
        this.stillLeaning = stillLeaning;
    }

    public static final float DEFAULT_SENSITIVITY_RIGHT = 5f;
    public static final float DEFAULT_SENSITIVITY_LEFT = -5f;
    public static final float DEFAULT_SENSITIVITY_UP = 5f;
    public static final float DEFAULT_SENSITIVITY_DOWN = -5f;
    public static final float DEFAULT_HYSTERESIS_RIGHT = DEFAULT_SENSITIVITY_RIGHT/2;
    public static final float DEFAULT_HYSTERESIS_LEFT = DEFAULT_SENSITIVITY_LEFT/2;
    public static final float DEFAULT_HYSTERESIS_UP = DEFAULT_SENSITIVITY_UP/2;
    public static final float DEFAULT_HYSTERESIS_DOWN = DEFAULT_SENSITIVITY_DOWN/2;
    public static final float DEFAULT_ZERO_POINT_X= 0;
    public static final float DEFAULT_ZERO_POINT_Y = 0;
    public static final int DEFAULT_ROW_LENGTH = 5;
    public static final int DEFAULT_SAME_ATTRIBUTES = 2;
    public static final int DEFAULT_ROUND_AMOUNT = 7;
    public static final int DEFAULT_STARTING_DIFFICULTY = 7;
    public static final boolean DEFAULT_ASSETS = false;
    public static final boolean DEFAULT_USE_DIFFICULTY = false;
    public static final boolean DEFAULT_INCREASING_DIFFICULTY = false;
    public static final int DEFAULT_WAITING_TIME = 3;
    public static final int DEFAULT_FACE_SHOWN = 5;



    public Face getChosenCriminal() {
        return chosenCriminal;
    }

    public void setChosenCriminal(Face criminal) {
        this.chosenCriminal = criminal;
    }

    public Face getCorrectCriminal() {
        return correctCriminal;
    }

    public void setCorrectCriminal(Face criminal) {
        correctCriminal = criminal;
    }
}
