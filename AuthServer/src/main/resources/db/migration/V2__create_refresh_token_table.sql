CREATE TABLE refresh_token
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    token        VARCHAR(512) NOT NULL,
    is_revoked   BOOLEAN,
    date_created DATETIME,
    user_id    BIGINT         NOT NULL,
    CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES auth_user(id) ON DELETE CASCADE
);