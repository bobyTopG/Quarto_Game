-- DROP EXISTING TABLES
DROP TABLE IF EXISTS pieces CASCADE;
DROP TABLE IF EXISTS boards CASCADE;
DROP TABLE IF EXISTS game_sessions CASCADE;
DROP TABLE IF EXISTS moves CASCADE;
DROP TABLE IF EXISTS players CASCADE;

-- CREATE TABLES
CREATE TABLE players
(
    player_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_player PRIMARY KEY,
    name      VARCHAR(50)
        CONSTRAINT nn_name NOT NULL,
    password  VARCHAR(20)
        CONSTRAINT nn_password NOT NULL
);

-- CREATE TABLE human
-- (
--     id       INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 1)
--         CONSTRAINT pk_player PRIMARY KEY,
--     name VARCHAR(20)
--         CONSTRAINT nn_name NOT NULL,
--     password VARCHAR(20)
--         CONSTRAINT nn_password NOT NULL
-- );
--
-- CREATE TABLE ai
-- (
--     id         INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1)
--         CONSTRAINT pk_player PRIMARY KEY,
--     difficulty VARCHAR(20)
-- );


CREATE TABLE game_sessions
(
    game_session_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_game_session PRIMARY KEY,
    player1_id      INTEGER
        CONSTRAINT fk_player1_id REFERENCES players (player_id),
    player2_id      INTEGER
        CONSTRAINT fk_player2_id REFERENCES players (player_id),
    start_time      timestamp,
    end_time        timestamp,
    winner          INTEGER
        CONSTRAINT ch_winner check ( winner IN (player1_id, player2_id) )
);

CREATE TABLE boards
(
    board_id        INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_board PRIMARY KEY,
    game_session_id INTEGER
        CONSTRAINT fk_game_session_id REFERENCES game_sessions (game_session_id)
);

CREATE TABLE pieces
(
    piece_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_piece PRIMARY KEY,
    board_id INTEGER
        CONSTRAINT fk_board_id REFERENCES boards (board_id),
    pos      integer,
    color    varchar(20),
    size     varchar(20),
    fill     varchar(20),
    shape    varchar(20)
);

CREATE TABLE moves
(
    move_id         INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_move PRIMARY KEY,
    game_session_id INTEGER
        CONSTRAINT fk_game_session_id REFERENCES game_sessions (game_session_id),
    player_id       INTEGER
        CONSTRAINT fk_player_id REFERENCES players (player_id),
    piece_id        INTEGER
        CONSTRAINT fk_piece_id REFERENCES pieces (piece_id),
    move_start_time timestamp,
    move_end_time   timestamp
);