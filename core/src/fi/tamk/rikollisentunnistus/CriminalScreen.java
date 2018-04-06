package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

    private Label waitingTimeText;

    final boolean SHOWING = true;
    final boolean WAITING = false;
    float elapsedTime = 0;

    float width;
    float height;

    float timeShown;
    float timeWaiting;
    Skin mySkin;



    public CriminalScreen(Rikollisentunnistus g, Face rightCriminal) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        status = SHOWING;
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        height = camera.viewportHeight;
        width = camera.viewportWidth;

        stage = new Stage(new FitViewport(camera.viewportWidth,camera.viewportHeight));
        criminal = rightCriminal;
        criminal.setLocation(width/2, -height/2);
        MoveToAction moveUp = new MoveToAction();
        moveUp.setPosition(width/2, height/4);
        moveUp.setDuration(0.4f);
        criminal.addAction(moveUp);

        timeShown = 5f;
        timeWaiting = 3f;

        waitingTimeText = new Label("" + (timeWaiting), mySkin, "big");
        waitingTimeText.setPosition(width/2 - waitingTimeText.getWidth()/2, height/2 - waitingTimeText.getHeight()/2);
        waitingTimeText.setVisible(false);

        stage.addActor(waitingTimeText);
        stage.addActor(criminal);
    }

    public void updateWaitingTimes() {
        if (game.useDifficulty) {
            float timeToShowAtMost = 5;
            float timeToShowAtLeast = 1;
            float timeToWaitAtMost = 8;
            float timeToWaitAtLeast = 1;

            timeShown = timeToShowAtMost - (game.difficulty*(timeToShowAtMost - timeToShowAtLeast))/14;
            timeWaiting = timeToWaitAtLeast + (game.difficulty*(timeToWaitAtMost - timeToWaitAtLeast))/14;
        } else {
            timeShown = 5f;
            timeWaiting = 3f;
        }

        Gdx.app.log("Time shown:", " " + timeShown );
        Gdx.app.log("Time waiting:", " " + timeWaiting);
    }

    public boolean timer(float timeToPass) {
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

        stage.act();
        stage.draw();
        game.batch.begin();

        if (status == SHOWING) {
            //Gdx.app.log("Criminal Screen", "Showing");

            if (timer(timeShown)) {
                status = WAITING;
                elapsedTime = 0;
            }

            criminal.setVisible(true);

        } else if (status == WAITING) {
            waitingTimeText.setVisible(true);

            MoveToAction move = new MoveToAction();
            move.setPosition(width/2, height/2);
            move.setDuration(0.4f);

            criminal.addAction(move);

            if (timer(timeWaiting)) {
                elapsedTime = 0;
                criminal.setVisible(true);
                criminal.clearActions();
                game.setRowScreen();
                game.setCriminals();
            }
            waitingTimeText.setText("" + (int)(timeWaiting - elapsedTime));
            //Gdx.app.log("Criminal Screen", "Waiting");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == SHOWING) {
            status = WAITING;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == WAITING) {
            game.setRowScreen();
            criminal.setVisible(true);
            criminal.clearActions();
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