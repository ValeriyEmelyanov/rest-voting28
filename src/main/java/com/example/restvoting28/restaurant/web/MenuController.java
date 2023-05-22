package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.common.validation.View;
import com.example.restvoting28.restaurant.MenuRepository;
import com.example.restvoting28.restaurant.dto.MenuResponse;
import com.example.restvoting28.restaurant.model.Menu;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(MenuController.URL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class MenuController {
    public static final String URL = "/api";
    public static final String READ_PATH = "/menu";
    public static final String WRITE_PATH = "/admin/menu";

    private final MenuRepository repository;
    private final ConversionService conversionService;

    @GetMapping(value = READ_PATH)
    @JsonView(View.Profile.class)
    public List<Menu> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get all menu by date={}", date);
        return repository.getAllByDate(date);
    }

    @GetMapping(value = READ_PATH + "/by-restaurant")
    @JsonView(View.Profile.class)
    public Menu getByRestaurantAndDate(
            @RequestParam long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repository.getByRestaurantIdAndDate(restaurantId, date)
                .orElseThrow(() -> new NotFoundException("No menu by restaurantId=" + restaurantId + " and date=" + date));
    }

    @GetMapping(READ_PATH + "/{id}")
    @JsonView(View.Profile.class)
    public Menu get(@PathVariable long id) {
        return repository.getExisted(id);
    }

    @GetMapping(READ_PATH + "/{id}/items")
    public MenuResponse getWithItems(@PathVariable long id) {
        Menu menu = repository.getWithItems(id).orElseThrow(() -> new NotFoundException("Menu with id=" + id + " not found"));
        return conversionService.convert(menu, MenuResponse.class);
    }

    @PostMapping(path = WRITE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = {"menu", "menu-existing", "menu-with-items"}, allEntries = true)
    @JsonView(View.Admin.class)
    public ResponseEntity<Menu> createWithLocation(@Validated(View.OnCreate.class) @RequestBody Menu menu) {
        log.info("Create the menu {}", menu);
        Menu created = repository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + READ_PATH + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = WRITE_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = {"menu", "menu-existing"}, allEntries = true)
            },
            put = {
                    @CachePut(value = "menu-with-items", key = "#id")
            }
    )
    @JsonView(View.Admin.class)
    public Menu update(@Validated @RequestBody Menu menu, @RequestParam long id) {
        log.info("Update the menu {} with id={}", menu, id);
        assureIdConsistent(menu, id);
        repository.getBelonged(id, menu.getRestaurantId())
                .orElseThrow(() -> new IllegalRequestDataException("Menu id=" + id + " doesn't exist or doesn't belong to Restaurant id=" + menu.getRestaurantId()));
        return repository.save(menu);
    }

    @DeleteMapping(WRITE_PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(
            evict = {
                    @CacheEvict(value = {"menu", "menu-existing"}, allEntries = true),
                    @CacheEvict(value = "menu-with-items", key = "#id")
            }
    )
    public void delete(@PathVariable long id) {
        repository.deleteExisted(id);
    }
}
