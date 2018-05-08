package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
 * @author Essi Supponen
 * @version 1.6
 * @since 2018-02-23
 *
 * Shows criminal to be memorized and sets RowScreen.
 */

public class CriminalScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private boolean status;

    private Stage stage;
    private Face criminal;
    private Image criminalFrame;
    private Image criminalText;
    private Image backgroundImage;

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

        /**
         * Takes the height and width parameters from the camera.
         */
        height = camera.viewportHeight;
        width = camera.viewportWidth;

        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        backgroundImage = new Image(new Texture("criminalscreenbackground.jpg"));
        backgroundImage.setBounds(0,0,width,height);

        /**
         * Sets the criminal and gives it an action to move to the screen.
         */
        criminal = rightCriminal;
        criminal.changeScale(0.7f);
        criminal.setLocation(width/2, -height);
        MoveToAction moveUp = new MoveToAction();
        moveUp.setPosition(width/2, height/6);
        moveUp.setDuration(0.6f);
        criminal.addAction(moveUp);

        /**
         * Sets criminalFrame and gives it an action to move to the screen. The animation
         * is the same as criminals, but the start and the end points are different.
         */
        criminalFrame = new Image(new Texture("criminalframe.jpg"));
        criminalFrame.setPosition((width - criminalFrame.getWidth())/2, -height - 79);
        MoveToAction frameMoveUp = new MoveToAction();
        frameMoveUp.setPosition((width - criminalFrame.getWidth())/2, height/6 - 79);
        frameMoveUp.setDuration(0.6f);
        criminalFrame.addAction(frameMoveUp);

        if (game.texts.get(0).equals("FI")) {
            criminalText = new Image(new Texture("textimages/fi_criminalscreentext.png"));
        } else {
            criminalText = new Image(new Texture("textimages/en_criminalscreentext.png"));
        }
        criminalText.setPosition((width - criminalText.getWidth())/2, -height - 79);
        MoveToAction textMoveUp = new MoveToAction();
        textMoveUp.setPosition((width - criminalText.getWidth())/2, height/6 - 79);
        textMoveUp.setDuration(0.6f);
        criminalText.addAction(textMoveUp);

        /**
         * Gets timeShown and timeWaiting from prefs-file.
         */
        settings = Settings.getInstance();
        timeShown = settings.getInteger("faceShown", GameData.DEFAULT_FACE_SHOWN);
        timeWaiting = settings.getInteger("waitingTime", GameData.DEFAULT_WAITING_TIME);

        /**
         * Makes label for the timer counting down waiting time.
         */
        waitingTimeText = new Label("" + (timeWaiting), mySkin, "big");
        waitingTimeText.setPosition(width/2 - waitingTimeText.getWidth()/2, height/2 - waitingTimeText.getHeight()/2);
        waitingTimeText.setVisible(false);
        waitingTimeText.setAlignment(Align.center);

        /**
         * Adds actors to the stage.
         */
        stage.addActor(backgroundImage);
        stage.addActor(waitingTimeText);
        stage.addActor(criminalFrame);
        stage.addActor(criminalText);
        stage.addActor(criminal);
    }

    /**
     * Updates times to show the criminal and wait the next screen.
     *
     * If game uses difficulty, counts the times between min and max values. Time to show increases
     * and time to wait decreases steadily when difficulty level rises. If game does not use
     * difficulty, takes the values from preferences-file.
     */
    public void updateWaitingTimes() {
        float timeToShowAtMost = 5;
        float timeToShowAtLeast = 1.25f;
        float timeToWaitAtMost = 8;
        float timeToWaitAtLeast = 1.5f;

        if (game.useDifficulty && game.gameData.getProfileUsed() == 0) {
            timeShown = timeToShowAtMost - (game.difficulty*(timeToShowAtMost - timeToShowAtLeast))/14;
            timeWaiting = timeToWaitAtLeast + (game.difficulty*(timeToWaitAtMost - timeToWaitAtLeast))/14;
        } else if (!game.useDifficulty && game.gameData.getProfileUsed() == 0) {
            timeShown = settings.getInteger("faceShown", GameData.DEFAULT_FACE_SHOWN);
            timeWaiting = settings.getInteger("waitingTime", GameData.DEFAULT_WAITING_TIME);
        } else if (game.gameData.getProfileUsed() != 0) {
            if (game.gameData.getProfileUsed() == 1) {
                timeShown = timeToShowAtMost - (game.saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY)*(timeToShowAtMost - timeToShowAtLeast))/14;
                timeWaiting = timeToWaitAtLeast + (game.saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY)*(timeToWaitAtMost - timeToWaitAtLeast))/14;
            } else if (game.gameData.getProfileUsed() == 2) {
                timeShown = timeToShowAtMost - (game.saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY)*(timeToShowAtMost - timeToShowAtLeast))/14;
                timeWaiting = timeToWaitAtLeast + (game.saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY)*(timeToWaitAtMost - timeToWaitAtLeast))/14;
            } else if (game.gameData.getProfileUsed() == 3) {
                timeShown = timeToShowAtMost - (game.saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY)*(timeToShowAtMost - timeToShowAtLeast))/14;
                timeWaiting = timeToWaitAtLeast + (game.saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY)*(timeToWaitAtMost - timeToWaitAtLeast))/14;
            }
        }


            Gdx.app.log("Time shown:", " " + timeShown );
        Gdx.app.log("Time waiting:", " " + timeWaiting);
    }

    /**
     * Returns true if given time has passed since the function was called for the first time.
     *
     * @param timeToPass
     * @return
     */
    public boolean timer(float timeToPass) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= timeToPass) {
            elapsedTime = 0;
            return true;
        }
        return false;
    }

    /**
     * Resets the screen to default values.
     */
    public void reset() {
        status = SHOWING;
        criminal.setLocation((Gdx.graphics.getWidth()-criminal.getSpriteWidth())/2, 125);
        stage.addActor(criminal);
    }

    @Override
    public void show() {
        SoundManager.playIngameMusic();
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
            /**
             * While status is showing, counts waiting time. When the time is up, changes the
             * status to waiting.
             */

            if (timer(timeShown)) {
                status = WAITING;
                elapsedTime = 0;
                SoundManager.playTimerSound(game.soundEffectsOn);
            }

        } else if (status == WAITING) {
            /**
             * Starts counting down the waiting time and updates it every frame. Gives criminal and
             * the criminalFrame action to move out of screen. When the waiting time is over sets
             * the rowScreen.
             */

            waitingTimeText.setVisible(true);

            MoveToAction move = new MoveToAction();
            move.setPosition(width/2, -height);
            move.setDuration(0.6f);
            criminal.addAction(move);

            MoveToAction moveFrame = new MoveToAction();
            moveFrame.setPosition((width - criminalFrame.getWidth())/2, -height - 80);
            moveFrame.setDuration(0.6f);
            criminalFrame.addAction(moveFrame);

            MoveToAction moveText = new MoveToAction();
            moveText.setPosition((width - criminalText.getWidth())/2, -height - 80);
            moveText.setDuration(0.6f);
            criminalText.addAction(moveText);

            if (timer(timeWaiting)) {
                SoundManager.stopTimerSound();
                elapsedTime = 0;
                criminal.setVisible(true);
                criminal.clearActions();
                game.setRowScreen();
                game.setCriminals();
            }
            waitingTimeText.setText("" + (int)(timeWaiting - elapsedTime +1));
        }

        /**
         * For cheating, shhh...
         */
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == SHOWING) {
            SoundManager.playTimerSound(game.soundEffectsOn);
            status = WAITING;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && status == WAITING) {
            SoundManager.stopTimerSound();
            game.setRowScreen();
            criminal.setVisible(true);
            criminal.clearActions();
            game.setCriminals();
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
        SoundManager.stopTimerSound();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        stage.clear();
    }
}