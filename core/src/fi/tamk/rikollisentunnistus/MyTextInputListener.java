package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * @author Samu Koivulahti
 * @version 1.6
 * @since 4.5.2018
 *
 * Gets users text input
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

    /**
     * Gets the input value and shortens it if its over 8 char long
     * @return players input
     */
    public String getText() {
        if (newText.length() > 12) {
            return  newText == null ? "" : newText.substring(0, 12);
        } else {
            return newText == null ? "" : newText;
        }
    }
}
