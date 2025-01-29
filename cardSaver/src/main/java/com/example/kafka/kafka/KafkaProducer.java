package com.example.kafka.kafka;

import com.example.kafka.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final String TOPIC = "cards";

    @Autowired
    private KafkaTemplate<String, Card> kafkaTemplate;

    public void sendCard(Card card) {
        kafkaTemplate.send(TOPIC, card);
    }
}