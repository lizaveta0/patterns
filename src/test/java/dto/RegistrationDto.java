package dto;

import lombok.Value;

@Value
public class RegistrationDto {
    String login;
    String password;
    String status;
}
