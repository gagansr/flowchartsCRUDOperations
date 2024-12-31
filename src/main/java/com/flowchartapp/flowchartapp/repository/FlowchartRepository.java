package com.flowchartapp.flowchartapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowchartapp.flowchartapp.entity.Flowchart;

@Repository
public interface FlowchartRepository extends JpaRepository<Flowchart, Long>  {
    
}
