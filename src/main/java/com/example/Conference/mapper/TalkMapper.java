package com.example.Conference.mapper;

import com.example.Conference.domain.Talk;
import com.example.Conference.domain.dto.TalkDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
public class TalkMapper extends PropertyMap<TalkDto, Talk> {

    private final Converter<String, Integer> durationConverter = new AbstractConverter<String, Integer>() {
        @Override
        protected Integer convert(String sourceDuration) {
            if ("lightning".equals(sourceDuration)) {
                return 5;
            }else{
                return Integer.valueOf(sourceDuration);
            }
        }
    };

    @Override
    protected void configure() {
        map().setTitle(source.getTitle());
        using(durationConverter).map(source.getDuration()).setDuration(null);
    }
}
