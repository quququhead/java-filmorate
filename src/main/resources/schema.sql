DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS rating_mpas;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name varchar(255),
    login varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    birthday_date date NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT REFERENCES users (user_id),
    friend_id BIGINT REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS rating_mpas (
    rating_mpa_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rating_mpa_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_name varchar(255) NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    rating_mpa_id BIGINT REFERENCES rating_mpas (rating_mpa_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT REFERENCES films (film_id),
    user_id BIGINT REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES films (film_id),
    genre_id BIGINT REFERENCES genres (genre_id)
);