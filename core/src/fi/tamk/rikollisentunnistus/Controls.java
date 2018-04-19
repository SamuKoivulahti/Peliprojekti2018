package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;


/**
 * Created by Koivulahti on 5.3.2018.
 */

public class Controls {

    public float accelY;
    public float accelX;


    public float moveRight;
    public float moveLeft;
    public float moveUp;
    public float moveDown;

    public float hysteresisRight;
    public float hysteresisLeft;
    public float hysteresisUp;
    public float hysteresisDown;

    public float elapsedTimeR;
    public float elapsedTimeL;
    public float elapsedTimeU;
    public float elapsedTimeD;

    public boolean isAbleMoveLeft;
    public boolean isAbleMoveRight;
    public boolean isAbleMoveUp;
    public boolean isAbleMoveDown;

    float zeroPointX;
    float zeroPointY;

    public boolean timerR;
    public boolean timerL;
    public boolean timerU;
    public boolean timerD;


    float timerUp;
    float timerDown;
    float timerSides;

    public Controls() {
        Gdx.app.log("Controls", "constructor");

        zeroPointX = Gdx.input.getAccelerometerY();
        zeroPointY = Gdx.input.getAccelerometerZ();

        updateControls();

        elapsedTimeR = 0;
        elapsedTimeL = 0;
        elapsedTimeU = 0;
        elapsedTimeD = 0;


        isAbleMoveLeft = true;
        isAbleMoveRight = true;
        isAbleMoveUp = true;
        isAbleMoveDown = true;

        timerR = false;
        timerL = false;
        timerU = false;
        timerD = false;

    }
    /**
     * Gets all control values
     */
    public void updateControls() {
        Settings settings = Settings.getInstance();
        moveRight = settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT);
        moveLeft = settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT);
        moveUp = settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP);
        moveDown = settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN);
        zeroPointX = settings.getFloat("zeroPointX", GameData.DEFAULT_ZERO_POINT_X);
        zeroPointY = settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y);
        timerSides = settings.getFloat("timerSides", GameData.DEFAULT_TIMER_SIDES);
        timerUp = settings.getFloat("timerUp", GameData.DEFAULT_TIMER_UP);
        timerDown = settings.getFloat("timerDown", GameData.DEFAULT_TIMER_DOWN);
        hysteresisRight = moveRight/2;
        hysteresisLeft = moveLeft/2;
        hysteresisUp = moveUp/2;
        hysteresisDown = moveDown/2;
        //Gdx.app.log("controls", "zero Y: "+settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y));


    }

    /**
     * gets used Y value
     */
    public float accelerometerY() {
        accelY = Gdx.input.getAccelerometerZ() - zeroPointY;
        //Gdx.app.log("TAG", "Y:" + accelY);
        return accelY;
    }

    /**
     * gets used X value
     */
    public float accelerometerX() {
        accelX = Gdx.input.getAccelerometerY() - zeroPointX;
        //Gdx.app.log("TAG", "X:" + accelX);
        return accelX;
    }


    /**
     * gets value of movement to the right
     * @param isTimed is timer used or not
     */
    public boolean moveRight(boolean isTimed) {
        accelerometerX();
        if (accelX > moveRight && isAbleMoveRight) {
            isAbleMoveRight = false;
            timerR = isTimed;
            return !isTimed;
        } else if (!isAbleMoveRight && accelX < hysteresisRight && accelX > 0) {
            isAbleMoveRight = true;
            elapsedTimeR = 0;
        } else if (accelX > moveRight && !isAbleMoveRight && timerR) {
            return timerR(timerSides);
        }
        return false;
    }


    /**
     * gets value of movement to the left
     * @param isTimed is timer used or not
     */
    public boolean moveLeft(boolean isTimed) {
        accelerometerX();
        if (accelX < moveLeft && isAbleMoveLeft) {
            isAbleMoveLeft = false;
            timerL = isTimed;
            //Gdx.app.log("TAG", "L");
            return !isTimed;
        } else if (!isAbleMoveLeft && accelX > hysteresisLeft && accelX < 0) {
            isAbleMoveLeft = true;
            elapsedTimeL = 0;
            //Gdx.app.log("TAG", "Lback");
        }
        else if (accelX < moveLeft && !isAbleMoveLeft && timerL) {
            return timerL(timerSides);
        }
        return false;
    }

    /**
     * gets value of movement to the up direction
     * @param isTimed is timer used or not
     */
    public boolean moveUp(boolean isTimed) {
        accelerometerY();
        if (accelY > moveUp && isAbleMoveUp) {
            //Gdx.app.log("TAG", "L");
            timerU = isTimed;
            isAbleMoveUp = false;
            return !isTimed;
        } else if (!isAbleMoveUp && accelY < hysteresisUp && accelY > 0) {
            isAbleMoveUp = true;
            elapsedTimeU = 0;
            //Gdx.app.log("TAG", "Lback");
        } else if (accelY > moveUp && !isAbleMoveUp && timerU) {
            return timerU(timerUp);
        }
        return false;
    }

    /**
     * gets value of movement to the down direction
     * @param isTimed is timer used or not
     */
    public boolean moveDown(boolean isTimed) {
        accelerometerY();
        if (accelY < moveDown && isAbleMoveDown) {
            timerD = isTimed;
            isAbleMoveDown = false;
            return !isTimed;

        } else if (!isAbleMoveDown && accelY > hysteresisDown && accelY < 0) {
            isAbleMoveDown = true;
            elapsedTimeD = 0;
        } else if (accelY < moveDown && !isAbleMoveDown && timerD) {
            return timerD(timerDown);
        }
        return false;
    }

    /**
     * timer that counts up in seconds
     */
    public boolean timerR(float timeLeaned) {
        elapsedTimeR += Gdx.graphics.getDeltaTime();
        if (elapsedTimeR >= timeLeaned) {
            elapsedTimeR = 0;
            timerR = false;
            return true;
        }
        return false;
    }

    public boolean timerL(float timeLeaned) {
        elapsedTimeL += Gdx.graphics.getDeltaTime();
        if (elapsedTimeL >= timeLeaned) {
            elapsedTimeL = 0;
            timerL = false;
            return true;
        }
        return false;
    }

    public boolean timerU(float timeLeaned) {
        elapsedTimeU += Gdx.graphics.getDeltaTime();
        if (elapsedTimeU >= timeLeaned) {
            elapsedTimeU = 0;
            timerU = false;
            return true;
        }
        return false;
    }

    public boolean timerD(float timeLeaned) {
        elapsedTimeD += Gdx.graphics.getDeltaTime();
        if (elapsedTimeD >= timeLeaned) {
            elapsedTimeD = 0;
            timerD = false;
            return true;
        }
        return false;
    }
}
