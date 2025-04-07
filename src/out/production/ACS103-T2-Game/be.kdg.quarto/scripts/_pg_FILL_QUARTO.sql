INSERT INTO players (name, is_ai)
VALUES ('Bob', true),
       ('Robert', true);

INSERT INTO players (name, password)
VALUES ('Alice', 'password'),
       ('Bob', 'password'),
       ('Charlie', 'password'),
       ('David', 'password');

-- Game session 1: Alice vs. Bob (Winner: Alice)
INSERT INTO game_sessions (player_id1, player_id2, winner_id, is_completed)
VALUES (1, 2, 1, true);
-- Alice wins

-- Game session 2: Charlie vs. David (Draw)
INSERT INTO game_sessions (player_id1, player_id2, winner_id, is_completed)
VALUES (3, 4, NULL, true);
-- Draw

-- Game 1: Alice vs. Bob
INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)
VALUES (1, 1, '2025-04-01 10:05:00', '2025-04-01 10:07:10', 1),
       (1, 2, '2025-04-01 10:07:00', '2025-04-01 10:08:20', 2),
       (1, 1, '2025-04-01 10:10:00', '2025-04-01 10:14:30', 3),
       (1, 2, '2025-04-01 10:12:00', '2025-04-01 10:14:40', 4),
       (1, 1, '2025-04-01 10:15:00', '2025-04-01 10:16:50', 5),
       (1, 2, '2025-04-01 10:17:00', '2025-04-01 10:19:00', 6);

-- Game 2: Charlie vs. David
INSERT INTO moves (game_session_id, player_id, start_time, end_time, move_nr)
VALUES (2, 3, '2025-04-01 14:05:00', '2025-04-01 14:08:10', 1),
       (2, 4, '2025-04-01 14:07:00', '2025-04-01 14:09:20', 2),
       (2, 3, '2025-04-01 14:10:00', '2025-04-01 14:12:30', 3),
       (2, 4, '2025-04-01 14:12:00', '2025-04-01 14:13:40', 4),
       (2, 3, '2025-04-01 14:15:00', '2025-04-01 14:17:50', 5),
       (2, 4, '2025-04-01 14:17:00', '2025-04-01 14:20:00', 6);

INSERT INTO piece_types (fill, shape, color, size)
VALUES ('Full', 'Circle', 'White', 'Small'),
       ('Full', 'Circle', 'White', 'Big'),
       ('Full', 'Circle', 'Black', 'Small'),
       ('Full', 'Circle', 'Black', 'Big'),
       ('Full', 'Square', 'White', 'Small'),
       ('Full', 'Square', 'White', 'Big'),
       ('Full', 'Square', 'Black', 'Small'),
       ('Full', 'Square', 'Black', 'Big'),
       ('Hallow', 'Circle', 'White', 'Small'),
       ('Hallow', 'Circle', 'White', 'Big'),
       ('Hallow', 'Circle', 'Black', 'Small'),
       ('Hallow', 'Circle', 'Black', 'Big'),
       ('Hallow', 'Square', 'White', 'Small'),
       ('Hallow', 'Square', 'White', 'Big'),
       ('Hallow', 'Square', 'Black', 'Small'),
       ('Hallow', 'Square', 'Black', 'Big');

-- Pieces for Game 1
INSERT INTO pieces (piece_type_id, move_id, pos)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 4),
       (5, 5, 5),
       (6, 6, 6);

-- Pieces for Game 2
INSERT INTO pieces (piece_type_id, move_id, pos)
VALUES (7, 7, 1),
       (8, 8, 2),
       (9, 9, 3),
       (10, 10, 4),
       (11, 11, 5),
       (12, 12, 6);