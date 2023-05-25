package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.restaurant.MenuItemRepository;
import com.example.restvoting28.restaurant.dto.MenuRequest;
import com.example.restvoting28.restaurant.dto.MenuResponse;
import com.example.restvoting28.restaurant.model.MenuItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(MenuController.URL)
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    public static final String URL = "/api";
    public static final String READ_PATH = "/menu";
    public static final String WRITE_PATH = "/admin/menu";
    public static final String ITEMS_PATH = "/items";

    private final MenuItemRepository repository;
    private final ConversionService conversionService;

    @GetMapping(READ_PATH)
    @Cacheable(value = "menu-day", key = "#date")
    public List<MenuResponse> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get all menu on date={}", date);
        return repository.getAllByDate(date).stream()
                .collect(Collectors.groupingBy(MenuItem::getRestaurantId))
                .values().stream()
                .map(items -> conversionService.convert(items, MenuResponse.class))
                .filter(Objects::nonNull)
                .toList();
    }

    @GetMapping(READ_PATH + "/by-restaurant")
    @Cacheable(value = "menu", key = "{#restaurantId, #date}")
    public MenuResponse getByRestaurantIdAndDate(
            @RequestParam long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get menu by restaurantId={} on date={}", restaurantId, date);
        List<MenuItem> menuItems = repository.getAllByRestaurantIdAndDate(restaurantId, date);
        if (menuItems.isEmpty()) {
            throw new NotFoundException("Menu by restaurantId=" + restaurantId + " on date=" + date + " not found");
        }
        return conversionService.convert(menuItems, MenuResponse.class);
    }

    @GetMapping(READ_PATH + "/existing-by-restaurant")
    public boolean existsByRestaurantIdAndDate(
            @RequestParam long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get menu existing by restaurantId={} on date={}", restaurantId, date);
        return repository.existsByRestaurantIdAndDated(restaurantId, date);
    }

    @GetMapping(READ_PATH + ITEMS_PATH + "/{id}")
    public MenuItem getItem(@PathVariable long id) {
        log.info("Get menu item by id={}", id);
        return repository.getExisted(id);
    }

    @PostMapping(path = WRITE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Caching(
            put = {
                    @CachePut(value = "menu", key = "{#menu.restaurantId, #menu.date}")
            },
            evict = {
                    @CacheEvict(value = "menu-day", key = "#menu.date"),
            })
    public ResponseEntity<MenuResponse> createWithLocation(@Valid @RequestBody MenuRequest menu) {
        log.info("Create menu {}", menu);
        Long restaurantId = menu.getRestaurantId();
        LocalDate date = menu.getDate();
        List<MenuItem> items = menu.getItems().stream()
                .map(i -> MenuItem.builder()
                        .restaurantId(restaurantId)
                        .dated(date)
                        .dishId(i.getDishId())
                        .price(i.getPrice())
                        .build())
                .toList();
        List<MenuItem> created = repository.saveAll(items);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + READ_PATH + "/by-restaurant?restaurantId={restaurantId}&date={date}")
                .buildAndExpand(Map.of("restaurantId", restaurantId, "date", date))
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(conversionService.convert(created, MenuResponse.class));
    }

    @PostMapping(path = WRITE_PATH + ITEMS_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Caching(evict = {
            @CacheEvict(value = "menu-day", key = "#menuItem.dated"),
            @CacheEvict(value = "menu", key = "{#menuItem.restaurantId, #menuItem.dated}")
    })
    public ResponseEntity<MenuItem> createItemWithLocation(@Validated @RequestBody MenuItem menuItem) {
        log.info("Create menu item {}", menuItem);
        MenuItem created = repository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + WRITE_PATH + "/items/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = WRITE_PATH + ITEMS_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = {"menu", "menu-day"}, allEntries = true)
    public MenuItem updateItem(@Validated @RequestBody MenuItem menuItem, @RequestParam long id) {
        log.info("Update menu item {} with id={}", menuItem, id);
        assureIdConsistent(menuItem, id);
        repository.getBelonged(id, menuItem.getRestaurantId())
                .orElseThrow(() -> new IllegalRequestDataException("Menu item id=" + id + " doesn't exist or doesn't belong to Restaurant id=" + menuItem.getRestaurantId()));
        return repository.save(menuItem);
    }

    @DeleteMapping(WRITE_PATH + ITEMS_PATH + "/by-restaurant")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(evict = {
            @CacheEvict(value = "menu-day", key = "#menuItem.dated"),
            @CacheEvict(value = "menu", key = "{#menuItem.restaurantId, #menuItem.dated}")
    })
    public void deleteAllItemsByRestaurantIdAndDate(
            @RequestParam long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Delete menu items by restaurantId={} on date={}", restaurantId, date);
        repository.deleteAllByRestaurantIdAndDateExisted(restaurantId, date);
    }

    @DeleteMapping(WRITE_PATH + ITEMS_PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"menu", "menu-day"}, allEntries = true)
    public void deleteItem(@PathVariable long id) {
        log.info("Delete menu item with id={}", id);
        repository.deleteExisted(id);
    }
}
