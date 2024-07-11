package com.rocketseat.planner.activity;

import com.rocketseat.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public ActivityCreateResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        var activity = new Activity(payload.title(), payload.occursAt(), trip);
        activityRepository.save(activity);
        return new ActivityCreateResponse(activity.getId());
    }

    public List<ActivityResponse> getAllActivities(UUID tripId) {
        return activityRepository
                .findByTripId(tripId)
                .stream()
                .map(activity -> new ActivityResponse(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
