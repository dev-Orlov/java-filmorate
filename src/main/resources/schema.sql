CREATE TYPE IF NOT EXISTS rating_enum AS ENUM ('G', 'PG', 'PG-13', 'R', 'NC-17');

CREATE TABLE IF NOT EXISTS films (
    film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name varchar(100),
    description varchar(200),
    release_date date,
    duration int,
    rating rating_enum
);

CREATE TYPE IF NOT EXISTS genre_enum AS ENUM ('Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик');

CREATE TABLE IF NOT EXISTS film_genre (
    film_id integer NOT NULL REFERENCES films,
    genre genre_enum
);

CREATE TABLE IF NOT EXISTS users (
   user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   email varchar,
   login varchar,
   user_name varchar,
   birthday date
);

CREATE TABLE IF NOT EXISTS likes (
    film_id integer REFERENCES films,
    liked_user_id integer REFERENCES users
);

CREATE TABLE IF NOT EXISTS friends (
    user_id integer NOT NULL REFERENCES users,
    friend_id integer NOT NULL REFERENCES users,
    status boolean
);
