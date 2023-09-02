package controller;

import main.HighScore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HighScoreDAO {
    public static void insertScore(HighScore score) {
        String insertSQL = "INSERT INTO main.HighScores (name, score) VALUES (?, ?)";
        try (Connection connection = JDBCUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, score.getName());
            preparedStatement.setInt(2, score.getScore());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HighScore[] getTopScores() {
        String getSQL = "SELECT * FROM main.HighScores ORDER BY Score DESC LIMIT 10";
        try (Connection connection = JDBCUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(getSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<HighScore> highScores = new ArrayList<>();
            while (resultSet.next()) {
                highScores.add(new HighScore(resultSet.getString("name"), resultSet.getInt("score")));
            }
            return highScores.toArray(new HighScore[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInTopTen(int score) {
        if (score == 0) {
            return false;
        }
        HighScore[] highScores = getTopScores();
        if (highScores.length < 10) {
            return true;
        }
        for (HighScore highScore : highScores) {
            if (score > highScore.getScore()) {
                return true;
            }
        }
        return false;
    }
}
