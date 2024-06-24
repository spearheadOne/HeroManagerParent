CREATE INDEX idx_hero_alias ON hero(alias);
CREATE INDEX idx_hero_name ON hero(name);
CREATE INDEX idx_hero_property_hero_id ON hero_property(hero_id);
CREATE INDEX idx_hero_property_property_type ON hero_property(property_type);