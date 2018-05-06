package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Samu Koivulahti on 4.5.2018.
 */

public class RankScreen implements Screen {
    Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Stage stage;
    Skin mySkin;

    float row_height;
    float col_width;
    float height;
    float width;

    int rank;

    public RankScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        height = camera.viewportHeight;
        width = camera.viewportWidth;

        if (game.gameData.getProfileUsed() == 1) {
            game.saveFiles.setInteger("points1", 0);
            rank = game.saveFiles.getInteger("rank1", GameData.DEFAULT_RANK);
            game.saveFiles.setInteger("rank1", rank + 1);
        } else if (game.gameData.getProfileUsed() == 2) {
            game.saveFiles.setInteger("points2", 0);
            rank = game.saveFiles.getInteger("rank2", GameData.DEFAULT_RANK);
            game.saveFiles.setInteger("rank2", rank + 1);
        } else if (game.gameData.getProfileUsed() == 3) {
            game.saveFiles.setInteger("points3", 0);
            rank = game.saveFiles.getInteger("rank3", GameData.DEFAULT_RANK);
            game.saveFiles.setInteger("rank3", rank + 1);
        }
        game.saveFiles.saveNewFiles();

        Label rankText = new Label(game.script.get("Rank" + (rank + 1)), mySkin);
        rankText.setPosition(width/2 - rankText.getWidth()/2, height/2 - rankText.getHeight()/2);
        stage.addActor(rankText);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            game.resetAll();
        }
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
    }
}
