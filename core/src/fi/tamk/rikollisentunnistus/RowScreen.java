package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

// change

public class RowScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Face[] criminalRow;
    private int rightSuspectID;
    private Stage stage;

    public RowScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);

        stage = new Stage(new FitViewport(camera.viewportWidth,camera.viewportHeight));
    }

    public void setCriminals(Face[] criminals, int suspectID) {
        criminalRow = criminals;
        rightSuspectID = suspectID;
        float xCrd = 0;

        for (Face criminal : criminalRow) {
            criminal.setLocation(xCrd, 0);
            stage.addActor(criminal);

            xCrd = xCrd + 230;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        stage.act();
        stage.draw();
        Gdx.app.log("Row Screen", "ok");

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
