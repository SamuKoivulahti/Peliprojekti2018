package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koivulahti on 5.3.2018.
 */

public class Controls {
    float accelX;
    float accelY;
    float accelZ;
    boolean overLR;
    boolean overUD;
    float moveR;
    float moveL;
    float moveU;
    float moveD;
    float hysteresisR;
    float hysteresisL;
    float hysteresisU;
    float hysteresisD;

    public Controls() {
        overLR = false;
        overUD = false;

        // Later set by users input
        moveR = 3;
        moveL = -3;
        moveU = -3;
        moveD = 3;
        hysteresisR = 1.5f;
        hysteresisL = -1.5f;
        hysteresisU = -1.5f;
        hysteresisD = 1.5f;

        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();

        if (accelY >= moveR && !overLR) {
            overLR = true;
            //moveSelectionRight() etc.
        } else if (accelY <= moveL && !overLR) {
            overLR = true;
            //moveSelectionLeft() etc
        }

        if (accelY < hysteresisR && accelY > hysteresisL) {
            overLR = false;
        }

        if (accelX <= moveU && !overUD) {
            // selection
            overUD = true;
        } else if (accelX >= moveD && !overUD) {
            // cancel selection
            overUD = true;
        }

        if (accelX > hysteresisU && accelX < hysteresisD) {
            overUD = false;
        }
    }


}
