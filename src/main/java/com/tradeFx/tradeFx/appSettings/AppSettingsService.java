package com.tradeFx.tradeFx.appSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppSettingsService {
    @Autowired
    private AppSettingsRepository appSettingsRepository;

    public Optional<AppSettings> getSettings() {
        return appSettingsRepository.findFirstByIdNotNull();
    }

    public AppSettings saveSettings(AppSettings settings) {
        Optional<AppSettings> existingSettings = appSettingsRepository.findFirstByIdNotNull();
        if (existingSettings.isPresent()) {
            // Update existing settings
            AppSettings existing = existingSettings.get();
            existing.setDefaultLanguage(settings.getDefaultLanguage());
            existing.setDefaultBaseCurrency(settings.getDefaultBaseCurrency());
            existing.setUpdatedAt(LocalDateTime.now());
            return appSettingsRepository.save(existing);
        } else {
            // Create new settings (assuming there are default values)
            settings.setCreatedAt(LocalDateTime.now());
            settings.setUpdatedAt(settings.getCreatedAt());
            return appSettingsRepository.save(settings);
        }
    }
}
