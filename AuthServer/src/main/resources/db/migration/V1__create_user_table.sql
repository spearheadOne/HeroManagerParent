CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      password_hash VARCHAR(255) NOT NULL
);