DROP TABLE tutorial IF EXISTS;
CREATE TABLE tutorial (
    id INTEGER IDENTITY PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    subtotal INT NOT NULL
);

INSERT INTO tutorial (category, quantity, subtotal) VALUES ('Transportation', 10, 2400);
INSERT INTO tutorial (category, quantity, subtotal) VALUES ('Food', 16, 15);
INSERT INTO tutorial (category, quantity, subtotal) VALUES ('Lodging', 12, 160);
INSERT INTO tutorial (category, quantity, subtotal) VALUES ('Entertainment', 11, 120);