package com.example.Conference.service;

import com.example.Conference.domain.Session;
import com.example.Conference.domain.Talk;
import com.example.Conference.domain.Track;
import com.example.Conference.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrackServiceTest {

    @InjectMocks
    private TrackService trackService;

    @Mock
    private TalkService talkService;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllTracks() {
        List<Track> expectedTracks = Arrays.asList(new Track(), new Track());
        when(trackRepository.findAll()).thenReturn(expectedTracks);

        List<Track> actualTracks = trackService.getTrackList();

        assertEquals(expectedTracks, actualTracks);
        verify(trackRepository, times(1)).findAll();
    }

    @Test
    public void shouldScheduleTalks() {
        List<Talk> talks = new ArrayList<>();
        List<Integer> durations = Arrays.asList(60,45,30,45,60,45,45,30,30,30);
        for(int i = 0; i < durations.size(); i++){
            Talk talk = Talk.builder()
                    .duration(durations.get(i))
                    .title("Talk " + i)
                    .build();
            talks.add(talk);
        }
        List<Track> initialTracks = new ArrayList<>();
        List<Track> expectedTracks = new ArrayList<>();
        Track track = Track.builder()
                .morning(Session.builder()
                        .remainingDuration(0)
                        .closedWithPossibleMinDuration(false)
                        .talks(Arrays.asList(talks.get(0),talks.get(1),talks.get(2),talks.get(3)))
                        .build())
                .afternoon(Session.builder()
                        .remainingDuration(0)
                        .closedWithPossibleMinDuration(false)
                        .talks(Arrays.asList(talks.get(4),talks.get(5),talks.get(6),talks.get(7),talks.get(8),talks.get(9)))
                        .build())
                .build();
        expectedTracks.add(track);
        when(trackRepository.findAll()).thenReturn(initialTracks);

        List<Track> actualTracks = trackService.scheduleTalks(talks);

        assertEquals(expectedTracks, actualTracks);
        verify(trackRepository, times(1)).saveAll(expectedTracks);
    }

    @Test
    public void shouldReturnFormattedSchedule() {
        List<Integer> durations = Arrays.asList(60,45,30,45,45,5,60,45,30,30,45,60,60,45,30,30,60,30,30);
        List<Talk> talks =  new ArrayList<>();
        for(int i = 0; i < durations.size(); i++){
            Talk talk = Talk.builder()
                    .duration(durations.get(i))
                    .title("Talk " + i)
                    .build();
            talks.add(talk);
        }
        List<Track> scheduledTracks = Arrays.asList();
        List<String> expectedSchedule = Arrays.asList("Track 1 :", "09:00AM Talk 0 60min", "10:00AM Talk 1 45min", "10:45AM Talk 2 30min", "11:15AM Talk 3 45min", "12:00PM Lunch", "01:00PM Talk 4 45min", "01:45PM Talk 6 60min", "02:45PM Talk 7 45min", "03:30PM Talk 8 30min",
                "04:00PM Talk 11 60min", "05:00PM Networking Event","--", "Track 2 :", "09:00AM Talk 9 30min", "09:30AM Talk 10 45min", "10:15AM Talk 12 60min", "11:15AM Talk 13 45min", "12:00PM Lunch", "01:00PM Talk 18 30min", "01:30PM Talk 17 30min", "02:00PM Talk 16 60min",
                "03:00PM Talk 15 30min", "03:30PM Talk 14 30min", "04:00PM Talk 5 lightning", "05:00PM Networking Event");
        when(talkService.getTalkList()).thenReturn(talks);
        when(trackRepository.findAll()).thenReturn(scheduledTracks);

        List<String> actualSchedule = trackService.GetFormattedSchedule();

        assertEquals(expectedSchedule, actualSchedule);
    }
    @Test
    public void shouldReturnFormattedSchedule_FittingAllBlocks() {
        List<Integer> durations = Arrays.asList(30,45,45,60,30,60,60,60,10,5,45,60,30,15,45,50,10,45,60,30,30,5,5,5);
        List<Talk> talks =  new ArrayList<>();
        for(int i = 0; i < durations.size(); i++){
            Talk talk = Talk.builder()
                    .duration(durations.get(i))
                    .title("Talk " + i)
                    .build();
            talks.add(talk);
        }
        List<Track> scheduledTracks = Arrays.asList();
        List<String> expectedSchedule = Arrays.asList("Track 1 :", "09:00AM Talk 0 30min", "09:30AM Talk 1 45min", "10:15AM Talk 2 45min", "11:00AM Talk 3 60min", "12:00PM Lunch", "01:00PM Talk 5 60min", "02:00PM Talk 6 60min", "03:00PM Talk 7 60min", "04:00PM Talk 8 10min", "04:10PM Talk 9 lightning", "04:15PM Talk 10 45min", "05:00PM Networking Event", "--", "Track 2 :", "09:00AM Talk 4 30min", "09:30AM Talk 11 60min", "10:30AM Talk 12 30min",
                "11:00AM Talk 13 15min", "11:15AM Talk 14 45min", "12:00PM Lunch", "01:00PM Talk 15 50min", "01:50PM Talk 16 10min", "02:00PM Talk 17 45min", "02:45PM Talk 18 60min", "03:45PM Talk 19 30min", "04:15PM Talk 20 30min", "04:45PM Talk 21 lightning", "04:50PM Talk 22 lightning", "04:55PM Talk 23 lightning", "05:00PM Networking Event");
        when(talkService.getTalkList()).thenReturn(talks);
        when(trackRepository.findAll()).thenReturn(scheduledTracks);

        List<String> actualSchedule = trackService.GetFormattedSchedule();

        assertEquals(expectedSchedule, actualSchedule);
    }

    @Test
    public void shouldReturnFormattedSchedule_EdgeCase_1() {
        List<Integer> durations = Arrays.asList(180,180,180);
        List<Talk> talks =  new ArrayList<>();
        for(int i = 0; i < durations.size(); i++){
            Talk talk = Talk.builder()
                    .duration(durations.get(i))
                    .title("Talk " + i)
                    .build();
            talks.add(talk);
        }
        List<Track> scheduledTracks = Arrays.asList();
        List<String> expectedSchedule = Arrays.asList("Track 1 :", "09:00AM Talk 0 180min", "12:00PM Lunch", "01:00PM Talk 2 180min", "05:00PM Networking Event", "--", "Track 2 :", "09:00AM Talk 1 180min", "12:00PM Lunch", "05:00PM Networking Event");
        when(talkService.getTalkList()).thenReturn(talks);
        when(trackRepository.findAll()).thenReturn(scheduledTracks);

        List<String> actualSchedule = trackService.GetFormattedSchedule();

        assertEquals(expectedSchedule, actualSchedule);
    }
    @Test
    public void shouldReturnFormattedSchedule_EdgeCase_2() {
        List<Integer> durations = Arrays.asList(60,98,15,60,77,57,19);
        List<Talk> talks =  new ArrayList<>();
        for(int i = 0; i < durations.size(); i++){
            Talk talk = Talk.builder()
                    .duration(durations.get(i))
                    .title("Talk " + i)
                    .build();
            talks.add(talk);
        }
        List<Track> scheduledTracks = Arrays.asList();
        List<String> expectedSchedule = Arrays.asList("Track 1 :", "09:00AM Talk 6 19min", "09:19AM Talk 3 60min", "10:19AM Talk 1 98min", "12:00PM Lunch", "01:00PM Talk 5 57min", "01:57PM Talk 4 77min", "03:14PM Talk 2 15min", "03:29PM Talk 0 60min", "05:00PM Networking Event");
        when(talkService.getTalkList()).thenReturn(talks);
        when(trackRepository.findAll()).thenReturn(scheduledTracks);

        List<String> actualSchedule = trackService.GetFormattedSchedule();

        assertEquals(expectedSchedule, actualSchedule);
    }
}
