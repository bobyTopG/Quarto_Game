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
       (move_nr + 1) / 2                                                as move_nr,
       round(extract(epoch from age(end_time, start_time))::numeric, 2) as duration
FROM moves
WHERE player_id = 1
  and game_session_id = 1;

-- 3.
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
   and upper(name) != 'GUEST';

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

SELECT *
FROM players;

SELECT *
FROM game_sessions;

SELECT *
FROM moves
WHERE game_session_id = 184;

SELECT *
FROM pieces
WHERE move_id >= 34;

SELECT *
FROM piece_types;

--2025-04-24 17:52:54.642000
select *
from pause_periods;

select *
from players;

select *
from moves
order by 1 desc;

-- specific unfinished game session
SELECT gs.game_session_id,
       p2.player_id                                    as opponent_id,
       (SELECT max(pp.start_time)
        FROM pause_periods pp
                 JOIN moves m2 on m2.move_id = pp.move_id
        WHERE m2.game_session_id = gs.game_session_id) as start_time
FROM game_sessions gs
         JOIN players p2 on (gs.player_id2 = p2.player_id)
         LEFT JOIN (SELECT *
                    FROM moves
                    WHERE (game_session_id, move_id) in (SELECT game_session_id, max(move_id)
                                                         FROM moves
                                                         GROUP BY game_session_id)) m
                   on gs.game_session_id = m.game_session_id
WHERE gs.game_session_id = 13;

SELECT *
FROM pause_periods;

-- all unfinished game sessions
-- SELECT gs.game_session_id,
--        gs.player_id2,
--        TO_CHAR(age((select max(pp.start_time)
--                     from pause_periods pp
--                     where pp.move_id = (select max(m.move_id)
--                                         from moves m
--                                         where m.game_session_id = gs.game_session_id)), gs.start_time),
--                'HH24:MI:SS') as duration
-- FROM game_sessions gs
-- WHERE gs.is_completed = false
--   and gs.player_id1 = 6
-- -- can be deleted
--   and (SELECT MAX(pp.start_time)
--        FROM pause_periods pp
--        WHERE pp.move_id = (SELECT MAX(m.move_id)
--                            FROM moves m
--                            WHERE m.game_session_id = gs.game_session_id)) IS NOT NULL
-- -- ...
-- ORDER BY 2;

SELECT gs.game_session_id,
       gs.player_id2,
       TO_CHAR(age(latest_pause.start_time, gs.start_time), 'HH24:MI:SS') as duration
FROM game_sessions gs
         JOIN (SELECT m.game_session_id,
                      max(pp.start_time) as start_time
               FROM moves m
                        JOIN pause_periods pp on pp.move_id = m.move_id
               GROUP BY m.game_session_id) latest_pause on latest_pause.game_session_id = gs.game_session_id
WHERE gs.is_completed = false
  and gs.player_id1 = 6
  and latest_pause.start_time IS NOT NULL
ORDER BY gs.player_id2;

-- moves for continuing a game session
SELECT m.move_id,
       pl.player_id,
       name,
       start_time,
       end_time,
       move_nr,
       pt.piece_type_id,
       pt.color,
       pt.size,
       pt.fill,
       pt.shape,
       coalesce(pos, -1) as pos
FROM moves m
         JOIN players pl on m.player_id = pl.player_id
         LEFT JOIN pieces pi on m.move_id = pi.move_id
         LEFT JOIN piece_types pt on pi.piece_type_id = pt.piece_type_id
WHERE game_session_id = 13
ORDER BY m.move_id;

select * from game_sessions order by 1 desc;
select * from moves where game_session_id = 43;