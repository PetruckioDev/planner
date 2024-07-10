package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("trips")
public class TripController {

    private final ParticipantService participantService;
    private final TripRepository tripRepository;

    public TripController(ParticipantService participantService, TripRepository tripRepository) {
        this.participantService = participantService;
        this.tripRepository = tripRepository;
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequestPayload payload) {
        var trip = new Trip(payload);
        tripRepository.save(trip);
        participantService.registerParticipantsToEvent(payload.emailsToInvite(), trip);
        return ResponseEntity.ok(TripResponse.of(trip.getId()));
    }

    @GetMapping("{tripId}")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID tripId) {
        var trip = tripRepository.findById(tripId);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();

            rawTrip.setDestination(payload.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

            tripRepository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();
            rawTrip.setConfirmed(true);
            tripRepository.save(rawTrip);
            participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());
            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();
            var response = participantService.registerParticipantToEvent(payload.participantEmail(), rawTrip);

            if (Boolean.TRUE.equals(rawTrip.getConfirmed())) {
                participantService.triggerConfirmationEmailToParticipant(payload.participantEmail());
            }
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{tripId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getAllParticipants(@PathVariable UUID tripId) {
        var participants = participantService.getAllParticipants(tripId);
        return ResponseEntity.ok(participants);
    }
}
