package com.tradeFx.tradeFx.user;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public User createAccount(User user) {
        if (user.getEmail() == null || findByEmail(user.getEmail()).isPresent()){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        return userRepository.save(user);
    }

    public void deleteAccount(long id) {
        userRepository.deleteById(id);
    }

    public User user(long id) {
        return userRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public static String generateTransactionID() {
        // Generate a random UUID (Universally Unique Identifier)
        UUID uuid = UUID.randomUUID();
        // Convert UUID to string and remove hyphens
        return uuid.toString().replaceAll("-", "");
    }

    public User updateUser(long id, User user){
        userRepository.findById(id).orElseThrow(
                ()-> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found")
        );
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void updateBalance(Long userId, BalanceDTO balance) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found")
        );

        Money transactionAmount = Money.of(Double.parseDouble(balance.getAmount()), "USD");
        Money userBalance = Money.of(Double.parseDouble(user.getBalance()), "USD");

        if (balance.getAction() == BalanceDTO.Action.ADD){
            user.setBalance(
                    transactionAmount.add(userBalance).toString()
            );
        }
        else if(balance.getAction() == BalanceDTO.Action.SUB){
            user.setBalance(
                    userBalance.subtract(transactionAmount).toString()
            );
        }

        userRepository.save(user);
    }
}
