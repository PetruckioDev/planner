package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        participantService.registerParticipantsToEvent(payload.emailsToInvite(), trip.getId());
        return ResponseEntity.ok(TripResponse.of(trip.getId()));
    }

    @GetMapping("{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) {
        var trip = tripRepository.findById(tripId);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }
}
