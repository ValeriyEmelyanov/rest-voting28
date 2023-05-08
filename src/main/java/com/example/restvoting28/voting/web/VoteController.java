package com.example.restvoting28.voting.web;

import com.example.restvoting28.login.model.User;
import com.example.restvoting28.security.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VoteController.URL)
public class VoteController {
    public static final String URL = "/api/votes";

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}
