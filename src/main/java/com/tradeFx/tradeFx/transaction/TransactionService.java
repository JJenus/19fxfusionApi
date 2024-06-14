package com.tradeFx.tradeFx.transaction;

import com.tradeFx.tradeFx.user.User;
import com.tradeFx.tradeFx.user.UserService;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        User user = userService.user(transaction.getUser().getId());

        Money transactionAmount = Money.of(Double.parseDouble(transaction.getAmount()), "USD");
        Money userBalance = Money.of(Double.parseDouble(user.getBalance()), "USD");

        if (transaction.getTransactionType() == TransactionType.DEPOSIT){
            user.setBalance(
                    userBalance.add(transactionAmount).toString()
            );
        } else if (transaction.getTransactionType() == TransactionType.WITHDRAW){
            if (userBalance.isLessThan(transactionAmount)){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }
            user.setBalance(
                    userBalance.subtract(transactionAmount).toString()
            );
        }

        System.out.println("BALANCE: "+user.getBalance());

        userService.updateUser(user.getId(), user);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Transaction not found with id " + id));
    }

    public Transaction updateTransaction(Long id, TransactionStatus status) {
        Transaction existingTrans= transactionRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Transaction not found with id " + id));

        existingTrans.setStatus(status);
        return transactionRepository.save(existingTrans);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Transaction not found with id " + id);
        }
        transactionRepository.deleteById(id);
    }
}

