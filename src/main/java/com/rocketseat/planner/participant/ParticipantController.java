package com.rocketseat.planner.participant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("participants")
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantRepository participantRepository;

    public ParticipantController(ParticipantService participantService, ParticipantRepository participantRepository) {
        this.participantService = participantService;
        this.participantRepository = participantRepository;
    }

    @PostMapping("{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantId, @RequestBody ParticipantRequestPayload payload) {
        var participant = participantRepository.findById(participantId);

        if (participant.isPresent()) {
            var rawParticipant = participant.get();
            rawParticipant.setConfirmed(true);
            rawParticipant.setParticipantName(payload.participantName());
            participantRepository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }
}
