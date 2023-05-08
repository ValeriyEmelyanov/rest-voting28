package com.example.restvoting28.login.web;

import com.example.restvoting28.login.dto.PasswordRequest;
import com.example.restvoting28.login.model.User;
import com.example.restvoting28.login.UserRepository;
import com.example.restvoting28.common.validation.View;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AdminUserController.URL)
@Validated
public class AdminUserController extends AbstractUserController {
    public static final String URL = "/api/admin/users";

    public AdminUserController(UserRepository userRepository) {
        super(userRepository);
    }

    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return super.get(id);
    }

    @GetMapping("/by-email")
    public User getByEmail(@RequestParam String email) {
        return super.getByEmail(email);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Validated(View.OnCreate.class) @RequestBody User user) {
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(View.OnUpdate.class) @RequestBody User user, @PathVariable long id) {
        return super.update(user, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        super.delete(id);
    }

    @PostMapping("/{id}/change_password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@NotNull @Validated(View.Admin.class) @RequestBody PasswordRequest request, @PathVariable long id) {
        super.changePassword(request.getNewPassword(), id);
    }
}
