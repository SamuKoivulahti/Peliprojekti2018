package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class Face extends Actor {
    private Texture baseImg;
    private TextureRegion noseImg;
    private TextureRegion eyesImg;
    private String idCode;

    boolean active;

    public Face(TextureRegion[] noses, TextureRegion[] eyes) {
        baseImg = new Texture("base.png");

        int randomEyes = MathUtils.random(0, eyes.length - 1);
        int randomNose = MathUtils.random(0, noses.length - 1);

        idCode = randomEyes + "-" + randomNose;

        eyesImg = eyes[randomEyes];
        noseImg = noses[randomNose];

        active = false;
    }

    public void setLocation(float x, float y) {
        this.setBounds(x,y,baseImg.getWidth(),baseImg.getHeight());
    }

    public void toggleActive() {
        active = !active;
    }

    public String getIdCode() {
        return idCode;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(baseImg,getX(),getY());
        batch.draw(eyesImg,
                getX() + (baseImg.getWidth() - eyesImg.getRegionWidth())/2,
                getY() + 250f);
        batch.draw(noseImg,
                getX() + (baseImg.getWidth() - noseImg.getRegionWidth())/2,
                getY() + 200f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
