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

    public float elapsedTime;

    public boolean isAbleMoveLeft;
    public boolean isAbleMoveRight;
    public boolean isAbleMoveUp;
    public boolean isAbleMoveDown;

    float zeroPointX;
    float zeroPointY;

    public boolean timer;

    public float timerTime;

    public Controls() {
        Gdx.app.log("Controls", "constructor");

        zeroPointX = Gdx.input.getAccelerometerY();
        zeroPointY = Gdx.input.getAccelerometerZ();

        updateControls();

        elapsedTime = 0;

        timerTime = 3;

        isAbleMoveLeft = true;
        isAbleMoveRight = true;
        isAbleMoveUp = true;
        isAbleMoveDown = true;

        timer = false;
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
        hysteresisRight = moveRight/2;
        hysteresisLeft = moveLeft/2;
        hysteresisUp = moveUp/2;
        hysteresisDown = moveDown/2;
        Gdx.app.log("controls", "zero Y: "
                +settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y));

        //System.out.println("peruna");

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
            timer = isTimed;
            //Gdx.app.log("TAG", "R");
            return !isTimed;
        } else if (!isAbleMoveRight && accelX < hysteresisRight && accelX > 0) {
            isAbleMoveRight = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Rback");
        } else if (accelX > moveRight && !isAbleMoveRight && timer) {
            return timer();
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
            timer = isTimed;
            //Gdx.app.log("TAG", "L");
            return !isTimed;
        } else if (!isAbleMoveLeft && accelX > hysteresisLeft && accelX < 0) {
            isAbleMoveLeft = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Lback");
        }
        else if (accelX < moveLeft && !isAbleMoveLeft && timer) {
            return timer();
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
            timer = isTimed;
            isAbleMoveUp = false;
            return !isTimed;
        } else if (!isAbleMoveUp && accelY < hysteresisUp && accelY > 0) {
            isAbleMoveUp = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Lback");
        } else if (accelY > moveUp && !isAbleMoveUp && timer) {
            return timer();
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
            timer = isTimed;
            isAbleMoveDown = false;
            return !isTimed;

        } else if (!isAbleMoveDown && accelY > hysteresisDown && accelY < 0) {
            isAbleMoveDown = true;
            elapsedTime = 0;
        } else if (accelY < moveDown && !isAbleMoveDown && timer) {
            return timer();
        }
        return false;
    }

    /**
     * timer that counts up in seconds
     */
    public boolean timer() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= 3) {
            elapsedTime = 0;
            timer = false;
            return true;
        }
        return false;
    }
}
