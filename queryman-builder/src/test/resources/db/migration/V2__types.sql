CREATE TABLE types (
  smallint SMALLINT,
  integer integer ,
  bigint bigint,
  decimal decimal,
  numeric numeric,
  real real,
  double_precision double precision,

  money money,

  varchar varchar(255),
  char char(1),
  text text,

  bytea bytea,

  timestamp timestamp,
  date date,
  time time,
  interval interval,

  boolean boolean,

  cidr cidr,
  inet inet,
  macaddr macaddr,

  bit bit, -- B'1'
  bit_varying  bit varying(5),
  uuid uuid, -- a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11
  xml xml,

  arr_int integer[]
);