package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
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
    float accelX;
    float accelY;
    float accelZ;
    boolean overLR;
    boolean overUD;
    float moveR;
    float moveL;
    float moveU;
    float moveD;
    float hysteresisR;
    float hysteresisL;
    float hysteresisU;
    float hysteresisD;

    public RowScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);

        stage = new Stage(new FitViewport(camera.viewportWidth,camera.viewportHeight));

        overLR = false;
        overUD = false;

        // Later set by users input
        moveR = 3;
        moveL = -3;
        moveU = -3;
        moveD = 3;
        hysteresisR = 1.5f;
        hysteresisL = -1.5f;
        hysteresisU = -1.5f;
        hysteresisD = 1.5f;

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

        criminalRow[2].toggleActive();
    }

    @Override
    public void show() {

    }

    public void moveSelectionRight() {
        int help = 0;

        for (int i = 0; i < 5; i++) {
            if (criminalRow[i].active) {
                help = i;
            }
        }

        if (help <= 3) {
            MoveToAction moveDown = new MoveToAction();
            moveDown.setPosition(criminalRow[help].getX(), 0);
            moveDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(moveDown);
            criminalRow[help].toggleActive();
            criminalRow[help + 1].toggleActive();
        }
    }

    public void moveSelectionLeft() {
        int help = 0;

        for (int i = 0; i < 5; i++) {
            if (criminalRow[i].active) {
                help = i;
            }
        }

        if (help >= 1) {
            MoveToAction moveDown = new MoveToAction();
            moveDown.setPosition(criminalRow[help].getX(), 0);
            moveDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(moveDown);
            criminalRow[help].toggleActive();
            criminalRow[help - 1].toggleActive();
        }
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();

        game.batch.begin();

        for (Face criminal : criminalRow) {
            if (criminal.active && !criminal.hasActions()) {
                MoveToAction moveUp = new MoveToAction();
                moveUp.setPosition(criminal.getX(), 100);
                moveUp.setDuration(0.5f);
                criminal.addAction(moveUp);
            }
        }

        stage.act();
        stage.draw();

        if (accelY >= moveR && !overLR) {
            overLR = true;
            moveSelectionRight();
        } else if (accelY <= moveL && !overLR) {
            overLR = true;
            moveSelectionLeft();
        }

        if (accelY < hysteresisR && accelY > hysteresisL) {
            overLR = false;
        }

        if (accelX <= moveU && !overUD) {
            overUD = true;
        } else if (accelX >= moveD && !overUD) {
            overUD = true;
        }

        if (accelX > hysteresisU && accelX < hysteresisD) {
            overUD = false;
        }




        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            moveSelectionRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            moveSelectionLeft();
        }

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
