package org.abondar.industrial.heromanager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record HeroCreateRequest(
        @NotBlank @JsonProperty(required = true) String alias,

        @NotBlank @JsonProperty(required = true) String name,

        @NotEmpty @JsonProperty(required = true) @Size(min = 1, max = 10) List<String> powers,

        @JsonProperty(required = true) @Size(max = 10) List<String> weapons,

        @NotBlank @JsonProperty(required = true) String origin,

        @NotEmpty @JsonProperty(required = true) @Size(min = 1, max = 10) List<String> associations
) {
}
