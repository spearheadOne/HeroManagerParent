CREATE TABLE hero_property (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               hero_id BIGINT NOT NULL,
                               property_type VARCHAR(255) NOT NULL,
                               property_value VARCHAR(255) NOT NULL,
                               CONSTRAINT fk_hero FOREIGN KEY (hero_id) REFERENCES hero(id) ON DELETE CASCADE
);