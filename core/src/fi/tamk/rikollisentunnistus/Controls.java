package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.Set;

/**
 * Created by Koivulahti on 5.3.2018.
 */

public class Controls {

    static float accelX;
    static float accelY;

    static float moveRight =5f;
    static float moveLeft = -5f;
    static float moveUp = -9f;
    static float moveDown = 9f;

    static float hysteresisRight = moveRight/2;
    static float hysteresisLeft = moveLeft/2;
    static float hysteresisUp = moveUp/2;
    static float hysteresisDown = moveDown/2;

    static float elapsedTime = 0;

    static boolean isAbleMoveLeft = true;
    static boolean isAbleMoveRight = true;
    static boolean isAbleMoveUp = true;
    static boolean isAbleMoveDown = true;

    static boolean timer = false;
    static Screen currentScreen;

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

    static boolean moveRight(boolean isTimed) {
        accelerometerY();
        if (accelY > moveRight && isAbleMoveRight) {
            isAbleMoveRight = false;
            timer = isTimed;
            //Gdx.app.log("TAG", "R");
            return !isTimed;
        } else if (!isAbleMoveRight && accelY < hysteresisRight && accelY > 0) {
            isAbleMoveRight = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Rback");
        } else if (accelY > moveRight && !isAbleMoveRight && timer) {
            return timer();
        }
        return false;
    }

    static boolean moveLeft(boolean isTimed) {
        accelerometerY();
        if (accelY < moveLeft && isAbleMoveLeft) {
            isAbleMoveLeft = false;
            timer = isTimed;
            //Gdx.app.log("TAG", "L");
            return !isTimed;
        } else if (!isAbleMoveLeft && accelY > hysteresisLeft && accelY < 0) {
            isAbleMoveLeft = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Lback");
        }
        else if (accelY < moveLeft && !isAbleMoveLeft && timer) {
            return timer();
        }
        return false;
    }

    static boolean moveUp(boolean isTimed) {
        accelerometerX();
        if (accelX < moveUp && isAbleMoveUp) {
            //Gdx.app.log("TAG", "L");
            timer = isTimed;
            isAbleMoveUp = false;
            return !isTimed;
        } else if (!isAbleMoveUp && accelX > hysteresisUp && accelX < 0) {
            isAbleMoveUp = true;
            elapsedTime = 0;
            //Gdx.app.log("TAG", "Lback");
        } else if (accelX < moveUp && !isAbleMoveUp && timer) {
            return timer();
        }
        return false;
    }

    static boolean moveDown(boolean isTimed) {
        accelerometerX();
        if (accelX > moveDown && isAbleMoveDown) {
            timer = isTimed;
            isAbleMoveDown = false;
            return !isTimed;

        } else if (!isAbleMoveDown && accelX < hysteresisDown && accelX > 0) {
            isAbleMoveDown = true;
            elapsedTime = 0;
        } else if (accelX > moveDown && !isAbleMoveDown && timer) {
            return timer();
        }
        return false;
    }

    static boolean timer() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= 3) {
            elapsedTime = 0;
            timer = false;
            return true;
        }
        return false;
    }
}
