package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * @author Samu Koivulahti
 * @version 1.6
 * @since 24.3.2018
 */

public class Settings {

    private static Settings settings;
    private Preferences prefs;

    private Settings() {
        prefs = Gdx.app.getPreferences("Rikollisentunnistus.config");
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
     * Saves all changes to prefs file
     */
    public void saveSettings() {
        prefs.flush();
    }

    /**
     * Creates an instance of prefs file
     * @return
     */
    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}
