SELECT *
FROM game_sessions
WHERE game_session_id = 1;

-- general information about player
SELECT winner,
       count(*)                                                                    as total_moves,
       concat
       (sum(extract(hour from age(move_end_time, move_start_time))), ' h ',
        sum(extract(minute from age(move_end_time, move_start_time))), ' m ',
        sum(extract(second from age(move_end_time, move_start_time))), ' seconds') as total_time,
       round(
               (cast(sum(extract(epoch from age(move_end_time, move_start_time))) as integer) / 60.0),
               4) / count(*)                                                       as average_per_move
FROM moves m
         INNER JOIN public.game_sessions gs on m.game_session_id = gs.game_session_id
WHERE m.game_session_id = 1
  and player_id = 1
GROUP BY winner;

-- average minutes per move
SELECT game_session_id,
       player_id,
       round(
               count(*) /
               (cast(sum(extract(epoch from age(move_end_time, move_start_time))) as integer) / 60.0),
               4) as average_per_move
FROM moves m
GROUP BY game_session_id, player_id;

-- retrieves data for a line chart for both players x-axis -> move number y-axis -> time in minutes
SELECT player_id,
       (select COUNT(*)
        from moves m2
        where m2.player_id = m1.player_id
          and m2.move_start_time <= m1.move_start_time)                                          as move_number,
       round(cast(extract(epoch from age(move_end_time, move_start_time)) as integer) / 60.0, 2) as mins
FROM moves m1
WHERE game_session_id = 1
-- and player_id in (1, 2)
GROUP BY player_id, move_start_time, move_end_time
ORDER BY player_id, move_start_time;