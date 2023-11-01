package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    private static final String JDBC_URL = "jdbc:sqlite:src/main/resources/score_data.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void closeQuietly(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            // ignore
        }
    }

}
