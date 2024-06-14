package com.tradeFx.tradeFx.auth;

import com.tradeFx.tradeFx.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthToken {
    private Long userId;
    private User user;
    private String token;
}
