-- drop tables
DROP TABLE IF EXISTS pause_periods;
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
    is_completed    BOOLEAN DEFAULT false,
    start_time      timestamp,
    end_time        timestamp
);

CREATE TABLE moves
(
    move_id         INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_move_id PRIMARY KEY,
    game_session_id INTEGER NOT NULL
        CONSTRAINT fk_game_session_id REFERENCES game_sessions (game_session_id)
            ON DELETE CASCADE,
    player_id       INTEGER NOT NULL
        CONSTRAINT fk_player_id REFERENCES players (player_id),
    start_time      timestamp,
    end_time        timestamp,
    move_nr         INTEGER,
    --     to prevent for the same move being recorded (will trigger constraint error if triggered, for debugging purposes)

    CONSTRAINT unique_game_move UNIQUE (game_session_id, move_nr)


);

CREATE TABLE pause_periods
(
    pause_period_id INTEGER GENERATED ALWAYS AS IDENTITY,
    move_id         INTEGER NOT NULL
        CONSTRAINT fk_move_id REFERENCES moves (move_id)
            ON DELETE CASCADE,
    start_time      timestamp,
    end_time        timestamp,
--     to prevent for the same pause_period being recorded (will trigger constraint error if triggered, for debugging purposes)
    CONSTRAINT unique_timestamps UNIQUE (start_time, end_time, move_id)

);

CREATE TABLE piece_types
(
    piece_type_id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pk_piece_type_id PRIMARY KEY,
    fill          varchar(20) NOT NULL ,
    shape         varchar(20) NOT NULL,
    color         varchar(20) NOT NULL,
    size          varchar(20) NOT NULL
);

CREATE TABLE pieces
(
--     NEW pieces table implemented for incomplete moves
    /*we make piece_types not null for the incomplete move when the player exits with an unfinished game*/
    move_id                INTEGER NOT NULL
        CONSTRAINT pk_piece_move_id PRIMARY KEY
        CONSTRAINT fk_move_id REFERENCES moves (move_id)
            ON DELETE CASCADE,
    selected_piece_type_id INTEGER,
    placed_piece_type_id   INTEGER,
    pos                    INTEGER,

    CONSTRAINT fk_selected_piece_type_id FOREIGN KEY (selected_piece_type_id) REFERENCES piece_types (piece_type_id),
    CONSTRAINT fk_placed_piece_type_id FOREIGN KEY (placed_piece_type_id) REFERENCES piece_types (piece_type_id)
);