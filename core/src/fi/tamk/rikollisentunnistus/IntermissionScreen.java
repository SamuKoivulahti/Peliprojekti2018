package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    boolean win;

    Skin mySkin;

    float row_height;
    float col_width;
    float elapsedTime;
    int timeWaiting = 3;

    public IntermissionScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        win = game.gameData.getWin();
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));
        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;

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
                super.clicked(event, x, y);
                Gdx.app.log("TAG", "back");
                game.resetAll();
                MainScreen MainScreen = new MainScreen(game);
                game.setScreen(MainScreen);
            }
        });
        stage.addActor(back);
    }

    public void textPrint() {
        winText = new Label("You Identified the Correct Criminal!", mySkin);
        winText.setPosition(camera.viewportWidth/2 - winText.getWidth()/2, camera.viewportHeight/2 - winText.getHeight() / 2);
        loseText = new Label("You Identified the Incorrect Criminal!", mySkin);
        loseText.setPosition(camera.viewportWidth/2 - loseText.getWidth()/2, camera.viewportHeight/2 - loseText.getHeight() / 2);
        pointsText = new Label("points: " + game.gameData.getPoints(), mySkin);
        pointsText.setPosition(camera.viewportWidth/2 - pointsText.getWidth()/2, camera.viewportHeight/12 * 5);


        stage.addActor(winText);
        stage.addActor(loseText);
        stage.addActor(pointsText);
        if (win) {
            winText.setVisible(true);
            loseText.setVisible(false);
        } else {
            winText.setVisible(false);
            loseText.setVisible(true);
        }
    }

    public boolean timer(int timeToPass) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= timeToPass) {
            elapsedTime = 0;
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
        if (win) {
            Gdx.gl.glClearColor(20 / 255f, 170 / 255f, 150 / 255f, 1);
        } else {
            Gdx.gl.glClearColor(150 / 255f, 30 / 255f, 30 / 255f, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (timer(timeWaiting)) {
            elapsedTime = 0;
            game.resetAll();
            game.setCriminalScreen();
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
