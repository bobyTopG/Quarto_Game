-- winner
SELECT winner_id, w.name, player_id1, p1.name, player_id2, p2.name
FROM game_sessions gs
         LEFT JOIN players w on (w.player_id = gs.winner_id)
         INNER JOIN players p1 on (p1.player_id = gs.player_id1)
         INNER JOIN players p2 on (p2.player_id = gs.player_id2)
WHERE game_session_id = 2;

-- 1.
SELECT p.player_id,
       name,
       count(*)                                                                                as total_moves,
       sum(extract(epoch from age(end_time, start_time))::numeric)                             as total_duration,
       round(sum(extract(epoch from age(end_time, start_time)))::numeric / 60.0 / count(*), 2) as avg
FROM moves m
         INNER JOIN game_sessions gs on (gs.game_session_id = m.game_session_id)
         INNER JOIN players p on (p.player_id = m.player_id)
WHERE p.player_id = 3
  and gs.game_session_id = 2
GROUP BY p.player_id, name;

-- 2.
SELECT player_id,
       (move_nr + 1) / 2                                                       as move_nr,
       round(extract(epoch from age(end_time, start_time))::numeric / 60.0, 2) as duration
FROM moves
WHERE player_id = 1
  and game_session_id = 1;

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
HAVING count(distinct gs.game_session_id) > 0
   and is_completed = true;

select end_time
from moves;

INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)
VALUES (2, 3, '2025-04-01 14:05:00', '2025-04-01 14:08:10', 1);

INSERT INTO game_sessions (player_id1, player_id2, winner_id, is_completed)
VALUES (3, 4, NULL, true);

UPDATE game_sessions
SET winner_id    = 4,
    is_completed = true
WHERE game_session_id = 2;

SELECT name
FROM players
WHERE is_ai = true;
SELECT *
FROM game_sessions;


SELECT piece_type_id
FROM piece_types
WHERE upper(fill) = 'FULL'
  and upper(shape) = 'CIRCLE'
  and upper(color) = 'BLACK'
  and upper(size) = 'SMALL';

INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)
VALUES (1, 1, current_timestamp, 2);

INSERT INTO pieces (piece_type_id, move_id, pos)
VALUES (1, 1, 2);

SELECT * FROM players;
SELECT * FROM game_sessions;
SELECT * FROM moves WHERE game_session_id > 4;
SELECT * FROM pieces WHERE move_id > 32;

SELECT * FROM piece_types;