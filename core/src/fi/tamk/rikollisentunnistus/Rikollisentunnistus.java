package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rikollisentunnistus extends Game {
    CriminalScreen criminalScreen;
    RowScreen rowScreen;

    public Controls controls;

	SpriteBatch batch;
	GameData gameData;

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

        controls = new Controls();
        gameData = new GameData();

        SplashScreen splashScreen = new SplashScreen(this);
        //MainScreen mainScreen = new MainScreen(this);


        batch = new SpriteBatch();

        rowConstructor = new RowConstructor();

        updateSettings();

        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }

        //setScreen(mainScreen);
	    setScreen(splashScreen);
	}

    private TextureRegion[] toArrays(TextureRegion[][] table) {
        TextureRegion[] array = new TextureRegion[table.length * table[0].length];

        int index = 0;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                array[index] = table[i][j];

                index++;
            }
        }

        return array;
    }

    public void updateSettings() {
        Settings settings = Settings.getInstance();

        try {
            rowLength = settings.getInteger("rowLength");
            sameAttributes = settings.getInteger("sameAttributes");
            accessories = settings.getBoolean("assets");
            rounds = settings.getInteger("roundAmount");
            difficulty = settings.getInteger("startingDifficulty");
            useDifficulty = settings.getBoolean("useDifficulty");
            increasingDifficulty = settings.getBoolean("increasingDifficulty");

            if (useDifficulty) {
                criminals = rowConstructor.makeRowDifficulty(difficulty);
            } else {
                criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
            }

        } catch (Exception e) {
            rowLength = 5;
            sameAttributes = 2;
            accessories = false;
            rounds = 7;
            difficulty = 7;
            useDifficulty = false;
            increasingDifficulty = false;

            settings.setInteger("rowLength", rowLength);
            settings.setInteger("sameAttributes", sameAttributes);
            settings.setBoolean("assets", accessories);
            settings.setInteger("roundAmount", rounds);
            settings.setInteger("startingDifficulty", difficulty);
            settings.setBoolean("useDifficulty", useDifficulty);
            settings.setBoolean("increasingDifficulty", increasingDifficulty);
            settings.saveSettings();
        }
    }

    public void resetAll() {
        if (useDifficulty) {
            criminals = rowConstructor.makeRowDifficulty(difficulty);
        } else {
            criminals = rowConstructor.makeRow(rowLength, sameAttributes, accessories);
        }
	    criminalScreen = new CriminalScreen(this, criminals[0]);
	    setScreen(criminalScreen);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setRowScreen() {
        rowScreen = new RowScreen(this);
        setScreen(rowScreen);
    }

    public void setCriminalScreen() {
        criminalScreen = new CriminalScreen(this, criminals[0]);
        setScreen(criminalScreen);
    }

    public void setCriminals() {
	    String rightId = criminals[0].getIdCode();

	    if (accessories && MathUtils.random(0f,1f) <= 0.8f) {
	        criminals[0].addAccessory(rowConstructor.accessoryTextures);
	    }

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
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
