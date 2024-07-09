package org.abondar.industrial.heromanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.abondar.industrial.heromanager.model.request.HeroCreateRequest;
import org.abondar.industrial.heromanager.model.request.HeroUpdateRequest;
import org.abondar.industrial.heromanager.model.response.HeroCreateResponse;
import org.abondar.industrial.heromanager.model.response.HeroResponse;
import org.abondar.industrial.heromanager.service.HeroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointUtil.API_V1_ROOT)
@RequiredArgsConstructor
@Tag(name = "", description = "Operations related to hero management")
public class HeroController {

    private final HeroService heroService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hero created"),
            @ApiResponse(responseCode = "400", description = "Missing hero data"),
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HeroCreateResponse> createHero(@Valid @RequestBody HeroCreateRequest request) {
        var resp = heroService.createHero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(description = "Fully updates a hero")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero updated")
    })
    @PutMapping(
            path = EndpointUtil.ID_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HeroResponse> updateHeroFull(@PathVariable @Min(0) long id, @Valid @RequestBody HeroCreateRequest request) {
        var updRequest = new HeroUpdateRequest(request.alias(), request.name(), request.powers(),
                request.weapons(), request.origin(), request.associations());
        var resp = heroService.updateHero(id, updRequest);
        return ResponseEntity.ok(resp);
    }


    @Operation(description = "Updates hero name,alias, origin and extends hero properties like powers,weapons,associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero updated")
    })
    @PatchMapping(
            path = EndpointUtil.ID_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HeroResponse> updateHero(@PathVariable @Min(0) long id, @Valid @RequestBody HeroUpdateRequest request) {
        var resp = heroService.updateHero(id, request);
        return ResponseEntity.ok(resp);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero found"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @GetMapping(
            path = EndpointUtil.ALIAS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HeroResponse> getHeroByAlias(@PathVariable String alias) {
        var resp = heroService.getHeroByAlias(alias);
        return ResponseEntity.ok(resp);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero found"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @GetMapping(
            path = EndpointUtil.NAME_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HeroResponse> getHeroByName(@PathVariable String name) {
        var resp = heroService.getHeroByName(name);
        return ResponseEntity.ok(resp);
    }

    @Operation(description = "Fetch multiple heroes, to get all of them use limit as -1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heroes found"),
            @ApiResponse(responseCode = "404", description = "Heroes not found"),
    })
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroResponse>> getAllHeroes(@RequestParam(value = "offset") @Min(0) int offset,
                                                           @RequestParam(value = "limit") @Min(1) @Max(10) int limit) {
        var resp = heroService.getAllHeroes(offset, limit);

        return ResponseEntity.ok(resp);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero found"),
            @ApiResponse(responseCode = "400", description = "Wrong Property type"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @GetMapping(
            path = EndpointUtil.HERO_PROP_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroResponse>> getHeroByProperty(@PathVariable String value,
                                                                @PathVariable String type,
                                                                @RequestParam(value = "offset") @Min(0) int offset,
                                                                @RequestParam(value = "limit") @Min(1) @Max(10) int limit) {
        var resp = heroService.getHeroByProperty(value, type, offset, limit);
        return ResponseEntity.ok(resp);
    }

    @Operation(description = "Delete hero All corresponding hero properties will be deleted accrodingly")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero deleted"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @DeleteMapping(
            path = EndpointUtil.ID_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity deleteHero(@PathVariable @Min(0) long id) {
        heroService.deleteHero(id);

        return ResponseEntity.ok().build();
    }


    @Operation(description = "Delete property - only required property will be deleted, not a hero himself.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero or property deleted"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @DeleteMapping(
            path = EndpointUtil.DELETE_PROP_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity deleteHeroOrProperty(@PathVariable @Min(0) long id, @PathVariable String prop) {
        heroService.deleteHeroProperty(id, prop);

        return ResponseEntity.ok().build();
    }


}
