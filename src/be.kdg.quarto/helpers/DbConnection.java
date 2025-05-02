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
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        closeConnection();
                    } catch (SQLException ignored) {
                    }
                }));
            } catch (SQLException ignored) {
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

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new SQLException("Failed to close Connection");
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
        return """
                SELECT winner_id, w.name, player_id1, p1.name, player_id2, p2.name
                FROM game_sessions gs
                         LEFT JOIN players w on (w.player_id = gs.winner_id)
                         INNER JOIN players p1 on (p1.player_id = gs.player_id1)
                         INNER JOIN players p2 on (p2.player_id = gs.player_id2)
                WHERE game_session_id = ?;""";
    }

    public static String getPartialStatistics() {
        return """
                SELECT p.player_id,
                       count(*)                                                                                as total_moves,
                       sum(extract(epoch from age(m.end_time, m.start_time))::numeric)                             as total_duration,
                       round(sum(extract(epoch from age(m.end_time, m.start_time)))::numeric / 60.0 / count(*), 2) as avg
                FROM moves m
                         INNER JOIN game_sessions gs on (gs.game_session_id = m.game_session_id)
                         INNER JOIN players p on (p.player_id = m.player_id)
                WHERE p.player_id = ? and gs.game_session_id = ?
                GROUP BY p.player_id, name;""";
    }

    public static String getStatistics() {
        return """
                SELECT player_id,
                       (move_nr + 1) / 2                                                as move_nr,
                       round(extract(epoch from age(end_time, start_time))::numeric, 2) as duration
                FROM moves
                WHERE game_session_id = ?;""";
    }

    public static String getLeaderboard() {
        return """
                SELECT name,
                       count(distinct gs.game_session_id)                                               as played,
                       count(distinct case when gs.winner_id = p.player_id then gs.game_session_id end) as wins,
                       count(distinct gs.game_session_id) -
                       count(distinct case
                                          when gs.winner_id = p.player_id or
                                               gs.winner_id IS NULL then gs.game_session_id end)        as losses,
                       round(count(distinct case when gs.winner_id = p.player_id then gs.game_session_id end) * 100.0
                                 / count(distinct gs.game_session_id), 2)                               as wins_p,
                       round(count(m.move_id) * 1.0
                                 / count(distinct gs.game_session_id), 2)                               as avg_moves,
                       round(sum(extract(epoch from age(m.end_time, m.start_time))::numeric / 60)
                                 / count(m.move_id), 2)                                                 as avg_duration_per_move
                FROM players p
                         LEFT JOIN game_sessions gs on p.player_id in (gs.player_id1, gs.player_id2)
                         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id
                GROUP BY name, is_completed, is_ai
                HAVING count(distinct gs.game_session_id) > 0
                   and is_completed = true
                   and is_ai = false
                   and upper(name) NOT IN ('GUEST', 'FRIEND');""";
    }

    public static String setGameSession() {
        return """
                INSERT INTO game_sessions (player_id1, player_id2, start_time)
                VALUES (?, ?, ?);""";
    }

    public static String updateGameSession() {
        return """
                UPDATE game_sessions
                SET winner_id    = ?,
                    is_completed = ?,\
                    end_time     = ?
                WHERE game_session_id = ?;""";
    }

    //new Variant (calculating including all pause_periods)
    public static String loadUnfinishedSessions(){
        return """
        SELECT
            gs.game_session_id,
            gs.player_id2,
            gs.end_time,
            TO_CHAR(
                MAKE_INTERVAL(secs => (
                    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - gs.start_time)) -
                    COALESCE(SUM(
                        EXTRACT(EPOCH FROM (COALESCE(pp.end_time, CURRENT_TIMESTAMP) - pp.start_time))
                    ), 0)
                )),
                'HH24:MI:SS'
            ) AS formatted_duration,
            CASE WHEN COUNT(pp.pause_period_id) FILTER (WHERE pp.end_time IS NULL) > 0 THEN true ELSE false END AS is_corrupted
        FROM game_sessions gs
        LEFT JOIN moves m ON gs.game_session_id = m.game_session_id
        LEFT JOIN pause_periods pp ON m.move_id = pp.move_id
        WHERE gs.is_completed = false
          AND gs.player_id1 = ?
        GROUP BY gs.game_session_id, gs.player_id2, gs.start_time, gs.end_time;
        """;
    }


    public static String getGameSession() {
        return """
            SELECT gs.player_id2 as opponent_id,
                   gs.start_time
            FROM game_sessions gs
            WHERE gs.game_session_id = ?;
            """;
    }




    public static String loadMoves() {
        return """
                SELECT  m.move_id            move_id,
                       m.player_id,
                       start_time,
                       end_time,
                       move_nr,
                       spt.piece_type_id    selected_piece_id,
                       spt.color            selected_piece_color,
                       spt.size             selected_piece_size,
                       spt.fill             selected_piece_fill,
                       spt.shape            selected_piece_shape,
                       ppt.piece_type_id    placed_piece_id,
                       ppt.color            placed_piece_color,
                       ppt.size             placed_piece_size,
                       ppt.fill             placed_piece_fill,
                       ppt.shape            placed_piece_shape,
                       coalesce(pos, -1) as pos
                FROM moves m
                         LEFT JOIN pieces pi on m.move_id = pi.move_id
                         LEFT JOIN piece_types spt on pi.selected_piece_type_id = spt.piece_type_id
                         LEFT JOIN piece_types ppt on pi.placed_piece_type_id = ppt.piece_type_id
                WHERE game_session_id = ? ORDER BY m.move_id;""";
    }

    public static String loadPausePeriods(){
        return """
                SELECT pause_period_id, start_time, end_time FROM pause_periods
                 WHERE move_id = ?;""";
    }

    public static String setPausePeriod() {
        return """
                INSERT INTO pause_periods (move_id, start_time, end_time)
                VALUES (?, ?, ?);""";
    }
    //update entry if already exists
    public static String setMove() {
        return """
        INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)
        VALUES (?, ?, ?, ?, ?)
        """;
    }

    public static String setPiece() {
        return """
                INSERT INTO pieces (selected_piece_type_id, placed_piece_type_id, move_id, pos)
                VALUES (?, ?, ?, ?);""";
    }
    public static String deleteMoveWithCascade() {
        return """
                DELETE FROM pause_periods WHERE move_id = ?;
                DELETE FROM pieces WHERE move_id = ?;
                DELETE FROM moves WHERE move_id = ?;""";
    }
    public static String getPieceId() {
        return """
                SELECT piece_type_id FROM piece_types
                WHERE upper(fill)  = ?
                  and upper(shape) = ?
                  and upper(color) = ?
                  and upper(size)  = ?;""";
    }
}
