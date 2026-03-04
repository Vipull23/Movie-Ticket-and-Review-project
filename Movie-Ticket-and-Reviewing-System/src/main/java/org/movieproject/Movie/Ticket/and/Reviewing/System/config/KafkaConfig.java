package org.movieproject.Movie.Ticket.and.Reviewing.System.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import tools.jackson.databind.deser.jdk.StringDeserializer;
import tools.jackson.databind.ser.jdk.StringSerializer;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    public Properties getProducerConfig() {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return producerConfig;
    }

    public Properties getConsumerConfig() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return consumerConfig;
    }

    @Bean
    public ProducerFactory getProducerFactory() {
        return new DefaultKafkaProducerFactory(getProducerConfig());
    }

    @Bean
    public ConsumerFactory getConsumerFactory() {
        return new DefaultKafkaConsumerFactory(getConsumerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> getKafkaTemplate() {
        return new KafkaTemplate<String, String>(getProducerFactory());
    }
}
