package com.example.Conference;

import com.example.Conference.mapper.TalkMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceApplication.class, args);
	}

	@Bean
	public ModelMapper setMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(new TalkMapper());
		return mapper;
	}

}
