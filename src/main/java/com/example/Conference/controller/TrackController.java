package com.example.Conference.controller;

import com.example.Conference.domain.Track;
import com.example.Conference.service.TalkService;
import com.example.Conference.service.TrackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackService trackService;
    private final TalkService talkService;
    public TrackController(TrackService trackService, TalkService talkService) {
        this.trackService = trackService;
        this.talkService = talkService;
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<String>> scheduleTalks() {
        List<String> scheduledTalks = trackService.GetFormattedSchedule();
        return new ResponseEntity<>(scheduledTalks, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Track>> getTracks(){
        List<Track> tracks = trackService.getTrackList();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

     @DeleteMapping("/bulk")
     public ResponseEntity<Void> removeTracks(){
        trackService.deleteAllTracks();
        talkService.updateAllTalksToUnscheduled();
        return ResponseEntity.noContent().build();
     }
}
