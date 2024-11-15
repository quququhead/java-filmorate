DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS film_directors;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS reviews_likes;
DROP TABLE IF EXISTS reviews_dislikes;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS rating_mpas;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS directors;
DROP TABLE IF EXISTS feed;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name varchar(255),
    login varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    birthday_date date NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE
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
    film_id BIGINT REFERENCES films (film_id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS directors (
    director_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    director_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_directors (
    film_id BIGINT REFERENCES films (film_id) ON DELETE CASCADE,
    director_id BIGINT REFERENCES directors (director_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    is_positive BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_likes (
    review_id BIGINT NOT NULL REFERENCES reviews(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_dislikes (
    review_id BIGINT NOT NULL REFERENCES reviews(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feed (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    type VARCHAR(6) NOT NULL,
    operation VARCHAR(6) NOT NULL,
    entity_id BIGINT NOT NULL,
    updated BIGINT NOT NULL
);
