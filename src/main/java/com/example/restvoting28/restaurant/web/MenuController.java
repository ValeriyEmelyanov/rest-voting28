package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.common.exception.NotFoundException;
import com.example.restvoting28.common.validation.View;
import com.example.restvoting28.restaurant.MenuRepository;
import com.example.restvoting28.restaurant.dto.MenuResponse;
import com.example.restvoting28.restaurant.model.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.restvoting28.common.validation.ValidationUtil.assureIdConsistent;
import static com.example.restvoting28.common.validation.ValidationUtil.assureOwnerIdConsistent;

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

    @GetMapping(READ_PATH + "/date/{date}")
    public List<MenuResponse> getAllByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Get all menu by date={}", date);
        return repository.findAllByDated(date)
                .stream()
                .map(m -> conversionService.convert(m, MenuResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping(READ_PATH + "/restaurant/{restaurantId}/date/{date}")
    public MenuResponse getByRestaurantAndDate(
            @PathVariable long restaurantId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Menu menu = repository.findByRestaurantIdAndDated(restaurantId, date)
                .orElseThrow(() -> new NotFoundException("No menu by restaurantId=" + restaurantId + " and date=" + date));
        return conversionService.convert(menu, MenuResponse.class);
    }

    @GetMapping(READ_PATH + "/{id}")
    public MenuResponse get(@PathVariable long id) {
        return conversionService.convert(repository.getExisted(id), MenuResponse.class);
    }

    @PostMapping(path = WRITE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuResponse> createWithLocation(@Validated(View.OnCreate.class) @RequestBody Menu menu) {
        log.info("Create the menu {}", menu);
        Menu created = repository.save(menu);
        Menu dbCreated = repository.getExisted(created.id());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + READ_PATH + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(conversionService.convert(dbCreated, MenuResponse.class));
    }

    @PutMapping(path = WRITE_PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "menu", key = "{#menu.restaurantId, #menu.dated}")
    public MenuResponse update(@Validated @RequestBody Menu menu, @RequestParam long id) {
        log.info("Update the menu {} with id={}", menu, id);
        assureIdConsistent(menu, id);
        Menu dbMenu = repository.getExisted(id);
        assureOwnerIdConsistent(menu, dbMenu.ownerId());
        repository.save(menu);
        Menu dbUpdated = repository.getExisted(id);
        return conversionService.convert(dbUpdated, MenuResponse.class);
    }
}
