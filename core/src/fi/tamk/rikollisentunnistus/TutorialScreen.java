package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @author Essi Supponen
 * @version 1.6
 * @since 2018-05-07
 *
 * Plays tutorial.
 */

public class TutorialScreen implements Screen {
    Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Stage stage;
    Skin mySkin;

    float height;
    float width;

    private Label text;
    private Image dialogBoxImage;
    private Label helpText;

    private Face criminal;
    private Image criminalFrame;
    private Image criminalText;
    private Image backgroundImageCriminal;
    float timeWaiting;

    private Label waitingTimeText;

    private Face[] criminalRow;
    private String rightSuspectID;
    private Image backgroundImageChoosing;
    private Image deskImage;
    private Image spotlightImage;

    Texture selectionBar;
    Animation<TextureRegion> animation;

    float elapsedTime;

    private int explainingRound;

    private final int SHOWING = 1;
    private final int WAITING = 2;
    private final int EXPLAINING = 3;
    private final int CHOOSING = 4;
    private final int READY = 5;

    int mode;

    public TutorialScreen(Rikollisentunnistus host) {
        this.game = host;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        height = camera.viewportHeight;
        width = camera.viewportWidth;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));
        text = new Label("", mySkin);
        helpText = new Label(host.texts.get(49), mySkin);
        helpText.setPosition(width/2 - helpText.getWidth()/2, helpText.getHeight()/2);

        dialogBoxImage = new Image(new Texture("cutscenes/dialogbox.png"));
        dialogBoxImage.setPosition(width/2 - dialogBoxImage.getWidth()/2,height/60f);

        prepareCriminalPart();

        stage.addActor(dialogBoxImage);
        stage.addActor(text);
        stage.addActor(helpText);

        explainingRound = 0;

        setText(game.script.get("scene1_line4"));
        mode = SHOWING;
    }

    /**
     * Makes own "criminalScreen" for single use.
     *
     * Takes a lot of assets and code for a single use "criminalScreen".
     */
    private void prepareCriminalPart() {
        backgroundImageCriminal = new Image(new Texture("criminalscreenbackground.jpg"));
        backgroundImageCriminal.setBounds(0,0,width,height);

        RowConstructor rowConstructor = new RowConstructor();

        criminalRow = rowConstructor.makeRowDifficulty(0);
        criminal = criminalRow[0];
        rightSuspectID = criminal.getIdCode();

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

        timeWaiting = 2.5f;

        waitingTimeText = new Label("" + (timeWaiting), mySkin, "big");
        waitingTimeText.setPosition(width/2 - waitingTimeText.getWidth()/2,
                height/2 - waitingTimeText.getHeight()/2);
        waitingTimeText.setVisible(false);
        waitingTimeText.setAlignment(Align.center);

        stage.addActor(backgroundImageCriminal);
        stage.addActor(waitingTimeText);
        stage.addActor(criminalFrame);
        stage.addActor(criminalText);
        stage.addActor(criminal);
    }

    /**
     * Makes own "rowScreen" for single use.
     *
     * Takes a lot of assets and code for a single use "rowScreen".
     */
    private void setChoosingPart() {
        stage.clear();

        backgroundImageChoosing = new Image(new Texture("rowscreentausta.png"));
        backgroundImageChoosing.setPosition(0,0);
        backgroundImageChoosing.setSize(camera.viewportWidth, camera.viewportHeight);

        spotlightImage = new Image(new Texture("selectionspotlight.png"));
        deskImage = new Image(new Texture("rowscreendesk.png"));
        deskImage.setPosition(0,0);
        deskImage.setSize(width, height/8);

        selectionBar = new Texture("selectionBarYellowToGreen.png");

        TextureRegion[] animationFrames = new TextureRegion[26];

        for (int i = 0; i < 26; i++) {
            int length = selectionBar.getHeight() / animationFrames.length;
            animationFrames[i] = new TextureRegion(selectionBar, 0, i * length, selectionBar.getWidth(), length);
        }

        animation = new Animation<TextureRegion>(game.controls.timerUp / 26, animationFrames);

        stage.addActor(backgroundImageChoosing);
        stage.addActor(spotlightImage);
        stage.addActor(deskImage);
        setCriminals();
        stage.addActor(dialogBoxImage);
        stage.addActor(text);
        stage.addActor(helpText);

        setText(game.script.get("scene1_line6"));
    }

    /**
     * Sets criminals to "rowScreen".
     */
    private void setCriminals() {
        float separation = width / 5;
        float xCrd = separation*1.5f;
        int startpoint = 1;

        spotlightImage.setPosition(xCrd + startpoint*separation - spotlightImage.getWidth()/2, 0);
        spotlightImage.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.5f), Actions.delay(0)));

        float scale = separation / criminalRow[0].getSpriteWidth();

        for (Face criminal : criminalRow) {
            criminal.clearActions();
            criminal.setLocation(xCrd - width, height/8);

            MoveToAction moveToScreen = new MoveToAction();
            moveToScreen.setDuration(1.5f);
            moveToScreen.setPosition(xCrd, height/8);
            criminal.addAction(moveToScreen);

            stage.addActor(criminal);

            criminal.changeScale(scale);

            xCrd = xCrd + separation;
        }

        criminalRow[startpoint].toggleActive();
    }

    /**
     * Changes explaining textlines forward.
     *
     * @param round
     */
    private void explain(int round) {
        if (round == 0) {
            setText(game.script.get("scene1_line7"));
            explainingRound++;
        } else if (round == 1) {
            setText(game.script.get("scene1_line8"));
            explainingRound++;
        } else if (round == 2) {
            setText(game.script.get("scene1_line9"));
            helpText.setVisible(false);
            mode = CHOOSING;
        }
    }

    /**
     * Sets given string.
     *
     * @param string
     */
    private void setText(String string) {
        text.setText(string);
        text.setPosition(width/2-dialogBoxImage.getWidth()*24/50,dialogBoxImage.getHeight()*1/2f + height/60f);
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);
    }

    private boolean timer(float timeToPass) {
        return timeToPass <= elapsedTime;
    }

    /**
     * Moves selection right if possible.
     */
    public void moveRight() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help <= criminalRow.length - 2) {
            SoundManager.playChangeSelectionSound(game.soundEffectsOn);
            ScaleToAction scaleDown = new ScaleToAction();
            scaleDown.setScale(1.0f);
            scaleDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(scaleDown);
            criminalRow[help].toggleActive();
            criminalRow[help + 1].toggleActive();
        }
    }

    /**
     * Moves selection left if possible.
     */
    public void moveLeft() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help >= 1) {
            SoundManager.playChangeSelectionSound(game.soundEffectsOn);
            ScaleToAction scaleDown = new ScaleToAction();
            scaleDown.setScale(1.0f);
            scaleDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(scaleDown);
            criminalRow[help].toggleActive();
            criminalRow[help - 1].toggleActive();
        }
    }

    /**
     * Checks if selected criminal was the right one.
     */
    private void select() {
        String selectedID = "";
        for (Face criminal : criminalRow) {
            if (criminal.active) {
                game.gameData.setChosenCriminal(criminal);
                selectedID = criminal.getIdCode();
                break;
            }
        }

        if (selectedID.equals(rightSuspectID)) {
            setText(game.script.get("scene1_line10right"));
            helpText.setVisible(true);
            helpText.toFront();
            mode = READY;
        } else {
            setText(game.script.get("scene1_line10wrong"));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        /**
         * Shows the criminal.
         */
        if (mode == SHOWING) {

            if (timer(0.5f) && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
                setText(game.script.get("scene1_line5"));
                elapsedTime = 0;
                mode = WAITING;
                waitingTimeText.setVisible(true);
                helpText.setVisible(false);
            }
        }

        /**
         * Waits for the rowScreen.
         */
        if (mode == WAITING) {
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

            waitingTimeText.setText("" + (int)(timeWaiting - elapsedTime +1));

            if (timer(timeWaiting)) {
                setChoosingPart();
                helpText.setVisible(true);
                helpText.toFront();
                mode = EXPLAINING;
            }
        }

        /**
         * Explains the controls of rowScreen.
         */
        if (mode == EXPLAINING) {
            if (timer(0.5f) && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
                elapsedTime = 0;
                explain(explainingRound);
            }
        }

        /**
         * Lets player choose the suspect.
         */
        if (mode == CHOOSING) {
            if (game.settingsScreen.horizontalAxis.isChecked()) {
                if (game.controls.accelerometerY() > game.controls.hysteresisUp) {
                    game.gameData.setStillLeaning(true);
                }
            } else {
                if (game.controls.accelerometerY() < game.controls.hysteresisUp) {
                    game.gameData.setStillLeaning(true);
                }
            }

            if (((game.controls.elapsedTimeU != 0)
                    && !game.controls.isAbleMoveUp && game.gameData.getStillLeaning())
                    || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (game.controls.elapsedTimeU > elapsedTime) {
                    elapsedTime += delta;
                }

                if (!SoundManager.isSelectionBarSoundPlaying()) {
                    SoundManager.playSelectionBarSound(game.soundEffectsOn);
                }

                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    elapsedTime += delta;
                }
            } else if (game.controls.elapsedTimeU == 0
                    || !Gdx.input.isKeyPressed(Input.Keys.UP)) {
                SoundManager.stopSelectionBarSound(game.soundEffectsOn);
                elapsedTime = 0;
            }

            for (Face criminal : criminalRow) {
                if (criminal.active && !criminal.hasActions()) {
                    MoveToAction moveSpotlight = new MoveToAction();
                    moveSpotlight.setPosition(criminal.getX() - spotlightImage.getWidth()/2, 0);
                    moveSpotlight.setDuration(0.5f);
                    spotlightImage.addAction(moveSpotlight);


                    ScaleToAction scaleUp = new ScaleToAction();
                    scaleUp.setScale(1.3f);
                    scaleUp.setDuration(0.5f);
                    criminal.addAction(scaleUp);

                    if (delta != 0) {
                        criminal.toFront();
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                    game.controls.moveRight(true)) {
                moveRight();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                    game.controls.moveLeft(true)) {
                moveLeft();
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.UP) && elapsedTime >= game.controls.timerUp)
                    || (game.controls.moveUp(true) && game.gameData.getStillLeaning())) {
                SoundManager.stopSelectionBarSound(game.soundEffectsOn);
                select();
            }

            dialogBoxImage.toFront();
            text.toFront();
            helpText.toFront();

            game.batch.begin();
            TextureRegion frame = animation.getKeyFrame(elapsedTime, false);
            game.batch.draw(
                    frame,
                    (width - frame.getRegionWidth()) / 2,
                    height - frame.getRegionHeight()*2,
                    frame.getRegionWidth(),
                    frame.getRegionHeight()
            );
            game.batch.end();
        }

        /**
         * Changes screen back to cutscene.
         */
        if (mode == READY) {
            if (timer(0.5f) && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
                game.setScreen(game.cutsceneScreen);
            }
        }

        elapsedTime += delta;
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
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}