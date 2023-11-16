package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestResponseDto {
    private int id;
    @NotBlank(message = "Поле не должно быть пустым")
    private final String description;
    private List<ItemRequestResponseDtoItem> items;
    @JsonProperty("created")
    private LocalDateTime created;
}
