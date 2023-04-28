package com.example.restvoting28.web;

import com.example.restvoting28.dto.PasswordRequest;
import com.example.restvoting28.exception.IllegalRequestDataException;
import com.example.restvoting28.model.Role;
import com.example.restvoting28.model.User;
import com.example.restvoting28.repository.UserRepository;
import com.example.restvoting28.security.AuthUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

import static com.example.restvoting28.config.WebSecurityConfig.PASSWORD_ENCODER;

@RestController
@RequestMapping(ProfileController.URL)
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
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
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
    public User update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        user.setRoles(authUser.getUser().getRoles());
        return super.update(user, authUser.id());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping("/change_password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@NotNull @RequestBody PasswordRequest request, @AuthenticationPrincipal AuthUser authUser) {
        if (!PASSWORD_ENCODER.matches(request.getOldPassword(), authUser.getUser().getPassword())) {
            throw new IllegalRequestDataException("Wrong old password");
        }
        super.changePassword(request.getNewPassword(), authUser.id());
    }
}
