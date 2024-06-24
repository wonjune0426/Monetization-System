package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {

    @CreatedDate
    @Column(updatable = false)
    private String create_at;

    @LastModifiedDate
    @Column
    private String update_at;

    @PrePersist  // 저장하기 전에 실행
    public void onPrePersist() {
        this.create_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.update_at = this.create_at;
    }

    @PreUpdate // 수정하기 전에 실행
    public void onPreUpdate() {
        this.update_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }


}
