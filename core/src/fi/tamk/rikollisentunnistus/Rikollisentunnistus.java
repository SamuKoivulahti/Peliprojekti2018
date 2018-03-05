package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rikollisentunnistus extends Game {
    CriminalScreen criminalScreen;
    RowScreen rowScreen;

	SpriteBatch batch;
	
	@Override
	public void create () {
        criminalScreen = new CriminalScreen(this);
        rowScreen = new RowScreen(this);

		batch = new SpriteBatch();
		setCriminalScreen();
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
	    int rightId = criminal.getIdCode();
	    Face[] criminals = new Face[5];

	    criminals[0] = criminal;
	    criminals[1] = new Face(new Texture("testinaama2.png"), 2);
        criminals[2] = new Face(new Texture("testinaama3.png"), 3);
        criminals[3] = new Face(new Texture("testinaama4.png"), 4);
        criminals[4] = new Face(new Texture("testinaama5.png"), 5);

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
