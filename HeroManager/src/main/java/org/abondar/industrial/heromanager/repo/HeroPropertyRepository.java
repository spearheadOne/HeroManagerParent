package org.abondar.industrial.heromanager.repo;

import org.abondar.industrial.heromanager.model.db.HeroProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroPropertyRepository extends JpaRepository<HeroProperty, Long> {

    @Cacheable("heroPropertiesCache")
    List<HeroProperty> findByHeroId(Long heroId);

    @Query("SELECT hp FROM HeroProperty hp WHERE hp.hero.id IN :heroIds")
    @Cacheable("heroPropertiesCache")
    List<HeroProperty> findByHeroIds(@Param("heroIds") List<Long> heroIds);

    @Cacheable("heroPropertiesCache")
    Optional<HeroProperty> findByHeroIdAndPropertyValue(Long heroId, String propertyValue);


    @Cacheable("heroPropertiesCache")
    List<Long> findHeroIdsByPropertyValueAndPropertyType(@Param("propertyValue") String propertyValue,
                                                         @Param("propertyType")String propertyType);

    void deleteByHeroIdAndPropertyValue(@Param("heroId") Long heroId, @Param("propertyValue") String propertyValue);
}
