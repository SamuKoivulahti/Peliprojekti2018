package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
