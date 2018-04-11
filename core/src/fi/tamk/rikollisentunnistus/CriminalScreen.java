package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class CriminalScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private boolean status;

    private Stage stage;
    private Face criminal;
    private Image criminalFrame;

    private Label waitingTimeText;

    final boolean SHOWING = true;
    final boolean WAITING = false;
    float elapsedTime = 0;

    float width;
    float height;

    float timeShown;
    float timeWaiting;
    Skin mySkin;
    Settings settings;



    public CriminalScreen(Rikollisentunnistus game, Face rightCriminal) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        status = SHOWING;
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        height = camera.viewportHeight;
        width = camera.viewportWidth;

        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        criminal = rightCriminal;
        criminal.changeScale(0.7f);
        criminal.setLocation(width/2, -height);
        MoveToAction moveUp = new MoveToAction();
        moveUp.setPosition(width/2, height/6);
        moveUp.setDuration(0.6f);
        criminal.addAction(moveUp);

        criminalFrame = new Image(new Texture("criminalframe.jpg"));
        criminalFrame.setPosition((width - criminalFrame.getWidth())/2, -height - 79);
        MoveToAction frameMoveUp = new MoveToAction();
        frameMoveUp.setPosition((width - criminalFrame.getWidth())/2, height/6 - 79);
        frameMoveUp.setDuration(0.6f);
        criminalFrame.addAction(frameMoveUp);
        settings = Settings.getInstance();

        timeShown = settings.getInteger("faceShown", GameData.DEFAULT_FACE_SHOWN);
        timeWaiting = settings.getInteger("waitingTime", GameData.DEFAULT_WAITING_TIME);

        waitingTimeText = new Label("" + (timeWaiting), mySkin, "big");
        waitingTimeText.setPosition(width/2 - waitingTimeText.getWidth()/2, height/2 - waitingTimeText.getHeight()/2);
        waitingTimeText.setVisible(false);
        waitingTimeText.setAlignment(Align.center);

        stage.addActor(waitingTimeText);
        stage.addActor(criminalFrame);
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
            timeShown = settings.getInteger("faceShown", GameData.DEFAULT_FACE_SHOWN);
            timeWaiting = settings.getInteger("waitingTime", GameData.DEFAULT_WAITING_TIME);
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
        Gdx.input.setCatchBackKey(true);
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
            move.setPosition(width/2, -height);
            move.setDuration(0.6f);
            criminal.addAction(move);

            MoveToAction moveFrame = new MoveToAction();
            moveFrame.setPosition((width - criminalFrame.getWidth())/2, -height - 80);
            moveFrame.setDuration(0.6f);
            criminalFrame.addAction(moveFrame);

            if (timer(timeWaiting)) {
                elapsedTime = 0;
                criminal.setVisible(true);
                criminal.clearActions();
                game.setRowScreen();
                game.setCriminals();
            }
            waitingTimeText.setText("" + (int)(timeWaiting - elapsedTime +1));
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
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}