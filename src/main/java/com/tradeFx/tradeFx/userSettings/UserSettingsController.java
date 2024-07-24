package com.tradeFx.tradeFx.userSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("user-settings")
@CrossOrigin
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping("/{id}")
    public ResponseEntity<UserSettings> getUserSettingsById(@PathVariable Long id) {
        Optional<UserSettings> userSettings = userSettingsService.getUserSettingsById(id);
        return userSettings.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSettings> updateUserSettings(@PathVariable Long id, @RequestBody UserSettings userSettings) {
        Optional<UserSettings> existingUserSettings = userSettingsService.getUserSettingsById(id);
        if (existingUserSettings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userSettings.setId(id);
        UserSettings savedUserSettings = userSettingsService.saveUserSettings(userSettings);
        return ResponseEntity.ok(savedUserSettings);
    }
}


