package org.abondar.industrial.heromanager.repo;

import org.abondar.industrial.heromanager.model.db.Hero;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    @Cacheable("heroesCache")
    Optional<Hero> findByAlias(String alias);

    @Cacheable("heroesCache")
    Optional<Hero> findByName(String name);

    @Cacheable("heroesCache")
    Page<Hero> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Hero h SET " +
            "h.alias = COALESCE(:alias, h.alias), " +
            "h.name = COALESCE(:name, h.name), " +
            "h.origin = COALESCE(:origin, h.origin) " +
            "WHERE h.id = :id")
    void updateHero(long id, String alias, String name, String origin);
}
