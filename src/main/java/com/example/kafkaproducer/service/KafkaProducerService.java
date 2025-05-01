package com.example.kafkaproducer.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;


    private final String encryptionKey;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,  @Value("${encryption.key}") String encryptionKey) {
        this.kafkaTemplate = kafkaTemplate;
        this.encryptionKey = encryptionKey;
    }

    public void sendMessage(String topic, String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, ExecutionException, InterruptedException {
        List<Header> kafkaHeaders = new ArrayList<>();
        var uuid = UUID.randomUUID();
        var producerRecord = new ProducerRecord<>(topic, null, uuid.toString(), message, kafkaHeaders);
        kafkaTemplate.send(producerRecord).get();
    }
}
