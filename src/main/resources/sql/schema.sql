CREATE TABLE IF NOT EXISTS users (
  user_code  VARCHAR(7),
  photo      LONGBLOB,
  first_name VARCHAR(32) NOT NULL,
  last_name  VARCHAR(32) NOT NULL,
  bio		 VARCHAR(1024) NOT NULL DEFAULT 'This user does not have a bio of themselves.',
  username   VARCHAR(16) NOT NULL,
  password   VARCHAR(60) NOT NULL,
  PRIMARY KEY (user_code)
);

CREATE TABLE IF NOT EXISTS books (
  book_id      INT UNSIGNED  AUTO_INCREMENT,
  cover        LONGBLOB,
  title        VARCHAR(128)  NOT NULL,
  author	   VARCHAR(64),
  description  VARCHAR(1024) NOT NULL DEFAULT 'This book doesn\'t have description yet.',
  pages		   SMALLINT NOT NULL DEFAULT 0,
  publisher    VARCHAR(64),
  publish_date VARCHAR(18),
  rating       TINYINT(1)    NOT NULL DEFAULT 0,
  PRIMARY KEY (book_id)
);

CREATE TABLE IF NOT EXISTS roles (
  role_id INT UNSIGNED AUTO_INCREMENT,
  label   VARCHAR(16)  NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS book_codes (
  book_id INT UNSIGNED    AUTO_INCREMENT,
  isbn10  VARCHAR(10),
  isbn13  VARCHAR(13),
  PRIMARY KEY (book_id),
  FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_code VARCHAR(7)  NOT NULL,
  role_id INT UNSIGNED 	NOT NULL,
  FOREIGN KEY (user_code) REFERENCES users(user_code),
  FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE IF NOT EXISTS favourited_books (
  user_code VARCHAR(7)  NOT NULL,
  book_id INT UNSIGNED  AUTO_INCREMENT 	NOT NULL,
  FOREIGN KEY (user_code) REFERENCES users(user_code),
  FOREIGN KEY (book_id) REFERENCES books(book_id)
);
