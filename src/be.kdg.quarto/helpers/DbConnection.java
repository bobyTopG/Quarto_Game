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
    public static String getGeneralStatistics() {
        return "SELECT winner,\n" +
                "       count(*)                                                                    as total_moves,\n" +
                "       concat\n" +
                "       (sum(extract(hour from age(move_end_time, move_start_time))), ' h ',\n" +
                "        sum(extract(minute from age(move_end_time, move_start_time))), ' m ',\n" +
                "        sum(extract(second from age(move_end_time, move_start_time))), ' seconds') as total_time,\n" +
                "       round(\n" +
                "               (cast(sum(extract(epoch from age(move_end_time, move_start_time))) as integer) / 60.0),\n" +
                "               4) / count(*)                                                       as average_per_move\n" +
                "FROM moves m\n" +
                "         INNER JOIN public.game_sessions gs on m.game_session_id = gs.game_session_id\n" +
                "WHERE m.game_session_id = ?\n" +
                "  and player_id = ?\n" +
                "GROUP BY winner;";
    }

    public static String getGameStatistics() {
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
}
