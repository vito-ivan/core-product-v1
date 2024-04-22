CREATE SCHEMA IF NOT EXISTS prod_dire;


DROP TABLE IF EXISTS prod_dire.product;


CREATE TABLE prod_dire.product(id varchar(36) PRIMARY KEY,
                               code varchar(5),
                               name varchar(20));

GRANT ALL PRIVILEGES ON SCHEMA prod_dire TO postgresuser;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA prod_dire TO postgresuser;