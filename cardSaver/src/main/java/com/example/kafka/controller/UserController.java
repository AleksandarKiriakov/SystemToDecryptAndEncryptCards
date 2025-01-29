package com.example.kafka.controller;

import com.example.kafka.kafka.KafkaProducer;
import com.example.kafka.model.Card;
import com.example.kafka.model.User;
import com.example.kafka.service.CardService;
import com.example.kafka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    private CardService cardService;

    @Autowired
    private KafkaProducer producer;

    @Autowired
    public UserController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.getCards().forEach(Card::encrypt);
        User savedUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getUsers() {
        Iterable<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/encrypt")
    public ResponseEntity<String> encryptUserCards(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        user.getCards().forEach(Card::encrypt);
        userService.updateUser(user);

        return ResponseEntity.ok("Cards encrypted successfully!");
    }

    @PostMapping("/{userId}/decrypt")
    public ResponseEntity<String> decryptUserCards(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        user.getCards().forEach(Card::decrypt);
        userService.updateUser(user);

        return ResponseEntity.ok("Cards decrypted successfully!");
    }

    @GetMapping("/{userId}/{cardId}")
    public ResponseEntity<Card> getCardById(@PathVariable Long userId, @PathVariable Long cardId) {
        Card card = cardService.getCardById(userId, cardId);
        if (card == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> addCard(@PathVariable Long userId, @RequestBody Card card) {
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        card.setUserId(user.getId());
        cardService.saveCard(card);
        producer.sendCard(card);

        return ResponseEntity.status(HttpStatus.CREATED).body("Card added successfully!");
    }
}