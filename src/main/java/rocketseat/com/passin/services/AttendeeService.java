package rocketseat.com.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkin.Checkin;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeDetailsDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;

@Service
@RequiredArgsConstructor
public class AttendeeService {

  private final AttendeeRepository attendeeRepository;
  private final CheckInService checkInService;

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);

    return attendeeList;
  }

  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

    List<AttendeeDetailsDTO> attendeeDetailsList = attendeeList.stream().map(attendee -> {
      Optional<Checkin> checkIn = this.checkInService.getCheckIn(attendee.getId());
      LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;

      return new AttendeeDetailsDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
          checkedInAt);
    }).toList();

    return new AttendeesListResponseDTO(attendeeDetailsList);

  }

  public void verifyAttendeeSubscription(String email, String eventId) {
    Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
    if (isAttendeeRegistered.isPresent())
      throw new AttendeeAlreadyExistException("Attendee already registered");
  }

  public void checkInAttendee(String attendeeId) {
    Attendee attendee = getAttendee(attendeeId);
    this.checkInService.registerCheckIn(attendee);
  }

  private Attendee getAttendee(String attendeeId) {
    return this.attendeeRepository.findById(attendeeId)
        .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
  }

  public Attendee registerAttendee(Attendee newAttendee) {
    return this.attendeeRepository.save(newAttendee);
  }

  public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    Attendee attendee = getAttendee(attendeeId);

    var uri = uriComponentsBuilder.path("/api/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri()
        .toString();

    AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(
        attendee.getName(),
        attendee.getEmail(),
        uri,
        attendee.getEvent().getId());

    return new AttendeeBadgeResponseDTO(badgeDTO);
  }
}
