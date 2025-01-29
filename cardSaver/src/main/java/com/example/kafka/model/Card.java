package com.example.kafka.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cardNumber;
    private boolean encrypted;
    private Long userId;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
        this.encrypted = false;
    }

    public void encrypt() {
        if (!encrypted) {
            this.cardNumber = Base64.getEncoder().encodeToString(this.cardNumber.getBytes());
            this.encrypted = true;
        }
    }

    public void decrypt() {
        if (encrypted) {
            this.cardNumber = new String(Base64.getDecoder().decode(this.cardNumber));
            this.encrypted = false;
        }
    }
}