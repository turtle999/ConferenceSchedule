package com.example.Conference.controller;

import com.example.Conference.domain.Talk;
import com.example.Conference.domain.dto.TalkDto;
import com.example.Conference.service.TalkService;
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

public class TalkControllerTest {

    @InjectMocks
    private TalkController talkController;

    @Mock
    private TalkService talkService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAllTalks() {
        List<Talk> expectedTalks = Arrays.asList(new Talk(), new Talk());
        when(talkService.getTalkList()).thenReturn(expectedTalks);

        List<Talk> actualTalks = talkController.getTalks();

        assertEquals(expectedTalks, actualTalks);
        verify(talkService, times(1)).getTalkList();
    }

    @Test
    public void shouldAddTalk() {
        TalkDto talkDto = new TalkDto("Talk 1", "60");
        Talk talk = new Talk();
        when(talkService.createTalk(talkDto)).thenReturn(talk);

        ResponseEntity<Talk> response = talkController.addTalk(talkDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(talk, response.getBody());
        verify(talkService, times(1)).createTalk(talkDto);
    }

    @Test
    public void shouldAddTalks() {
        List<TalkDto> talksDto = Arrays.asList(new TalkDto("Talk 1", "60"), new TalkDto("Talk 2", "30"));
        List<Talk> talks = Arrays.asList(new Talk(), new Talk());
        when(talkService.saveTalks(talksDto)).thenReturn(talks);

        ResponseEntity<List<Talk>> response = talkController.addTalks(talksDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(talks, response.getBody());
        verify(talkService, times(1)).saveTalks(talksDto);
    }

    @Test
    public void shouldRemoveTalk() {
        Long id = 1L;
        doNothing().when(talkService).deleteTalk(id);

        ResponseEntity<Void> response = talkController.removeTalk(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(talkService, times(1)).deleteTalk(id);
    }
}