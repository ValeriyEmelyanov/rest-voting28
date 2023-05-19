package com.example.restvoting28.login.web;

import com.example.restvoting28.common.exception.IllegalRequestDataException;
import com.example.restvoting28.common.validation.View;
import com.example.restvoting28.login.UserRepository;
import com.example.restvoting28.login.dto.PasswordRequest;
import com.example.restvoting28.login.model.Role;
import com.example.restvoting28.login.model.User;
import com.example.restvoting28.security.AuthUser;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;

@RestController
@RequestMapping(ProfileController.URL)
@CacheConfig(cacheNames = "users")
@Validated
public class ProfileController extends AbstractUserController {
    public static final String URL = "/api/profile";

    public ProfileController(UserRepository userRepository) {
        super(userRepository);
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(
            @Validated(View.OnCreate.class) @RequestBody @JsonView(View.ProfileOnCreate.class) User user) {
        user.setRoles(Set.of(Role.USER));
        User created = super.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(URL)
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User update(
            @Validated(View.OnUpdate.class) @RequestBody @JsonView(View.ProfileOnUpdate.class) User user,
            @AuthenticationPrincipal AuthUser authUser) {
        user.setRoles(authUser.getUser().getRoles());
        return super.update(user, authUser.id());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(key = "#authUser.user.email")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(key = "#authUser.user.email")
    public void changePassword(
            @NotNull @Validated(View.Profile.class) @RequestBody PasswordRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        if (!PASSWORD_ENCODER.matches(request.getOldPassword(), authUser.getUser().getPassword())) {
            throw new IllegalRequestDataException("Wrong old password");
        }
        super.changePassword(request.getNewPassword(), authUser.id());
    }
}
