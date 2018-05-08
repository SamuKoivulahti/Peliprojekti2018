package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author Samu Koivulahti
 * @version 1.6
 * @since 19.4.2018
 */

public class SaveFiles {

    private Preferences prefs;

    public SaveFiles() {
        prefs = Gdx.app.getPreferences("saveFile.config");
    }

    /**
     * Checks prefs file for the key value
     * @param key
     * @return value of the key
     */
    private boolean hasKey(String key) {
        return prefs.contains(key);
    }

    /**
     * Getter for float value
     * @param key
     * @param defaultValue if key not found, inserts default value
     * @return
     */
    public float getFloat(String key, float defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getFloat(key);
    }

    /**
     * Setter for float value
     * @param key
     * @param value
     */
    public void setFloat(String key, float value) {
        prefs.putFloat(key, value);
    }

    /**
     * Getter for integer value
     * @param key
     * @param defaultValue if key not found, inserts default value
     * @return
     */
    public int getInteger(String key, int defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getInteger(key);
    }

    /**
     * Setter for Integer
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
    }

    /**
     * Getter for boolean
     * @param key
     * @param defaultValue if key not found, inserts default value
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getBoolean(key);
    }

    /**
     * Setter for boolean
     * @param key
     * @param value
     */
    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    /**
     * Getter for string
     * @param key
     * @param defaultValue if key not found, inserts default value
     * @return
     */
    public String getString(String key, String defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getString(key);
    }

    /**
     * Setter for string
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        prefs.putString(key, value);
    }

    /**
     * removes name1 from prefs file
     */
    public void remove1() {
        prefs.remove("name1");
    }

    /**
     * removes name2 from prefs file
     */
    public void remove2() {
        prefs.remove("name2");
    }

    /**
     * removes name3 from prefs file
     */
    public void remove3() {
        prefs.remove("name3");
    }

    /**
     * Saves all changes to prefs file
     */
    public void saveNewFiles() {
        prefs.flush();
    }
}
