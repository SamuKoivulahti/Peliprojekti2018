package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Samu Koivulahti on 19.4.2018.
 */

public class SaveFiles {

    private Preferences prefs;

    public SaveFiles() {
        prefs = Gdx.app.getPreferences("saveFile.config");
    }

    private boolean hasKey(String key) {
        return prefs.contains(key);
    }

    public float getFloat(String key, float defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getFloat(key);
    }
    public void setFloat(String key, float value) {
        prefs.putFloat(key, value);
    }

    public int getInteger(String key, int defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getInteger(key);
    }

    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getBoolean(key);
    }

    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public void saveNewFiles() {
        prefs.flush();
    }
}
