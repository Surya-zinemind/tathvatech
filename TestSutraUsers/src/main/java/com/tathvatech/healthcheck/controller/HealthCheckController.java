package com.tathvatech.healthcheck.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/health-check")
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<String> healthcheck()throws Exception
    {
        return ResponseEntity.ok("{\"message\": \"server running successfully\"}");
    }
}
