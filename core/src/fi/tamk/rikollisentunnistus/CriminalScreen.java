package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class CriminalScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private boolean status;

    private Stage stage;
    private Face criminal;

    final boolean SHOWING = true;
    final boolean WAITING = false;
    float elapsedTime = 0;

    int timeShown = 5;
    int timeWaiting = 3;
    int sameAttributes;
    boolean assets;
    int roundAmount;



    public CriminalScreen(Rikollisentunnistus g, Face rightCriminal) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);
        status = SHOWING;

        stage = new Stage(new FitViewport(camera.viewportWidth,camera.viewportHeight));
        criminal = rightCriminal;
        criminal.setLocation(450, 125);

        stage.addActor(criminal);
    }

    public boolean timer(int timeToPass) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= timeToPass) {
            elapsedTime = 0;
            return true;
        }
        return false;
    }

    public void reset() {
        status = SHOWING;
        criminal.setLocation((Gdx.graphics.getWidth()-criminal.getSpriteWidth())/2, 125);
        stage.addActor(criminal);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(25/255f, 25/255f, 100/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        if (status == SHOWING) {
            //Gdx.app.log("Criminal Screen", "Showing");

            stage.act();
            stage.draw();

            if (timer(timeShown)) {
                status = WAITING;
                elapsedTime = 0;
            }

        } else if (status == WAITING) {
            if (timer(timeWaiting)) {
                elapsedTime = 0;
                game.setRowScreen();
                game.setCriminals();
            }
            //Gdx.app.log("Criminal Screen", "Waiting");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == SHOWING) {
            status = WAITING;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == WAITING) {
            game.setRowScreen();
            game.setCriminals();
        }
        /*if (Gdx.input.isTouched() && status == SHOWING) {
            status = WAITING;
        } else if (Gdx.input.isTouched() && status == WAITING) {
            game.setRowScreen();
            game.setCriminals(criminal);
        }*/

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
        dispose();
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}
