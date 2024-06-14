package com.tradeFx.tradeFx.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tradeFx.tradeFx.trade.Trade;
import com.tradeFx.tradeFx.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String ethAddress;
    private boolean emailVerified;
    private boolean accountVerified;
    private String secret;
    private String password;
    private String balance;
    private String status;
    private String imgUrl;
    private String userRole;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", targetEntity = Trade.class, orphanRemoval = true)
    List<Trade> trades;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", targetEntity = Transaction.class, orphanRemoval = true)
    List<Transaction> transactions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void onInsert(){
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    private void onInsertUpdate(){
        updatedAt = LocalDateTime.now();
        if (trades == null){
            trades = new ArrayList<>();
        }
        if (transactions == null){
            transactions = new ArrayList<>();
        }
    }
}
