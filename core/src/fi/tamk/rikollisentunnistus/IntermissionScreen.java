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
 * Created by Samu Koivulahti on 25.3.2018.
 */

public class IntermissionScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    private Stage stage;
    private Label winText;
    private Label loseText;
    private Label longestStreakText;
    private Label currentStreakText;
    private Label pointsText;
    private Label levelText;
    private Label gameEndText;
    private Label chosenCriminalText;
    private Label correctCriminalText;
    private Label moveForwardText;
    private Face chosenCriminal;
    private Face correctCriminal;
    private boolean gameEnd;
    boolean win;

    Skin mySkin;

    float row_height;
    float col_width;
    float height;
    float width;
    float elapsedTime;
    Settings settings;
    int points;
    int level;
    int difficulty;
    boolean loop;
    int currentStreak;
    int longestStreak;

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
        gameEnd = false;

        if (game.gameData.getProfileUsed() == 1) {
            points = game.saveFiles.getInteger("points1", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level1", GameData.DEFAULT_LEVEL);
            difficulty = game.saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY);
            loop = game.saveFiles.getBoolean("loop1", GameData.DEFAULT_LOOP);
            currentStreak = game.saveFiles.getInteger("currentStreak1", GameData.DEFAULT_CURRENT_STREAK);
            longestStreak = game.saveFiles.getInteger("longestStreak1", GameData.DEFAULT_LONGEST_STREAK);
        } else if (game.gameData.getProfileUsed() == 2) {
            points = game.saveFiles.getInteger("points2", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level2", GameData.DEFAULT_LEVEL);
            difficulty = game.saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY);
            loop = game.saveFiles.getBoolean("loop2", GameData.DEFAULT_LOOP);
            currentStreak = game.saveFiles.getInteger("currentStreak2", GameData.DEFAULT_CURRENT_STREAK);
            longestStreak = game.saveFiles.getInteger("longestStreak2", GameData.DEFAULT_LONGEST_STREAK);
        } else if (game.gameData.getProfileUsed() == 3) {
            points = game.saveFiles.getInteger("points3", GameData.DEFAULT_POINTS);
            level = game.saveFiles.getInteger("level3", GameData.DEFAULT_LEVEL);
            difficulty = game.saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY);
            loop = game.saveFiles.getBoolean("loop3", GameData.DEFAULT_LOOP);
            currentStreak = game.saveFiles.getInteger("currentStreak3", GameData.DEFAULT_CURRENT_STREAK);
            longestStreak = game.saveFiles.getInteger("longestStreak3", GameData.DEFAULT_LONGEST_STREAK);
        } else if (game.gameData.getProfileUsed() == 0) {
            points = game.gameData.getPoints();
            level = game.gameData.getLevel();
        }

        if (currentStreak > longestStreak) {
            if (game.gameData.profileUsed == 1) {
                game.saveFiles.setInteger("longestStreak1", currentStreak);
                longestStreak = currentStreak;
            } else if (game.gameData.profileUsed == 2) {
                game.saveFiles.setInteger("longestStreak2", currentStreak);
                longestStreak = currentStreak;
            } else if (game.gameData.profileUsed == 3) {
                game.saveFiles.setInteger("longestStreak3", currentStreak);
                longestStreak = currentStreak;
            }
            game.saveFiles.saveNewFiles();
        }

        textPrint();
    }

    public void textPrint() {
        winText = new Label(game.texts.get(19), mySkin, "big");
        winText.setPosition(width/2 - winText.getWidth()/2, height/2 - winText.getHeight() / 2);
        loseText = new Label(game.texts.get(18), mySkin, "big");
        loseText.setPosition(width/2 - loseText.getWidth()/2, height/2 - loseText.getHeight() / 2);
        currentStreakText = new Label(game.script.format("postgame1", currentStreak), mySkin);
        currentStreakText.setPosition(width/2 - currentStreakText.getWidth()/2, row_height * 2);
        longestStreakText = new Label(game.script.format("postgame2", longestStreak), mySkin);
        longestStreakText.setPosition(width/2 - longestStreakText.getWidth()/2, row_height * 1);
        pointsText = new Label(game.texts.get(11) + points + "/" + (5+(int)(difficulty/2)), mySkin);
        pointsText.setPosition(width/2 - pointsText.getWidth()/2, row_height * 5);
        levelText = new Label(game.texts.get(10) + level, mySkin, "big");
        levelText.setPosition(width/2 - levelText.getWidth()/2, row_height * 10);
        gameEndText = new Label(game.texts.get(41)+ game.gameData.getPoints() + " " + game.texts.get(42), mySkin, "big");
        gameEndText.setPosition(width/2 - gameEndText.getWidth()/2, row_height*8);
        moveForwardText = new Label(game.texts.get(20), mySkin);
        moveForwardText.setPosition(width/2 - moveForwardText.getWidth()/2, height/100);
        correctCriminalText = new Label(game.texts.get(17), mySkin);
        correctCriminalText.setPosition(width*7/9 - correctCriminalText.getWidth()/2, height*7/9f);
        chosenCriminalText = new Label(game.texts.get(16), mySkin);
        chosenCriminalText.setPosition(width*2/9 - chosenCriminalText.getWidth()/2, height*7/9f);

        chosenCriminal = game.gameData.getChosenCriminal();
        correctCriminal = game.gameData.getCorrectCriminal();

        if (!win) {
            chosenCriminal = game.gameData.getChosenCriminal();
            correctCriminal = game.gameData.getCorrectCriminal();
            chosenCriminal.clearActions();
            chosenCriminal.setScale(1.0f);

            chosenCriminal.changeScale(0.75f);
            correctCriminal.changeScale(0.75f);

            chosenCriminal.setLocation(width*2/9,height/10);
            correctCriminal.setLocation(width*7/9, height/10);
        }

        stage.addActor(chosenCriminal);
        stage.addActor(correctCriminal);

        stage.addActor(winText);
        stage.addActor(loseText);
        if (!loop) {
            stage.addActor(pointsText);
        } else {
            stage.addActor(longestStreakText);
            stage.addActor(currentStreakText);
        }
        stage.addActor(levelText);
        stage.addActor(gameEndText);
        stage.addActor(correctCriminalText);
        stage.addActor(chosenCriminalText);
        stage.addActor(moveForwardText);

        moveForwardText.setVisible(true);

        if (win) {
            SoundManager.playRightAnswerSound(game.soundEffectsOn);
            winText.setVisible(true);
            loseText.setVisible(false);
            chosenCriminal.setVisible(false);
            correctCriminal.setVisible(false);
            chosenCriminalText.setVisible(false);
            correctCriminalText.setVisible(false);
        } else {
            SoundManager.playWrongAnswerSound(game.soundEffectsOn);
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
        Gdx.input.setCatchBackKey(true);
    }

    private boolean anyInput() {
        return (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                game.controls.moveDown(false) || game.controls.moveUp(false) ||
                game.controls.moveLeft(false) || game.controls.moveRight(false));
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

        if (settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT) != level && game.gameData.profileUsed == 0) {
            if (anyInput()) {
                elapsedTime = 0;
                game.resetAll();
            }
        } else if (settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT) == level && game.gameData.profileUsed == 0) {
            if (anyInput()) {
                gameEnd = true;
                gameEndText.setVisible(true);
                winText.setVisible(false);
                loseText.setVisible(false);
                pointsText.setVisible(false);
                correctCriminal.setVisible(false);
                chosenCriminal.setVisible(false);
                correctCriminalText.setVisible(false);
                chosenCriminalText.setVisible(false);
            }

            if (gameEnd && timer(1) && anyInput()) {
                elapsedTime = 0;
                game.setMainScreen();
                SoundManager.stopIngameMusic();
            }
        } else if (points >= 5 + (int)(difficulty/2) && game.gameData.profileUsed != 0 && !loop) {
            if (anyInput()) {
                elapsedTime = 0;
                game.cutsceneScreen.setScene(difficulty + 2);
                game.setScreen(game.cutsceneScreen);
            }
        } else if (points >= 5 + (int)(difficulty/2) && game.gameData.profileUsed != 0 && loop ) {
            if (anyInput()) {
                elapsedTime = 0;
                game.resetAll();
            }
        } else if (points < 5 + (int)(difficulty/2) && game.gameData.profileUsed != 0) {
            if (anyInput()) {
                elapsedTime = 0;
                game.resetAll();
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
        Gdx.input.setCatchBackKey(false);
        if (game.gameData.getProfileUsed() == 1) {
            game.saveFiles.setInteger("level1", level +1);
        } else if (game.gameData.getProfileUsed() == 2) {
            game.saveFiles.setInteger("level2",level +1);
        } else if (game.gameData.getProfileUsed() == 3) {
            game.saveFiles.setInteger("level3", level +1);
        } else if (game.gameData.getProfileUsed() == 0) {
            game.gameData.setLevel(game.gameData.getLevel() + 1);
        }
        game.saveFiles.saveNewFiles();

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
