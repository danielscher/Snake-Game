package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Player;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {

    @FXML
    private VBox leaderboardRoot;

    @FXML
    private TableView<Player> scoreTable;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Integer> scoreColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL stylesheetUrl = getClass().getResource("/leaderboard.css");
        leaderboardRoot.getStylesheets().add(stylesheetUrl.toExternalForm());
        scoreTable.setPlaceholder(new Label("No scores yet!"));
        nameColumn.setReorderable(false);
        scoreColumn.setReorderable(false);
        populateTable();
    }

    @FXML
    public void clearLeaderboard() {
        HighScoreDAO.deleteEntries();
        scoreTable.getItems().clear();
    }

    private void populateTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        Player[] players = HighScoreDAO.getTopScores();

        scoreTable.getItems().setAll(players);
    }
}
