package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Koivulahti on 20.4.2018.
 */

public class SaveFileSelectScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    MyTextInputListener listener;
    Skin mySkin;
    private Stage stage;
    private float row_height;
    private float col_width;
    private float height;
    private float width;
    TextButton profile1Button;
    TextButton profile2Button;
    TextButton profile3Button;
    TextButton back;
    int profileUsed;
    GameData gameData;
    String InputIsValitaded;

    public SaveFileSelectScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;
        InputIsValitaded = null;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        profile1Button();
        profile2Button();
        profile3Button();
        buttonBack();
    }

    public void profile1Button() {
        profile1Button = new TextButton(game.saveFiles.getString("name1", game.texts.get(46)), mySkin);
        profile1Button.setHeight(row_height*1.5f);
        profile1Button.setPosition(width/2 - profile1Button.getWidth()/2, row_height*8.5f);
        profile1Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                profile1Button.setTouchable(Touchable.disabled);
                profile2Button.setTouchable(Touchable.disabled);
                profile3Button.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);
                game.gameData.setProfileUsed(1);
                SoundManager.stopMenuMusic();
                if (profile1Button.getText().toString().equals(game.texts.get(46))) {
                    listener = new MyTextInputListener(SaveFileSelectScreen.this, "name1");
                    Gdx.input.getTextInput(listener, game.texts.get(47), "", game.texts.get(48));
                } else {
                    game.setCriminalScreen();
                }
            }

        });

        stage.addActor(profile1Button);
    }

    public void profile2Button() {
        profile2Button = new TextButton(game.saveFiles.getString("name2", game.texts.get(46)),mySkin);
        profile2Button.setHeight(row_height*1.5f);
        profile2Button.setPosition(width/2 - profile2Button.getWidth()/2,height/2 - profile2Button.getHeight()/2);
        profile2Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                profile1Button.setTouchable(Touchable.disabled);
                profile2Button.setTouchable(Touchable.disabled);
                profile3Button.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);
                game.gameData.setProfileUsed(2);
                SoundManager.stopMenuMusic();
                if (profile2Button.getText().toString().equals(game.texts.get(46))) {
                    listener = new MyTextInputListener(SaveFileSelectScreen.this, "name2");
                    Gdx.input.getTextInput(listener, game.texts.get(47), "", game.texts.get(48));
                } else {
                    game.setCriminalScreen();
                }
            }

        });

        stage.addActor(profile2Button);
    }

    public void profile3Button() {
        profile3Button = new TextButton(game.saveFiles.getString("name3", game.texts.get(46)),mySkin);
        profile3Button.setHeight(row_height*1.5f);
        profile3Button.setPosition(width/2 - profile3Button.getWidth()/2, row_height*2);
        profile3Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                profile1Button.setTouchable(Touchable.disabled);
                profile2Button.setTouchable(Touchable.disabled);
                profile3Button.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);
                game.gameData.setProfileUsed(3);
                SoundManager.stopMenuMusic();
                if (profile3Button.getText().toString().equals(game.texts.get(46))) {
                    listener = new MyTextInputListener(SaveFileSelectScreen.this, "name3");
                    Gdx.input.getTextInput(listener, game.texts.get(47), "", game.texts.get(48));
                } else {
                    game.setCriminalScreen();
                }
            }

        });

        stage.addActor(profile3Button);
    }

    public void buttonBack() {
        back = new TextButton(game.texts.get(6),mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "back");
                game.setMainScreen();
            }

        });

        stage.addActor(back);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        profile1Button.setText(game.saveFiles.getString("name1", game.texts.get(46)));
        profile2Button.setText(game.saveFiles.getString("name2", game.texts.get(46)));
        profile3Button.setText(game.saveFiles.getString("name3", game.texts.get(46)));
        game.saveFiles.saveNewFiles();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(70/255f,130/255f,180/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (InputIsValitaded != null) {
            game.saveFiles.setString(listener.ID, listener.getText());
            game.setCriminalScreen();
            InputIsValitaded = null;
        }
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
        profile1Button.setTouchable(Touchable.enabled);
        profile2Button.setTouchable(Touchable.enabled);
        profile3Button.setTouchable(Touchable.enabled);
        back.setTouchable(Touchable.enabled);
    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
