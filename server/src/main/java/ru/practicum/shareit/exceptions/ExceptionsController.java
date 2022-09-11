package ru.practicum.shareit.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.exceptions.model.ErrorResponse;

@RestControllerAdvice
public class ExceptionsController {

    @ExceptionHandler
    public ResponseEntity<?> handleValidation(final ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<?> handleNotFound(final NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDuplicateEmailConflict(final EmailDuplicateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDataBaseEmailUnique(DataIntegrityViolationException e) {
        return new ResponseEntity<>("This email already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleValidEmail(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Email must be valid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationStateException(final MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(String.format("Unknown %s: %s", e.getName(), e.getValue()));
    }

}

