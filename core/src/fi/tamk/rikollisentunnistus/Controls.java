package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import java.util.Set;

/**
 * Created by Koivulahti on 5.3.2018.
 */

public class Controls {

    public float accelX;
    public float accelY;
    

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
    public Screen currentScreen;

    public Controls() {

        moveRight = 5f;
        moveLeft = -5f;
        moveUp = -9f;
        moveDown = 9f;

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

    public void setControlValues(float right, float left, float up, float down) {
        moveRight = right;
        moveLeft = left;
        moveUp = up;
        moveDown = down;
        hysteresisRight = right/2;
        hysteresisLeft = left/2;
        hysteresisUp = up/2;
        hysteresisDown = down/2;
    }

    public float accelerometerX() {
        accelX = Gdx.input.getAccelerometerX();
        //Gdx.app.log("TAG", "X:" + accelX);
        return accelX;
    }

    public float accelerometerY() {
        accelY = Gdx.input.getAccelerometerY();
        //Gdx.app.log("TAG", "Y:" + accelY);
        return accelY;
    }

    public boolean moveRight(boolean isTimed) {
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

    public boolean moveLeft(boolean isTimed) {
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

    public boolean moveUp(boolean isTimed) {
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

    public boolean moveDown(boolean isTimed) {
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
