package com.vaibhav.urlshortener.controller;

import com.vaibhav.urlshortener.entity.Url;
import com.vaibhav.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/test")
    public String test() {
        return "API Working";
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> createShortUrl(
            @RequestBody String originalUrl,
            HttpServletRequest request) {

        urlService.checkRateLimit(request);

        Url savedUrl = urlService.createShortUrl(originalUrl);

        return new ResponseEntity<>(savedUrl, HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        Url url = urlService.getAndIncrement(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }
}