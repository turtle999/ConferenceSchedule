package com.example.Conference.controller;

import com.example.Conference.domain.Talk;
import com.example.Conference.domain.dto.TalkDto;
import com.example.Conference.service.TalkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/talks")
public class TalkController {
    private final TalkService talkService;

    public TalkController(TalkService talkService) {
        this.talkService = talkService;
    }

    @GetMapping
    public List<Talk> getTalks() {
        return talkService.getTalkList();
    }

    @PostMapping
    public ResponseEntity<Talk> addTalk(@Valid @RequestBody TalkDto talkDto ) {
        Talk createdTalk= talkService.createTalk(talkDto);
        return  new ResponseEntity<>(createdTalk, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Talk>> addTalks(@Validated @RequestBody List<TalkDto> talks) {
        List<Talk> createdTalks= talkService.saveTalks(talks);
        return new ResponseEntity<>(createdTalks, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTalk(@Valid @PathVariable Long id) {
        talkService.deleteTalk(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> removeTalks(){
        talkService.deleteAllTalks();
        return ResponseEntity.noContent().build();
    }
}
