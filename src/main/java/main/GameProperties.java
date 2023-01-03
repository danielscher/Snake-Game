package main;

import main.consumables.Effect;

public class GameProperties {

    private double speed = 1;
    private double scoreMultiplier = 1;
    private int numConsumables = 1;

    /**@return current speed of snakes movement*/
    public double getSpeed() {
        return speed;
    }

    /**@return current score multiplier*/
    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**@return max possible number of main.consumables appearing on screen*/
    public int getNumConsumables() {
        return numConsumables;
    }


    /**applies the effect of different main.consumables*/
    public void applyEffect(Effect effect) {
        switch (effect){
            case Effect.SPEED -> speed = 1.5;
            case Effect.DOUBLE -> scoreMultiplier = 2;
            case Effect.FRENZY -> numConsumables = 5;
        }
    }

    /**resets the effect of main.consumables*/
    public void resetEffects(){
        speed = 1;
        scoreMultiplier = 1;
        numConsumables = 1;
    }
}
