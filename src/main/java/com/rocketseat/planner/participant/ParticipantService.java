package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        var participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        participantRepository.saveAll(participants);
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        var participant = new Participant(email, trip);
        participantRepository.save(participant);
        return new ParticipantCreateResponse(participant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        //TODO
    }

    public void triggerConfirmationEmailToParticipant(String participantEmail) {

    }

    public List<ParticipantResponse> getAllParticipants(UUID tripId) {
        return participantRepository
                .findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantResponse(participant.getId(), participant.getParticipantName(), participant.getParticipantEmail(), participant.isConfirmed()))
                .toList();
    }
}
