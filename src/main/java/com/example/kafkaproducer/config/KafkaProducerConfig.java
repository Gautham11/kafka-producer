package com.example.kafkaproducer.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

// Security Protocol and Mechanism
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-512");

// JAAS Config for SCRAM-SHA-512
        props.put(SaslConfigs.SASL_JAAS_CONFIG,
                "org.apache.kafka.common.security.scram.ScramLoginModule required " +
                        "username=\"producer\" password=\"producer-secret\";");

// Truststore for SSL
        String truststorePath = new File(
                getClass().getClassLoader().getResource("kafka.client.truststore.jks").getFile()
        ).getAbsolutePath();

        props.put("ssl.truststore.location", truststorePath);
        props.put("ssl.truststore.password", "changeit");

// Optional: disable hostname verification (useful in dev/local)
        props.put("ssl.endpoint.identification.algorithm", "");

// Optionally enable debug logging for SASL
// props.put("request.timeout.ms", "30000");
// props.put("retries", "3");
// props.put("sasl.login.callback.handler.class", "org.apache.kafka.common.security.authenticator.AbstractLogin");

        return new DefaultKafkaProducerFactory<>(props);


    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
