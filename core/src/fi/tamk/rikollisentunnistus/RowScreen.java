package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Essi Supponen on 23/02/2018.
 */

public class RowScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Face[] criminalRow;
    private String rightSuspectID;
    private Stage stage;
    IntermissionScreen InterMissionScreen;

    boolean win;
    boolean lose;

    private Label winText;
    private Label loseText;
    private Label pointsText;

    Skin mySkin;

    int row_height;
    int col_width;
    float width;
    float height;

    int points;

    int rowLength;

    public RowScreen(Rikollisentunnistus g) {
        Gdx.app.log("RowScreen", "constructor");
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1200,650);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        game.gameData.setWin(false);
        win = false;
        lose = false;

        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        winText = new Label("Right, you won!", mySkin);
        winText.setPosition(camera.viewportWidth / 2 - winText.getWidth()/2,camera.viewportHeight - winText.getHeight()*2);
        winText.setVisible(false);

        loseText = new Label("Wrong, you lose.", mySkin);
        loseText.setPosition(camera.viewportWidth / 2 - loseText.getWidth()/2,camera.viewportHeight - loseText.getHeight()*2);
        loseText.setVisible(false);

        points = game.gameData.getPoints();
        System.out.println(points + "rowscreen top");
        pointsText = new Label("Pisteet: " + points, mySkin);
        pointsText.setPosition(camera.viewportWidth / 12 * 11, camera.viewportHeight - pointsText.getHeight()*2);

        buttonBack();
        stage.addActor(winText);
        stage.addActor(loseText);
        stage.addActor(pointsText);


    }

    public void buttonBack() {
        Button back = new TextButton("Main Menu",mySkin,"small");
        back.setSize(col_width*2,row_height);
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

    public void setCriminals(Face[] criminals, String suspectID) {
        criminalRow = criminals;
        rightSuspectID = suspectID;
        float separationEven = width / 6;
        float separationOdd = width / 5;
        float separation = separationEven;
        float xCrd = 20;
        int startpoint = 1;

        if (criminalRow.length == 3) {
            xCrd = separationOdd;
            separation = separationOdd;
            startpoint = 1;
        } else if (criminalRow.length == 4) {
            xCrd = separationOdd/2;
            separation = separationOdd;
            startpoint = 1;
        } else if (criminalRow.length == 5) {
            xCrd = separationEven/2;
            separation = separationEven;
            startpoint = 2;
        } else if (criminalRow.length == 6) {
            xCrd = 20;
            separation = separationEven;
            startpoint = 2;
        }

        if (criminalRow.length == 5 || criminalRow.length == 6) {
            float scale = separationEven / criminalRow[0].getSpriteWidth();

            System.out.println(separationEven);
            System.out.println(criminalRow[0].getSpriteWidth());
            System.out.println(scale);

            for (Face criminal : criminalRow) {
                criminal.changeScale(scale);
            }
        }

        for (Face criminal : criminalRow) {
            criminal.setLocation(xCrd, 0);
            stage.addActor(criminal);

            xCrd = xCrd + separation;
        }

        criminalRow[startpoint].toggleActive();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void select() {
        String selectedID = "";

        for (Face criminal : criminalRow) {
            if (criminal.active) {
                selectedID = criminal.getIdCode();
                break;
            }
        }

        System.out.println(rightSuspectID);
        System.out.println(selectedID);

        Gdx.app.log("Select", "selected");
        if (selectedID.equals(rightSuspectID)) {
            win = true;
            pointsText.setText("Pisteet: " + points);
            winText.setVisible(true);
            loseText.setVisible(false);
            game.gameData.setPoints(points + 1);
            game.gameData.setWin(true);

        } else {
            lose = true;
            winText.setVisible(false);
            loseText.setVisible(true);
        }

        setInterMissionScreen();
    }

    public void cancel() {
        stage.clear();
        game.resetAll();
        Gdx.app.log("Cancel", "cancelled");
    }

    public void moveRight() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help <= criminalRow.length - 2) {
            MoveToAction moveDown = new MoveToAction();
            moveDown.setPosition(criminalRow[help].getX(), 0);
            moveDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(moveDown);
            criminalRow[help].toggleActive();
            criminalRow[help + 1].toggleActive();
        }
    }

    public void moveLeft() {
        int help = 0;

        for (int i = 0; i < criminalRow.length; i++) {
            if (criminalRow[i].active) {
                help = i;
                break;
            }
        }

        if (help >= 1) {
            MoveToAction moveDown = new MoveToAction();
            moveDown.setPosition(criminalRow[help].getX(), 0);
            moveDown.setDuration(0.5f);

            criminalRow[help].clearActions();
            criminalRow[help].addAction(moveDown);
            criminalRow[help].toggleActive();
            criminalRow[help - 1].toggleActive();
        }
    }

    public void setInterMissionScreen() {
        InterMissionScreen = new IntermissionScreen(game);
        game.setScreen(InterMissionScreen);
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(25/255f,100/255f,25/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();


        for (Face criminal : criminalRow) {
            if (criminal.active && !criminal.hasActions()) {
                MoveToAction moveUp = new MoveToAction();
                moveUp.setPosition(criminal.getX(), 100);
                moveUp.setDuration(0.5f);
                criminal.addAction(moveUp);
            }
        }

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || game.controls.moveRight(false)) {
            moveRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || game.controls.moveLeft(false)) {
            moveLeft();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || game.controls.moveUp(true)) {
            select();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || game.controls.moveDown(true)) {
            cancel();
        }
        game.batch.end();
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
        stage.clear();
        mySkin.dispose();
    }
}
