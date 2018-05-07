package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Rikollisentunnistus extends Game {
    CriminalScreen criminalScreen;
    RowScreen rowScreen;
    MainScreen mainScreen;
    SettingsScreen settingsScreen;
    SplashScreen splashScreen;
    SaveFileSelectScreen saveFileSelectScreen;
    CutsceneScreen cutsceneScreen;

    public Controls controls;
	SpriteBatch batch;

    GameData gameData;
    public SaveFiles saveFiles;

    boolean soundEffectsOn;

    private RowConstructor rowConstructor;
    private Face[] criminals;

    int rowLength;
    int sameAttributes;
    boolean accessories;
	int rounds;
	int difficulty;
	boolean useDifficulty;
	boolean increasingDifficulty;

	public ArrayList<String> texts;
	public I18NBundle script;

	@Override
	public void create () {
        splashScreen = new SplashScreen(this);
        setScreen(splashScreen);

        controls = new Controls();
        gameData = new GameData();
        saveFiles = new SaveFiles();
        language();

        mainScreen = new MainScreen(this);
        settingsScreen = new SettingsScreen(this);
        saveFileSelectScreen = new SaveFileSelectScreen(this);
        cutsceneScreen = new CutsceneScreen(this);


        batch = new SpriteBatch();

        rowConstructor = new RowConstructor();
        updateSettings();


        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }
	}

	public void updateLanguage(String language) {
	    Locale locale;
	    if (language.equals("FI")) {
	        locale = new Locale("fi", "FI");
        } else {
	        locale = new Locale("en", "US");
        }

        script = I18NBundle.createBundle(Gdx.files.internal("cutscenes/script"), locale);

        I18NBundle myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
        texts = new ArrayList<String>();
        int i = 0;

        while (true) {
            try {
                myBundle.get(Integer.toString(i));
                texts.add(myBundle.get(Integer.toString(i)));
                i++;
            } catch (Exception e) {
                break;
            }
        }

        settingsScreen = new SettingsScreen(this);
    }

	public void language() {
	    Locale locale;
	    I18NBundle myBundle;
        try {
            locale = Locale.getDefault();
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
            script = I18NBundle.createBundle(Gdx.files.internal("cutscenes/script"), locale);
        } catch (Exception e) {
            locale = new Locale("en", "US");
            myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
            script = I18NBundle.createBundle(Gdx.files.internal("cutscenes/script"), locale);
        }

        texts = new ArrayList<String>();
        int i = 0;

        while (true) {
            try {
                myBundle.get(Integer.toString(i));
                texts.add(myBundle.get(Integer.toString(i)));
                i++;
            } catch (Exception e) {
                break;
            }
        }

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
        soundEffectsOn = settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS);

        Gdx.app.log("Rikollisentunnistus", "update settings");

        if (gameData.getProfileUsed() == 0 && useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else if (gameData.getProfileUsed() == 0) {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        } else {
            if (gameData.getProfileUsed() == 1) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY));
            } else if (gameData.getProfileUsed() == 2) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY));
            } else if (gameData.getProfileUsed() == 3) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY));
            }
        }
    }

    public void resetAll() {
        if (gameData.getProfileUsed() == 0 && useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else if (gameData.getProfileUsed() == 0) {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        } else {
            if (gameData.getProfileUsed() == 1) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty1", GameData.DEFAULT_DIFFICULTY));
            } else if (gameData.getProfileUsed() == 2) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty2", GameData.DEFAULT_DIFFICULTY));
            } else if (gameData.getProfileUsed() == 3) {
                criminals = rowConstructor.makeRowDifficulty(saveFiles.getInteger("difficulty3", GameData.DEFAULT_DIFFICULTY));
            }
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
