package com.tradeFx.tradeFx.transaction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tradeFx.tradeFx.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String amount;
    private TransactionStatus status;
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String transactionId;
    private String transactionRef;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void onInsert(){
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null){
            status = TransactionStatus.PROCESSING;
        }
    }

    @PreUpdate
    private void onInsertUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
