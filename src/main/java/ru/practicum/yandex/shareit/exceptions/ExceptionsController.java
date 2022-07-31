package ru.practicum.yandex.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsController {

    @ExceptionHandler
    public ResponseEntity<?> handleValidation(final ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<?> handleUserNotFound(final UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleItemNotFound(final ItemNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> handleDuplicateEmail(final EmailDuplicateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

  /*  @ExceptionHandler
    public ResponseEntity<?> handleMpaNotFound(final MpaNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
*/

 /*   @ExceptionHandler
    public ResponseEntity<?> handleThrowable(final Throwable e) {
        return new ResponseEntity<>("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
    @ExceptionHandler
    public ResponseEntity<?> handleThrowable(final Throwable e) {
        return new ResponseEntity<>("Unexpected error occurred", HttpStatus.BAD_REQUEST);
    }


}

