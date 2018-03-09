package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rikollisentunnistus extends Game {
    CriminalScreen criminalScreen;
    RowScreen rowScreen;

	SpriteBatch batch;

    TextureRegion[] noseTextures;
    TextureRegion[] eyesTextures;
	
	@Override
	public void create () {
        rowScreen = new RowScreen(this);

		batch = new SpriteBatch();

        makeTextureArrays();

        criminalScreen = new CriminalScreen(this, new Face(noseTextures, eyesTextures));
        setCriminalScreen();
	}

    private void makeTextureArrays() {
        TextureRegion[][] noses = TextureRegion.split(new Texture("noses.png"),
                80, 80);

        TextureRegion[][] eyes = TextureRegion.split(new Texture("eyes.png"),
                156, 66);

        noseTextures = toArrays(noses);
        eyesTextures = toArrays(eyes);
    }

    private TextureRegion[] toArrays(TextureRegion[][] table) {
        TextureRegion[] array = new TextureRegion[table.length * table[0].length];

        int index = 0;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                array[index] = table[i][j];

                index++;
            }
        }

        return array;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setRowScreen() {
        setScreen(rowScreen);
    }

    public void setCriminalScreen() {
        setScreen(criminalScreen);
    }

    public void setCriminals(Face criminal) {
	    String rightId = criminal.getIdCode();
	    Face[] criminals = new Face[5];

	    criminals[0] = criminal;
	    criminals[1] = new Face(noseTextures, eyesTextures);
        criminals[2] = new Face(noseTextures, eyesTextures);
        criminals[3] = new Face(noseTextures, eyesTextures);
        criminals[4] = new Face(noseTextures, eyesTextures);

        criminals = shuffleArray(criminals);
        rowScreen.setCriminals(criminals,rightId);
    }

    public void resetCriminalScreen() {
	    criminalScreen.reset();
    }

    public Face[] shuffleArray(Face[] array) {
        List<Face> list = new ArrayList();
        for (Face i : array) {
            list.add(i);
        }

        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
