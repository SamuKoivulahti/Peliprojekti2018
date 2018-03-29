package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;


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
    public Slider sliderA;
    public Slider sliderRow;
    public Slider sliderAttribute;
    public Slider sliderRound;
    public Slider sliderStaringDifficulty;
    public Slider sliderUseDifficulty;
    public Slider sliderIncreasingDifficulty;

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

    private InputMultiplexer multiplexer;

    float row_height;
    float col_width;
    

    private Label calibrateText;
    private Label assetsText;
    private Label sameAttributesText;
    private Label rowLengthText;
    private Label roundsText;
    private Label onText;
    private Label offText;
    private Label rowLengthMinText;
    private Label rowLengthMaxText;
    private Label sameAttributesMinText;
    private Label sameAttributesMaxText;
    private Label roundsMinText;
    private Label roundsMaxText;
    private Label staringDifficultyText;
    private Label staringDifficultyMinText;
    private Label staringDifficultyMaxText;
    private Label useDifficultyText;
    private Label increasingDifficultyText;


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

        sliderSize = camera.viewportWidth * 0.15f;

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
    }

    public void buttonBack() {
        Button back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,camera.viewportHeight - back.getHeight());
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
        save.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "save");
                settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                settings.setFloat("sensitivityRight", valueRight);
                settings.setFloat("sensitivityLeft", valueLeft);
                settings.setFloat("sensitivityUp", valueUp);
                settings.setFloat("sensitivityDown", valueDown);
                settings.setInteger("rowLength", valueRow);
                settings.setInteger("sameAttributes", valueAttribute);
                settings.setBoolean("assets", valueAssets);
                settings.setInteger("roundAmount", valueRound);
                settings.setInteger("startingDifficulty", valueStartingDifficulty);
                settings.setBoolean("useDifficulty", valueUseDifficulty);
                settings.setBoolean("increasingDifficulty", valueIncreasingDifficulty);
                settings.saveSettings();

                // rowlength, sameattributes, accessories, rounds
                return true;
            }

        });
        stage.addActor(save);

    }

    public float sliderRight() {
        sliderR = new Slider(0f,10f,0.5f,false, mySkin);
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
                Gdx.app.log("up","slider Value:"+sliderR.getValue());
                valueRight = sliderR.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+sliderR.getValue());
                return true;
            }
        });
        stage.addActor(sliderR);
        return valueRight;
    }

    public float sliderLeft() {
        sliderL = new Slider(-10f,0f,0.5f,false, mySkin);
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
        sliderU = new Slider(0f,10f,0.5f,true, mySkin);
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
        sliderD = new Slider(-10f,0f,0.5f,true, mySkin);
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
        sliderA = new Slider(0,1,1,false, mySkin);
        sliderA.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderA.setWidth(sliderSize/2);
        sliderA.setPosition(col_width *8 + (sliderSize*2 - sliderA.getWidth())/ 2, row_height * 13);
        sliderA.setValue(settings.getBoolean("assets")? 1 : 0);
        sliderA.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderA.getValue());
                valueAssets = (sliderA.getValue() == 1);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderA.getValue());
                return true;
            }
        });
        assetsText = new Label("Accessories", mySkin);
        assetsText.setPosition(col_width * 8 + (sliderSize) - assetsText.getWidth()/2, row_height * 14);
        onText = new Label("ON", mySkin);
        onText.setPosition(col_width *8 + (sliderSize*2), row_height * 13);
        offText = new Label("OFF", mySkin);
        offText.setPosition(col_width * 8 - offText.getWidth(), row_height * 13);
        stage.addActor(onText);
        stage.addActor(offText);
        stage.addActor(assetsText);
        stage.addActor(sliderA);
        return valueAssets;
    }

    public int rowSlider() {
        sliderRow = new Slider(3,6,1,false, mySkin);
        sliderRow.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderRow.setWidth(sliderSize*2);
        sliderRow.setPosition(col_width *8, row_height * 11);
        sliderRow.setValue(settings.getInteger("rowLength"));
        sliderRow.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderRow.getValue());
                valueRow = (int)sliderRow.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderRow.getValue());
                return true;
            }
        });
        rowLengthText = new Label("Rows Length", mySkin);
        rowLengthText.setPosition(col_width *8 + (sliderSize) - rowLengthText.getWidth()/2, row_height *12);
        rowLengthMinText = new Label("3", mySkin);
        rowLengthMinText.setPosition(col_width * 8 - rowLengthMinText.getWidth(), row_height * 11);
        rowLengthMaxText = new Label("6", mySkin);
        rowLengthMaxText.setPosition(col_width *8 + (sliderSize*2)+rowLengthMaxText.getWidth(), row_height * 11);
        stage.addActor(rowLengthText);
        stage.addActor(rowLengthMinText);
        stage.addActor(rowLengthMaxText);
        stage.addActor(sliderRow);
        return valueRow;
    }

    public int attributeSlider() {
        sliderAttribute = new Slider(0,4,1,false, mySkin);
        sliderAttribute.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderAttribute.setWidth(sliderSize*2);
        sliderAttribute.setPosition(col_width *8, row_height * 9);
        sliderAttribute.setValue(settings.getInteger("sameAttributes"));
        sliderAttribute.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                valueAttribute = (int)sliderAttribute.getValue();
                Gdx.app.log("up","slider Value:"+ sliderAttribute.getValue());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderAttribute.getValue());
                return true;
            }
        });
        sameAttributesText = new Label("Same Attributes", mySkin);
        sameAttributesText.setPosition(col_width *8 + (sliderSize) - sameAttributesText.getWidth()/2, row_height *10);
        sameAttributesMinText = new Label("0", mySkin);
        sameAttributesMinText.setPosition(col_width * 8 - sameAttributesMinText.getWidth(), row_height * 9);
        sameAttributesMaxText = new Label("4", mySkin);
        sameAttributesMaxText.setPosition(col_width *8 + (sliderSize*2)+sameAttributesMaxText.getWidth(), row_height * 9);
        stage.addActor(sameAttributesText);
        stage.addActor(sameAttributesMinText);
        stage.addActor(sameAttributesMaxText);
        stage.addActor(sliderAttribute);

        return valueAttribute;
    }

    public int roundSlider() {
        sliderRound = new Slider(5,15,1,false, mySkin);
        sliderRound.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderRound.setWidth(sliderSize*2);
        sliderRound.setPosition(col_width *8, row_height * 7);
        sliderRound.setValue(settings.getInteger("roundAmount"));
        sliderRound.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                valueRound = (int)sliderRound.getValue();
                Gdx.app.log("up","slider Value:"+ sliderRound.getValue());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderRound.getValue());
                return true;
            }
        });
        roundsText = new Label("Round Amount", mySkin);
        roundsText.setPosition(col_width *8 + (sliderSize) - roundsText.getWidth()/2, row_height *8);
        roundsMinText = new Label("5", mySkin);
        roundsMinText.setPosition(col_width * 8 - rowLengthMinText.getWidth(), row_height * 7);
        roundsMaxText = new Label("15", mySkin);
        roundsMaxText.setPosition(col_width *8 + (sliderSize*2), row_height * 7);
        stage.addActor(roundsText);
        stage.addActor(roundsMinText);
        stage.addActor(roundsMaxText);
        stage.addActor(sliderRound);

        return valueRound;
    }

    public int startingDifficultySlider() {
        sliderStaringDifficulty= new Slider(0,14,1,false, mySkin);
        sliderStaringDifficulty.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderStaringDifficulty.setWidth(sliderSize*2);
        sliderStaringDifficulty.setPosition(col_width *8, row_height * 5);
        sliderStaringDifficulty.setValue(settings.getInteger("startingDifficulty"));
        sliderStaringDifficulty.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                valueStartingDifficulty = (int)sliderStaringDifficulty.getValue();
                Gdx.app.log("up","slider Value:"+ sliderStaringDifficulty.getValue());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderStaringDifficulty.getValue());
                return true;
            }
        });
        staringDifficultyText = new Label("Starting Difficulty", mySkin);
        staringDifficultyText.setPosition(col_width *8 + (sliderSize) - roundsText.getWidth()/2, row_height *6);
        staringDifficultyMinText = new Label("0", mySkin);
        staringDifficultyMinText.setPosition(col_width * 8 - rowLengthMinText.getWidth(), row_height * 5);
        staringDifficultyMaxText = new Label("14", mySkin);
        staringDifficultyMaxText.setPosition(col_width *8 + (sliderSize*2), row_height * 5);
        stage.addActor(staringDifficultyText);
        stage.addActor(staringDifficultyMinText);
        stage.addActor(staringDifficultyMaxText);
        stage.addActor(sliderStaringDifficulty);

        return valueStartingDifficulty;
    }

    public boolean useDifficultySlider() {
        sliderUseDifficulty = new Slider(0,1,1,false, mySkin);
        sliderUseDifficulty.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderUseDifficulty.setWidth(sliderSize/2);
        sliderUseDifficulty.setPosition(col_width *8 + (sliderSize*2 - sliderUseDifficulty.getWidth())/ 2, row_height * 3);
        sliderUseDifficulty.setValue(settings.getBoolean("useDifficulty")? 1 : 0);
        sliderUseDifficulty.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderUseDifficulty.getValue());
                valueUseDifficulty = (sliderUseDifficulty.getValue() == 1);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderUseDifficulty.getValue());
                return true;
            }
        });
        useDifficultyText = new Label("Use Difficulty", mySkin);
        useDifficultyText.setPosition(col_width * 8 + (sliderSize) - useDifficultyText.getWidth()/2, row_height * 4);
        onText = new Label("ON", mySkin);
        onText.setPosition(col_width *8 + (sliderSize*2), row_height * 3);
        offText = new Label("OFF", mySkin);
        offText.setPosition(col_width * 8 - offText.getWidth(), row_height * 3);
        stage.addActor(onText);
        stage.addActor(offText);
        stage.addActor(useDifficultyText);
        stage.addActor(sliderUseDifficulty);
        return valueUseDifficulty;
    }

    public boolean increasingDifficultySlider() {
        sliderIncreasingDifficulty = new Slider(0,1,1,false, mySkin);
        sliderIncreasingDifficulty.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderIncreasingDifficulty.setWidth(sliderSize/2);
        sliderIncreasingDifficulty.setPosition(col_width *8 + (sliderSize*2 - sliderA.getWidth())/ 2, row_height * 1);
        sliderIncreasingDifficulty.setValue(settings.getBoolean("increasingDifficulty")? 1 : 0);
        sliderIncreasingDifficulty.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderIncreasingDifficulty.getValue());
                valueIncreasingDifficulty = (sliderIncreasingDifficulty.getValue() == 1);

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderIncreasingDifficulty.getValue());
                return true;
            }
        });
        increasingDifficultyText = new Label("Increasing Difficulty", mySkin);
        increasingDifficultyText.setPosition(col_width * 8 + (sliderSize) - increasingDifficultyText.getWidth()/2, row_height * 2);
        onText = new Label("ON", mySkin);
        onText.setPosition(col_width *8 + (sliderSize*2), row_height * 1);
        offText = new Label("OFF", mySkin);
        offText.setPosition(col_width * 8 - offText.getWidth(), row_height * 1);
        stage.addActor(onText);
        stage.addActor(offText);
        stage.addActor(increasingDifficultyText);
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
