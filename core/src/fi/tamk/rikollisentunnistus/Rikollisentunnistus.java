package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rikollisentunnistus extends Game {
    CriminalScreen criminalScreen;
    RowScreen rowScreen;
    MainScreen mainScreen;
    SettingsScreen settingsScreen;
    SplashScreen splashScreen;
    SaveFileSelectScreen saveFileSelectScreen;

    public Controls controls;
	SpriteBatch batch;

    GameData gameData;
    public SaveFiles saveFiles;

    private RowConstructor rowConstructor;
    private Face[] criminals;

    int rowLength;
    int sameAttributes;
    boolean accessories;
	int rounds;
	int difficulty;
	boolean useDifficulty;
	boolean increasingDifficulty;

	@Override
	public void create () {
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);

        controls = new Controls();
        gameData = new GameData();
        saveFiles = new SaveFiles();

        mainScreen = new MainScreen(this);
        settingsScreen = new SettingsScreen(this);
        saveFileSelectScreen = new SaveFileSelectScreen(this);


        batch = new SpriteBatch();

        rowConstructor = new RowConstructor();
        updateSettings();


        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }

        //setScreen(mainScreen);
	}

    public void updateSettings() {
        Settings settings = Settings.getInstance();

        rowLength = settings.getInteger("rowLength", GameData.DEFAULT_ROW_LENGTH);
        sameAttributes = settings.getInteger("sameAttributes", GameData.DEFAULT_SAME_ATTRIBUTES);
        accessories = settings.getBoolean("assets", GameData.DEFAULT_ASSETS);
        rounds = settings.getInteger("roundAmount", GameData.DEFAULT_ROUND_AMOUNT);
        difficulty = settings.getInteger("startingDifficulty", GameData.DEFAULT_STARTING_DIFFICULTY);
        useDifficulty = settings.getBoolean("useDifficulty", GameData.DEFAULT_USE_DIFFICULTY);
        increasingDifficulty = settings.getBoolean("increasingDifficulty", GameData.DEFAULT_INCREASING_DIFFICULTY);

        Gdx.app.log("Rikollisentunnistus", "update settings");

        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }
    }

    public void resetAll() {
        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }

	    criminalScreen = new CriminalScreen(this, criminals[0]);
        criminalScreen.updateWaitingTimes();
	    setScreen(criminalScreen);
    }

    public void setRowScreen() {
        rowScreen = new RowScreen(this);
        setScreen(rowScreen);
    }

    public void setCriminalScreen() {
        criminalScreen = new CriminalScreen(this, criminals[0]);
        criminalScreen.updateWaitingTimes();
        setScreen(criminalScreen);
    }

    public void setMainScreen() {
	    setScreen(mainScreen);
    }

    public void setSettingsScreen() {
	    setScreen(settingsScreen);
    }

    public void setSaveFileSelectScreen() {
	    setScreen(saveFileSelectScreen);
	}

    public void setCriminals() {
	    String rightId = criminals[0].getIdCode();
	    accessories = rowConstructor.getAccessories();

	    if (accessories && MathUtils.random(0f,1f) <= 0.8f) {
	        criminals[0].addAccessory(rowConstructor.accessoryTextures);
	    }

	    gameData.setCorrectCriminal(criminals[0]);

        criminals = shuffleArray(criminals);
        rowScreen.setCriminals(criminals,rightId);
    }

    public void resetCriminalScreen() {
	    criminalScreen.reset();
    }

    public Face[] shuffleArray(Face[] array) {
        List<Face> list = new ArrayList();
        for (Face i : array) {
            list.add(i);
        }

        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
