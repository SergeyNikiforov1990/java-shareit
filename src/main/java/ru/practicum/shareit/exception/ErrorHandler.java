package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistExceptionUserOrFilm(final UserAlreadyExistException ex) {
        log.warn("Received status 409 Conflict {}:", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleValidationIdException(final EntityNotFoundException ex) {
        log.warn("Received status 404 Not found {}:", ex.getMessage());
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({ValidationException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception ex) {
        String errorMessage;

        if (ex instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError();
            errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation error";
        } else {
            errorMessage = ex.getMessage();
        }

        log.warn("Received status 400 Bad request {}:", errorMessage);
        return new ErrorResponse(errorMessage);
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllUnhandledExceptions(final Throwable t) {
        log.error("Received status 500 Internal server error", t);
        return new ErrorResponse("Internal server error. Please try again later.");
    }
}
