package rocketseat.com.passin.services;

import java.time.LocalDateTime;

import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.Checkin;
import rocketseat.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import rocketseat.com.passin.repositories.CheckinRepository;

@Service
@RequiredArgsConstructor
public class CheckInService {
  private final CheckinRepository checkinRepository;

  public void registerCheckIn(Attendee attendee) {
    this.verifyCheckInExists(attendee.getId());

    Checkin newCheckIn = new Checkin();
    newCheckIn.setAttendee(attendee);
    newCheckIn.setCreatedAt(LocalDateTime.now());
    this.checkinRepository.save(newCheckIn);
  }

  private void verifyCheckInExists(String attendeeId) {
    this.getCheckIn(attendeeId).ifPresent(checkIn -> {
      throw new CheckInAlreadyExistsException("CheckIn already exists");
    });
  }

  public Optional<Checkin> getCheckIn(String attendeeId) {
    return this.checkinRepository.findByAttendeeId(attendeeId);
  }
}
