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
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    IntermissionScreen InterMissionScreen;

    Texture selectionBar;
    Animation<TextureRegion> animation;

    boolean win;
    boolean lose;

    private Label pointsText;
    private Label levelText;

    private Texture lineUp;
    private Texture desk;
    private Image lineUpImage;
    private Image deskImage;

    Skin mySkin;

    float row_height;
    float col_width;
    float width;
    float height;

    int points;
    int level;
    boolean stillLeaning;

    float elapsedTime;

    boolean letMove;
    float timeToMove;

    Window pauseWindow;
    Window sensitivityWindow;
    Settings settings;

    public RowScreen(Rikollisentunnistus g) {
        Gdx.app.log("RowScreen", "constructor");
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        game.gameData.setWin(false);
        game.gameData.setStillLeaning(false);
        win = false;
        lose = false;
        elapsedTime = 0;


        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        lineUp = new Texture("rowscreentausta.png");
        lineUpImage = new Image(lineUp);
        lineUpImage.setPosition(0,0);
        lineUpImage.setSize(camera.viewportWidth, camera.viewportHeight);

        desk = new Texture("rowscreendesk.png");
        deskImage = new Image(desk);
        deskImage.setPosition(0,0);
        deskImage.setSize(width, height/8);


        points = game.gameData.getPoints();
        level = game.gameData.getLevel();

        game.gameData.setLevel(level + 1);
        pointsText = new Label("Points: " + points, mySkin);
        pointsText.setPosition(camera.viewportWidth / 12 * 11, camera.viewportHeight - pointsText.getHeight()*2);
        levelText = new Label("Level " + game.gameData.getLevel(), mySkin, "big");
        levelText.setPosition(camera.viewportWidth/2 - levelText.getWidth()/2, camera.viewportHeight/12 * 10);

        selectionBar = new Texture("selectionBar.png");
        Gdx.app.log("row", "sele" + selectionBar.toString());

        TextureRegion[] animationFrames = new TextureRegion[10];

        for (int i = 0; i < 10; i++) {
            int length = selectionBar.getHeight() / animationFrames.length;
            animationFrames[i] = new TextureRegion(selectionBar, 0, i * length, selectionBar.getWidth(), length);
        }

        animation = new Animation<TextureRegion>(game.controls.timerTime / 10, animationFrames);

        stage.addActor(lineUpImage);
        stage.addActor(deskImage);
        stage.addActor(pointsText);
        stage.addActor(levelText);
        createPauseWindow();

        letMove = false;
        timeToMove = 0;
    }

    private void createPauseWindow () {
        pauseWindow = new Window("Pause", mySkin);
        pauseWindow.setVisible(false);
        pauseWindow.setResizable(false);
        pauseWindow.setMovable(false);

        TextButton continueButton = new TextButton("Continue", mySkin);
        continueButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    pauseWindow.setVisible(false);
                }
            }
        );

        TextButton exitButton = new TextButton("Exit to Main Menu", mySkin);
        exitButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.resetAll();
                    game.setMainScreen();
                }
            }
        );

        TextButton calibrateButton = new TextButton("Calibrate", mySkin);
        calibrateButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.settingsScreen.setZeroPoint();
                }
            }
        );

        sensitivityWindow = new Window("Sensitivity", mySkin);
        sensitivityWindow.setVisible(false);
        sensitivityWindow.setResizable(false);
        sensitivityWindow.setMovable(false);

        TextButton sensitivityButton = new TextButton("Sensitivity", mySkin);
        sensitivityButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    pauseWindow.setVisible(false);
                    sensitivityWindow.setVisible(true);
                    game.settingsScreen.externalSensitivityWindow(stage);
                    game.settingsScreen.sliderR.setVisible(true);
                    game.settingsScreen.sliderL.setVisible(true);
                    game.settingsScreen.sliderU.setVisible(true);
                    game.settingsScreen.sliderD.setVisible(true);
                    game.settingsScreen.sensitivityGraphImage.setVisible(true);
                    game.settingsScreen.sliderR.setPosition(width/5,height/2 - game.settingsScreen.sliderR.getHeight()/2);
                    game.settingsScreen.sliderL.setPosition(width/5 - game.settingsScreen.sliderSize,height/2 - game.settingsScreen.sliderL.getHeight()/2);
                    game.settingsScreen.sliderU.setPosition(width/5 - game.settingsScreen.sliderU.getWidth()/2,height/2);
                    game.settingsScreen.sliderD.setPosition(width/5 - game.settingsScreen.sliderD.getWidth()/2,height/2 - game.settingsScreen.sliderSize);
                    game.settingsScreen.sensitivityGraphImage.setPosition(width/5-game.settingsScreen.sliderSize, height/2 - game.settingsScreen.sliderSize);
                }
            }
        );

        TextButton saveButton = new TextButton("Save", mySkin);
        saveButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    settings = Settings.getInstance();
                    settings.setFloat("sensitivityRight", game.settingsScreen.valueRight);
                    settings.setFloat("sensitivityLeft", game.settingsScreen.valueLeft);
                    settings.setFloat("sensitivityUp", game.settingsScreen.valueUp);
                    settings.setFloat("sensitivityDown", game.settingsScreen.valueDown);
                    game.controls.hysteresisRight = game.settingsScreen.valueRight/2;
                    game.controls.hysteresisLeft = game.settingsScreen.valueLeft/2;
                    game.controls.hysteresisUp = game.settingsScreen.valueUp/2;
                    game.controls.hysteresisDown = game.settingsScreen.valueDown/2;
                    settings.saveSettings();
                    pauseWindow.setVisible(true);
                    sensitivityWindow.setVisible(false);
                    game.settingsScreen.sliderR.setVisible(false);
                    game.settingsScreen.sliderL.setVisible(false);
                    game.settingsScreen.sliderU.setVisible(false);
                    game.settingsScreen.sliderD.setVisible(false);
                    game.settingsScreen.sensitivityGraphImage.setVisible(false);
                }
            }
        );

        TextButton cancelButton = new TextButton("Cancel", mySkin);
        cancelButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    pauseWindow.setVisible(true);
                    sensitivityWindow.setVisible(false);
                    game.settingsScreen.sliderR.setVisible(false);
                    game.settingsScreen.sliderL.setVisible(false);
                    game.settingsScreen.sliderU.setVisible(false);
                    game.settingsScreen.sliderD.setVisible(false);
                    game.settingsScreen.sensitivityGraphImage.setVisible(false);

                }
            }
        );

        pauseWindow.add(continueButton).pad(10).row();
        pauseWindow.add(calibrateButton).pad(10).row();
        pauseWindow.add(sensitivityButton).pad(10).row();
        pauseWindow.add(exitButton).pad(10, 10, 20, 10);
        pauseWindow.pack();
        pauseWindow.setPosition((width - pauseWindow.getWidth()) / 2, (height - pauseWindow.getHeight()) / 2);


        sensitivityWindow.add(saveButton).pad(10).row();
        sensitivityWindow.add(cancelButton).pad(10).row();
        sensitivityWindow.pack();
        sensitivityWindow.setPosition((width - sensitivityWindow.getWidth()) / 2, (height - sensitivityWindow.getHeight()) / 2);

        stage.addActor(pauseWindow);
        stage.addActor(sensitivityWindow);
    }

    public void setCriminals(Face[] criminals, String suspectID) {
        criminalRow = criminals;
        rightSuspectID = suspectID;
        float separationEven = width / 6;
        float separationOdd = width / 5;
        float separation = separationEven;
        float xCrd = 20;
        int startpoint = 1;

        if (criminalRow.length == 3) {
            xCrd = separationOdd*1.5f;
            separation = separationOdd;
            startpoint = 1;
        } else if (criminalRow.length == 4) {
            xCrd = separationOdd;
            separation = separationOdd;
            startpoint = 1;
        } else if (criminalRow.length == 5) {
            xCrd = separationEven;
            separation = separationEven;
            startpoint = 2;
        } else if (criminalRow.length == 6) {
            xCrd = separationEven*0.5f;
            separation = separationEven;
            startpoint = 2;
        }

        if (criminalRow.length == 5 || criminalRow.length == 6) {
            float scale = separationEven / criminalRow[0].getSpriteWidth();
            System.out.println(criminalRow[0].getSpriteWidth());

            for (Face criminal : criminalRow) {
                criminal.changeScale(scale);
            }
        } else {
            float scale = separationOdd / criminalRow[0].getSpriteWidth();
            System.out.println(criminalRow[0].getSpriteWidth());

            for (Face criminal : criminalRow) {
                criminal.changeScale(scale);
            }
        }

        for (Face criminal : criminalRow) {
            criminal.setLocation(xCrd - width, height/8);

            MoveToAction moveToScreen = new MoveToAction();
            moveToScreen.setDuration(1.5f);
            moveToScreen.setPosition(xCrd, height/8);
            criminal.addAction(moveToScreen);

            stage.addActor(criminal);

            xCrd = xCrd + separation;
        }

        criminalRow[startpoint].toggleActive();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    public void select() {
        String selectedID = "";
        for (Face criminal : criminalRow) {
            if (criminal.active) {
                game.gameData.setChosenCriminal(criminal);
                selectedID = criminal.getIdCode();
                break;
            }
        }

        System.out.println(rightSuspectID);
        System.out.println(selectedID);

        Gdx.app.log("Select", "selected");
        if (selectedID.equals(rightSuspectID)) {
            win = true;
            game.gameData.setPoints(points + 1);
            game.gameData.setWin(true);

        } else {
            lose = true;
        }

        if (game.useDifficulty && game.increasingDifficulty) {
            game.difficulty++;
        }

        setInterMissionScreen();
    }

    public void cancel() {
        stage.clear();
        game.resetAll();
        MainScreen mainScreen = new MainScreen(game);
        game.setScreen(mainScreen);
        Gdx.app.log("Cancel", "cancelled");
    }

    public void moveRight() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help <= criminalRow.length - 2 && letMove) {
            ScaleToAction scaleDown = new ScaleToAction();
            scaleDown.setScale(1.0f);
            scaleDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(scaleDown);
            criminalRow[help].toggleActive();
            criminalRow[help + 1].toggleActive();
        }
    }

    public void moveLeft() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help >= 1 && letMove) {
            ScaleToAction scaleDown = new ScaleToAction();
            scaleDown.setScale(1.0f);
            scaleDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(scaleDown);
            criminalRow[help].toggleActive();
            criminalRow[help - 1].toggleActive();
        }
    }

    public void setInterMissionScreen() {
        InterMissionScreen = new IntermissionScreen(game);
        game.setScreen(InterMissionScreen);
    }

    @Override
    public void render(float delta) {

        if (pauseWindow.isVisible() || sensitivityWindow.isVisible()) {
            delta = 0;
        }
        if (game.controls.accelerometerY() < game.controls.hysteresisUp) {
            game.gameData.setStillLeaning(true);
            stillLeaning = game.gameData.getStillLeaning();
        }

        if (((game.controls.accelerometerY() > game.controls.moveUp) && letMove && stillLeaning)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            elapsedTime += delta;
        } else if ((game.controls.accelerometerY() < game.controls.hysteresisUp)
                || !Gdx.input.isKeyPressed(Input.Keys.UP)) {
            elapsedTime = 0;
        }

        if (!letMove) {
            timeToMove += delta;
            if (timeToMove >= 1.5) {
                letMove = true;
            }
        }


        game.batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        for (Face criminal : criminalRow) {
            if (criminal.active && !criminal.hasActions() && letMove) {
                ScaleToAction scaleUp = new ScaleToAction();
                scaleUp.setScale(1.25f);
                scaleUp.setDuration(0.5f);
                criminal.addAction(scaleUp);
                if (delta != 0) {
                    criminal.toFront();
                }
            }
        }

        stage.act();
        stage.draw();

        pauseWindow.toFront();
        sensitivityWindow.toFront();

        game.batch.begin();
        if (!pauseWindow.isVisible() && !sensitivityWindow.isVisible()) {
            TextureRegion frame = animation.getKeyFrame(elapsedTime, false);
            game.batch.draw(
                    frame,
                    (width - frame.getRegionWidth()) / 2,
                    row_height * 9,
                    frame.getRegionWidth(),
                    frame.getRegionHeight()
            );

        }
        //Gdx.app.log("rowscreen", "animation" + animation.getKeyFrameIndex(elapsedTime));

        if (delta != 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                    game.controls.moveRight(false)) {
                moveRight();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                    game.controls.moveLeft(false)) {
                moveLeft();
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.UP) && elapsedTime >= 3)
                    || (game.controls.moveUp(true) && letMove && stillLeaning)) {
                select();
            }

            /*if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || game.controls.moveDown(true)) {
                cancel();
            }*/

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
                    Gdx.input.isKeyJustPressed(Input.Keys.BACK) ||
                    game.controls.moveDown(true)) {
                pauseWindow.setVisible(true);
                pauseWindow.toFront();

            }

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
        Gdx.input.setCatchBackKey(false);
        dispose();
    }

    @Override
    public void dispose() {
        stage.clear();
        mySkin.dispose();
        selectionBar.dispose();
    }
}