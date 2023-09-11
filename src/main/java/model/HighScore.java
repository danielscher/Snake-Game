package model;

public class HighScore {

    private final int score;
    private final String name;

    public HighScore(final String name, final int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }
}
