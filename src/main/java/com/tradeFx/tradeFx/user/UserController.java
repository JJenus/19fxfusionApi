package com.tradeFx.tradeFx.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
    public void update(@PathVariable("id") long id, @RequestBody User user){
        userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteAccount(id);
    }

}
