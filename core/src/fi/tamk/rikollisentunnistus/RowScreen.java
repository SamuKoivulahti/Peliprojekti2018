package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

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

    Skin mySkin;

    int row_height;
    int col_width;
    float width;
    float height;

    float rowLength;

    public RowScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        win = false;
        lose = false;

        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        winText = new TextActor("Right, you won!");
        loseText = new TextActor("Wrong, you lose.");
        winText.setPosition(550,600);
        loseText.setPosition(550,600);

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        buttonBack();
        Gdx.app.log("RowScreen", "constructor");

    }

    public void buttonBack() {
        Button back = new TextButton("<--",mySkin,"small");
        back.setSize(col_width*2,row_height);
        back.setPosition(0,camera.viewportHeight - back.getHeight());
        back.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("TAG", "back");
                game.resetAll();
                MainScreen MainScreen = new MainScreen(game);
                game.setScreen(MainScreen);
            }
        });
        stage.addActor(back);
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
        Gdx.input.setInputProcessor(stage);
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

        Gdx.app.log("Select", "selected");
    }

    public void cancel() {
        stage.clear();
        game.resetAll();
        Gdx.app.log("Cancel", "cancelled");
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || game.controls.moveRight(false)) {
            moveRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || game.controls.moveLeft(false)) {
            moveLeft();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || game.controls.moveUp(true)) {
            select();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || game.controls.moveDown(true)) {
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.clear();
        mySkin.dispose();
    }
}
