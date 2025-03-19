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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // retrieves the winner, total number of moves, total time played and average time per move
    public static String getPartialStats() {
        return "SELECT winner,\n" +
                "       count(*)                                                                    as total_moves,\n" +
                "       concat\n" +
                "       (sum(extract(hour from age(move_end_time, move_start_time))), ' h ',\n" +
                "        sum(extract(minute from age(move_end_time, move_start_time))), ' m ',\n" +
                "        sum(extract(second from age(move_end_time, move_start_time))), ' seconds') as total_time,\n" +
                "       round(\n" +
                "               (cast(sum(extract(epoch from age(move_end_time, move_start_time))) as integer) / 60.0),\n" +
                "               4) / count(*)                                                       as avg_duration_per_move\n" +
                "FROM moves m\n" +
                "         INNER JOIN game_sessions gs on m.game_session_id = gs.game_session_id\n" +
                "WHERE m.game_session_id = ?\n" +
                "  and player_id = ?\n" +
                "GROUP BY winner;";
    }

    public static String getStatistics() {
        return "SELECT player_id,\n" +
                "       (select COUNT(*)\n" +
                "        from moves m2\n" +
                "        where m2.player_id = m1.player_id\n" +
                "          and m2.move_start_time <= m1.move_start_time)                                          as move_number,\n" +
                "       round(cast(extract(epoch from age(move_end_time, move_start_time)) as integer) / 60.0, 2) as mins\n" +
                "FROM moves m1\n" +
                "WHERE game_session_id = ?\n" +
                "-- and player_id in (1, 2)\n" +
                "GROUP BY player_id, move_start_time, move_end_time\n" +
                "ORDER BY player_id, move_start_time;";
    }

    public static String getLeaderboard() {
        return "SELECT p.player_id,\n" +
                "       p.name,\n" +
                "       count(distinct gs.*)                                                       as total_games,\n" +
                "       count(case when gs.winner = p.player_id then 1 end)                        as wins,\n" +
                "       count(distinct gs.*) - count(case when gs.winner = p.player_id then 1 end) as losses,\n" +
                "       round(count(case when gs.winner = p.player_id then 1 end) * 100.0 / count(distinct gs.*),\n" +
                "             2)                                                                   as percentage,\n" +
                "       round(count(m.*) * 1.0 / count(distinct gs.*),\n" +
                "             2)                                                                   as avg_moves,\n" +
                "       round(sum(extract(epoch from age(m.move_end_time, m.move_start_time))::numeric / 60) / count(m.*),\n" +
                "             2)                                                                   as avg_duration_per_move\n" +
                "FROM players p\n" +
                "         LEFT JOIN game_sessions gs on p.player_id in (gs.player1_id, gs.player2_id)\n" +
                "         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id\n" +
                "GROUP BY p.player_id, p.name\n" +
                "HAVING count(distinct gs.*) > 0\n" +
                "ORDER BY name desc;";
    }
}
