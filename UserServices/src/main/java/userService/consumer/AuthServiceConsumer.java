package userService.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import userService.entities.UserInfo;
import userService.entities.UserInfoDto;
import userService.repository.UserRepository;
import userService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceConsumer
{

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$"); // Basic validation

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(UserInfoDto eventData) {
        try{
            // Todo: Make it transactional, to handle idempotency and validate email, phoneNumber etc
            if (!EmailValidator.getInstance().isValid(eventData.getEmail())) {
                System.out.println("Invalid email: " + eventData.getEmail());
                return;
            }

            // Validate phone number
            if (!PHONE_PATTERN.matcher(eventData.getPhoneNumber()).matches()) {
                System.out.println("Invalid phone number: " + eventData.getPhoneNumber());
                return;
            }

            // Check for idempotency: if a user already exists and data hasn't changed, skip update
            UserInfo existingUser = userRepository.findById(eventData.getUserId()).orElse(null);
            if (existingUser != null && existingUser.equals(eventData.transformToUserInfo())) {
                System.out.println("Duplicate Kafka event. Skipping update for userId: " + eventData.getUserId());
                return;
            }
            userService.createOrUpdateUser(eventData);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("AuthServiceConsumer: Exception is thrown while consuming kafka event");
        }
    }

}