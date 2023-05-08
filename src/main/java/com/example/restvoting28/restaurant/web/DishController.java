package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.login.model.User;
import com.example.restvoting28.security.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DishController.URL)
public class DishController {
    public static final String URL = "/api/admin/dishes";

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}
