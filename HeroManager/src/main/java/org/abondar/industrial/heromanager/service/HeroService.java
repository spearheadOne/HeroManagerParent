package org.abondar.industrial.heromanager.service;

import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.abondar.industrial.heromanager.model.request.HeroUpdateRequest;
import org.abondar.industrial.heromanager.model.response.HeroCreateResponse;
import org.abondar.industrial.heromanager.model.response.HeroResponse;

import java.util.List;

public interface HeroService {

    HeroCreateResponse createHero(HeroCreateRequest request);

    HeroResponse updateHero(Long heroId, HeroUpdateRequest request);

    HeroResponse getHeroByAlias(String alias);

    HeroResponse getHeroByName(String name);

    List<HeroResponse> getAllHeroes(int offset, int limit);

    List<HeroResponse> getHeroByProperty(String propertyType, String propertyValue);

    void deleteHero(long heroId);

    void deleteHeroProperty(long heroId, String propertyValue);
}
