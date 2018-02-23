package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class Face extends Actor {
    private Texture spriteImage;
    private int idCode;                 // muutetaan myöhemmin stringiksi
    private boolean active;

    public Face(Texture img, int id) {
        super();

        spriteImage = img;
        idCode = id;
        active = false;

        this.setBounds(250,40,spriteImage.getWidth(),spriteImage.getHeight());
    }

    public void toggleActive() {
        active = !active;
    }

    public int getIdCode() {
        return idCode;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(spriteImage,getX(),getY(),getWidth(),getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
