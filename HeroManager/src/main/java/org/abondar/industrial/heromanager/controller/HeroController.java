package org.abondar.industrial.heromanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(description = "Updates hero name,alias, origin and extends hero properties like powers,weapons,associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero updated")
    })
    @PutMapping(
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
    public ResponseEntity<List<HeroResponse>> getAllHeroes(@RequestParam(value = "offset") @Min(0) int offset, @RequestParam(value = "limit") @Min(1) int limit) {
        var resp = heroService.getAllHeroes(offset, limit);

        //TODO: throw exception
        //TODO: limit limit(set 10 as max)
        if (resp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resp);
    }

    //TODO polish with validaitotion and test a flow
    //TODO add pagination
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hero found"),
            @ApiResponse(responseCode = "404", description = "Hero not found"),
    })
    @GetMapping(
            path = EndpointUtil.HERO_PROP_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroResponse>> getHeroByProperty(@PathVariable String type,@PathVariable String value) {
        var resp = heroService.getHeroByProperty(type, value);
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
