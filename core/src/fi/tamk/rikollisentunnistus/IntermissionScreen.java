package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Samu Koivulahti on 25.3.2018.
 */

public class IntermissionScreen implements Screen {

    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Stage stage;
    private Label winText;
    private Label loseText;
    private Label pointsText;
    private Label levelText;
    private Label gameEndText;
    boolean win;

    Skin mySkin;

    float row_height;
    float col_width;
    float elapsedTime;
    int timeWaiting = 3;
    int gameEndWait = 5;
    Settings settings;

    public IntermissionScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);

        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        win = game.gameData.getWin();
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));
        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        settings = Settings.getInstance();
        elapsedTime = 0;

        textPrint();
        buttonBack();
    }

    public void buttonBack() {
        Button back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,camera.viewportHeight - back.getHeight());
        back.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("TAG", "back");
                game.resetAll();
                MainScreen MainScreen = new MainScreen(game);
                game.setScreen(MainScreen);
            }
        });
        stage.addActor(back);
    }

    public void textPrint() {
        winText = new Label("Correct!", mySkin, "big");
        winText.setPosition(camera.viewportWidth/2 - winText.getWidth()/2, camera.viewportHeight/2 - winText.getHeight() / 2);
        loseText = new Label("Wrong!", mySkin, "big");
        loseText.setPosition(camera.viewportWidth/2 - loseText.getWidth()/2, camera.viewportHeight/2 - loseText.getHeight() / 2);
        pointsText = new Label("points: " + game.gameData.getPoints(), mySkin);
        pointsText.setPosition(camera.viewportWidth/2 - pointsText.getWidth()/2, camera.viewportHeight/12 * 5);
        levelText = new Label("Level " + game.gameData.getLevel(), mySkin, "big");
        levelText.setPosition(camera.viewportWidth/2 - levelText.getWidth()/2, camera.viewportHeight/12 * 10);
        gameEndText = new Label("Congratulations! You got " + game.gameData.getPoints() + " points!", mySkin, "big");
        gameEndText.setPosition(camera.viewportWidth/2 - gameEndText.getWidth()/2, camera.viewportHeight/2 - gameEndText.getHeight() / 2);


        stage.addActor(winText);
        stage.addActor(loseText);
        stage.addActor(pointsText);
        stage.addActor(levelText);
        stage.addActor(gameEndText);

        if (settings.getInteger("roundAmount") == game.gameData.getLevel()) {
            gameEndText.setVisible(true);
            winText.setVisible(false);
            loseText.setVisible(false);
            pointsText.setVisible(false);
        } else {
            if (win) {
                winText.setVisible(true);
                loseText.setVisible(false);
            } else {
                winText.setVisible(false);
                loseText.setVisible(true);
            }
            gameEndText.setVisible(false);
        }
    }

    public boolean timer(int timeToPass) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= timeToPass) {
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (settings.getInteger("roundAmount") == game.gameData.getLevel()) {
            Gdx.gl.glClearColor(20 / 255f, 180 / 255f, 255 / 255f, 1);
        } else {
            if (win) {
                Gdx.gl.glClearColor(20 / 255f, 170 / 255f, 150 / 255f, 1);
            } else {
                Gdx.gl.glClearColor(255 / 255f, 100 / 255f, 20 / 255f, 1);
            }
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (timer(timeWaiting) && settings.getInteger("roundAmount") != game.gameData.getLevel()) {
            elapsedTime = 0;
            game.resetAll();
            game.setCriminalScreen();
        } else if (settings.getInteger("roundAmount") == game.gameData.getLevel()) {
            if (timer(gameEndWait)) {
                elapsedTime = 0;
                MainScreen mainScreen = new MainScreen(game);
                game.setScreen(mainScreen);
            }
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
