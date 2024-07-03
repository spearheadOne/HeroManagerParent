CREATE TABLE refresh_token
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    token        VARCHAR(512) NOT NULL,
    is_revoked   BOOLEAN,
    username     VARCHAR(255) NOT NULL,
    date_created DATETIME
);