package ru.practicum.shareit.constants;

import java.util.regex.Pattern;

public class HeaderConstants {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final Pattern RFC_2822 = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$");

}
