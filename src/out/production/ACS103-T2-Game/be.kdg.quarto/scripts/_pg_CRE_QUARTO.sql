-- DROP EXISTING TABLES
DROP TABLE IF EXISTS piece CASCADE ;
DROP TABLE IF EXISTS board CASCADE ;
DROP TABLE IF EXISTS game_session CASCADE ;
DROP TABLE IF EXISTS move CASCADE ;
DROP TABLE IF EXISTS player CASCADE ;

-- CREATE TABLES
CREATE TABLE player (
                        player_id           SERIAL
                            CONSTRAINT pk_player PRIMARY KEY,
                        name                varchar(50)
                            CONSTRAINT nn_name NOT NULL,
                        password            varchar(50)
                            CONSTRAINT nn_password NOT NULL

);

CREATE TABLE game_session (
                              game_session_id       SERIAL
                                  CONSTRAINT pk_game_session PRIMARY KEY,
                              player_id1           INTEGER
                                  CONSTRAINT fk_player_id1 REFERENCES player(player_id),
                              player_id2           INTEGER
                                  CONSTRAINT fk_player_id2 REFERENCES Player(player_id)
                                  CONSTRAINT ck_player_id  CHECK (player_id2!= player_id1),
                              start_time           timestamp,
                              end_time             timestamp,
                              total_turns          integer
                                  CONSTRAINT ps_total_turns CHECK (total_turns > -1)

);

CREATE TABLE board (
                       board_id            SERIAL
                           CONSTRAINT pk_board PRIMARY KEY,
                       game_session_id     INTEGER
                           CONSTRAINT fk_game_session_id REFERENCES game_session(game_session_id)
);

CREATE TABLE piece (
                       piece_id            SERIAL
                           CONSTRAINT pk_piece PRIMARY KEY,
                       board_id            INTEGER
                           CONSTRAINT fk_board_id REFERENCES board(board_id),
                       pos                 integer,
                       color               varchar(20),
                       size                varchar(20),
                       fill                varchar(20),
                       shape               varchar(20)
);

CREATE TABLE move (
                      move_id             SERIAL
                          CONSTRAINT pk_move PRIMARY KEY,
                      game_session_id     INTEGER
                          CONSTRAINT fk_game_session_id REFERENCES game_session(game_session_id),
                      player_id           INTEGER
                          CONSTRAINT fk_player_id REFERENCES player(player_id),
                      piece_id            INTEGER
                          CONSTRAINT fk_piece_id REFERENCES piece(piece_id),
                      move_start_time     timestamp,
                      move_end_time       timestamp
);