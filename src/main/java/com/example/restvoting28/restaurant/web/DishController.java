package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.common.validation.View;
import com.example.restvoting28.restaurant.DishRepository;
import com.example.restvoting28.restaurant.model.Dish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;
import static com.example.restvoting28.common.validation.ValidationUtil.assureOwnerIdConsistent;

@RestController
@RequestMapping(DishController.URL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class DishController {
    public static final String URL = "/api/admin/dishes";

    private final DishRepository repository;

    @GetMapping("/by-restaurant")
    public List<Dish> getAllByRestaurant(@RequestParam long restaurantId) {
        log.info("get all dishes by restaurantId={}", restaurantId);
        return repository.findAllByRestaurantIdOrderByName(restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable long id) {
        log.info("Get the dish by id={}", id);
        return repository.getExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Validated(View.OnCreate.class) @RequestBody Dish dish) {
        log.info("Create the dish {}", dish);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish update(@Validated(View.OnUpdate.class) @RequestBody Dish dish, @PathVariable long id) {
        log.info("Update the dish {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        assureOwnerIdConsistent(dish, repository.getExisted(id).getOwnerId());
        return repository.save(dish);
    }
}
