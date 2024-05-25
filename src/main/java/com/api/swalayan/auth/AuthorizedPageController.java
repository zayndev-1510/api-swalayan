package com.api.swalayan.auth;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("auth-page") // Ubah mapping endpoint
public class AuthorizedPageController {

    @GetMapping
    public String home() {
        return "authorized";
    }
}