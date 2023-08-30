package controller;


import gui.GUI;
import main.Direction;
import main.GameModel;


public class Controller {

    private GameModel game;
    private final GUI gui;
    long eatenSince = 0;

    public Controller(final GUI gui, final int cellSize) {
        this.gui = gui;
        game = new GameModel(2, 512, cellSize);
        //updateGui();
    }

    public void createNewGame(final int cellSize){
        game = new GameModel(2, 512, cellSize);
        updateGui();
    }

    public void handleTick(Direction dir, long currTick){

        // make move and check if game is over.
        if (game.makeMove(dir)){
            gui.setGameOverFlag();
        }

        //respawn fruit if not eaten.
        if (currTick - eatenSince > 32) {
            if (!game.getEaten()){
                game.respawnConsumable();
            }
            game.resetEaten();
            eatenSince = currTick;
        }
        // refresh gui.
        updateGui();
    }

    public void updateGui(){
        gui.updateScore(game.getScore());
        gui.updateSnakeBody(game.getSnakePixelPos());
        gui.updateFruits(game.getFruits());
        gui.setRefreshSpeed(game.getGp().getSpeed());
    }



}
