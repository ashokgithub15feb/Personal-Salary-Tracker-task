package com.walkingtree.salary.tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salaries")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Successfully access the TestController class";
    }
}
