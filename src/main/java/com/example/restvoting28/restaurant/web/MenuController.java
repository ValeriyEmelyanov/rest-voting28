package com.example.restvoting28.restaurant.web;

import com.example.restvoting28.login.model.User;
import com.example.restvoting28.security.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MenuController.URL)
public class MenuController {
    public static final String URL = "/api";
    public static final String READ_PATH = "/menus";
    public static final String WRITE_PATH = "/admin/menus";

    @GetMapping(READ_PATH)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}
