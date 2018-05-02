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
    int profileUsed;

   /**
    * getter for points
    */
    public int getPoints() {
        return points;
    }

   /**
    * setter for points
    */
    public void setPoints(int points) {
        this.points = points;
    }

   /**
    * getter for win
    */
    public boolean getWin() {
        return win;
    }

   /**
    * setter for win
    */
    public void setWin(boolean win) {
        this.win = win;
    }

   /**
    * getter for level
    */
    public int getLevel() {
        return level;
    }

   /**
    * setter for level
    */
    public void setLevel(int level) {
        this.level = level;
    }

   /**
    * getter for stillLeaning
    */
    public boolean getStillLeaning() {
        return stillLeaning;
    }

   /**
    * setter for stillLeaning
    */
    public void setStillLeaning(boolean stillLeaning) {
        this.stillLeaning = stillLeaning;
    }

    /**
     * getter for ChosenCriminal
     */
    public Face getChosenCriminal() {
        return chosenCriminal;
    }

    /**
     * setter for ChosenCriminal
     */
    public void setChosenCriminal(Face criminal) {
        this.chosenCriminal = criminal;
    }

    /**
     * getter for CorrectCriminal
     */
    public Face getCorrectCriminal() {
        return correctCriminal;
    }

    /**
     * setter for CorrectCriminal
     */
    public void setCorrectCriminal(Face criminal) {
        correctCriminal = criminal;
    }

    public int getProfileUsed() {
        return  profileUsed;
    }

    public void setProfileUsed(int profileUsed) {
        this.profileUsed = profileUsed;
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
    public static final float DEFAULT_ZERO_POINT_Z = 0;
    public static final int DEFAULT_ROW_LENGTH = 5;
    public static final int DEFAULT_SAME_ATTRIBUTES = 2;
    public static final int DEFAULT_ROUND_AMOUNT = 7;
    public static final int DEFAULT_STARTING_DIFFICULTY = 7;
    public static final boolean DEFAULT_ASSETS = false;
    public static final boolean DEFAULT_USE_DIFFICULTY = false;
    public static final boolean DEFAULT_INCREASING_DIFFICULTY = false;
    public static final int DEFAULT_WAITING_TIME = 3;
    public static final int DEFAULT_FACE_SHOWN = 5;
    public static final float DEFAULT_VOLUME = 50;
    public static final boolean DEFAULT_SOUND_EFFECTS = true;
    public static final float DEFAULT_TIMER_SIDES = 0.25f;
    public static final float DEFAULT_TIMER_UP = 3f;
    public static final float DEFAULT_TIMER_DOWN = 1f;
    public static final int DEFAULT_POINTS = 0;
    public static final int DEFAULT_LEVEL = 1;
    public static final boolean DEFAULT_HORIZONTAL_AXIS = false;
    public static final boolean DEFAULT_USE_CHAIR = false;
}
