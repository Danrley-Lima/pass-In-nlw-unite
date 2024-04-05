package rocketseat.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import rocketseat.com.passin.domain.event.exceptions.EventFullException;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.general.ErrorResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<String> handleEventNotFound(EventNotFoundException ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<ErrorResponseDTO> handleCheckInAlreadyExists(EventFullException ex) {
    return ResponseEntity.badRequest().body(new ErrorResponseDTO(ex.getMessage()));
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity<String> handleAttendeeNotFound(AttendeeNotFoundException ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadyExistException.class)
  public ResponseEntity<String> handleAttendeeAlreadyExists(AttendeeAlreadyExistException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistsException.class)
  public ResponseEntity<String> handleCheckInAlreadyExists(CheckInAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
