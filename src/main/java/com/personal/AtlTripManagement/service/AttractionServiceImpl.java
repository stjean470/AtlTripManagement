package com.personal.AtlTripManagement.service;

import com.personal.AtlTripManagement.dto.AttractionDto;
import com.personal.AtlTripManagement.mapper.AttractionMapper;
import com.personal.AtlTripManagement.model.Attraction;
import com.personal.AtlTripManagement.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttractionServiceImpl implements AttractionService{

    @Autowired
    private AttractionRepository attractionRepository;
    @Override
    public AttractionDto addAttraction(AttractionDto attractionDto) {
        Attraction attraction = AttractionMapper.mapToAttraction(attractionDto);
        Attraction savedAttraction = attractionRepository.save(attraction);
        return AttractionMapper.mapToAttractionDto(savedAttraction);
    }

    @Override
    public List<AttractionDto> getAllAttraction() {
        List<Attraction> attractions = attractionRepository.findAll();
        List<AttractionDto> attractionDtos = new ArrayList<>();
        for (Attraction attraction: attractions) {
            attractionDtos.add(AttractionMapper.mapToAttractionDto(attraction));
        }
        return attractionDtos;
    }

    @Override
    public AttractionDto getAttractionById(Long id) {
        Attraction attraction = attractionRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find Attraction"));
        return AttractionMapper.mapToAttractionDto(attraction);
    }

}
