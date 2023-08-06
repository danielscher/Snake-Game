package main;

public class GameProperties {

    private double speed = 1;
    private double speedMultiplier = 1.11;
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


    public void increaseSpeed(){
        speed = speed*speedMultiplier;
    }


}
