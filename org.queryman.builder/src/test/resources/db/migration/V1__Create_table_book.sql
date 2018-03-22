CREATE TABLE "user" (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255)
);

CREATE TABLE book
(
  id     SERIAL NOT NULL,
  name   VARCHAR(255),
  author_id BIGINT,
  year   SMALLINT,
  price  REAL,

  CONSTRAINT index_name PRIMARY KEY (name)
);

-- CREATE UNIQUE INDEX index_name on book (name);

CREATE TABLE sales
(
  id     SERIAL NOT NULL,
  book_id int
);

CREATE TABLE orders (
  id     SERIAL NOT NULL,
  book_id BIGINT,
  author_id BIGINT
);

CREATE TABLE author (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255)
);