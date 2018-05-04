package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Samu Koivulahti on 17.3.2018.
 */
public class SettingsScreen implements Screen {

    Rikollisentunnistus host;
    ShapeRenderer shapeRenderer;
    Stage stage;
    OrthographicCamera camera;
    public Slider sliderR;
    public Slider sliderL;
    public Slider sliderU;
    public Slider sliderD;
    public CheckBox sliderA;
    public SelectBox sliderRow;
    public SelectBox sliderAttribute;
    public SelectBox sliderRound;
    public SelectBox sliderStaringDifficulty;
    public CheckBox sliderUseDifficulty;
    public CheckBox sliderIncreasingDifficulty;
    public SelectBox waitingTime;
    public SelectBox faceShown;
    public TextButton calibrate;
    public TextButton freePlay;
    public TextButton general;
    public TextButton save;
    public TextButton back;
    public Slider sliderV;
    public CheckBox soundEffects;
    public SelectBox timerSides;
    public SelectBox timerUp;
    public SelectBox timerDown;
    public TextButton clearProfile1;
    public TextButton clearProfile2;
    public TextButton clearProfile3;
    public CheckBox horizontalAxis;
    public CheckBox useChair;

    float selectBoxSize;

    float row_height;
    float col_width;
    float height;
    float width;

    private Label calibrateText;
    private Label sameAttributesText;
    private Label rowLengthText;
    private Label roundsText;
    private Label startingDifficultyText;
    private Label selectionWaitingTimeText;
    private Label faceShownText;
    private Label volumeText;
    private Label savedText;
    private Label calibratedText;
    private Label timerSidesText;
    private Label timerUpText;
    private Label timerDownText;

    private Texture sensitivityGraphTexture;
    public Image sensitivityGraphImage;


    Skin mySkin;
    Settings settings;
    final float MEDIUM_TEXT_SCALE = 0.5f;


    public SettingsScreen(Rikollisentunnistus host) {
        this.host = host;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        row_height = camera.viewportHeight / 15;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        selectBoxSize = camera.viewportWidth * 0.15f;


        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        calibrateText = new Label(host.texts.get(14), mySkin, "big");
        calibrateText.setPosition(col_width*5 - calibrateText.getWidth()/2, row_height * 13);

        savedText = new Label(host.texts.get(39), mySkin, "big");
        savedText.setPosition(col_width - savedText.getWidth()/2, row_height*2);
        savedText.setAlignment(Align.center);
        savedText.setVisible(false);

        calibratedText = new Label(host.texts.get(40), mySkin, "big");
        calibratedText.setPosition(col_width*1.5f - calibratedText.getWidth()/2, row_height*2);
        calibratedText.setAlignment(Align.center);
        calibratedText.setVisible(false);

        stage.addActor(calibrateText);
        freePlaySettingsButton();
        gameSettingsButton();
        waitingTime();
        faceShownTime();
        assetSlider();
        rowSlider();
        attributeSlider();
        roundSlider();
        startingDifficultySlider();
        useDifficultySlider();
        increasingDifficultySlider();
        buttonSave();
        buttonBack();
        buttonCalibrate();
        sliderVolume();
        soundEffects();
        timerSides();
        timerUp();
        timerDown();
        clearProfile1Button();
        clearProfile2Button();
        clearProfile3Button();
        horizontalAxis();
        useChair();
        stage.addActor(savedText);
        stage.addActor(calibratedText);
    }

    public void settingValues () {
        settings = Settings.getInstance();

        soundEffects.setChecked(settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS));
        sliderV.setValue(settings.getFloat("volume", GameData.DEFAULT_VOLUME));
        sliderIncreasingDifficulty.setChecked(settings.getBoolean("increasingDifficulty", GameData.DEFAULT_INCREASING_DIFFICULTY));
        sliderStaringDifficulty.setSelectedIndex(settings.getInteger("startingDifficulty", GameData.DEFAULT_STARTING_DIFFICULTY));
        sliderRound.setSelectedIndex(settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT)-5);
        sliderAttribute.setSelectedIndex(settings.getInteger("sameAttributes", GameData.DEFAULT_SAME_ATTRIBUTES));
        faceShown.setSelectedIndex(settings.getInteger("faceShown", GameData.DEFAULT_FACE_SHOWN)-1);
        waitingTime.setSelectedIndex(settings.getInteger("waitingTime", GameData.DEFAULT_WAITING_TIME)-1);
        sliderRow.setSelectedIndex(settings.getInteger("rowLength", GameData.DEFAULT_ROW_LENGTH)-3);
        if (useChair.isChecked()) {
            sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT)/0.5f);
            sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT)/0.5f);
            sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP)/0.7f);
            sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN)/0.3f);
        } else {
            sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT));
            sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT));
            sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP));
            sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN));
        }

        horizontalAxis.setChecked(settings.getBoolean("horizontalAxis", GameData.DEFAULT_HORIZONTAL_AXIS));
        if (timerSides.getSelectedIndex() == 0.25f) {
            timerSides.setSelectedIndex(0);
        } else {
            timerSides.setSelectedIndex((int)settings.getFloat("timerSides", GameData.DEFAULT_TIMER_SIDES));
        }

        if (timerUp.getSelectedIndex() == 0.25f) {
            timerUp.setSelectedIndex(0);
        } else {
            timerUp.setSelectedIndex((int)settings.getFloat("timerUp", GameData.DEFAULT_TIMER_UP));
        }

        if (timerDown.getSelectedIndex() == 0.25f) {
            timerDown.setSelectedIndex(0);
        } else {
            timerDown.setSelectedIndex((int)settings.getFloat("timerDown", GameData.DEFAULT_TIMER_DOWN));
        }
        sliderA.setChecked(settings.getBoolean("assets", GameData.DEFAULT_ASSETS));
        sliderUseDifficulty.setChecked(settings.getBoolean("useDifficulty", GameData.DEFAULT_USE_DIFFICULTY));
    }

    public void externalSensitivityWindow(Stage stage) {

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        sensitivityGraph(stage);
        sliderRight(stage);
        sliderLeft(stage);
        sliderUp(stage);
        sliderDown(stage);
    }

    public void clearProfile1Button () {
        clearProfile1 = new TextButton(host.texts.get(21) + " 1",mySkin,"small");
        clearProfile1.setHeight(row_height*2);
        clearProfile1.setPosition(0,row_height*10);
        clearProfile1.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.saveFiles.setInteger("points1", GameData.DEFAULT_POINTS);
                host.saveFiles.setInteger("level1", GameData.DEFAULT_LEVEL);
                host.saveFiles.remove1();
            }

        });

        stage.addActor(clearProfile1);
    }

    public void clearProfile2Button () {
        clearProfile2 = new TextButton(host.texts.get(21) + " 2",mySkin,"small");
        clearProfile2.setHeight(row_height*2);
        clearProfile2.setPosition(0,row_height* 7.5f);
        clearProfile2.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.saveFiles.setInteger("points2", GameData.DEFAULT_POINTS);
                host.saveFiles.setInteger("level2", GameData.DEFAULT_LEVEL);
                host.saveFiles.remove2();
            }

        });

        stage.addActor(clearProfile2);
    }

    public void clearProfile3Button () {
        clearProfile3 = new TextButton(host.texts.get(21) + " 3",mySkin,"small");
        clearProfile3.setHeight(row_height*2);
        clearProfile3.setPosition(0, row_height*5);
        clearProfile3.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.saveFiles.setInteger("points3", GameData.DEFAULT_POINTS);
                host.saveFiles.setInteger("level3", GameData.DEFAULT_LEVEL);
                host.saveFiles.remove3();
            }

        });

        stage.addActor(clearProfile3);
    }

    public void gameSettingsButton() {
        general = new TextButton(host.texts.get(8),mySkin,"small");
        general.setHeight(row_height*2);
        general.setPosition(width/3*2 - general.getWidth()/2,height/2 - general.getHeight());
        general.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                back.setText(host.texts.get(6));
                sliderRound.setVisible(false);
                roundsText.setVisible(false);
                sliderA.setVisible(false);
                sliderRow.setVisible(false);
                rowLengthText.setVisible(false);
                sliderAttribute.setVisible(false);
                sameAttributesText.setVisible(false);
                sliderUseDifficulty.setVisible(false);
                sliderIncreasingDifficulty.setVisible(false);
                sliderStaringDifficulty.setVisible(false);
                startingDifficultyText.setVisible(false);
                waitingTime.setVisible(false);
                selectionWaitingTimeText.setVisible(false);
                faceShown.setVisible(false);
                faceShownText.setVisible(false);

                calibrateText.setVisible(true);
                sensitivityGraphImage.setVisible(true);
                sliderR.setVisible(true);
                sliderL.setVisible(true);
                sliderU.setVisible(true);
                sliderD.setVisible(true);
                calibrate.setVisible(true);
                sliderV.setVisible(true);
                volumeText.setVisible(true);
                soundEffects.setVisible(true);
                timerSides.setVisible(true);
                timerSidesText.setVisible(true);
                timerUp.setVisible(true);
                timerUpText.setVisible(true);
                timerDown.setVisible(true);
                timerDownText.setVisible(true);
                general.setVisible(false);
                freePlay.setVisible(false);
                save.setVisible(true);
                clearProfile1.setVisible(true);
                clearProfile2.setVisible(true);
                clearProfile3.setVisible(true);
                horizontalAxis.setVisible(true);
                useChair.setVisible(true);
            }

        });

        stage.addActor(general);
    }

    public void freePlaySettingsButton() {
        freePlay = new TextButton(host.texts.get(9),mySkin,"small");
        freePlay.setHeight(row_height*2);
        freePlay.setPosition(width/3 - freePlay.getWidth()/2,height/2 - freePlay.getHeight());
        freePlay.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                back.setText(host.texts.get(6));
                freePlay.setVisible(false);
                general.setVisible(false);
                sliderRound.setVisible(true);
                roundsText.setVisible(true);
                sliderA.setVisible(true);
                sliderRow.setVisible(true);
                rowLengthText.setVisible(true);
                sliderAttribute.setVisible(true);
                sameAttributesText.setVisible(true);
                sliderUseDifficulty.setVisible(true);
                sliderIncreasingDifficulty.setVisible(true);
                sliderStaringDifficulty.setVisible(true);
                startingDifficultyText.setVisible(true);
                waitingTime.setVisible(true);
                selectionWaitingTimeText.setVisible(true);
                faceShown.setVisible(true);
                faceShownText.setVisible(true);

                calibrateText.setVisible(false);
                sensitivityGraphImage.setVisible(false);
                sliderR.setVisible(false);
                sliderL.setVisible(false);
                sliderU.setVisible(false);
                sliderD.setVisible(false);
                calibrate.setVisible(false);
                sliderV.setVisible(false);
                volumeText.setVisible(false);
                soundEffects.setVisible(false);
                timerSides.setVisible(false);
                timerSidesText.setVisible(false);
                timerUp.setVisible(false);
                timerUpText.setVisible(false);
                timerDown.setVisible(false);
                timerDownText.setVisible(false);
                save.setVisible(true);
                clearProfile1.setVisible(false);
                clearProfile2.setVisible(false);
                clearProfile3.setVisible(false);
                horizontalAxis.setVisible(false);
                useChair.setVisible(false);
            }

        });

        stage.addActor(freePlay);
    }

    public void buttonBack() {
        back = new TextButton(host.texts.get(7),mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "back");
                if (general.isVisible() && freePlay.isVisible()) {
                    host.setMainScreen();
                } else {
                    hideSettings();
                    freePlay.setVisible(true);
                    general.setVisible(true);
                    save.setVisible(false);
                    back.setText(host.texts.get(7));
                    settingValues();
                }
            }

        });

        stage.addActor(back);
    }

    public void buttonSave() {
        save = new TextButton(host.texts.get(22),mySkin);
        save.setHeight(row_height*2);
        save.setPosition(0,0);
        save.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "save");
                //settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                //settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                //Gdx.app.log("save", "accel Y: "+ settings.getFloat("zeroPointY"));
                if (useChair.isChecked()) {
                    settings.setFloat("sensitivityRight", sliderR.getValue()*0.5f);
                    settings.setFloat("sensitivityLeft", sliderL.getValue()*0.5f);
                    settings.setFloat("sensitivityUp", sliderU.getValue()*0.7f);
                    settings.setFloat("sensitivityDown", sliderD.getValue()*0.3f);
                } else {
                    settings.setFloat("sensitivityRight", sliderR.getValue());
                    settings.setFloat("sensitivityLeft", sliderL.getValue());
                    settings.setFloat("sensitivityUp", sliderU.getValue());
                    settings.setFloat("sensitivityDown", sliderD.getValue());
                }

                settings.setInteger("rowLength", sliderRow.getSelectedIndex()+3);
                settings.setInteger("sameAttributes", sliderAttribute.getSelectedIndex());
                settings.setBoolean("assets", sliderA.isChecked());
                settings.setInteger("roundAmount", sliderRound.getSelectedIndex()+5);
                settings.setInteger("startingDifficulty", sliderStaringDifficulty.getSelectedIndex());
                settings.setBoolean("useDifficulty", sliderUseDifficulty.isChecked());
                settings.setBoolean("increasingDifficulty", sliderIncreasingDifficulty.isChecked());
                settings.setInteger("waitingTime", waitingTime.getSelectedIndex()+1);
                settings.setInteger("faceShown", faceShown.getSelectedIndex()+1);
                settings.setBoolean("soundEffects", soundEffects.isChecked());
                settings.setFloat("volume", sliderV.getValue());
                settings.setBoolean("horizontalAxis", horizontalAxis.isChecked());
                settings.setBoolean("useChair", useChair.isChecked());
                if (timerSides.getSelectedIndex() == 0) {
                    settings.setFloat("timerSides", timerSides.getSelectedIndex() + 0.25f);
                } else {
                    settings.setFloat("timerSides", timerSides.getSelectedIndex());
                }

                if (timerUp.getSelectedIndex() == 0) {
                    settings.setFloat("timerUp", timerUp.getSelectedIndex() + 0.25f);
                } else {
                    settings.setFloat("timerUp", timerUp.getSelectedIndex());
                }

                if (timerDown.getSelectedIndex() == 0) {
                    settings.setFloat("timerDown", timerDown.getSelectedIndex() + 0.25f);
                } else {
                    settings.setFloat("timerDown", timerDown.getSelectedIndex());
                }
                SoundManager.changeVolume(settings.getFloat("volume", 50)/100f);
                settings.saveSettings();
                host.updateSettings();
                host.controls.updateControls();
                savedText.setVisible(true);
                savedText.toFront();
                savedText.addAction(Actions.sequence(Actions.alpha(1f),
                        Actions.fadeOut(3.0f), Actions.delay(3f)));
            }

        });

        stage.addActor(save);
        save.setVisible(false);
    }

    public void buttonCalibrate() {
        calibrate = new TextButton(host.texts.get(13), mySkin, "small");
        calibrate.setSize(selectBoxSize*2, row_height*2);
        calibrate.setPosition(col_width*5 - calibrate.getWidth()/2, row_height * 2);
        calibrate.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setZeroPoint();
                calibratedText.setVisible(true);
                calibratedText.toFront();
                calibratedText.addAction(Actions.sequence(Actions.alpha(1f),
                        Actions.fadeOut(3.0f), Actions.delay(3f)));
            }

        });

        stage.addActor(calibrate);
    }

    public void setZeroPoint() {
        settings = Settings.getInstance();

        settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
        settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
        settings.setFloat("zeroPointZ", Gdx.input.getAccelerometerX());

        settings.saveSettings();
        host.controls.updateControls();
        Gdx.app.log("SettingsScreen", "Zero point set");
        Gdx.app.log("SettingsScreen", "zeropointX" + settings.getFloat("zeroPointX", GameData.DEFAULT_ZERO_POINT_X));
        Gdx.app.log("SettingsScreen", "zeropointY" + settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y));
    }

    private void sensitivityGraph (Stage stage) {
        sensitivityGraphTexture = new Texture("sensitivityGraph.png");
        sensitivityGraphImage = new Image(sensitivityGraphTexture);
        sensitivityGraphImage.setSize(selectBoxSize *2, selectBoxSize *2);
        sensitivityGraphImage.setOrigin(Align.center);
        sensitivityGraphImage.setPosition(col_width*5 - selectBoxSize, row_height * 8 - selectBoxSize);
        stage.addActor(sensitivityGraphImage);
    }

    public void sensitivityGraph () {
        sensitivityGraph(stage);
    }

    public void sliderRight() {
        sliderRight(stage);
    }

    public void sliderLeft() {
        sliderLeft(stage);
    }

    public void sliderUp() {
        sliderUp(stage);
    }

    public void sliderDown() {
        sliderDown(stage);
    }

    private void sliderRight(Stage stage) {
        sliderR = new Slider(0f,10f,1f,false, mySkin);
        sliderR.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderR.setWidth(selectBoxSize);
        sliderR.setPosition(col_width*5, row_height * 8 - sliderR.getHeight()/2);

        stage.addActor(sliderR);
    }

    private void sliderLeft(Stage stage) {
        sliderL = new Slider(-10f,0f,1f,false, mySkin);
        sliderL.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderL.setWidth(selectBoxSize);
        sliderL.setPosition(col_width*5 - sliderL.getWidth(), row_height * 8 - sliderR.getHeight()/2);
        stage.addActor(sliderL);
    }

    private void sliderUp(Stage stage) {
        sliderU = new Slider(0f,10f,1f,true, mySkin);
        sliderU.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderU.setHeight(selectBoxSize);
        sliderU.setPosition(col_width*5 - sliderU.getWidth()/2, row_height * 8);
        stage.addActor(sliderU);
    }

    private void sliderDown(Stage stage) {
        sliderD = new Slider(-10f,0f,1f,true, mySkin);
        sliderD.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderD.setHeight(selectBoxSize);
        sliderD.setPosition(col_width*5 - sliderD.getWidth()/2, row_height * 8 - sliderD.getHeight());
        stage.addActor(sliderD);
    }

    public void assetSlider() {
        sliderA = new CheckBox(host.texts.get(36), mySkin);
        sliderA.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        sliderA.setPosition(col_width *8 + (selectBoxSize *2 - sliderA.getWidth())/ 2, row_height * 10);
        stage.addActor(sliderA);
    }

    public void rowSlider() {
        sliderRow = new SelectBox(mySkin);
        String[] array = {"3","4","5","6"};
        sliderRow.setItems(array);
        sliderRow.setWidth(selectBoxSize *2);
        sliderRow.setPosition(col_width *8, row_height * 7);
        rowLengthText = new Label(host.texts.get(37), mySkin,"big");
        rowLengthText.setPosition(col_width *8 + (selectBoxSize) - rowLengthText.getWidth()*0.5f/2, row_height *8.5f);
        rowLengthText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderRow);
        stage.addActor(rowLengthText);
    }

    public void waitingTime() {
        waitingTime = new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8"};
        waitingTime.setItems(array);
        waitingTime.setWidth(selectBoxSize *2);
        waitingTime.setPosition(col_width*3, row_height * 12);
        selectionWaitingTimeText = new Label(host.texts.get(30), mySkin, "big");
        selectionWaitingTimeText.setPosition(col_width *3 + (selectBoxSize) - selectionWaitingTimeText.getWidth()*0.5f/2, row_height *13.5f);
        selectionWaitingTimeText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(waitingTime);
        stage.addActor(selectionWaitingTimeText);
    }

    public void faceShownTime () {
        faceShown = new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5"};
        faceShown.setItems(array);
        faceShown.setWidth(selectBoxSize *2);
        faceShown.setPosition(col_width * 3, row_height *9);
        faceShownText = new Label(host.texts.get(31), mySkin, "big");
        faceShownText.setPosition(col_width * 3 + selectBoxSize - faceShownText.getWidth()*0.5f/2, row_height * 10.5f);
        faceShownText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(faceShown);
        stage.addActor(faceShownText);
    }

    public void attributeSlider() {
        sliderAttribute = new SelectBox(mySkin);
        String[] array = {"0", "1", "2", "3", "4"};
        sliderAttribute.setItems(array);
        sliderAttribute.setWidth(selectBoxSize *2);
        sliderAttribute.setPosition(col_width *8, row_height * 4);
        sameAttributesText = new Label(host.texts.get(38), mySkin, "big");
        sameAttributesText.setPosition(col_width *8 + (selectBoxSize) - sameAttributesText.getWidth()*0.5f/2, row_height *5.5f);
        sameAttributesText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderAttribute);
        stage.addActor(sameAttributesText);
    }

    public void roundSlider() {
        sliderRound = new SelectBox(mySkin);
        String[] array = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderRound.setItems(array);
        sliderRound.setWidth(selectBoxSize *2);
        sliderRound.setPosition(col_width *8, row_height * 12);
        roundsText = new Label(host.texts.get(35), mySkin, "big");
        roundsText.setPosition(col_width *8 + (selectBoxSize) - roundsText.getWidth()*0.5f/2, row_height *13.5f);
        roundsText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderRound);
        stage.addActor(roundsText);
    }

    public void startingDifficultySlider() {
        sliderStaringDifficulty= new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderStaringDifficulty.setItems(array);
        sliderStaringDifficulty.setWidth(selectBoxSize *2);
        sliderStaringDifficulty.setPosition(col_width *3, row_height * 3);
        startingDifficultyText = new Label(host.texts.get(34), mySkin, "big");
        startingDifficultyText.setPosition(col_width *3 + (selectBoxSize) - startingDifficultyText.getWidth()*0.5f/2, row_height *4.5f);
        startingDifficultyText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderStaringDifficulty);
        stage.addActor(startingDifficultyText);
    }

    public void useDifficultySlider() {
        sliderUseDifficulty = new CheckBox(host.texts.get(32), mySkin);
        sliderUseDifficulty.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        sliderUseDifficulty.setPosition(col_width *3 + (selectBoxSize *2 - sliderUseDifficulty.getWidth())/ 2, row_height * 7);
        stage.addActor(sliderUseDifficulty);
    }

    public void increasingDifficultySlider() {
        sliderIncreasingDifficulty = new CheckBox(host.texts.get(33), mySkin);
        sliderIncreasingDifficulty.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        sliderIncreasingDifficulty.setPosition(col_width *3 + (selectBoxSize *2 - sliderIncreasingDifficulty.getWidth())/ 2, row_height * 6);
        stage.addActor(sliderIncreasingDifficulty);
    }

    private void sliderVolume() {
        sliderV = new Slider(0f,100f,1f,false, mySkin);
        sliderV.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderV.setWidth(selectBoxSize*2);
        sliderV.setPosition(col_width*8, row_height * 13);
        volumeText = new Label(host.texts.get(24), mySkin, "big");
        volumeText.setPosition(col_width *8 + (selectBoxSize) - volumeText.getWidth()*0.5f/2, row_height *14);
        volumeText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderV);
        stage.addActor(volumeText);
    }

    public void soundEffects() {
        soundEffects = new CheckBox(host.texts.get(25), mySkin);
        soundEffects.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        soundEffects.setPosition(col_width*8 + selectBoxSize - soundEffects.getWidth()/2, row_height * 11);
        stage.addActor(soundEffects);
    }

    public void timerSides() {
        timerSides = new SelectBox(mySkin);
        String[] array = {host.texts.get(45), "1", "2", "3", "4", "5"};
        timerSides.setItems(array);
        timerSides.setWidth(selectBoxSize *2);
        timerSides.setPosition(col_width *8 , row_height * 8);
        timerSidesText = new Label(host.texts.get(26), mySkin, "big");
        timerSidesText.setPosition(col_width *8 + (selectBoxSize) - timerSidesText.getWidth()*0.5f/2, row_height * 9.5f);
        timerSidesText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(timerSides);
        stage.addActor(timerSidesText);
    }

    public void timerUp() {
        timerUp = new SelectBox(mySkin);
        String[] array = {host.texts.get(45), "1", "2", "3", "4", "5"};
        timerUp.setItems(array);
        timerUp.setWidth(selectBoxSize *2);
        timerUp.setPosition(col_width *8, row_height * 5);
        timerUpText = new Label(host.texts.get(27), mySkin, "big");
        timerUpText.setPosition(col_width *8 + (selectBoxSize) - timerUpText.getWidth()*0.5f/2, row_height * 6.5f);
        timerUpText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(timerUp);
        stage.addActor(timerUpText);
    }

    public void timerDown() {
        timerDown = new SelectBox(mySkin);
        String[] array = {host.texts.get(45), "1", "2", "3", "4", "5"};
        timerDown.setItems(array);
        timerDown.setWidth(selectBoxSize *2);
        timerDown.setPosition(col_width *8, row_height * 2);
        timerDownText = new Label(host.texts.get(28), mySkin, "big");
        timerDownText.setPosition(col_width *8 + (selectBoxSize) - timerDownText.getWidth()*0.5f/2, row_height *3.5f);
        timerDownText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(timerDown);
        stage.addActor(timerDownText);
    }

    public void horizontalAxis() {
        horizontalAxis = new CheckBox(host.texts.get(29), mySkin);
        horizontalAxis.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        horizontalAxis.setPosition(col_width*8 + selectBoxSize - horizontalAxis.getWidth()/2, row_height * 0.5f);
        stage.addActor(horizontalAxis);
    }

    public void useChair() {
        useChair = new CheckBox(host.texts.get(23), mySkin);
        useChair.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        useChair.setPosition(col_width*5 - useChair.getWidth()/2, row_height * 0.5f);
        stage.addActor(useChair);
    }

    public void hideSettings() {
        sliderR.setVisible(false);
        sliderL.setVisible(false);
        sliderU.setVisible(false);
        sliderD.setVisible(false);
        sensitivityGraphImage.setVisible(false);
        sliderRound.setVisible(false);
        roundsText.setVisible(false);
        sliderA.setVisible(false);
        sliderRow.setVisible(false);
        rowLengthText.setVisible(false);
        sliderAttribute.setVisible(false);
        sameAttributesText.setVisible(false);
        sliderUseDifficulty.setVisible(false);
        sliderIncreasingDifficulty.setVisible(false);
        sliderStaringDifficulty.setVisible(false);
        startingDifficultyText.setVisible(false);
        waitingTime.setVisible(false);
        selectionWaitingTimeText.setVisible(false);
        faceShown.setVisible(false);
        faceShownText.setVisible(false);
        calibrateText.setVisible(false);
        calibrate.setVisible(false);
        sliderV.setVisible(false);
        volumeText.setVisible(false);
        soundEffects.setVisible(false);
        timerSides.setVisible(false);
        timerSidesText.setVisible(false);
        timerUp.setVisible(false);
        timerUpText.setVisible(false);
        timerDown.setVisible(false);
        timerDownText.setVisible(false);
        clearProfile1.setVisible(false);
        clearProfile2.setVisible(false);
        clearProfile3.setVisible(false);
        horizontalAxis.setVisible(false);
        useChair.setVisible(false);
    }

    @Override
    public void show() {
        sensitivityGraph();
        sliderRight();
        sliderLeft();
        sliderUp();
        sliderDown();
        hideSettings();
        settingValues();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(13/255f,54/255f,70/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
        sliderR.setVisible(false);
        sliderL.setVisible(false);
        sliderU.setVisible(false);
        sliderD.setVisible(false);
        sensitivityGraphImage.setVisible(false);
        save.setVisible(false);
        host.saveFiles.saveNewFiles();

    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}