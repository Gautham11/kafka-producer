package com.example.kafkaproducer.controller;

import com.example.kafkaproducer.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@RestController
public class KafkaProducerController {

    private final KafkaProducerService kafkaProducerService;


    @Value("${kafka.topic}")
    private String topic;

    public KafkaProducerController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/publish")
    public String publish(@RequestBody String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, ExecutionException, InterruptedException {
        kafkaProducerService.sendMessage(topic, message);
        return "Message sent to Kafka topic: " + topic;
    }
}
