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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;


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
    public Slider sliderA;
    public Slider sliderRow;
    public Slider sliderAttribute;

    float valueRight;
    float valueLeft;
    float valueUp;
    float valueDown;
    float valueAssets;
    float valueRow;
    float valueAttribute;

    float sliderPositionX;
    float sliderPositionY;
    float sliderSize;

    private InputMultiplexer multiplexer;

    int row_height;
    int col_width;
    float width;
    float height;

    Skin mySkin;


    public SettingsScreen(Rikollisentunnistus host) {
        this.host = host;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 650);

        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        sliderSize = Gdx.graphics.getWidth()*0.15f;
        sliderPositionX = width -sliderSize-10f;
        sliderPositionY = height - sliderSize-10f;

        stage = new Stage();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        sliderRight();
        sliderLeft();
        sliderUp();
        sliderDown();
        assetSlider();
        rowSlider();
        attributeSlider();
    }

    public float sliderRight() {
        sliderR = new Slider(0f,10f,0.5f,false, mySkin);
        sliderR.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderR.setWidth(sliderSize);
        sliderR.setPosition(sliderPositionX, sliderPositionY - sliderR.getHeight()/2);
        sliderR.setValue(5f);
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
        sliderL.setPosition(sliderPositionX - sliderSize, sliderPositionY - sliderL.getHeight()/2);
        sliderL.setValue(-5f);
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
        sliderU = new Slider(-10f,0f,0.5f,true, mySkin);
        sliderU.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderU.setHeight(sliderSize);
        sliderU.setPosition(sliderPositionX - sliderU.getWidth()/2, sliderPositionY - sliderSize);
        sliderU.setValue(-5f);
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
        sliderD = new Slider(0f,10f,0.5f,true, mySkin);
        sliderD.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderD.setHeight(sliderSize);
        sliderD.setPosition(sliderPositionX - sliderU.getWidth()/2, sliderPositionY );
        sliderD.setValue(5f);
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

    public float assetSlider() {
        sliderA = new Slider(0f,1f,1f,false, mySkin);
        sliderA.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderA.setWidth(Gdx.graphics.getWidth()*0.15f/2);
        sliderA.setPosition(sliderPositionX - sliderA.getWidth()/2, sliderPositionY - (sliderSize) - sliderA.getHeight()/2 - 50f);
        sliderA.setValue(1f);
        sliderA.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderA.getValue());
                valueAssets = sliderA.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderA.getValue());
                return true;
            }
        });
        stage.addActor(sliderA);
        return valueAssets;
    }

    public float rowSlider() {
        sliderRow = new Slider(3f,6f,1f,false, mySkin);
        sliderRow.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderRow.setWidth(Gdx.graphics.getWidth()*0.15f*2);
        sliderRow.setPosition(sliderPositionX - sliderRow.getWidth()/2, sliderPositionY - sliderSize*2 - sliderRow.getHeight()/2);
        sliderRow.setValue(1f);
        sliderRow.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("up","slider Value:"+ sliderRow.getValue());
                valueRow = sliderRow.getValue();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderRow.getValue());
                return true;
            }
        });
        stage.addActor(sliderRow);
        return valueRow;
    }

    public float attributeSlider() {
        sliderAttribute = new Slider(0f,4f,1f,false, mySkin);
        sliderAttribute.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderAttribute.setWidth(Gdx.graphics.getWidth()*0.15f*2);
        sliderAttribute.setPosition(sliderPositionX - sliderAttribute.getWidth()/2, sliderPositionY - (sliderSize) - sliderAttribute.getHeight()/2 - 225f);
        sliderAttribute.setValue(2f);
        sliderAttribute.addListener(new InputListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Gdx.app.log("touchDragged","slider Value:"+slider.getValue());
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                valueAttribute = sliderAttribute.getValue();
                Gdx.app.log("up","slider Value:"+ sliderAttribute.getValue());
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("down","slider Value:"+ sliderAttribute.getValue());
                return true;
            }
        });
        stage.addActor(sliderAttribute);

        return valueAttribute;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
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
    }
}
