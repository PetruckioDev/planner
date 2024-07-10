package com.rocketseat.planner.participant;

import java.util.UUID;

public record ParticipantResponse(UUID id, String participantName, String participantEmail, Boolean isConfirmed) {
}
