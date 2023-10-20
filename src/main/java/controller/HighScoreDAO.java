package controller;

import model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HighScoreDAO {
    public static void insertScore(Player player) {
        String insertSQL = "INSERT INTO main.HighScores (name, score) VALUES (?, ?)";
        try (Connection connection = JDBCUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.setInt(2, player.getScore());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Player[] getTopScores() {
        String getSQL = "SELECT * FROM main.HighScores ORDER BY Score DESC LIMIT 10";
        try (Connection connection = JDBCUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(getSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                players.add(new Player(resultSet.getString("name"), resultSet.getInt("score")));
            }
            return players.toArray(new Player[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInTopTen(int score) {
        if (score == 0) {
            return false;
        }
        Player[] players = getTopScores();
        if (players.length < 10) {
            return true;
        }
        for (Player player : players) {
            if (score > player.getScore()) {
                return true;
            }
        }
        return false;
    }


    public static void deleteEntries() {
        String deleteSQL = "DELETE FROM main.HighScores";
        try (Connection connection = JDBCUtil.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //ignore
        }
    }
}
