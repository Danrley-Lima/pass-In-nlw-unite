package rocketseat.com.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;
  private final AttendeeService attendeeService;

  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEvent(@PathVariable("id") String eventId) {
    EventResponseDTO event = this.eventService.getEventDetail(eventId);
    return ResponseEntity.ok(event);
  }

  @GetMapping("/attendees/{id}")
  public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable("id") String eventId) {
    AttendeesListResponseDTO attendeeListResponse = this.attendeeService.getEventsAttendee(eventId);
    return ResponseEntity.ok(attendeeListResponse);
  }

  @PostMapping
  public ResponseEntity<EventIdDTO> createEvent(
      @RequestBody EventRequestDTO body,
      UriComponentsBuilder uriComponentsBuilder) {

    EventIdDTO eventIdDto = this.eventService.createEvent(body);
    var uri = uriComponentsBuilder.path("/api/events/{id}").buildAndExpand(eventIdDto.eventId()).toUri();

    return ResponseEntity.created(uri).body(eventIdDto);
  }

  @PostMapping("/{eventId}/attendees")
  public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable("eventId") String eventId,
      @RequestBody AttendeeRequestDTO body,
      UriComponentsBuilder uriComponentsBuilder) {

    AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);
    var uri = uriComponentsBuilder.path("/api/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId())
        .toUri();

    return ResponseEntity.created(uri).body(attendeeIdDTO);
  }
}
