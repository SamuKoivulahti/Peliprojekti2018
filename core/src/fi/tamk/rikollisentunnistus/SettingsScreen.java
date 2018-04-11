package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
//TODO: Yleiset: kalibrointi, äänenvoimakkuus, soundeffektit
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

    private Texture sensitivityGraphTexture;
    public Image sensitivityGraphImage;


    Skin mySkin;
    Settings settings;


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

        calibrateText = new Label("Sensitivity", mySkin, "big");
        calibrateText.setPosition(col_width*5 - calibrateText.getWidth()/2, row_height * 13);


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
        sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT));
        sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT));
        sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP));
        sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN));
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

    public void gameSettingsButton() {
        general = new TextButton("General Settings",mySkin,"small");
        general.setSize(col_width*2,row_height*2);
        general.setPosition(col_width * 3,height/2 - general.getHeight());
        general.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                back.setText("Back");
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
                general.setVisible(false);
                freePlay.setVisible(false);
                save.setVisible(true);
            }

        });
        stage.addActor(general);
    }

    public void freePlaySettingsButton() {
        freePlay = new TextButton("Free Play Settings",mySkin,"small");
        freePlay.setSize(col_width*2,row_height*2);
        freePlay.setPosition(col_width * 7,height/2 - freePlay.getHeight());
        freePlay.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                back.setText("Back");
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
                save.setVisible(true);

            }

        });
        stage.addActor(freePlay);
    }

    public void buttonBack() {
        back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("TAG", "back");
                if (general.isVisible() && freePlay.isVisible()) {
                    host.setMainScreen();
                } else {
                    hideSettings();
                    freePlay.setVisible(true);
                    general.setVisible(true);
                    save.setVisible(false);
                    back.setText("Main Menu");
                    settingValues();
                }
            }

        });
        stage.addActor(back);
    }

    public void buttonSave() {
        save = new TextButton("SAVE",mySkin);
        save.setSize(col_width*2,row_height*2);
        save.setPosition(0,0);
        save.addListener(new ClickListener(){

            @Override
            public void clicked (InputEvent event, float x, float y) {
                Gdx.app.log("TAG", "save");
                //settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                //settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                //Gdx.app.log("save", "accel Y: "+ settings.getFloat("zeroPointY"));
                settings.setFloat("sensitivityRight", sliderR.getValue());
                settings.setFloat("sensitivityLeft", sliderL.getValue());
                settings.setFloat("sensitivityUp", sliderU.getValue());
                settings.setFloat("sensitivityDown", sliderD.getValue());
                settings.setInteger("rowLength", sliderRow.getSelectedIndex()+3);
                settings.setInteger("sameAttributes", sliderAttribute.getSelectedIndex());
                settings.setBoolean("assets", sliderA.isChecked());
                settings.setInteger("roundAmount", sliderRound.getSelectedIndex()+5);
                settings.setInteger("startingDifficulty", sliderStaringDifficulty.getSelectedIndex());
                settings.setBoolean("useDifficulty", sliderUseDifficulty.isChecked());
                settings.setBoolean("increasingDifficulty", sliderIncreasingDifficulty.isChecked());
                settings.setInteger("waitingTime", waitingTime.getSelectedIndex()+1);
                settings.setInteger("faceShown", faceShown.getSelectedIndex()+1);
                settings.setFloat("volume", sliderV.getValue());
                settings.saveSettings();
                host.controls.updateControls();
            }
        });
        stage.addActor(save);
        save.setVisible(false);
    }

    public void buttonCalibrate() {
        calibrate = new TextButton("Calibrate", mySkin, "small");
        calibrate.setSize(selectBoxSize*2, row_height*2);
        calibrate.setPosition(col_width*8 + selectBoxSize - calibrate.getWidth()/2, row_height * 9);
        calibrate.addListener(new ClickListener(){

            @Override
            public void clicked (InputEvent event, float x, float y) {
                setZeroPoint();
            }

        });
        stage.addActor(calibrate);
    }

    public void setZeroPoint() {
        Gdx.app.log("TAG", "save");
        settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
        settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());

        settings.saveSettings();
        host.controls.updateControls();
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
        sliderA = new CheckBox("Accessories", mySkin);
        sliderA.setPosition(col_width *8 + (selectBoxSize *2 - sliderA.getWidth())/ 2, row_height * 11);
        stage.addActor(sliderA);
    }

    public void rowSlider() {
        sliderRow = new SelectBox(mySkin);
        String[] array = {"3","4","5","6"};
        sliderRow.setItems(array);
        sliderRow.setWidth(selectBoxSize *2);
        sliderRow.setPosition(col_width *8, row_height * 9);
        rowLengthText = new Label("Rows Length", mySkin);
        rowLengthText.setPosition(col_width *8 + (selectBoxSize) - rowLengthText.getWidth()/2, row_height *10);
        stage.addActor(rowLengthText);
        stage.addActor(sliderRow);
    }

    public void waitingTime() {
        waitingTime = new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8"};
        waitingTime.setItems(array);
        waitingTime.setWidth(selectBoxSize *2);
        waitingTime.setPosition(col_width*3, row_height * 13);
        selectionWaitingTimeText = new Label("Waiting Time", mySkin);
        selectionWaitingTimeText.setPosition(col_width *3 + (selectBoxSize) - selectionWaitingTimeText.getWidth()/2, row_height *14);
        stage.addActor(waitingTime);
        stage.addActor(selectionWaitingTimeText);
    }

    public void faceShownTime () {
        faceShown = new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5"};
        faceShown.setItems(array);
        faceShown.setWidth(selectBoxSize *2);
        faceShown.setPosition(col_width * 3, row_height *11);
        faceShownText = new Label("Face Shown", mySkin);
        faceShownText.setPosition(col_width * 3 + selectBoxSize - faceShownText.getWidth()/2, row_height * 12);
        stage.addActor(faceShown);
        stage.addActor(faceShownText);
    }

    public void attributeSlider() {
        sliderAttribute = new SelectBox(mySkin);
        String[] array = {"0", "1", "2", "3", "4"};
        sliderAttribute.setItems(array);
        sliderAttribute.setWidth(selectBoxSize *2);
        sliderAttribute.setPosition(col_width *8, row_height * 7);
        sameAttributesText = new Label("Same Attributes", mySkin);
        sameAttributesText.setPosition(col_width *8 + (selectBoxSize) - sameAttributesText.getWidth()/2, row_height *8);
        stage.addActor(sameAttributesText);
        stage.addActor(sliderAttribute);
    }

    public void roundSlider() {
        sliderRound = new SelectBox(mySkin);
        String[] array = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderRound.setItems(array);
        sliderRound.setWidth(selectBoxSize *2);
        sliderRound.setPosition(col_width *8, row_height * 13);
        roundsText = new Label("Round Amount", mySkin);
        roundsText.setPosition(col_width *8 + (selectBoxSize) - roundsText.getWidth()/2, row_height *14);
        stage.addActor(roundsText);
        stage.addActor(sliderRound);
    }

    public void startingDifficultySlider() {
        sliderStaringDifficulty= new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderStaringDifficulty.setItems(array);
        sliderStaringDifficulty.setWidth(selectBoxSize *2);
        sliderStaringDifficulty.setPosition(col_width *3, row_height * 6);
        startingDifficultyText = new Label("Starting Difficulty", mySkin);
        startingDifficultyText.setPosition(col_width *3 + (selectBoxSize) - roundsText.getWidth()/2, row_height *7);
        stage.addActor(startingDifficultyText);
        stage.addActor(sliderStaringDifficulty);
    }

    public void useDifficultySlider() {
        sliderUseDifficulty = new CheckBox("Use Difficulty", mySkin);
        sliderUseDifficulty.setPosition(col_width *3 + (selectBoxSize *2 - sliderUseDifficulty.getWidth())/ 2, row_height * 9);
        stage.addActor(sliderUseDifficulty);
    }

    public void increasingDifficultySlider() {
        sliderIncreasingDifficulty = new CheckBox("Increasing Difficulty", mySkin);
        sliderIncreasingDifficulty.setPosition(col_width *3 + (selectBoxSize *2 - sliderIncreasingDifficulty.getWidth())/ 2, row_height * 8);
        stage.addActor(sliderIncreasingDifficulty);
    }

    private void sliderVolume() {
        sliderV = new Slider(0f,100f,1f,false, mySkin);
        sliderV.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderV.setWidth(selectBoxSize*2);
        sliderV.setPosition(col_width*8, row_height * 13);
        volumeText = new Label("Volume", mySkin);
        volumeText.setPosition(col_width *8 + (selectBoxSize) - volumeText.getWidth()/2, row_height *14);
        stage.addActor(sliderV);
        stage.addActor(volumeText);
    }

    public void soundEffects() {
        soundEffects = new CheckBox("Sound Effects", mySkin);
        soundEffects.setPosition(col_width*8 + selectBoxSize - soundEffects.getWidth()/2, row_height * 12);
        stage.addActor(soundEffects);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
