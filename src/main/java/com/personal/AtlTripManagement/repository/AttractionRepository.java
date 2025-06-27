package com.personal.AtlTripManagement.repository;

import com.personal.AtlTripManagement.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
}
