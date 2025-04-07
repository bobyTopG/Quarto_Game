-- drop tables
DROP TABLE IF EXISTS pieces;
DROP TABLE IF EXISTS piece_types;
DROP TABLE IF EXISTS moves;
DROP TABLE IF EXISTS game_sessions;
DROP TABLE IF EXISTS players;

-- new tables
CREATE TABLE players
(
    player_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_player PRIMARY KEY,
    name      varchar(20) NOT NULL,
    password  varchar(256),
    is_ai     BOOLEAN DEFAULT false
);

CREATE TABLE game_sessions
(
    game_session_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_game_session_id PRIMARY KEY,
    player_id1      INTEGER NOT NULL
        CONSTRAINT fk_player_id1 REFERENCES players (player_id),
    player_id2      INTEGER NOT NULL
        CONSTRAINT fk_player_id2 REFERENCES players (player_id),
    winner_id       INTEGER
        CONSTRAINT fk_winner_id REFERENCES players (player_id),
    is_completed    BOOLEAN DEFAULT false
);

CREATE TABLE moves
(
    move_id         INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_move_id PRIMARY KEY,
    game_session_id INTEGER NOT NULL
        CONSTRAINT fk_game_session_id REFERENCES game_sessions (game_session_id),
    player_id       INTEGER NOT NULL
        CONSTRAINT fk_player_id REFERENCES players (player_id),
    start_time      timestamp,
    end_time        timestamp,
    move_nr         INTEGER
);

CREATE TABLE piece_types
(
    piece_type_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_piece_type_id PRIMARY KEY,
    fill          varchar(20),
    shape         varchar(20),
    color         varchar(20),
    size          varchar(20)
);

CREATE TABLE pieces
(
    piece_type_id INTEGER NOT NULL,
    move_id       INTEGER NOT NULL,
    pos           INTEGER,

    CONSTRAINT pk_piece_type_move_id PRIMARY KEY (piece_type_id, move_id),
    CONSTRAINT fk_piece_type_id FOREIGN KEY (piece_type_id) REFERENCES piece_types (piece_type_id),
    CONSTRAINT fk_move_id FOREIGN KEY (move_id) REFERENCES moves (move_id)
);