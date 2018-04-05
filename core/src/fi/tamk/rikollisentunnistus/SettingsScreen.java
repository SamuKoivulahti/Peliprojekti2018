package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import javax.swing.GroupLayout;


/**
 * Created by Samu Koivulahti on 17.3.2018.
 */
//TODO: boolean useDifficulty, int startingDifficulty 0-14, boolean incresingDifficulty
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

    float valueRight;
    float valueLeft;
    float valueUp;
    float valueDown;
    boolean valueAssets;
    int valueRow;
    int valueAttribute;
    int valueRound;
    int valueStartingDifficulty;
    boolean valueUseDifficulty;
    boolean valueIncreasingDifficulty;

    float sliderSize;

    float row_height;
    float col_width;
    float height;
    float width;

    private Label calibrateText;
    private Label sameAttributesText;
    private Label rowLengthText;
    private Label roundsText;
    private Label staringDifficultyText;

    private Texture sensitivityGraphTexture;
    private Image sensitivityGraphImage;


    Skin mySkin;
    Settings settings;


    public SettingsScreen(Rikollisentunnistus host) {
        this.host = host;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 650);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        row_height = camera.viewportHeight / 15;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        sliderSize = camera.viewportWidth * 0.15f;

        sensitivityGraphTexture = new Texture("sensitivityGraph.png");
        sensitivityGraphImage = new Image(sensitivityGraphTexture);
        sensitivityGraphImage.setPosition(col_width*4 - sliderSize, row_height * 8 - sliderSize);
        sensitivityGraphImage.setSize(sliderSize*2, sliderSize*2);
        sensitivityGraphImage.setOrigin(Align.center);

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        calibrateText = new Label("Calibrate", mySkin);
        calibrateText.setPosition(col_width*4 - calibrateText.getWidth()/2, row_height * 14 - calibrateText.getHeight());

        settings = Settings.getInstance();

        valueRight = settings.getFloat("sensitivityRight");
        valueLeft = settings.getFloat("sensitivityLeft");
        valueUp = settings.getFloat("sensitivityUp");
        valueDown = settings.getFloat("sensitivityDown");
        valueRow = settings.getInteger("rowLength");
        valueAttribute = settings.getInteger("sameAttributes");
        valueAssets = settings.getBoolean("assets");
        valueRound = settings.getInteger("roundAmount");
        valueStartingDifficulty = settings.getInteger("startingDifficulty");
        valueUseDifficulty = settings.getBoolean("useDifficulty");
        valueIncreasingDifficulty = settings.getBoolean("increasingDifficulty");

        stage.addActor(calibrateText);
        stage.addActor(sensitivityGraphImage);
        sliderRight();
        sliderLeft();
        sliderUp();
        sliderDown();
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
    }

    public void buttonBack() {
        Button back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.log("TAG", "back");
                host.setMainScreen();
            }

        });
        stage.addActor(back);
    }

    public void buttonSave() {
        Button save = new TextButton("SAVE",mySkin,"small");
        save.setSize(col_width*2,row_height*2);
        save.setPosition(0,0);
        save.addListener(new ClickListener(){

            @Override
            public void clicked (InputEvent event, float x, float y) {
                Gdx.app.log("TAG", "save");
                //settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                //settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                //Gdx.app.log("save", "accel Y: "+ settings.getFloat("zeroPointY"));
                settings.setFloat("sensitivityRight", valueRight);
                settings.setFloat("sensitivityLeft", valueLeft);
                settings.setFloat("sensitivityUp", valueUp);
                settings.setFloat("sensitivityDown", valueDown);
                settings.setInteger("rowLength", sliderRow.getSelectedIndex()+3);
                settings.setInteger("sameAttributes", sliderAttribute.getSelectedIndex());
                settings.setBoolean("assets", sliderA.isChecked());
                settings.setInteger("roundAmount", sliderRound.getSelectedIndex()+5);
                settings.setInteger("startingDifficulty", sliderStaringDifficulty.getSelectedIndex());
                settings.setBoolean("useDifficulty", sliderUseDifficulty.isChecked());
                settings.setBoolean("increasingDifficulty", sliderIncreasingDifficulty.isChecked());
                settings.saveSettings();
                host.controls.updateControls();
            }
        });
        stage.addActor(save);
    }

    public void buttonCalibrate() {
        Button calibrate = new TextButton("Calibrate", mySkin, "small");
        calibrate.setSize(col_width*2, row_height*2);
        calibrate.setPosition(col_width*4 - calibrate.getWidth()/2, row_height * 1.5f);
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
        Gdx.app.log("SettingsScreen", "zeropointX" + settings.getFloat("zeroPointX"));
        Gdx.app.log("SettingsScreen", "zeropointY" + settings.getFloat("zeroPointY"));
    }

    public float sliderRight() {
        sliderR = new Slider(0f,10f,1f,false, mySkin);
        sliderR.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderR.setWidth(sliderSize);
        sliderR.setPosition(col_width*4, row_height * 8 - sliderR.getHeight()/2);
        sliderR.setValue(settings.getFloat("sensitivityRight"));
        sliderR.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Slider Right","slider up Value:"+sliderR.getValue());
                valueRight = sliderR.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Slider Right","slider down Value:"+sliderR.getValue());
                return true;
            }
        });
        stage.addActor(sliderR);
        return valueRight;
    }

    public float sliderLeft() {
        sliderL = new Slider(-10f,0f,1f,false, mySkin);
        sliderL.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderL.setWidth(sliderSize);
        sliderL.setPosition(col_width*4 - sliderL.getWidth(), row_height * 8 - sliderR.getHeight()/2);
        sliderL.setValue(settings.getFloat("sensitivityLeft"));
        sliderL.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderL.getValue());
                valueLeft = sliderL.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderL.getValue());
                return true;
            }
        });

        stage.addActor(sliderL);
        return valueLeft;
    }

    public float sliderUp() {
        sliderU = new Slider(0f,10f,1f,true, mySkin);
        sliderU.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderU.setHeight(sliderSize);
        sliderU.setPosition(col_width*4 - sliderU.getWidth()/2, row_height * 8);
        sliderU.setValue(settings.getFloat("sensitivityUp"));
        sliderU.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+sliderU.getValue());
                valueUp = sliderU.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+sliderU.getValue());
                return true;
            }
        });
        stage.addActor(sliderU);
        return valueUp;
    }

    public float sliderDown() {
        sliderD = new Slider(-10f,0f,1f,true, mySkin);
        sliderD.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderD.setHeight(sliderSize);
        sliderD.setPosition(col_width*4 - sliderD.getWidth()/2, row_height * 8 - sliderD.getHeight());
        sliderD.setValue(settings.getFloat("sensitivityDown"));
        sliderD.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+sliderD.getValue());
                valueDown = sliderD.getValue();


            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+sliderD.getValue());
                return true;
            }
        });
        stage.addActor(sliderD);
        return valueDown;
    }

    public boolean assetSlider() {
        sliderA = new CheckBox("Accessories", mySkin);
        sliderA.setPosition(col_width *8 + (sliderSize*2 - sliderA.getWidth())/ 2, row_height * 11);
        sliderA.setChecked(settings.getBoolean("assets"));
        stage.addActor(sliderA);
        return valueAssets;
    }

    public int rowSlider() {
        sliderRow = new SelectBox(mySkin);
        String[] array = {"3","4","5","6"};
        sliderRow.setItems(array);
        sliderRow.setWidth(sliderSize*2);
        sliderRow.setPosition(col_width *8, row_height * 9);
        sliderRow.setSelectedIndex(settings.getInteger("rowLength")-3);
        rowLengthText = new Label("Rows Length", mySkin);
        rowLengthText.setPosition(col_width *8 + (sliderSize) - rowLengthText.getWidth()/2, row_height *10);
        stage.addActor(rowLengthText);
        stage.addActor(sliderRow);
        return valueRow;
    }

    public int attributeSlider() {
        sliderAttribute = new SelectBox(mySkin);
        String[] array = {"0", "1", "2", "4"};
        sliderAttribute.setItems(array);
        sliderAttribute.setWidth(sliderSize*2);
        sliderAttribute.setPosition(col_width *8, row_height * 7);
        sliderAttribute.setSelectedIndex(settings.getInteger("sameAttributes"));
        sameAttributesText = new Label("Same Attributes", mySkin);
        sameAttributesText.setPosition(col_width *8 + (sliderSize) - sameAttributesText.getWidth()/2, row_height *8);
        stage.addActor(sameAttributesText);
        stage.addActor(sliderAttribute);

        return valueAttribute;
    }

    public int roundSlider() {
        sliderRound = new SelectBox(mySkin);
        String[] array = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderRound.setItems(array);
        sliderRound.setWidth(sliderSize*2);
        sliderRound.setPosition(col_width *8, row_height * 13);
        sliderRound.setSelectedIndex(settings.getInteger("roundAmount")-5);
        roundsText = new Label("Round Amount", mySkin);
        roundsText.setPosition(col_width *8 + (sliderSize) - roundsText.getWidth()/2, row_height *14);
        stage.addActor(roundsText);
        stage.addActor(sliderRound);

        return valueRound;
    }

    public int startingDifficultySlider() {
        sliderStaringDifficulty= new SelectBox(mySkin);
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        sliderStaringDifficulty.setItems(array);
        sliderStaringDifficulty.setWidth(sliderSize*2);
        sliderStaringDifficulty.setPosition(col_width *8, row_height * 2);
        sliderStaringDifficulty.setSelectedIndex(settings.getInteger("startingDifficulty")-1);
        staringDifficultyText = new Label("Starting Difficulty", mySkin);
        staringDifficultyText.setPosition(col_width *8 + (sliderSize) - roundsText.getWidth()/2, row_height *3);
        stage.addActor(staringDifficultyText);
        stage.addActor(sliderStaringDifficulty);

        return valueStartingDifficulty;
    }

    public boolean useDifficultySlider() {
        sliderUseDifficulty = new CheckBox("Use Difficulty", mySkin);
        sliderUseDifficulty.setPosition(col_width *8 + (sliderSize*2 - sliderUseDifficulty.getWidth())/ 2, row_height * 5);
        sliderUseDifficulty.setChecked(settings.getBoolean("useDifficulty"));
        stage.addActor(sliderUseDifficulty);
        return valueUseDifficulty;
    }

    public boolean increasingDifficultySlider() {
        sliderIncreasingDifficulty = new CheckBox("Increasing Difficulty", mySkin);
        sliderIncreasingDifficulty.setPosition(col_width *8 + (sliderSize*2 - sliderIncreasingDifficulty.getWidth())/ 2, row_height * 4);
        sliderIncreasingDifficulty.setChecked(settings.getBoolean("increasingDifficulty"));
        stage.addActor(sliderIncreasingDifficulty);
        return valueIncreasingDifficulty;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
