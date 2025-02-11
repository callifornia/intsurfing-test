CREATE DATABASE test;
CREATE SCHEMA IF NOT EXISTS records;
CREATE TABLE IF NOT EXISTS records.company (
  uuid              VARCHAR (255) PRIMARY KEY,
  name 			        VARCHAR (255) NULL,
  revenue   	      bigint default 0,
  employee_amount   bigint default 0);

CREATE INDEX name_index ON records.company (name);
