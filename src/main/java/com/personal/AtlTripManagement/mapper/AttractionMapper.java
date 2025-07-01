package com.personal.AtlTripManagement.mapper;

import com.personal.AtlTripManagement.dto.AttractionDto;
import com.personal.AtlTripManagement.model.Attraction;

public class AttractionMapper {
    public static Attraction mapToAttraction(AttractionDto attractionDto) {
        return new Attraction(attractionDto.getId(), attractionDto.getAttraction(), attractionDto.getCity(), attractionDto.getState());
    }

    public static AttractionDto mapToAttractionDto(Attraction attraction) {
        return new AttractionDto(attraction.getId(), attraction.getAttraction(), attraction.getCity(), attraction.getState());
    }
}
