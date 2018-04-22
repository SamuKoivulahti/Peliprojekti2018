package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    Image background;
    Image tutorial;

    GameData gameData;

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

        background = new Image(new Texture("menupicture.jpg"));
        background.setSize(width,height);
        background.setPosition(0,0);

        tutorial = new Image(new Texture("tutorialpicture.jpg"));
        tutorial.setSize(width,height);
        tutorial.setPosition(0,0);
        tutorial.setVisible(false);

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        TextButton play = new TextButton("PLAY",mySkin,"small");
        play.setSize(col_width*1.8f,row_height*1.5f);
        play.setPosition(col_width * 0.5f,row_height/2);
        play.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainScreen", "Play");
                host.setSaveFileSelectScreen();
            }

        });

        TextButton freePlay = new TextButton("FREE PLAY",mySkin,"small");
        freePlay.setSize(col_width*1.8f,row_height*1.5f);
        freePlay.setPosition(col_width * 2.8f,row_height/2);
        freePlay.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainScreen", "Play");
                host.gameData.setProfileUsed(0);
                host.setCriminalScreen();
            }

        });

        TextButton settings = new TextButton("Settings",mySkin,"small");
        settings.setSize(col_width*1.8f,row_height*1.5f);
        settings.setPosition(col_width * 5.1f,row_height/2);
        settings.addListener(new ClickListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("MainScreen", "settings");
                host.setSettingsScreen();
            }

        });

        TextButton exit = new TextButton("Exit",mySkin,"small");
        exit.setSize(col_width*1.8f,row_height*1.5f);
        exit.setPosition(col_width * 9.7f,row_height/2);
        exit.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("MainScreen", "exit");
                Gdx.app.exit();
            }

        });

        final TextButton tutorialButton = new TextButton("Controls",mySkin,"small");
        tutorialButton.setSize(col_width*1.8f,row_height*1.5f);
        tutorialButton.setPosition(col_width * 7.4f,row_height/2);
        tutorialButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tutorial.isVisible()) {
                    tutorial.setVisible(false);
                    tutorialButton.setPosition(col_width*7.4f,row_height/2);
                    tutorialButton.setText("Controls");
                    Gdx.app.log("MainScreen", "back");
                } else {
                    tutorial.setVisible(true);
                    tutorialButton.setPosition(col_width*3.5f,row_height/2);
                    tutorialButton.setText("Back");
                    Gdx.app.log("MainScreen", "tutorial");
                }
            }

        });

        stage.addActor(background);
        stage.addActor(play);
        stage.addActor(freePlay);
        stage.addActor(settings);
        stage.addActor(exit);
        stage.addActor(tutorial);
        stage.addActor(tutorialButton);

    }

    @Override
    public void show() {
        host.updateSettings();
        host.gameData.setLevel(0);
        host.gameData.setPoints(0);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
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
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
