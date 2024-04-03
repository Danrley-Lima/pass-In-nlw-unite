package rocketseat.com.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.Checkin;
import rocketseat.com.passin.dto.Attendee.AttendeeDetails;
import rocketseat.com.passin.dto.Attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckinRepository;

@Service
@RequiredArgsConstructor
public class AttendeeService {

  private final AttendeeRepository attendeeRepository;
  private final CheckinRepository checkInRepository;

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);

    return attendeeList;
  }

  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

    List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
      Optional<Checkin> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
      LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;

      return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
          checkedInAt);
    }).toList();

    return new AttendeesListResponseDTO(attendeeDetailsList);

  }
}
