package com.kuber.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/kuber/v1")
public class SearchRestController {

    @Resource
    Environment environment;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchResults() {
        return ResponseEntity.ok().body("Welcome to Kuber Securities");
    }
}
