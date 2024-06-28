package com.example.Monetization.System.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad")
public class Ad extends Timestamped{
    @Id
    private UUID ad_id;

    private String ad_name;

    private String ad_description;

    private Long ad_price;

    private boolean delete_check;
}
