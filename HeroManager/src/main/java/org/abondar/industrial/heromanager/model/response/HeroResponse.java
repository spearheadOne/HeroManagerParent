package org.abondar.industrial.heromanager.model.response;


import java.util.List;

public record HeroResponse(
        long id,

        String alias,

        String name,

        List<String> powers,

        List<String> weapons,

        String origin,

        List<String> associations
) {
}
