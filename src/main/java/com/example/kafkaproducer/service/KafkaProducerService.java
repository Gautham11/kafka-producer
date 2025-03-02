package com.example.kafkaproducer.service;

import encryption.aes.AESEncryptionAndDecryptionKeys;
import encryption.aes.AESEncryptionAndDecryptionService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
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

    private final AESEncryptionAndDecryptionService aesEncryptionAndDecryptionService;


    private final String encryptionKey;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, AESEncryptionAndDecryptionService aesEncryptionAndDecryptionService, @Value("${encryption.key}") String encryptionKey) {
        this.kafkaTemplate = kafkaTemplate;
        this.aesEncryptionAndDecryptionService = aesEncryptionAndDecryptionService;
        this.encryptionKey = encryptionKey;
    }

    public void sendMessage(String topic, String message) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, ExecutionException, InterruptedException {
        Pair<String, String> encryptedTextAndEncryptedKey = aesEncryptionAndDecryptionService.encrypt(message);
        List<Header> kafkaHeaders = new ArrayList<>();
        kafkaHeaders.add(new RecordHeader(AESEncryptionAndDecryptionKeys.ENCRYPTION_KEY_CIPHER_TEXT.getKey(), encryptedTextAndEncryptedKey.getValue1().getBytes()));
        kafkaHeaders.add(new RecordHeader(AESEncryptionAndDecryptionKeys.ENCRYPTION_KEY_NAME.getKey(), encryptionKey.getBytes()));
        var uuid = UUID.randomUUID();
        var producerRecord = new ProducerRecord<>(topic, null, uuid.toString(),encryptedTextAndEncryptedKey.getValue0(),kafkaHeaders);
        kafkaTemplate.send(producerRecord).get();
    }
}
