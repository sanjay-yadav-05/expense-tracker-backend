package authService.eventProducer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@JsonNaming (PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoEvent
{
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String userId;
}