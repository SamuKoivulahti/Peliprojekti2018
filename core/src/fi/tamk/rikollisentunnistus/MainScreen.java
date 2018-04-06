package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Samu Koivulahti on 17.3.2018.
 */

public class MainScreen implements Screen {
    private float row_height;
    private float col_width;
    private float height;
    private float width;
    Rikollisentunnistus host;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    Skin mySkin;
    Stage stage;

    public MainScreen(final Rikollisentunnistus host) {
        Gdx.app.log("MainScreen", "Constructor");
        this.host = host;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth, camera.viewportHeight));
        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        Button play = new TextButton("PLAY",mySkin,"small");
        play.setSize(col_width*2,row_height*2);
        play.setPosition(col_width * 5,row_height*7);
        play.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainScreen", "Play");
                host.setCriminalScreen();
            }

        });

        Button settings = new TextButton("Settings",mySkin,"small");
        settings.setSize(col_width*2,row_height*2);
        settings.setPosition(col_width * 5,row_height*3);
        settings.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                host.setSettingsScreen();
            }

        });

        stage.addActor(play);
        stage.addActor(settings);

    }

    @Override
    public void show() {
        host.updateSettings();
        host.gameData.setLevel(0);
        host.gameData.setPoints(0);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(70/255f,130/255f,180/255f, 1);
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
