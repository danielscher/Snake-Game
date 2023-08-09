package controller;


import gui.GUI;
import javafx.scene.input.KeyEvent;
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

    public void handleTick(KeyEvent key, long currTick){
        Direction moveDirection = null;

        //get move direction from key press.
        if (key != null){
            switch (key.getCode()) {
                case UP, W -> moveDirection = Direction.UP;
                case DOWN, S -> moveDirection = Direction.DOWN;
                case LEFT, A -> moveDirection = Direction.LEFT;
                case RIGHT, D -> moveDirection = Direction.RIGHT;
                case ESCAPE -> gui.pauseAnimation();
                default -> {}
            }
        }

        // make move and check if game is over.
        if (game.makeMove(moveDirection)){
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
