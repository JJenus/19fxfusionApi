package com.tradeFx.tradeFx.auth;

import com.tradeFx.tradeFx.user.User;
import com.tradeFx.tradeFx.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody User req){
        User user = authService.login(req);
        AuthToken authToken = AuthToken.builder().user(user).userId(user.getId()).token("").build();
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/find")
    public ResponseEntity<User> find(@RequestBody User user){
        User res = userService.findByEmail(user.getEmail()).orElseThrow(()->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found")
        );
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthToken> registerUser(@RequestBody User reg) throws Exception {
        User user = authService.createNewAccount(reg);
        AuthToken authToken = AuthToken.builder().user(user).userId(user.getId()).token("").build();

        return ResponseEntity.ok(authToken);
    }


    @PostMapping("/request-password-reset")
    public void requestPassReset(@RequestBody PasswordReset passwordReset) throws IllegalAccessException {
        authService.sendPasswordResetEmail(passwordReset);
    }

    @PostMapping("/change-password")
    public String resetPassword(@RequestBody PasswordReset passwordReset) {
        String message = "success";
        authService.resetPassword(passwordReset);

        return message;
    }

}
