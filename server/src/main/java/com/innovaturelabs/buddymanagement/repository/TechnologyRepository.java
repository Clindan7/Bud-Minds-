package com.innovaturelabs.buddymanagement.repository;

import com.innovaturelabs.buddymanagement.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {

    Optional<Technology> findByTechnologyId(Integer technologyId);
    Optional<Technology> findByTechnologyName(String technologyName);
    @Query(value="select * from technology where status = 1 order by technology_name",nativeQuery = true)
    List<Technology> findAllByStatus();

    @Query(value="select * from technology where status = 1 order by technology_name",nativeQuery = true)
    List<Technology> findByAllTechnologyName();
}
