DELETE FROM friends;
DELETE FROM likes;
DELETE FROM users;
DELETE FROM film_genre;
DELETE FROM genres;
DELETE FROM films;
DELETE FROM rating;

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN genre_id RESTART WITH 1;
ALTER TABLE rating ALTER COLUMN rating_id RESTART WITH 1;

INSERT INTO genres (genre_title)
VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

INSERT INTO rating (name)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');