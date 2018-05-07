package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Essi Supponen on 07/05/2018.
 */

public class TutorialScreen implements Screen {
    Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Stage stage;
    Skin mySkin;

    float row_height;
    float col_width;
    float height;
    float width;



    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,1);
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

    }
}
