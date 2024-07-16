package com.tradeFx.tradeFx.userSettings;

import com.tradeFx.tradeFx.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserSettingsService  {

    @Autowired
    private UserSettingsRepository userSettingsRepository;


    public Optional<UserSettings> getUserSettingsByUser(User user) {
        return userSettingsRepository.findByUser(user);
    }

    public UserSettings saveUserSettings(UserSettings userSettings) {
        return userSettingsRepository.save(userSettings);
    }

    public Optional<UserSettings> getUserSettingsById(Long id) {
        return userSettingsRepository.findById(id);
    }
}

