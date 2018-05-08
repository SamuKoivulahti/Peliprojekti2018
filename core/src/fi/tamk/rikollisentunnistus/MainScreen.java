package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @author Samu Koivulahti
 * @version 1.6
 * @since 17.3.2018
 *
 * Introduces the game. Ables player to navigate to settings or game
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
    Image title;

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

        if (host.texts.get(0).equals("EN")) {
            tutorial = new Image(new Texture("tutorialpicture.jpg"));
        } else {
            tutorial = new Image(new Texture("tutorialpicturefi.jpg"));
        }
        tutorial.setSize(width,height);
        tutorial.setPosition(0,0);
        tutorial.setVisible(false);

        title = new Image(new Texture("textimages/en_title.png"));
        title.setPosition(width*5/8f - title.getWidth()/2, height*5/6f);
        title.setVisible(true);

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        final TextButton play = new TextButton(host.texts.get(1),mySkin,"small");
        play.setSize(col_width*1.8f,row_height*1.5f);
        play.setPosition(col_width * 0.5f,row_height/2);
        play.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.setSaveFileSelectScreen();
                Gdx.app.log("MainScreen", "Play");
            }

        });

        final TextButton freePlay = new TextButton(host.texts.get(2),mySkin,"small");
        freePlay.setSize(col_width*1.8f,row_height*1.5f);
        freePlay.setPosition(col_width * 2.8f,row_height/2);
        freePlay.addListener(new ClickListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("MainScreen", "Play");
                SoundManager.stopMenuMusic();
                host.gameData.setProfileUsed(0);
                host.setCriminalScreen();
            }

        });

        final TextButton settings = new TextButton(host.texts.get(3),mySkin,"small");
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

        final TextButton exit = new TextButton(host.texts.get(5),mySkin,"small");
        exit.setSize(col_width*1.8f,row_height*1.5f);
        exit.setPosition(col_width * 9.7f,row_height/2);
        exit.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("MainScreen", "exit");
                Gdx.app.exit();
            }

        });

        final TextButton tutorialButton = new TextButton(host.texts.get(4),mySkin,"small");
        tutorialButton.setSize(col_width*1.8f,row_height*1.5f);
        tutorialButton.setPosition(col_width * 7.4f,row_height/2);
        tutorialButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (tutorial.isVisible()) {
                    tutorial.setVisible(false);
                    tutorialButton.setPosition(col_width * 7.4f,row_height/2);
                    tutorialButton.setText(host.texts.get(4));
                    Gdx.app.log("MainScreen", "back");
                } else {
                    tutorial.setVisible(true);
                    tutorialButton.setPosition(col_width*3.5f,row_height/2);
                    tutorialButton.setText(host.texts.get(6));
                    Gdx.app.log("MainScreen", "tutorial");
                }
            }

        });

        final ImageButton finnishButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("flag_fi.png"))));
        finnishButton.setSize(finnishButton.getWidth()/2,finnishButton.getHeight()/2);
        finnishButton.setPosition(0,height-finnishButton.getHeight());
        finnishButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.updateLanguage("FI");
                play.setText(host.texts.get(1));
                freePlay.setText(host.texts.get(2));
                settings.setText(host.texts.get(3));
                tutorialButton.setText(host.texts.get(4));
                exit.setText(host.texts.get(5));
                tutorial = new Image(new Texture("tutorialpicturefi.jpg"));
                stage.addActor(tutorial);
                tutorial.setVisible(false);
                tutorialButton.toFront();
            }

        });

        final ImageButton englishButton= new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("flag_gb.png"))));
        englishButton.setSize(englishButton.getWidth()/2,englishButton.getHeight()/2);
        englishButton.setPosition(englishButton.getWidth(),height-englishButton.getHeight());
        englishButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(host.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                host.updateLanguage("EN");
                play.setText(host.texts.get(1));
                freePlay.setText(host.texts.get(2));
                settings.setText(host.texts.get(3));
                tutorialButton.setText(host.texts.get(4));
                exit.setText(host.texts.get(5));
                tutorial = new Image(new Texture("tutorialpicture.jpg"));
                stage.addActor(tutorial);
                tutorial.setVisible(false);
                tutorialButton.toFront();
            }
        });

        stage.addActor(background);
        stage.addActor(title);
        stage.addActor(play);
        stage.addActor(freePlay);
        stage.addActor(settings);
        stage.addActor(exit);
        stage.addActor(finnishButton);
        stage.addActor(englishButton);
        stage.addActor(tutorial);
        stage.addActor(tutorialButton);
    }

    @Override
    public void show() {
        host.updateSettings();
        host.gameData.setLevel(1);
        host.gameData.setPoints(0);

        Settings settings = Settings.getInstance();

        SoundManager.changeVolume(settings.getFloat("volume", 50)/100f);
        SoundManager.playMenuMusic();

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
