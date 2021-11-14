CREATE TABLE book_order (
                        id   INTEGER AUTO_INCREMENT PRIMARY KEY,
                        book_id INTEGER NOT NULL,
                        user_id INTEGER NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        loaned_at TIMESTAMP NOT NULL,
                        return_in TIMESTAMP NOT NULL,
                        returned_at TIMESTAMP NULL
);

