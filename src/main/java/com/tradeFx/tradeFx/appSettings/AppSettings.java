package com.tradeFx.tradeFx.appSettings;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String defaultLanguage;
    private String defaultBaseCurrency;
    private LocalDateTime lastLiveFxDate;
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
    }
}
