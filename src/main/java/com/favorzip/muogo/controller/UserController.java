package com.favorzip.muogo.controller;

import com.favorzip.muogo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/token")
    public void testToken(@RequestParam String accessToken, @RequestParam String refreshToken) {

        System.out.println("[ Token Test api call!! ]");

        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

    }
}
