-- winner
SELECT winner_id
FROM game_sessions
WHERE game_session_id = 1;

-- 1.
SELECT p.player_id,
       name,
       count(*)                                                                                as total_moves,
       sum(extract(epoch from age(end_time, start_time))::numeric)                             as total_duration,
       round(sum(extract(epoch from age(end_time, start_time)))::numeric / 60.0 / count(*), 2) as avg
FROM moves m
         INNER JOIN game_sessions gs on (gs.game_session_id = m.game_session_id)
         INNER JOIN players p on (p.player_id = m.player_id)
WHERE p.player_id = 1 and gs.game_session_id = 1
GROUP BY p.player_id, name;

-- 2.
SELECT player_id,
       (move_nr + 1) / 2                                                       as move_nr,
       round(extract(epoch from age(end_time, start_time))::numeric / 60.0, 2) as duration
FROM moves
WHERE player_id = 1 and game_session_id = 1;

-- 3.
SELECT name,
       count(distinct gs.game_session_id)                                                      as played,
       count(distinct case when gs.winner_id = p.player_id then 1 end)                         as wins,
       count(distinct gs.game_session_id) -
       count(distinct case when gs.winner_id = p.player_id or gs.winner_id IS NULL then 1 end) as losses,
       count(distinct case when gs.winner_id = p.player_id then 1 end) * 100.0
           / count(distinct gs.game_session_id)                                                as wins_p,
       round(count(m.move_id) * 1.0
                 / count(distinct gs.game_session_id), 2)                                      as avg_moves,
       round(sum(extract(epoch from age(m.end_time, m.start_time))::numeric / 60)
                 / count(m.move_id), 2)                                                        as avg_duration_per_move
FROM players p
         LEFT JOIN game_sessions gs on p.player_id in (gs.player_id1, gs.player_id2)
         LEFT JOIN moves m on gs.game_session_id = m.game_session_id and p.player_id = m.player_id
GROUP BY name, is_completed
HAVING count(distinct gs.game_session_id) > 0 and is_completed = true;