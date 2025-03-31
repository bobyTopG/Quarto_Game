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


    public static String getPartialStats() {
        return "SELECT winner,\n" +
                "       count(*)   as total_moves,\n" +
                "       concat(\n" +
                "               floor(sum(extract(epoch from age(move_end_time, move_start_time))) / 3600), ' h ',\n" +
                "               floor(sum(extract(epoch from age(move_end_time, move_start_time))::numeric) / 60 % 60), ' m ',\n" +
                "               sum(extract(epoch from age(move_end_time, move_start_time)))::numeric % 60, ' s'\n" +
                "       )          as total_time,\n" +
                "       round(\n" +
                "               (sum(extract(epoch from age(move_end_time, move_start_time)))::numeric / 60.0) / count(*),\n" +
                "               2) as avg_duration_per_move\n" +
                "FROM moves m\n" +
                "         INNER JOIN game_sessions gs\n" +
                "                    on m.game_session_id = gs.game_session_id\n" +
                "WHERE m.game_session_id = ?\n" +
                "  and player_id = ?\n" +
                "GROUP BY winner;";
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

    public static String getStatistics() {
        return "SELECT player_id,\n" +
                "       (select COUNT(*)\n" +
                "        from moves m2\n" +
                "        where m2.player_id = m1.player_id\n" +
                "          and m2.move_start_time <= m1.move_start_time)                                  as move_number,\n" +
                "       round(extract(epoch from age(move_end_time, move_start_time))::numeric / 60.0, 2) as minutes\n" +
                "FROM moves m1\n" +
                "WHERE game_session_id = ?\n" +
                "-- and player_id in (1, 2)\n" +
                "GROUP BY player_id, move_start_time, move_end_time\n" +
                "ORDER BY player_id, move_start_time;";
    }

    public static String getLeaderboard() {
        return "SELECT p.player_id,\n" +
                "       p.name,\n" +
                "       count(distinct gs.*)                                                                as total_games,\n" +
                "       count(distinct case when gs.winner = p.player_id then 1 end)                        as wins,\n" +
                "       count(distinct gs.*) - count(distinct case when gs.winner = p.player_id then 1 end) as losses,\n" +
                "       round(count(distinct case when gs.winner = p.player_id then 1 end) * 100.0 / count(distinct gs.*),\n" +
                "             2)                                                                            as percentage,\n" +
                "       round(count(m.*) * 1.0 / count(distinct gs.*),\n" +
                "             2)                                                                            as avg_moves,\n" +
                "       round(sum(extract(epoch from age(m.move_end_time, m.move_start_time))::numeric / 60) / count(m.*),\n" +
                "             2)                                                                            as avg_duration_per_move\n" +
                "FROM players p\n" +
                "         LEFT JOIN game_sessions gs on p.player_id in (gs.player1_id, gs.player2_id)\n" +
                "         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id\n" +
                "GROUP BY p.player_id, p.name\n" +
                "HAVING count(distinct gs.*) > 0;";
    }
}
