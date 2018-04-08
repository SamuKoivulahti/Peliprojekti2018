package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Label chosenCriminalText;
    private Label correctCriminalText;
    private Face chosenCriminal;
    private Face correctCriminal;
    boolean win;

    Skin mySkin;

    float row_height;
    float col_width;
    float height;
    float width;
    float elapsedTime;
    int timeWaiting = 3;
    int gameEndWait = 5;
    Settings settings;

    public IntermissionScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);

        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        win = game.gameData.getWin();
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));
        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        height = camera.viewportHeight;
        width = camera.viewportWidth;
        settings = Settings.getInstance();
        elapsedTime = 0;

        textPrint();
        buttonBack();
    }

    public void buttonBack() {
        Button back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
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
        winText.setPosition(width/2 - winText.getWidth()/2, height/2 - winText.getHeight() / 2);
        loseText = new Label("Wrong!", mySkin, "big");
        loseText.setPosition(width/2 - loseText.getWidth()/2, height/2 - loseText.getHeight() / 2);
        pointsText = new Label("points: " + game.gameData.getPoints(), mySkin);
        pointsText.setPosition(width/2 - pointsText.getWidth()/2, row_height * 5);
        levelText = new Label("Level " + game.gameData.getLevel(), mySkin, "big");
        levelText.setPosition(width/2 - levelText.getWidth()/2, row_height * 10);
        gameEndText = new Label("Congratulations! You got " + game.gameData.getPoints() + " points!", mySkin, "big");
        gameEndText.setPosition(width/2 - gameEndText.getWidth()/2, row_height*8);
        correctCriminalText = new Label("Correct criminal", mySkin);
        correctCriminalText.setPosition(width*5/6 - correctCriminalText.getWidth()/2, height*1/8f);
        chosenCriminalText = new Label("Your choice", mySkin);
        chosenCriminalText.setPosition(width/6 - correctCriminalText.getWidth()/2, height*1/8f);

        chosenCriminal = game.gameData.getChosenCriminal();
        correctCriminal = game.gameData.getCorrectCriminal();

        if (!win) {
            chosenCriminal = game.gameData.getChosenCriminal();
            correctCriminal = game.gameData.getCorrectCriminal();
            chosenCriminal.clearActions();
            chosenCriminal.setScale(1.0f);

            chosenCriminal.changeScale(0.75f);
            correctCriminal.changeScale(0.75f);

            chosenCriminal.setLocation(width/6,height/6);
            correctCriminal.setLocation(width*5/6, height/6);
        }

        stage.addActor(chosenCriminal);
        stage.addActor(correctCriminal);

        stage.addActor(winText);
        stage.addActor(loseText);
        stage.addActor(pointsText);
        stage.addActor(levelText);
        stage.addActor(gameEndText);
        stage.addActor(correctCriminalText);
        stage.addActor(chosenCriminalText);

        if (win) {
            winText.setVisible(true);
            loseText.setVisible(false);
            chosenCriminal.setVisible(false);
            correctCriminal.setVisible(false);
            chosenCriminalText.setVisible(false);
            correctCriminalText.setVisible(false);
        } else {
            winText.setVisible(false);
            loseText.setVisible(true);
            chosenCriminal.setVisible(true);
            correctCriminal.setVisible(true);
            chosenCriminalText.setVisible(true);
            correctCriminalText.setVisible(true);
        }
        gameEndText.setVisible(false);
        if (win) {
            winText.setVisible(true);
            loseText.setVisible(false);
        } else {
            winText.setVisible(false);
            loseText.setVisible(true);
        }
        gameEndText.setVisible(false);

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
        if (settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT) == game.gameData.getLevel()) {
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

        if (timer(timeWaiting) && settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT) != game.gameData.getLevel()) {
            elapsedTime = 0;
            game.resetAll();
        } else if (settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT) == game.gameData.getLevel()) {
            Gdx.app.log("inter", "time 1 " + elapsedTime);
            if (timer(timeWaiting)) {
                Gdx.app.log("inter", "time 2 " + elapsedTime);
                gameEndText.setVisible(true);
                winText.setVisible(false);
                loseText.setVisible(false);
                pointsText.setVisible(false);
                if (elapsedTime > (timeWaiting + gameEndWait)) {
                    elapsedTime = 0;
                    game.setMainScreen();
                }
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
