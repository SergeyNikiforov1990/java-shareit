package ru.practicum.validation;

import javax.validation.groups.Default;

public interface ValidationGroups {

    interface Create extends Default {
    }

    interface Update extends Default {
    }
}