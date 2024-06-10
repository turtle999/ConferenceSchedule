package com.example.Conference.controller;

import com.example.Conference.controller.TrackController;
import com.example.Conference.domain.Track;
import com.example.Conference.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrackControllerTest {

    @InjectMocks
    private TrackController trackController;

    @Mock
    private TrackService trackService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnScheduledTalks() {
        List<String> expectedSchedule = Arrays.asList("Talk 1", "Talk 2");
        when(trackService.GetFormattedSchedule()).thenReturn(expectedSchedule);

        ResponseEntity<List<String>> response = trackController.scheduleTalks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSchedule, response.getBody());
        verify(trackService, times(1)).GetFormattedSchedule();
    }

    @Test
    public void shouldReturnAllTracks() {
        List<Track> expectedTracks = Arrays.asList(new Track(), new Track());
        when(trackService.getTrackList()).thenReturn(expectedTracks);

        ResponseEntity<List<Track>> response = trackController.getTracks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTracks, response.getBody());
        verify(trackService, times(1)).getTrackList();
    }
}