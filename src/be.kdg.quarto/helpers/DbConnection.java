package be.kdg.quarto.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection connection = null;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://10.134.178.22:5432/game", "game", "7sur7");
            System.out.println("Connection established");

            //close connection when app is shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(DbConnection::closeConnection));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static String getWinner() {
        return "SELECT winner_id\n" +
                "FROM game_sessions\n" +
                "WHERE game_session_id = ?;";
    }

    public static String getPartialStats() {
        return "SELECT p.player_id,\n" +
                "       name,\n" +
                "       count(*)                                                                                as total_moves,\n" +
                "       sum(extract(epoch from age(end_time, start_time))::numeric)                             as total_duration,\n" +
                "       round(sum(extract(epoch from age(end_time, start_time)))::numeric / 60.0 / count(*), 2) as avg\n" +
                "FROM moves m\n" +
                "         INNER JOIN game_sessions gs on (gs.game_session_id = m.game_session_id)\n" +
                "         INNER JOIN players p on (p.player_id = m.player_id)\n" +
                "WHERE p.player_id = ? and gs.game_session_id = ?\n" +
                "GROUP BY p.player_id, name;";
    }

    public static String getStatistics() {
        return "SELECT player_id,\n" +
                "       (move_nr + 1) / 2                                                       as move_nr,\n" +
                "       round(extract(epoch from age(end_time, start_time))::numeric / 60.0, 2) as duration\n" +
                "FROM moves\n" +
                "WHERE game_session_id = ?;";
    }

    public static String getLeaderboard() {
        return "SELECT name,\n" +
                "       count(distinct gs.game_session_id)                                                      as played,\n" +
                "       count(distinct case when gs.winner_id = p.player_id then 1 end)                         as wins,\n" +
                "       count(distinct gs.game_session_id) -\n" +
                "       count(distinct case when gs.winner_id = p.player_id or gs.winner_id IS NULL then 1 end) as losses,\n" +
                "       count(distinct case when gs.winner_id = p.player_id then 1 end) * 100.0\n" +
                "           / count(distinct gs.game_session_id)                                                as wins_p,\n" +
                "       round(count(m.move_id) * 1.0\n" +
                "                 / count(distinct gs.game_session_id), 2)                                      as avg_moves,\n" +
                "       round(sum(extract(epoch from age(m.end_time, m.start_time))::numeric / 60)\n" +
                "                 / count(m.move_id), 2)                                                        as avg_duration_per_move\n" +
                "FROM players p\n" +
                "         LEFT JOIN game_sessions gs on p.player_id in (gs.player_id1, gs.player_id2)\n" +
                "         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id\n" +
                "GROUP BY name, is_completed\n" +
                "HAVING count(distinct gs.game_session_id) > 0 and is_completed = true;";
    }
}
