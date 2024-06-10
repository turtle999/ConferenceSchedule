package com.example.Conference.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {

    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Duration is mandatory")
    private String duration;

}
