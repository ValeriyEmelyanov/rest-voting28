package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.restaurant.RestaurantRepository;
import com.example.restvoting28.restaurant.model.Restaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(RestaurantController.URL)
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {
    public static final String URL = "/api";
    public static final String READ_PATH = "/restaurants";
    public static final String WRITE_PATH = "/admin/restaurants";

    private final RestaurantRepository repository;

    @GetMapping(READ_PATH)
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping(READ_PATH + "/{id}")
    public Restaurant get(@PathVariable long id) {
        log.info("Get the restaurant by id={}", id);
        return repository.getExisted(id);
    }

    @PostMapping(path = WRITE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("Create the restaurant {}", restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + WRITE_PATH + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = WRITE_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Restaurant update(@Valid @RequestBody Restaurant restaurant, @PathVariable long id) {
        log.info("Update the restaurant {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        return repository.save(restaurant);
    }
}
