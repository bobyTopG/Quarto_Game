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
        } catch (SQLException | NullPointerException e) {
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

    public static String getPlayers() {
        return "SELECT winner_id, w.name, player_id1, p1.name, player_id2, p2.name\n" +
                "FROM game_sessions gs\n" +
                "         LEFT JOIN players w on (w.player_id = gs.winner_id)\n" +
                "         INNER JOIN players p1 on (p1.player_id = gs.player_id1)\n" +
                "         INNER JOIN players p2 on (p2.player_id = gs.player_id2)\n" +
                "WHERE game_session_id = ?;";
    }

    public static String getPartialStatistics() {
        return "SELECT p.player_id,\n" +
                "       count(*)                                                                                as total_moves,\n" +
                "       sum(extract(epoch from age(m.end_time, m.start_time))::numeric)                             as total_duration,\n" +
                "       round(sum(extract(epoch from age(m.end_time, m.start_time)))::numeric / 60.0 / count(*), 2) as avg\n" +
                "FROM moves m\n" +
                "         INNER JOIN game_sessions gs on (gs.game_session_id = m.game_session_id)\n" +
                "         INNER JOIN players p on (p.player_id = m.player_id)\n" +
                "WHERE p.player_id = ? and gs.game_session_id = ?\n" +
                "GROUP BY p.player_id, name;";
    }

    public static String getStatistics() {
        return "SELECT player_id,\n" +
                "       (move_nr + 1) / 2                                                as move_nr,\n" +
                "       round(extract(epoch from age(end_time, start_time))::numeric, 2) as duration\n" +
                "FROM moves\n" +
                "WHERE game_session_id = ?;";
    }

    public static String getLeaderboard() {
        return "SELECT name,\n" +
                "       count(distinct gs.game_session_id)                                               as played,\n" +
                "       count(distinct case when gs.winner_id = p.player_id then gs.game_session_id end) as wins,\n" +
                "       count(distinct gs.game_session_id) -\n" +
                "       count(distinct case\n" +
                "                          when gs.winner_id = p.player_id or\n" +
                "                               gs.winner_id IS NULL then gs.game_session_id end)        as losses,\n" +
                "       round(count(distinct case when gs.winner_id = p.player_id then gs.game_session_id end) * 100.0\n" +
                "                 / count(distinct gs.game_session_id), 2)                               as wins_p,\n" +
                "       round(count(m.move_id) * 1.0\n" +
                "                 / count(distinct gs.game_session_id), 2)                               as avg_moves,\n" +
                "       round(sum(extract(epoch from age(m.end_time, m.start_time))::numeric / 60)\n" +
                "                 / count(m.move_id), 2)                                                 as avg_duration_per_move\n" +
                "FROM players p\n" +
                "         LEFT JOIN game_sessions gs on p.player_id in (gs.player_id1, gs.player_id2)\n" +
                "         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id\n" +
                "GROUP BY name, is_completed, is_ai\n" +
                "HAVING count(distinct gs.game_session_id) > 0\n" +
                "   and is_completed = true\n" +
                "   and is_ai = false\n" +
                "   and upper(name) != 'GUEST';";
    }

    public static String setGameSession() {
        return "INSERT INTO game_sessions (player_id1, player_id2, start_time)\n" +
                "VALUES (?, ?, ?);";
    }

    public static String updateGameSession() {
        return "UPDATE game_sessions\n" +
                "SET winner_id    = ?,\n" +
                "    is_completed = ?," +
                "    end_time     = ?\n" +
                "WHERE game_session_id = ?;";
    }

    public static String setMove() {
        return "INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)\n" +
                "VALUES (?, ?, ?, ?, ?);";
    }

    public static String setPausePeriod(){
        return "INSERT INTO pause_periods (move_id, start_time, end_time)\n"+
                "VALUES (?, ?, ?);";
    }

    public static String setPiece() {
        return "INSERT INTO pieces (piece_type_id, move_id, pos)\n" +
                "VALUES (?, ?, ?);";
    }

    public static String getPieceId() {
        return "SELECT piece_type_id FROM piece_types\n" +
                "WHERE upper(fill)  = ?\n" +
                "  and upper(shape) = ?\n" +
                "  and upper(color) = ?\n" +
                "  and upper(size)  = ?;";
    }
}
