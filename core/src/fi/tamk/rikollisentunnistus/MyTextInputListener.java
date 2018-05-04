package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Input;

/**
 * Created by Koivulahti on 4.5.2018.
 */

public class MyTextInputListener implements Input.TextInputListener {
    String newText, ID;
    SaveFileSelectScreen game;

    public MyTextInputListener(SaveFileSelectScreen game, String ID) {
        this.game = game;
        this.ID = ID;
    }

    @Override
    public void input (String text) {
        System.out.println(text);
        newText = text;
        game.InputIsValitaded = ID;
    }

    @Override
    public void canceled () {
        game.profile1Button.setDisabled(false);
        game.profile2Button.setDisabled(false);
        game.profile3Button.setDisabled(false);
        game.back.setDisabled(false);

    }

    public String getText() {
        return newText == null ? "" : newText.toUpperCase();
    }
}
