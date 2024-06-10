package com.example.Conference.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Session morning;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Session afternoon;

    public Track() {
        this.morning = new Session(180); // 9:00 AM - 12:00 PM
        this.afternoon = new Session(240); // 1:00 PM - 5:00 PM
    }
}
