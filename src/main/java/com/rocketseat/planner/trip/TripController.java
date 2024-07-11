package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityCreateResponse;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.link.LinkCreateResponse;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.participant.ParticipantCreateResponse;
import com.rocketseat.planner.participant.ParticipantRequestPayload;
import com.rocketseat.planner.participant.ParticipantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequestPayload payload) {
        var response = tripService.createTrip(payload);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{tripId}")
    public ResponseEntity<Trip> tripDetails(@PathVariable UUID tripId) {
        var trip = tripService.tripDetails(tripId);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("{tripId}/activities")
    public ResponseEntity<List<ActivityResponse>> getAllActivities(@PathVariable UUID tripId) {
        var activities = tripService.getAllActivities(tripId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId) {
        var trip = tripService.confirmTrip(tripId);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("{tripId}/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(@PathVariable UUID tripId) {
        var link = tripService.getAllLinks(tripId);
        return ResponseEntity.ok(link);
    }

    @GetMapping("{tripId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getAllParticipants(@PathVariable UUID tripId) {
        var participants = tripService.getAllParticipants(tripId);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("{tripId}/activities")
    public ResponseEntity<ActivityCreateResponse> registerActivity(@PathVariable UUID tripId, @RequestBody ActivityRequestPayload payload) {
        var trip = tripService.registerActivity(tripId, payload);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody ParticipantRequestPayload payload) {
        var trip = tripService.inviteParticipant(tripId, payload);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("{tripId}/links")
    public ResponseEntity<LinkCreateResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequestPayload payload) {
        var trip = tripService.registerLink(tripId, payload);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload) {
        var trip = tripService.updateTrip(tripId, payload);
        return trip.map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }
}
