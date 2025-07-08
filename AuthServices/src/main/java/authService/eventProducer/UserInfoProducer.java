package authService.eventProducer;

//import authService.config.EventPublisherConfig;
import authService.Entities.UserInfo;
import authService.Model.UserInfoDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Properties;

@Service
public class UserInfoProducer
{
//    private EventPublisherConfig eventPublisherConfig;
//
//    private final Producer<String, UserInfoDto> kafkaProducer;
//
//    private final String TOPIC_NAME = "testingself";
//
//    @Autowired
//    UserInfoProducer(EventPublisherConfig eventPublisherConfig){
//        this.eventPublisherConfig = eventPublisherConfig;
//        Properties props = new Properties();
//        props.put("bootstrap.servers", eventPublisherConfig.getBootstrapServers());
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        this.kafkaProducer = new KafkaProducer<>(props);
//    }
//
//    public void sendEventToKafka( UserInfoDto eventData) {
//        ProducerRecord<String, UserInfoDto> record = new ProducerRecord<>(TOPIC_NAME, "testingselfkey", eventData);
//        kafkaProducer.send(record, (metadata, exception) -> {
//            if (exception != null) {
//                System.err.println("Error sending message to Kafka: " + exception.getMessage());
//            } else {
//                System.out.println("Message sent to Kafka, offset: " + metadata.offset());
//            }
//        });
//    }

    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;

    @Autowired
    UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent eventData) {
        Message<UserInfoEvent> message = MessageBuilder.withPayload(eventData)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName).build();
        kafkaTemplate.send(message);
    }

}
