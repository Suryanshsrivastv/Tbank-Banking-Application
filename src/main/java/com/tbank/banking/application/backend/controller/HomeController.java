package com.tbank.banking.application.backend.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tbank")
public class HomeController {

    @RequestMapping("/home")
    @RateLimiter(name = "default", fallbackMethod = "rateLimitFallback")
    public String welcome() {
        return "Welcome to TBank";
    }

    public String rateLimitFallback(Throwable t) {
        return "Too many requests. Please try again later.";
    }
}
