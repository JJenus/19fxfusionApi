package com.tradeFx.tradeFx.user;

import com.tradeFx.tradeFx.userSettings.UserSettings;
import com.tradeFx.tradeFx.userSettings.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping
    public List<User> usersList(){
        return userService.allUsers();
    }

    @GetMapping("/{id}")
    public User user(@PathVariable("id") long id){
        return userService.user(id);
    }

    @PostMapping
    public User create(@RequestBody User user){
        return userService.createAccount(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable("id") long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteAccount(id);
    }

    @GetMapping("/{userId}/settings")
    public ResponseEntity<UserSettings> getUserSettingsByUser(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<UserSettings> userSettings = userSettingsService.getUserSettingsByUser(user.get());
        return userSettings.map(ResponseEntity::ok)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Settings not found"));
    }

}
