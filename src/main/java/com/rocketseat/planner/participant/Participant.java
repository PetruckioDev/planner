package com.rocketseat.planner.participant;

import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "participant_name", nullable = false)
    private String participantName;
    @Column(name = "participant_email", nullable = false)
    private String participantEmail;
    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Participant() {
    }

    public Participant(UUID id, String participantName, String participantEmail, Boolean isConfirmed, Trip trip) {
        this.id = id;
        this.participantName = participantName;
        this.participantEmail = participantEmail;
        this.isConfirmed = isConfirmed;
        this.trip = trip;
    }

    public Participant(String email, Trip trip) {
        this.participantName = "";
        this.participantEmail = email;
        this.isConfirmed = false;
        this.trip = trip;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public Boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
