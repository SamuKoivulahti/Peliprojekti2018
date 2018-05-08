package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    private Image backgroundImage;

    float row_height;
    float col_width;
    float height;
    float width;

    Label rankText;
    Label oldRankText;
    Label continueText;
    MoveToAction rankToRight;
    MoveToAction oldRankToRight;

    int rank;
    float elapsedTime;

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

        backgroundImage = new Image(new Texture("criminalscreenbackground.jpg"));
        backgroundImage.setBounds(0,0,width,height);
        stage.addActor(backgroundImage);

        elapsedTime = 0;

        if (game.gameData.getProfileUsed() == 1) {
            game.saveFiles.setInteger("points1", 0);
            rank = game.saveFiles.getInteger("rank1", GameData.DEFAULT_RANK);
            if (rank == 0) {
                game.saveFiles.setInteger("rank1", rank + 1);
            } else if (rank < 14) {
                game.saveFiles.setInteger("difficulty1", game.saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY)+1);
                game.saveFiles.setInteger("rank1", rank + 1);
            } else {
                game.saveFiles.setBoolean("loop1", true);
            }
        } else if (game.gameData.getProfileUsed() == 2) {
            game.saveFiles.setInteger("points2", 0);
            rank = game.saveFiles.getInteger("rank2", GameData.DEFAULT_RANK);
            if (rank == 0) {
                game.saveFiles.setInteger("rank2", rank + 1);
            } else if (rank < 14) {
                game.saveFiles.setInteger("difficulty2", game.saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY)+1);
                game.saveFiles.setInteger("rank2", rank + 1);
            } else {
                game.saveFiles.setBoolean("loop2", true);
            }
        } else if (game.gameData.getProfileUsed() == 3) {
            game.saveFiles.setInteger("points3", 0);
            rank = game.saveFiles.getInteger("rank3", GameData.DEFAULT_RANK);
            if (rank == 0) {
                game.saveFiles.setInteger("rank3", rank + 1);
            } else if (rank < 14) {
                game.saveFiles.setInteger("difficulty3", game.saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY)+1);
                game.saveFiles.setInteger("rank3", rank + 1);
            } else {
                game.saveFiles.setBoolean("loop3", true);
            }
        }
        game.saveFiles.saveNewFiles();

        continueText = new Label(game.texts.get(49), mySkin);
        continueText.setPosition(width/2 - continueText.getWidth()/2, row_height*2);
        continueText.setVisible(false);
        stage.addActor(continueText);

        if (rank != 0 && rank < 15) {
            rankText = new Label(game.script.get("Rank" + (rank + 1)), mySkin, "big");
            rankText.setPosition(0 - rankText.getWidth(), height/2 - rankText.getHeight()/2);
            rankToRight = new MoveToAction();
            rankToRight.setPosition(width/2 - rankText.getWidth()/2, height/2 - rankText.getHeight()/2);
            rankToRight.setDuration(1.5f);
            oldRankText = new Label(game.script.get("Rank" + (rank)), mySkin, "big");
            oldRankText.setPosition(width/2 - oldRankText.getWidth()/2, height/2 - oldRankText.getHeight()/2);
            oldRankToRight = new MoveToAction();
            oldRankToRight.setPosition(width, height/2 - oldRankText.getHeight()/2);
            oldRankToRight.setDuration(1.5f);
            stage.addActor(oldRankText);
        } else if (rank == 0) {
            rankText = new Label(game.script.get("Rank" + (rank + 1)), mySkin, "big");
            rankText.setPosition(width/2 - rankText.getWidth()/2, height/2 - rankText.getHeight()/2);
        } else {
            rankText = new Label(game.script.get("Rank" + rank), mySkin, "big");
            rankText.setPosition(width/2 - rankText.getWidth()/2, height/2 - rankText.getHeight()/2);
        }
        stage.addActor(rankText);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(220/255f,223/255f,229/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && elapsedTime > 3.5f) {
            game.resetAll();
        }
        elapsedTime += delta;

        if (elapsedTime >= 2 && rank != 0 && rank < 15) {
            rankText.addAction(rankToRight);
            oldRankText.addAction(oldRankToRight);
        }

        if (elapsedTime > 3.5f) {
            continueText.setVisible(true);
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
