package com.tradeFx.tradeFx.appSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class AppSettingsController {

    @Autowired
    private AppSettingsService appSettingsService;

    @GetMapping
    public ResponseEntity<AppSettings> getSettings() {
        Optional<AppSettings> settings = appSettingsService.getSettings();
        return settings.map(ResponseEntity::ok)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AppSettings> createOrUpdateSettings(@RequestBody AppSettings settings) {
        AppSettings savedSettings = appSettingsService.saveSettings(settings);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSettings);
    }
}

