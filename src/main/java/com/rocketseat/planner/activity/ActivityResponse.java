package com.rocketseat.planner.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponse(UUID id, String title, LocalDateTime occursAt) {
}
