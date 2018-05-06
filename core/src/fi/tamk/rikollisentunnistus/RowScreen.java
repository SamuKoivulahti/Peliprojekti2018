package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */
//TODO: pisteenlasku
//TODO: INTERMISSIOSCREEN2, rank, pisteenlaskun nollaaminen, ruudusta oikein eteneminen

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
    private Label rankText;
    private Label savedText;
    private Label calibratedText;

    private Texture lineUp;
    private Texture desk;
    private Image lineUpImage;
    private Image deskImage;
    private Image spotlightImage;

    Skin mySkin;

    float row_height;
    float col_width;
    float width;
    float height;

    int points;
    int level;
    int rank;

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

        spotlightImage = new Image(new Texture("selectionspotlight.png"));

        desk = new Texture("rowscreendesk.png");
        deskImage = new Image(desk);
        deskImage.setPosition(0,0);
        deskImage.setSize(width, height/8);

        // Sets points and level depending on which profile player chose
        if (game.gameData.getProfileUsed() == 1) {
            points = game.saveFiles.getInteger("points1", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level1", GameData.DEFAULT_LEVEL);
            rank = game.saveFiles.getInteger("rank1", GameData.DEFAULT_RANK);
        } else if (game.gameData.getProfileUsed() == 2) {
            points = game.saveFiles.getInteger("points2", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level2", GameData.DEFAULT_LEVEL);
            rank = game.saveFiles.getInteger("rank2", GameData.DEFAULT_RANK);
        } else if (game.gameData.getProfileUsed() == 3) {
            points = game.saveFiles.getInteger("points3", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level3", GameData.DEFAULT_LEVEL);
            rank = game.saveFiles.getInteger("rank3", GameData.DEFAULT_RANK);
        } else if (game.gameData.getProfileUsed() == 0) {
            points = game.gameData.getPoints();
            level = game.gameData.getLevel();
        }

        pointsText = new Label(game.texts.get(11)+ points, mySkin, "big");
        pointsText.setFontScale(0.5f);
        pointsText.setPosition(width - pointsText.getWidth()*0.5f - width/100, height - pointsText.getHeight());
        levelText = new Label(game.texts.get(10) + level, mySkin, "big");
        levelText.setPosition(width/2 - levelText.getWidth()/2, row_height * 11);
        levelText.setColor(Color.BLACK);

        savedText = new Label(game.texts.get(39), mySkin, "big");
        savedText.setPosition(width/2 - savedText.getWidth()/2, row_height/2);
        savedText.setAlignment(Align.center);
        savedText.setVisible(false);

        calibratedText = new Label(game.texts.get(40), mySkin, "big");
        calibratedText.setPosition(width/2 - calibratedText.getWidth()/2, row_height/2);
        calibratedText.setAlignment(Align.center);
        calibratedText.setVisible(false);



        selectionBar = new Texture("selectionBarYellowToGreen.png");

        TextureRegion[] animationFrames = new TextureRegion[26];

        for (int i = 0; i < 26; i++) {
            int length = selectionBar.getHeight() / animationFrames.length;
            animationFrames[i] = new TextureRegion(selectionBar, 0, i * length, selectionBar.getWidth(), length);
        }

        animation = new Animation<TextureRegion>(game.controls.timerUp / 26, animationFrames);

        stage.addActor(lineUpImage);
        stage.addActor(spotlightImage);
        stage.addActor(deskImage);
        stage.addActor(pointsText);
        stage.addActor(levelText);
        if (game.gameData.getProfileUsed() != 0) {
            rankText = new Label(game.script.get("Rank"+rank), mySkin);
            rankText.setPosition(0, height - rankText.getHeight());
            stage.addActor(rankText);
        }
        createPauseWindow();
        stage.addActor(savedText);
        stage.addActor(calibratedText);

        letMove = false;
        timeToMove = 0;
    }

    public void createPauseWindow () {
        pauseWindow = new Window(game.texts.get(43), mySkin);
        pauseWindow.setVisible(false);
        pauseWindow.setResizable(false);
        pauseWindow.setMovable(false);

        TextButton continueButton = new TextButton(game.texts.get(12), mySkin);
        continueButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    pauseWindow.setVisible(false);
                }

            });

        TextButton exitButton = new TextButton(game.texts.get(15), mySkin);
        exitButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.resetAll();
                    game.setMainScreen();
                    SoundManager.stopIngameMusic();
                }

            });

        TextButton calibrateButton = new TextButton(game.texts.get(13), mySkin);
        calibrateButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.settingsScreen.setZeroPoint();
                    calibratedText.setVisible(true);
                    calibratedText.toFront();
                    calibratedText.addAction(Actions.sequence(Actions.alpha(1f),
                            Actions.fadeOut(3.0f), Actions.delay(3f)));
                }

            });


        sensitivityWindow = new Window(game.texts.get(14), mySkin);
        sensitivityWindow.setVisible(false);
        sensitivityWindow.setResizable(false);
        sensitivityWindow.setMovable(false);

        TextButton sensitivityButton = new TextButton(game.texts.get(14), mySkin);
        sensitivityButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    settings = Settings.getInstance();
                    pauseWindow.setVisible(false);
                    sensitivityWindow.setVisible(true);
                    game.settingsScreen.externalSensitivityWindow(stage);
                    game.settingsScreen.sliderR.setVisible(true);
                    game.settingsScreen.sliderL.setVisible(true);
                    game.settingsScreen.sliderU.setVisible(true);
                    game.settingsScreen.sliderD.setVisible(true);
                    game.settingsScreen.sensitivityGraphImage.setVisible(true);
                    game.settingsScreen.sliderR.setPosition(width/5,height/2 - game.settingsScreen.sliderR.getHeight()/2);
                    game.settingsScreen.sliderL.setPosition(width/5 - game.settingsScreen.selectBoxSize,height/2 - game.settingsScreen.sliderL.getHeight()/2);
                    game.settingsScreen.sliderU.setPosition(width/5 - game.settingsScreen.sliderU.getWidth()/2,height/2);
                    game.settingsScreen.sliderD.setPosition(width/5 - game.settingsScreen.sliderD.getWidth()/2,height/2 - game.settingsScreen.selectBoxSize);
                    if (settings.getBoolean("useChair", GameData.DEFAULT_USE_CHAIR)) {
                        game.settingsScreen.sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT)/0.5f);
                        game.settingsScreen.sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT)/0.5f);
                        game.settingsScreen.sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP)/0.7f);
                        game.settingsScreen.sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN)/0.3f);
                    } else {
                        game.settingsScreen.sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT)/0.9f);
                        game.settingsScreen.sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT)/0.9f);
                        game.settingsScreen.sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP)/0.9f);
                        game.settingsScreen.sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN)/0.9f);
                    }

                    game.settingsScreen.sensitivityGraphImage.setPosition(width/5-game.settingsScreen.selectBoxSize, height/2 - game.settingsScreen.selectBoxSize);
                }

            });

        TextButton saveButton = new TextButton(game.texts.get(22), mySkin);
        saveButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    settings = Settings.getInstance();
                    if (settings.getBoolean("useChair", GameData.DEFAULT_USE_CHAIR)) {
                        settings.setFloat("sensitivityRight", game.settingsScreen.sliderR.getValue()*0.5f);
                        settings.setFloat("sensitivityLeft", game.settingsScreen.sliderL.getValue()*0.5f);
                        settings.setFloat("sensitivityUp", game.settingsScreen.sliderU.getValue()*0.7f);
                        settings.setFloat("sensitivityDown", game.settingsScreen.sliderD.getValue()*0.3f);
                    } else {
                        settings.setFloat("sensitivityRight", game.settingsScreen.sliderR.getValue()*0.9f);
                        settings.setFloat("sensitivityLeft", game.settingsScreen.sliderL.getValue()*0.9f);
                        settings.setFloat("sensitivityUp", game.settingsScreen.sliderU.getValue()*0.9f);
                        settings.setFloat("sensitivityDown", game.settingsScreen.sliderD.getValue()*0.9f);
                    }

                    settings.saveSettings();
                    game.controls.hysteresisRight = game.settingsScreen.sliderR.getValue()/2;
                    game.controls.hysteresisLeft = game.settingsScreen.sliderL.getValue()/2;
                    game.controls.hysteresisUp = game.settingsScreen.sliderU.getValue()/2;
                    game.controls.hysteresisDown = game.settingsScreen.sliderD.getValue()/2;
                    pauseWindow.setVisible(true);
                    sensitivityWindow.setVisible(false);
                    game.settingsScreen.sliderR.setVisible(false);
                    game.settingsScreen.sliderL.setVisible(false);
                    game.settingsScreen.sliderU.setVisible(false);
                    game.settingsScreen.sliderD.setVisible(false);
                    game.settingsScreen.sensitivityGraphImage.setVisible(false);
                    savedText.setVisible(true);
                    savedText.toFront();
                    savedText.addAction(Actions.sequence(Actions.alpha(1f),
                            Actions.fadeOut(3.0f), Actions.delay(3f)));
                    game.controls.updateControls();
                }

            });

        TextButton cancelButton = new TextButton(game.texts.get(44), mySkin);
        cancelButton.addListener(
            new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    SoundManager.playButtonPushSound(game.soundEffectsOn);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    pauseWindow.setVisible(true);
                    sensitivityWindow.setVisible(false);
                    game.settingsScreen.sliderR.setVisible(false);
                    game.settingsScreen.sliderL.setVisible(false);
                    game.settingsScreen.sliderU.setVisible(false);
                    game.settingsScreen.sliderD.setVisible(false);
                    game.settingsScreen.sensitivityGraphImage.setVisible(false);
                }

            });

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
            Gdx.app.log("RowScreen", "sprite width: " + criminalRow[0].getSpriteWidth());

            for (Face criminal : criminalRow) {
                criminal.changeScale(scale);
            }
        } else {
            float scale = separationOdd / criminalRow[0].getSpriteWidth();
            Gdx.app.log("RowScreen", "sprite width: " + criminalRow[0].getSpriteWidth());

            for (Face criminal : criminalRow) {
                criminal.changeScale(scale);
            }
        }

        spotlightImage.setPosition(xCrd + startpoint*separation - spotlightImage.getWidth()/2, 0);
        spotlightImage.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.5f), Actions.delay(0)));

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

        Gdx.app.log("RowScreen", "Correct suspect id: " + rightSuspectID);
        Gdx.app.log("RowScreen", "Selected suspect id: "+ selectedID);

        Gdx.app.log("RowScreen", "selected");
        if (selectedID.equals(rightSuspectID)) {
            win = true;
            if (game.gameData.profileUsed == 1) {
                game.saveFiles.setInteger("points1", points + 2);
            } else if (game.gameData.profileUsed == 2) {
                game.saveFiles.setInteger("points2", points + 2);
            } else if (game.gameData.profileUsed == 3) {
                game.saveFiles.setInteger("points3", points + 2);
            } else if (game.gameData.profileUsed == 0) {
                game.gameData.setPoints(points + 1);
            }
            game.saveFiles.saveNewFiles();
            game.gameData.setWin(true);

        } else {
            lose = true;
            if (game.gameData.profileUsed == 1 && points > 0) {
                game.saveFiles.setInteger("points1", points - 1);
            } else if (game.gameData.profileUsed == 2 && points > 0) {
                game.saveFiles.setInteger("points2", points - 1);
            } else if (game.gameData.profileUsed == 3 && points > 0) {
                game.saveFiles.setInteger("points3", points - 1);
            }
            game.saveFiles.saveNewFiles();
        }

        if (game.useDifficulty && game.increasingDifficulty && game.gameData.getProfileUsed() == 0) {
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

    public void moveLeft() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help >= 1 && letMove) {
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

    public void setInterMissionScreen() {
        InterMissionScreen = new IntermissionScreen(game);
        game.setScreen(InterMissionScreen);
    }

    @Override
    public void render(float delta) {

        if (pauseWindow.isVisible() || sensitivityWindow.isVisible()) {
            delta = 0;
        }

        if (game.settingsScreen.horizontalAxis.isChecked()) {
            if (game.controls.accelerometerY() > game.controls.hysteresisUp) {
                game.gameData.setStillLeaning(true);
            }
        } else {
            if (game.controls.accelerometerY() < game.controls.hysteresisUp) {
                game.gameData.setStillLeaning(true);
                //Gdx.app.log("RowScreen", ""+game.gameData.getStillLeaning());
            }
        }

        if (((game.controls.elapsedTimeU != 0)
                && !game.controls.isAbleMoveUp && letMove && game.gameData.getStillLeaning())
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

        if (!letMove) {
            timeToMove += delta;
            if (timeToMove >= 1.5) {
                letMove = true;
            }
        }


        game.batch.setProjectionMatrix(camera.combined);
        // Gdx.gl.glClearColor(176/255f,179/255f,189/255f,1);
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        for (Face criminal : criminalRow) {
            if (criminal.active && !criminal.hasActions() && letMove) {
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
        //Gdx.app.log("RowScreen", ""+ game.controls.hysteresisRight);
        if (delta != 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                    game.controls.moveRight(true)) {
                moveRight();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                    game.controls.moveLeft(true)) {
                moveLeft();
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.UP) && elapsedTime >= game.controls.timerUp)
                    || (game.controls.moveUp(true) && letMove && game.gameData.getStillLeaning())) {
                SoundManager.stopSelectionBarSound(game.soundEffectsOn);
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
        //Gdx.app.log("RowScreen", ""+game.controls.hysteresisUp);

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