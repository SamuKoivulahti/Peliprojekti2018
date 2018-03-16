package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class RowScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Face[] criminalRow;
    private String rightSuspectID;
    private Stage stage;

    boolean win;
    boolean lose;

    private TextActor winText;
    private TextActor loseText;


    public RowScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);

        stage = new Stage(new FitViewport(camera.viewportWidth,camera.viewportHeight));
        win = false;
        lose = false;

        winText = new TextActor("Right, you won!");
        loseText = new TextActor("Wrong, you lose.");
        winText.setPosition(550,600);
        loseText.setPosition(550,600);

    }

    public void setCriminals(Face[] criminals, String suspectID) {
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
        Controls.currentScreen = this;
    }

    public void select() {
        String selectedID = "";

        for (Face criminal : criminalRow) {
            if (criminal.active) {
                selectedID = criminal.getIdCode();
                break;
            }
        }

        if (selectedID.equals(rightSuspectID)) {
            win = true;
            stage.addActor(winText);
        } else {
            lose = true;
            stage.addActor(loseText);
        }
    }

    public void cancel() {
        stage.clear();
        game.resetAll();
    }

    public void moveRight() {
        int help = 0;

        for (int i = 0; i < 5; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
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

    public void moveLeft() {
        int help = 0;

        for (int i = 0; i < 5; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Controls.moveRight(false)) {
            moveRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Controls.moveLeft(false)) {
            moveLeft();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Controls.moveUp(true)) {
            select();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Controls.moveDown(true)) {
            cancel();
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
        Controls.currentScreen = null;
    }

    @Override
    public void dispose() {

    }
}
