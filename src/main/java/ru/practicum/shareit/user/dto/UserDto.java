package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.validator.EmailRFC2822;
import ru.practicum.shareit.utils.validation.Create;
import ru.practicum.shareit.utils.validation.Update;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private int id;
    @NotBlank(message = "Name cannot be empty", groups = {Create.class})
    private String name;
    @NotBlank(message = "Email cannot be empty", groups = {Create.class})
    @EmailRFC2822(message = "Email must contain the @ symbol and match the pattern rfc2822",
            groups = {Create.class, Update.class})
    private String email;
}
