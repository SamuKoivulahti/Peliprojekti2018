package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by pohja on 05/03/2018.
 */

public class TextActor extends Actor {
    private BitmapFont font;
    private GlyphLayout text;

    public TextActor(String string) {
        super();
        font = new BitmapFont();
        text = new GlyphLayout(font, string);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        font.draw(batch, text, getX(), getY());
    }
}
