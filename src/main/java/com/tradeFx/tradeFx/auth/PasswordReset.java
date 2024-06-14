package com.tradeFx.tradeFx.auth;

import lombok.Data;

@Data
public class PasswordReset {
    private String email;
    private String token;
    private String password;
}
