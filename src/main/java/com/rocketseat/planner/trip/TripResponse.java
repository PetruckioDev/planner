package com.rocketseat.planner.trip;

import java.util.UUID;

public record TripResponse(UUID tripId) {
    public static TripResponse of(UUID tripId) {
        return new TripResponse(tripId);
    }
}
