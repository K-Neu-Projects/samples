package com.kneu.samples.spring.azure.appconfiguration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kneu.samples.spring.azure.appconfiguration.config.MessageProperties;

@RestController
public class HelloController {

    private final MessageProperties properties;

    public HelloController(MessageProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public String getMessage() {
        return "Message: " + properties.getMessage();
    }
}