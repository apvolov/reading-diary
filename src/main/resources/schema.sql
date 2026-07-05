DROP TABLE IF EXISTS shelf_books CASCADE;
DROP TABLE IF EXISTS shelves CASCADE;
DROP TABLE IF EXISTS user_books CASCADE;
DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE user_books (
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title          VARCHAR(255) NOT NULL,
    author         VARCHAR(255) NOT NULL,
    year           INT,
    description    TEXT,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PLANNED'
                       CHECK (status IN ('PLANNED', 'READING', 'READ')),
    rating         INT CHECK (rating BETWEEN 1 AND 10),
    review         TEXT,
    date_added     TIMESTAMP    NOT NULL DEFAULT now(),
    date_finished  DATE
);

CREATE TABLE shelves (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name    VARCHAR(100) NOT NULL,
    UNIQUE (user_id, name)
);

CREATE TABLE shelf_books (
    shelf_id      BIGINT NOT NULL REFERENCES shelves(id) ON DELETE CASCADE,
    user_book_id  BIGINT NOT NULL REFERENCES user_books(id) ON DELETE CASCADE,
    PRIMARY KEY (shelf_id, user_book_id)
);

CREATE INDEX idx_user_books_user_id ON user_books(user_id);
CREATE INDEX idx_shelves_user_id ON shelves(user_id);
