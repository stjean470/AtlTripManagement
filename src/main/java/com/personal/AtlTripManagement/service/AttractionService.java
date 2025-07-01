package com.personal.AtlTripManagement.service;

import com.personal.AtlTripManagement.dto.AttractionDto;
import com.personal.AtlTripManagement.model.Attraction;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AttractionService {
    AttractionDto addAttraction(AttractionDto attractionDto);

    List<AttractionDto> getAllAttraction();

    AttractionDto getAttractionById(Long id);

    //String deleteAttraction(Long id)
}
