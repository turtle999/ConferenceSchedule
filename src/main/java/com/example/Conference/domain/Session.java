package com.example.Conference.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int remainingDuration;
    private boolean closedWithPossibleMinDuration;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Talk> talks = new ArrayList<>();
    public Session(int remainingDuration) {
        this.remainingDuration = remainingDuration;
        this.closedWithPossibleMinDuration = false;
    }
    public boolean addTalk(Talk talk) {
        if(remainingDuration >= talk.getDuration()) {
            talks.add(talk);
            remainingDuration -= talk.getDuration();
            return true;
        }
        return false;
    }
    public void removeTalk(Talk talk) {
        talks.remove(talk);
        remainingDuration += talk.getDuration();
    }
}
