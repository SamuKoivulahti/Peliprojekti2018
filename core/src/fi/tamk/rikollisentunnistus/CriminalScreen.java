package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by pohja on 23/02/2018.
 */

public class CriminalScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private boolean status;

    final boolean SHOWING = true;
    final boolean WAITING = false;

    public CriminalScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        status = SHOWING;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        if (status == SHOWING) {
            Gdx.app.log("Criminal Screen","Showing");
        } else if (status == WAITING) {
            Gdx.app.log("Criminal Screen","Waiting");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == SHOWING) {
            status = WAITING;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == WAITING) {
            game.setRowScreen();
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
