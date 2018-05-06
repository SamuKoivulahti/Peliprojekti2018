package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Touchable;

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
        game.profile1Button.setTouchable(Touchable.enabled);
        game.profile2Button.setTouchable(Touchable.enabled);
        game.profile3Button.setTouchable(Touchable.enabled);
        game.back.setTouchable(Touchable.enabled);

    }

    public String getText() {
        return newText == null ? "" : newText.toUpperCase();
    }
}
