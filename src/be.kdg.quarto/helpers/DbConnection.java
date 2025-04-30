package be.kdg.quarto.helpers;

import javafx.application.Platform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection connection = null;
    public static boolean isConnecting = false;

    public interface ConnectionCallback {
        void onConnectionComplete(boolean isConnected);
    }

    //put the code into a thread to not Block the UI (asynchronous operation)
    public static void startConnection(ConnectionCallback callback) {
        if (isConnecting) return;
        isConnecting = true;

        Thread connectionThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();

            try {
                connection = DriverManager.getConnection("jdbc:postgresql://10.134.178.22:5432/game", "game", "7sur7");
                Runtime.getRuntime().addShutdownHook(new Thread(DbConnection::closeConnection));
            } catch (SQLException | NullPointerException e) {
                System.out.println(e.getMessage());
            }

            final boolean connectionResult = connectedToDb();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingDelay = 1950 - elapsedTime;

            // If connection was faster than 1950ms, wait for the remaining time
            if (remainingDelay > 0) {
                try {
                    Thread.sleep(remainingDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            Platform.runLater(() -> {
                isConnecting = false;
                if (callback != null) {

                    callback.onConnectionComplete(connectionResult);
                }
            });
        });

        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean connectedToDb() {
        try {
            // Use the existing connection from DbConnection
            // isValid() with a short timeout (2 seconds) avoids lengthy hangs
            return connection != null &&
                    !connection.isClosed() &&
                    connection.isValid(2);
        } catch (SQLException e) {
            return false;
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
                "   and upper(name) NOT IN ('GUEST', 'FRIEND');";
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

    public static String loadUnfinishedSessions() {
        return "SELECT gs.game_session_id,\n" +
                "       gs.player_id2,\n" +
                "       TO_CHAR(age(latest_pause.end_time, gs.start_time), 'HH24:MI:SS') as duration\n" +
                "FROM game_sessions gs\n" +
                "         JOIN (select m.game_session_id, max(pp.end_time) as end_time\n" +
                "               from moves m join pause_periods pp on pp.move_id = m.move_id\n" +
                "               group by m.game_session_id) latest_pause on latest_pause.game_session_id = gs.game_session_id\n" +
                "WHERE gs.is_completed = false\n" +
                "  and gs.player_id1 = ?\n" +
                "  and latest_pause.end_time IS NOT NULL\n" +
                "ORDER BY 2;";
    }

    public static String getGameSession() {
        return "SELECT gs.player_id2                                  as opponent_id,\n" +
                "       (select max(pp.end_time) from pause_periods pp\n" +
                "                 join moves m on m.move_id = pp.move_id\n" +
                "        where m.game_session_id = gs.game_session_id) as start_time\n" +
                "FROM game_sessions gs\n" +
                "         LEFT JOIN (select * from moves\n" +
                "                    where (game_session_id, move_id) in (select game_session_id, max(move_id)\n" +
                "                                                         from moves\n" +
                "                                                         group by game_session_id)) m\n" +
                "                   on gs.game_session_id = m.game_session_id\n" +
                "WHERE gs.game_session_id = ?;";
    }

    public static String setMove() {
        return "INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)\n" +
                "VALUES (?, ?, ?, ?, ?);";
    }

    public static String loadMoves() {
        return "SELECT m.move_id,\n" +
                "       m.player_id,\n" +
                "       start_time,\n" +
                "       end_time,\n" +
                "       move_nr,\n" +
                "       spt.piece_type_id    selected_piece_id,\n" +
                "       spt.color            selected_piece_color,\n" +
                "       spt.size             selected_piece_size,\n" +
                "       spt.fill             selected_piece_fill,\n" +
                "       spt.shape            selected_piece_shape,\n" +
                "       ppt.piece_type_id    placed_piece_id,\n" +
                "       ppt.color            placed_piece_color,\n" +
                "       ppt.size             placed_piece_size,\n" +
                "       ppt.fill             placed_piece_fill,\n" +
                "       ppt.shape            placed_piece_shape,\n" +
                "       coalesce(pos, -1) as pos\n" +
                "FROM moves m\n" +
                "         LEFT JOIN pieces pi on m.move_id = pi.move_id\n" +
                "         LEFT JOIN piece_types spt on pi.selected_piece_type_id = spt.piece_type_id\n" +
                "         LEFT JOIN piece_types ppt on pi.placed_piece_type_id = ppt.piece_type_id\n" +
                "WHERE game_session_id = ? ORDER BY m.move_id;";
    }

    public static String setPausePeriod() {
        return "INSERT INTO pause_periods (move_id, start_time, end_time)\n" +
                "VALUES (?, ?, ?);";
    }

    public static String setPiece() {
        return "INSERT INTO pieces (selected_piece_type_id, placed_piece_type_id, move_id, pos)\n" +
                "VALUES (?, ?, ?, ?);";
    }

    public static String getPieceId() {
        return "SELECT piece_type_id FROM piece_types\n" +
                "WHERE upper(fill)  = ?\n" +
                "  and upper(shape) = ?\n" +
                "  and upper(color) = ?\n" +
                "  and upper(size)  = ?;";
    }
}
