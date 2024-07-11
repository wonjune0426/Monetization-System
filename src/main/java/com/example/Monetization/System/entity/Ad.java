package com.example.Monetization.System.entity;

import com.example.Monetization.System.entity.timestapm.MainTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ad extends MainTimestamped {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID adId;

    @Column(nullable = false, length = 100)
    private String adName;

    @Column(nullable = false)
    private String adDescription;

    @Column(nullable = false)
    private Boolean deleteCheck;

    public Ad(String adName, String adDescription) {
        this.adName = adName;
        this.adDescription = adDescription;
        this.deleteCheck = false;
    }

    public void delete() {
        this.deleteCheck = true;
    }
}
