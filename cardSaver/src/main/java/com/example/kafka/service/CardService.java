package com.example.kafka.service;

import com.example.kafka.memory.CardRepository;
import com.example.kafka.memory.UserRepository;
import com.example.kafka.model.Card;
import com.example.kafka.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public CardService(UserRepository userRepository, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public Card getCardById(Long userId, Long cardId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        return user.getCards()
                .stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
