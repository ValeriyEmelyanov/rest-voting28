package com.example.restvoting28.web;

import com.example.restvoting28.model.User;
import com.example.restvoting28.security.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}
