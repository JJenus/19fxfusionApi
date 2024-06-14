package com.tradeFx.tradeFx.auth;

import com.tradeFx.tradeFx.user.User;
import com.tradeFx.tradeFx.user.UserService;
import com.tradeFx.tradeFx.util.CryptoGen;
import com.tradeFx.tradeFx.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(User user) {
        User nfTexUser = userService.findByEmail(user.getEmail()).orElseThrow(
                ()-> new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User does not exist")
        );

        if (!passwordEncoder.matches(user.getPassword(), nfTexUser.getPassword())){
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return nfTexUser;
    }

    public User createNewAccount(User user) throws Exception {
        if (user.getEmail() == null || userService.findByEmail(user.getEmail()).isPresent()){
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        CryptoGen cryptoGen = Util.generateAddress();
//        user.setEthAddress(cryptoGen.getAddress());
//        user.setSecret(Util.encrypt(cryptoGen.getKey()));
        user.setImgUrl(getAvatar(Util.encrypt(user.getEmail())));
        user.setBalance("0.0");

        return userService.createAccount(user);
    }

    public String getAvatar(String input) {
        // Construct the URL with the input string
        String ROBOHASH_API_URL = "https://robohash.org/";

        return ROBOHASH_API_URL + input + ".png";
    }

    public void sendPasswordResetEmail(PasswordReset passwordReset) {
    }

    public void resetPassword(PasswordReset passwordReset) {
    }
}
