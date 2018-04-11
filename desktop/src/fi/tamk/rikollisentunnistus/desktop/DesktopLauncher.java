package fi.tamk.rikollisentunnistus.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tamk.rikollisentunnistus.Rikollisentunnistus;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 960;
        config.height = 600;
		new LwjglApplication(new Rikollisentunnistus(), config);
	}
}
