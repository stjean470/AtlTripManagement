package com.personal.AtlTripManagement.controller;

import com.personal.AtlTripManagement.dto.AttractionDto;
import com.personal.AtlTripManagement.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@Controller
@RequestMapping("/attraction")
public class AttractionController {
    @Autowired
    private AttractionService attractionService;

    @PostMapping("/add-attraction")
    public ResponseEntity<AttractionDto> addAttraction(@RequestBody AttractionDto attractionDto) {
        AttractionDto savedAttraction = attractionService.addAttraction(attractionDto);
        return new ResponseEntity<>(savedAttraction, HttpStatus.CREATED);
    }

    @GetMapping("/attractions")
    public ResponseEntity<List<AttractionDto>> getAllAttractions() {
        return ResponseEntity.ok(attractionService.getAllAttraction());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDto> getAttractionById(@PathVariable Long id) {
        return ResponseEntity.ok(attractionService.getAttractionById(id));
    }
}
