package com.rocketseat.planner.link;

import com.rocketseat.planner.trip.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkCreateResponse registerLink(LinkRequestPayload payload, Trip trip) {
        var link = new Link(payload.title(), payload.url(), trip);
        linkRepository.save(link);
        return new LinkCreateResponse(link.getId());
    }

    public List<LinkResponse> getAllLinks(UUID tripId) {
        return linkRepository.findByTripId(tripId)
                .stream()
                .map(link -> new LinkResponse(link.getId(), link.getTitle(), link.getUrl()))
                .toList();
    }
}
