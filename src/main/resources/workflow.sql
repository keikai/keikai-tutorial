DROP TABLE workflow IF EXISTS;
CREATE TABLE workflow(
    id INTEGER IDENTITY PRIMARY KEY,
    form BLOB NOT NULL,
    formName VARCHAR(30) NOT NULL,
    owner VARCHAR(40) NOT NULL,
    state VARCHAR(20) NOT NULL,
    lastUpdate TIMESTAMP NOT NULL
);