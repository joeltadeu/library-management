CREATE TABLE author (
                        id   INTEGER AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL
);

CREATE TABLE category (
                          id   INTEGER AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);

CREATE TABLE book (
                      id INTEGER AUTO_INCREMENT PRIMARY KEY,
                      author_id INTEGER NOT NULL,
                      isbn VARCHAR(64) NOT NULL,
                      title VARCHAR(100) NOT NULL,
                      CONSTRAINT fk_bk_author FOREIGN KEY (author_id) REFERENCES author (id)
);

CREATE TABLE book_category (
                               id   INTEGER AUTO_INCREMENT PRIMARY KEY,
                               book_id   INTEGER NOT NULL,
                               category_id   INTEGER NOT NULL,
                               CONSTRAINT fk_bkcat_book FOREIGN KEY (book_id) REFERENCES book (id),
                               CONSTRAINT fk_bkcat_category FOREIGN KEY (category_id) REFERENCES category (id)
);