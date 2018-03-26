package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


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

    public boolean timer;

    public Controls() {
        Gdx.app.log("Controls", "constructor");

        Settings settings = Settings.getInstance();
        try {
            moveRight = settings.getFloat("sensitivityRight");
            moveLeft = settings.getFloat("sensitivityLeft");
            moveUp = settings.getFloat("sensitivityUp");
            moveDown = settings.getFloat("sensitivityDown");
        } catch (Exception e) {
            moveRight = 5f;
            moveLeft = -5f;
            moveUp = 5f;
            moveDown = -5f;

            settings.setFloat("sensitivityRight", moveRight);
            settings.setFloat("sensitivityLeft", moveLeft);
            settings.setFloat("sensitivityUp", moveUp);
            settings.setFloat("sensitivityDown", moveDown);
            settings.saveSettings();

            //System.out.println("peruna");
        }

        hysteresisRight = moveRight/2;
        hysteresisLeft = moveLeft/2;
        hysteresisUp = moveUp/2;
        hysteresisDown = moveDown/2;


        elapsedTime = 0;

        isAbleMoveLeft = true;
        isAbleMoveRight = true;
        isAbleMoveUp = true;
        isAbleMoveDown = true;

        timer = false;
    }

    public float accelerometerY() {
        accelY = Gdx.input.getAccelerometerZ();
        //Gdx.app.log("TAG", "Y:" + accelY);
        return accelY;
    }

    public float accelerometerX() {
        accelX = Gdx.input.getAccelerometerY();
        //Gdx.app.log("TAG", "X:" + accelX);
        return accelX;
    }

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
