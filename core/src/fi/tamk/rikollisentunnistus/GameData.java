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
