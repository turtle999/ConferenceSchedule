package com.example.Conference.service;

import com.example.Conference.domain.Session;
import com.example.Conference.domain.Talk;
import com.example.Conference.domain.dto.TalkDto;
import com.example.Conference.exception.DuplicateTitleException;
import com.example.Conference.repository.SessionRepository;
import com.example.Conference.repository.TalkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TalkServiceTest {

    @InjectMocks
    private TalkService talkService;

    @Mock
    private TalkRepository talkRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateTalk() {
        TalkDto talkDto = new TalkDto("Talk 1", "60");
        Talk talk = new Talk();
        when(talkRepository.existsByTitle(talkDto.getTitle())).thenReturn(false);
        when(modelMapper.map(talkDto, Talk.class)).thenReturn(talk);
        when(talkRepository.save(talk)).thenReturn(talk);

        Talk createdTalk = talkService.createTalk(talkDto);

        assertEquals(talk, createdTalk);
        verify(talkRepository, times(1)).save(talk);
    }

    @Test
    public void shouldThrowExceptionWhenCreatingDuplicateTalk() {
        TalkDto talkDto = new TalkDto("Talk 1", "60");
        when(talkRepository.existsByTitle(talkDto.getTitle())).thenReturn(true);

        assertThrows(DuplicateTitleException.class, () -> talkService.createTalk(talkDto));
    }

    @Test
    public void shouldReturnAllTalks() {
        List<Talk> expectedTalks = Arrays.asList(new Talk(), new Talk());
        when(talkRepository.findAll()).thenReturn(expectedTalks);

        List<Talk> actualTalks = talkService.getTalkList();

        assertEquals(expectedTalks, actualTalks);
        verify(talkRepository, times(1)).findAll();
    }

    @Test
    public void shouldSaveTalks() {
        List<TalkDto> talksDto = Arrays.asList(new TalkDto("Talk 1", "60"), new TalkDto("Talk 2", "30"));
        List<Talk> talks = Arrays.asList(new Talk(), new Talk());
        when(talkRepository.existsByTitle(anyString())).thenReturn(false);
        when(modelMapper.map(any(TalkDto.class), eq(Talk.class))).thenReturn(new Talk());
        when(talkRepository.save(any(Talk.class))).thenReturn(new Talk());

        List<Talk> savedTalks = talkService.saveTalks(talksDto);

        assertEquals(talks.size(), savedTalks.size());
        verify(talkRepository, times(talksDto.size())).save(any(Talk.class));
    }

    @Test
    public void shouldThrowExceptionWhenSavingDuplicateTalks() {
        List<TalkDto> talksDto = Arrays.asList(new TalkDto("Talk 1", "60"), new TalkDto("Talk 2", "60"));
        when(talkRepository.existsByTitle("Talk 1")).thenReturn(false);
        when(talkRepository.existsByTitle("Talk 2")).thenReturn(true);

        assertThrows(DuplicateTitleException.class, () -> talkService.saveTalks(talksDto));
    }

    @Test
    public void shouldDeleteTalk() {
        Long id = 1L;
        Talk talk = new Talk();
        talk.setScheduled(false);
        when(talkRepository.findById(id)).thenReturn(java.util.Optional.of(talk));

        talkService.deleteTalk(id);

        verify(talkRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingScheduledTalk() {
        Long id = 1L;
        Talk talk = new Talk();
        talk.setDuration(60);
        talk.setScheduled(true);
        Session session = new Session();
        talk.setSession(session);
        when(talkRepository.findById(id)).thenReturn(Optional.of(talk));

        talkService.deleteTalk(id);

        verify(sessionRepository, times(1)).save(session);
        verify(talkRepository, times(1)).deleteById(id);
    }
}