package com.example.kafka.kafka;

import com.example.kafka.model.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CardDeserializer implements Deserializer<Card> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Card deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Card.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Card", e);
        }
    }

    @Override
    public void close() {
    }
}
