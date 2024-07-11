package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.ActivityCreateResponse;
import com.rocketseat.planner.activity.ActivityRequestPayload;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkCreateResponse;
import com.rocketseat.planner.link.LinkRequestPayload;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.ParticipantCreateResponse;
import com.rocketseat.planner.participant.ParticipantRequestPayload;
import com.rocketseat.planner.participant.ParticipantResponse;
import com.rocketseat.planner.participant.ParticipantService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    private final ActivityService activityService;
    private final LinkService linkService;
    private final ParticipantService participantService;
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository, ActivityService activityService, LinkService linkService, ParticipantService participantService) {
        this.tripRepository = tripRepository;
        this.activityService = activityService;
        this.linkService = linkService;
        this.participantService = participantService;
    }

    public TripResponse createTrip(TripRequestPayload payload) {
        var trip = new Trip(payload);
        tripRepository.save(trip);
        participantService.registerParticipantsToEvent(payload.emailsToInvite(), trip);
        return new TripResponse(trip.getId());
    }

    public Optional<Trip> tripDetails(UUID tripId) {
        return tripRepository.findById(tripId);
    }

    public Optional<Trip> updateTrip(UUID tripId, TripRequestPayload payload) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();

            rawTrip.setDestination(payload.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

            tripRepository.save(rawTrip);
            return Optional.of(rawTrip);
        }

        return Optional.empty();
    }

    public Optional<Trip> confirmTrip(UUID tripId) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();
            rawTrip.setConfirmed(true);
            tripRepository.save(rawTrip);
            participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());
            return Optional.of(rawTrip);
        }

        return Optional.empty();
    }

    public Optional<ParticipantCreateResponse> inviteParticipant(UUID tripId, ParticipantRequestPayload payload) {
        var trip = tripRepository.findById(tripId);

        if (trip.isPresent()) {
            var rawTrip = trip.get();
            var response = participantService.registerParticipantToEvent(payload.participantEmail(), rawTrip);

            if (Boolean.TRUE.equals(rawTrip.getConfirmed())) {
                participantService.triggerConfirmationEmailToParticipant(payload.participantEmail());
            }
            return Optional.of(response);
        }
        return Optional.empty();
    }

    public Optional<LinkCreateResponse> registerLink(UUID tripId, LinkRequestPayload payload) {
        var trip = tripRepository.findById(tripId);
        if (trip.isPresent()) {
            var rawTrip = trip.get();
            var response = linkService.registerLink(payload, rawTrip);
            return Optional.of(response);
        }
        return Optional.empty();
    }

    public List<ActivityResponse> getAllActivities(UUID tripId) {
        return activityService.getAllActivities(tripId);
    }

    public List<LinkResponse> getAllLinks(UUID tripId) {
        return linkService.getAllLinks(tripId);
    }

    public List<ParticipantResponse> getAllParticipants(UUID tripId) {
        return participantService.getAllParticipants(tripId);
    }

    public Optional<ActivityCreateResponse> registerActivity(UUID tripId, ActivityRequestPayload payload) {
        var trip = tripRepository.findById(tripId);
        if (trip.isPresent()) {
            var rawTrip = trip.get();
            var response = activityService.registerActivity(payload, rawTrip);
            return Optional.of(response);
        }
        return Optional.empty();
    }
}
