package ru.practicum.shareit.utils.validation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ValidationException;

public class PaginationUtils {
    public static PageRequest createPageRequest(int from, int size) {
        validateFromValue(from);

        int offset = from > 0 ? from / size : 0;
        return PageRequest.of(offset, size);
    }

    private static void validateFromValue(int from) {
        if (from < 0) {
            throw new ValidationException("Неверное значение from");
        }
    }
}
