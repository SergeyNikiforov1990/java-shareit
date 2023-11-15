package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.validator.EmailRFC2822;
import ru.practicum.shareit.utils.validation.Create;
import ru.practicum.shareit.utils.validation.Update;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @JsonProperty("id")
    private int id;
    @NotBlank(message = "Name cannot be empty", groups = {Create.class})
    @JsonProperty("name")
    private String name;
    @NotBlank(message = "Email cannot be empty", groups = {Create.class})
    @EmailRFC2822(message = "Email must contain the @ symbol and match the pattern rfc2822",
            groups = {Create.class, Update.class})
    @JsonProperty("email")
    private String email;
}
