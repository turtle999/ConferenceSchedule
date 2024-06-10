package com.example.Conference.service;

import com.example.Conference.domain.Session;
import com.example.Conference.domain.Talk;
import com.example.Conference.domain.dto.TalkDto;
import com.example.Conference.exception.DuplicateTitleException;
import com.example.Conference.repository.SessionRepository;
import com.example.Conference.repository.TalkRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TalkService {

    private final TalkRepository talkRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;

    public TalkService(TalkRepository talkRepository, SessionRepository sessionRepository, ModelMapper modelMapper) {
        this.talkRepository = talkRepository;
        this.sessionRepository = sessionRepository;
        this.modelMapper = modelMapper;
    }

    public Talk createTalk(TalkDto talkDto) {
        if (talkRepository.existsByTitle(talkDto.getTitle())) {
            throw new DuplicateTitleException("A talk with the same title already exists.");
        }
        return talkRepository.save(modelMapper.map(talkDto, Talk.class));
    }

    public  List<Talk> getTalkList() {
        return talkRepository.findAll();
    }

    public List<Talk> saveTalks(List<TalkDto> talks) {
        List<Talk> savedTalks = new ArrayList<>();
        for (TalkDto talkDto : talks) {
            if (talkRepository.existsByTitle(talkDto.getTitle())) {
                throw new DuplicateTitleException("A talk with the same title already exists: " + talkDto.getTitle());
            }
            Talk savedTalk = talkRepository.save(modelMapper.map(talkDto, Talk.class));
            savedTalks.add(savedTalk);
        }
        return savedTalks;
    }

    public void deleteTalk(Long id) {
        Talk talk = talkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Talk not found with id: " + id));
        if (talk.isScheduled()) {
            Session session = talk.getSession();
            session.removeTalk(talk);
            sessionRepository.save(session);
        }
        talkRepository.deleteById(id);
    }

    public void updateAllTalksToUnscheduled() {
        List<Talk> talks = talkRepository.findAll();
        for (Talk talk : talks) {
            talk.setScheduled(false);
        }
        talkRepository.saveAll(talks);
    }

    public void deleteAllTalks() {
        talkRepository.deleteAll();
    }
}
