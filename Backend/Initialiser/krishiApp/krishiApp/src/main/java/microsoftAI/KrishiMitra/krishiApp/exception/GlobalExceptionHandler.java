package microsoftAI.KrishiMitra.krishiApp.exception;

import microsoftAI.KrishiMitra.krishiApp.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle our custom "Not Found" errors (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponseDTO errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 2. Handle the Concurrency Double-Booking errors (409 Conflict)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponseDTO> handleOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException ex, WebRequest request) {

        String message = "Conflict: This equipment was just modified or booked by someone else. Please refresh and try again.";
        ErrorResponseDTO errorResponse = buildErrorResponse(
                HttpStatus.CONFLICT, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // 3. Handle bad frontend input/validation errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Grab the specific field error (e.g., "Phone number cannot be blank")
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponseDTO errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 4. Catch-all for any other unexpected server crashes (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponseDTO errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to keep code clean
    private ErrorResponseDTO buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
}