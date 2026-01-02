package org.omer.connectfour.controller;

import lombok.extern.log4j.Log4j2;
import org.omer.connectfour.api.model.ErrorResponse;
import org.omer.connectfour.exception.GameNotFoundException;
import org.omer.connectfour.exception.IllegalMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Centralized exception handler for the Connect Four API.
 * Converts exceptions to standardized ErrorResponse objects.
 * Scoped to GameController to avoid intercepting springdoc or other endpoints.
 */
@ControllerAdvice(assignableTypes = GameController.class)
@Log4j2
public class GlobalExceptionHandler {
        /**
         * Handles illegal move exceptions (invalid column, full column, etc.).
         *
         * @param e The exception.
         * @return 400 Bad Request with error details.
         */
        @ExceptionHandler(IllegalMoveException.class)
        public ResponseEntity<ErrorResponse> handleIllegalMove(IllegalMoveException e) {
                log.warn("Invalid move [reason={}]: {}", e.getReason(), e.getMessage());

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse()
                                                .error(ErrorResponse.ErrorEnum.INVALID_MOVE)
                                                .message(e.getMessage()));
        }

        /**
         * Handles validation exceptions from @Valid annotations.
         *
         * @param e The exception.
         * @return 400 Bad Request with validation error details.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
                String message = e.getBindingResult().getFieldErrors().stream()
                                .findFirst()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .orElse("Validation failed");
                log.warn("Validation error: {}", message);

                return ResponseEntity.badRequest()
                                .body(new ErrorResponse()
                                                .error(ErrorResponse.ErrorEnum.INVALID_MOVE)
                                                .message(message));
        }

        /**
         * Handles game not found exceptions.
         *
         * @param e The exception.
         * @return 404 Not Found with error details.
         */
        @ExceptionHandler(GameNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException e) {
                log.debug("Game not found: {}", e.getGameId());

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse()
                                                .error(ErrorResponse.ErrorEnum.GAME_NOT_FOUND)
                                                .message(e.getMessage()));
        }

        /**
         * Handles unexpected exceptions.
         *
         * @param e The exception.
         * @return 500 Internal Server Error with generic message.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericError(Exception e) {
                log.error("Unexpected error", e);

                return ResponseEntity.internalServerError()
                                .body(new ErrorResponse()
                                                .error(ErrorResponse.ErrorEnum.SERVER_ERROR)
                                                .message("An unexpected error occurred"));
        }
}
