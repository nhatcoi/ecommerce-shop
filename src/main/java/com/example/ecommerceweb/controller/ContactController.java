package com.example.ecommerceweb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    @GetMapping("")
    public String contact() {
        return "contact";
    }
}
