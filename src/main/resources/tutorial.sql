DROP TABLE tutorial IF EXISTS;
CREATE TABLE tutorial (id INT NOT NULL, category VARCHAR(50) NOT NULL, quantity INT NOT NULL, subtotal INT NOT NULL, PRIMARY KEY (id));

INSERT INTO tutorial  VALUES (1, 'Transportation', 6, 2400);
INSERT INTO tutorial  VALUES (2, 'FOOD', 6, 15);
INSERT INTO tutorial  VALUES (3, 'Lodging', 2, 160);
INSERT INTO tutorial  VALUES (4, 'Entertainment', 1, 120);