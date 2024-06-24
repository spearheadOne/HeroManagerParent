package org.abondar.industrial.heromanager.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;


import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HeroUpdateRequest(
        @JsonProperty String alias,

        @JsonProperty String name,

        @JsonProperty  @Size(min = 1, max = 10) List<String> powers,

        @JsonProperty  @Size(min = 1, max = 10) List<String> weapons,

        @JsonProperty String origin,

        @JsonProperty  @Size(min = 1, max = 10) List<String> associations
) {
}
