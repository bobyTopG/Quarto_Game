-- Insert sample data into the player table
INSERT INTO players (name, password)
VALUES ('Alice', 'a1234'),
       ('Bob', 'b1234'),
       ('Charlie', 'c1234'),
       ('David', 'd1234');

-- Insert sample data into the game_session table
INSERT INTO game_sessions (player1_id, player2_id, start_time, end_time, winner)
VALUES (1, 2, '2024-07-26 10:00:00', '2024-07-26 11:30:00', 2),
       (3, 1, '2024-07-26 12:00:00', '2024-07-26 13:00:00', 1),
       (2, 4, '2024-07-26 14:00:00', '2024-07-26 15:00:00', 4);

-- Insert sample data into the board table
INSERT INTO boards (game_session_id)
VALUES (1),
       (2),
       (3);

-- Insert sample data into the piece table
INSERT INTO pieces (board_id, pos, color, size, fill, shape)
VALUES (1, 1, 'black', 'small', 'solid', 'circle'),
       (1, 2, 'white', 'large', 'hollow', 'square'),
       (1, 3, 'black', 'large', 'solid', 'square'),
       (1, 4, 'white', 'small', 'hollow', 'circle'),
       (1, 5, 'black', 'small', 'hollow', 'square'),
       (1, 6, 'white', 'large', 'solid', 'circle'),
       (1, 7, 'black', 'large', 'hollow', 'circle'),
       (1, 8, 'white', 'small', 'solid', 'square'),
       (1, 9, 'black', 'large', 'solid', 'circle'),
       (1, 10, 'white', 'small', 'hollow', 'square'),

       (2, 1, 'black', 'small', 'hollow', 'circle'),
       (2, 2, 'white', 'small', 'hollow', 'square'),
       (3, 1, 'white', 'large', 'solid', 'square'),
       (3, 2, 'black', 'large', 'hollow', 'circle');

-- Insert sample data into the move table
INSERT INTO moves (game_session_id, player_id, piece_id, move_start_time, move_end_time)
VALUES (1, 1, 1, '2024-07-26 10:05:12', '2024-07-26 10:06:43'),
       (1, 2, 2, '2024-07-26 10:07:30', '2024-07-26 10:09:02'),
       (1, 1, 3, '2024-07-26 10:10:20', '2024-07-26 10:12:48'),
       (1, 2, 4, '2024-07-26 10:13:55', '2024-07-26 10:16:01'),
       (1, 1, 5, '2024-07-26 10:17:14', '2024-07-26 10:18:45'),
       (1, 2, 6, '2024-07-26 10:20:05', '2024-07-26 10:22:49'),
       (1, 1, 7, '2024-07-26 10:24:10', '2024-07-26 10:25:35'),
       (1, 2, 8, '2024-07-26 10:26:50', '2024-07-26 10:29:32'),
       (1, 1, 9, '2024-07-26 10:30:40', '2024-07-26 10:32:59'),
       (1, 2, 10, '2024-07-26 10:34:10', '2024-07-26 10:35:46'),

       (2, 3, 4, '2024-07-26 12:05:00', '2024-07-26 12:06:00'),
       (2, 1, 5, '2024-07-26 12:10:00', '2024-07-26 12:11:00'),
       (3, 2, 6, '2024-07-26 14:05:00', '2024-07-26 14:06:00'),
       (3, 4, 7, '2024-07-26 14:10:00', '2024-07-26 14:11:00');

