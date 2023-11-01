package gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public abstract class PausableAnimationTimer extends AnimationTimer {

    double gameSpeed = 1;

    long timeSinceLastTick;
    int tick = 0;

    long pauseStart;
    long animationStart;
    DoubleProperty animationDuration = new SimpleDoubleProperty(0L);

    boolean isPaused;
    boolean isActive;

    boolean pauseScheduled;
    boolean playScheduled;
    boolean restartScheduled;

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isActive() {
        return isActive;
    }

    public DoubleProperty animationDurationProperty() {
        return animationDuration;
    }

    public void setSpeed(double speed){
        gameSpeed = speed;
    }

    public void pause() {
        if (!isPaused) {
            pauseScheduled = true;
        }
        else {
            play();
        }
    }

    public void play() {
        if (isPaused) {
            playScheduled = true;
        }
    }

    @Override
    public void start() {
        super.start();
        isActive = true;
        restartScheduled = true;
    }

    @Override
    public void stop() {
        super.stop();
        pauseStart = 0;
        isPaused = false;
        isActive = false;
        pauseScheduled = false;
        playScheduled = false;
        animationDuration.set(0);
    }

    @Override
    public void handle(long now) {
        if (pauseScheduled) {
            pauseStart = now;
            isPaused = true;
            pauseScheduled = false;
        }

        if (playScheduled) {
            animationStart += (now - pauseStart);
            isPaused = false;
            playScheduled = false;
        }

        if (restartScheduled) {
            isPaused = false;
            animationStart = now;
            restartScheduled = false;
        }

        if (!isPaused) {
            long animDuration = now - animationStart;
            animationDuration.set(animDuration / 1e9);
            if (animDuration - timeSinceLastTick > 33333333L/gameSpeed){
                timeSinceLastTick = animDuration;
                tick++;
                tick(tick);
            }
        }
    }

 /**
 * This method is the main game loop.
 * @param relativeNow The current tick.
 * */
    public abstract void tick(long relativeNow);
}