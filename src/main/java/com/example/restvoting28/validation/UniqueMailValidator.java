package com.example.restvoting28.validation;

import com.example.restvoting28.model.User;
import com.example.restvoting28.repository.UserRepository;
import com.example.restvoting28.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.swing.*;

@Component
@AllArgsConstructor
public class UniqueMailValidator implements Validator {
    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        if (StringUtils.hasText(user.getEmail())) {
            repository.findByEmailIgnoreCase(user.getEmail())
                    .ifPresent(dbUser -> {
                        long dbId = dbUser.id();
                        if (user.getId() != null) {
                            if (user.id() == dbId) return;
                        } else {
                            AuthUser authUser = AuthUser.safeGet();
                            // Doesn't work if admin assign own email to another user
                            if (authUser != null && authUser.id() == dbId) return;

                            String requestUri = request.getRequestURI();
                            if (requestUri.endsWith("/" + dbId)) return;
                        }

                        errors.rejectValue("email", "duplication",
                                "User with email " + user.getEmail() + " already exists");
                    });
        }
    }
}
