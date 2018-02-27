CREATE TABLE "user" (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255)
);

CREATE TABLE book (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255),
  publishYear SMALLINT
);

CREATE TABLE author (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255),
  book_id BIGINT
);