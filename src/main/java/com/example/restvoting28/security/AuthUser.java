package com.example.restvoting28.security;

import com.example.restvoting28.model.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@ToString(of = "user")
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public long id() {
        return user.id();
    }

    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser authUser) ? authUser : null;
    }
}