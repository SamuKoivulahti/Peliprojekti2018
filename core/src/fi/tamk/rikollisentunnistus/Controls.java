package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koivulahti on 5.3.2018.
 */

public class Controls {

    static float accelX;
    static float accelY;

    static float moveRight = 5f;
    static float moveLeft = -5f;
    static float moveUp = -9f;
    static float moveDown = 9f;

    static float hysteresisRight = 3f;
    static float hysteresisLeft = -3f;
    static float hysteresisUp = -1.5f;
    static float hysteresisDown = 1.5f;

    static boolean startTimer = false;
    static float elapsedTime = 0;

    static boolean isAbleMoveLeft = true;
    static boolean isAbleMoveRight = true;
    static boolean isAbleMoveUp = true;
    static boolean isAbleMoveDown = true;

    static float accelerometerX() {
        accelX = Gdx.input.getAccelerometerX();
        //Gdx.app.log("TAG", "X:" + accelX);
        return accelX;
    }

    static float accelerometerY() {
        accelY = Gdx.input.getAccelerometerY();
        //Gdx.app.log("TAG", "Y:" + accelY);
        return accelY;
    }

    static boolean moveRight() {
        accelerometerY();
        if (accelY > moveRight && isAbleMoveRight) {
            isAbleMoveRight = false;
            //Gdx.app.log("TAG", "R");
            return true;
        } else if (!isAbleMoveRight && accelY < hysteresisRight && accelY > 0) {
            isAbleMoveRight = true;
            //Gdx.app.log("TAG", "Rback");
        }
        return false;
    }

    static boolean moveLeft() {
        accelerometerY();
        if (accelY < moveLeft && isAbleMoveLeft) {
            isAbleMoveLeft = false;
            //Gdx.app.log("TAG", "L");
            return true;
        } else if (!isAbleMoveLeft && accelY > hysteresisLeft && accelY < 0) {
            isAbleMoveLeft = true;
            //Gdx.app.log("TAG", "Lback");
        }
        return false;
    }

    static boolean moveUp() {
        accelerometerX();
        if (accelX < moveUp && isAbleMoveUp) {
            //Gdx.app.log("TAG", "L");
            startTimer = true;
            isAbleMoveUp = false;
        } else if (!isAbleMoveUp && accelX > hysteresisUp && accelX < 0) {
            isAbleMoveUp = true;
            //Gdx.app.log("TAG", "Lback");
        } else if (accelX < moveUp && !isAbleMoveUp && startTimer) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            Gdx.app.log("moveUp", "moveup" + elapsedTime);
            if (elapsedTime >= 3) {
                elapsedTime = 0;
                startTimer = false;
                return true;
            }
        }
        return false;
    }

    static boolean moveDown() {
        accelerometerX();
        if (accelX > moveDown && isAbleMoveDown) {
            //Gdx.app.log("TAG", "L");
            startTimer = true;
            isAbleMoveDown = false;
        } else if (!isAbleMoveDown && accelX < hysteresisDown && accelX > 0) {
            isAbleMoveDown = true;
            //Gdx.app.log("TAG", "Lback");
        } else if (accelX > moveDown && !isAbleMoveDown && startTimer) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            Gdx.app.log("moveDown", "movedown" + elapsedTime);
            if (elapsedTime >= 3) {
                elapsedTime = 0;
                startTimer = false;
                return true;
            }
        }
        return false;
    }
}
