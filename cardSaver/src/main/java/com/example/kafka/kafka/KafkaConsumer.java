package com.example.kafka.kafka;

import com.example.kafka.model.Card;
import com.example.kafka.model.User;
import com.example.kafka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaConsumer {

    @Autowired
    private UserService userService;

    public KafkaConsumer(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "cards", groupId = "card_group")
    public void consumeCard(Card card) {
        if (card == null) {
            return;
        }

        Optional<User> optionalUser = userService.getUserById(card.getUserId());

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();
        user.addCard(card);
        userService.updateUser(user);
    }
}