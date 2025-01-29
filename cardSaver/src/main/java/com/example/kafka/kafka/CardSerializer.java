package com.example.kafka.kafka;

import com.example.kafka.model.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CardSerializer implements Serializer<Card> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Card card) {
        try {
            return objectMapper.writeValueAsBytes(card);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Card", e);
        }
    }

    @Override
    public void close() {
    }
}
