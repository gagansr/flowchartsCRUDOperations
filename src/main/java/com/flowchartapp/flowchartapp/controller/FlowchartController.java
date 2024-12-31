package com.flowchartapp.flowchartapp.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowchartapp.flowchartapp.entity.Edge;
import com.flowchartapp.flowchartapp.entity.Flowchart;
import com.flowchartapp.flowchartapp.service.FlowchartService;

@RestController
@RequestMapping("/flowcharts")
public class FlowchartController {

    @Autowired
    private FlowchartService service;

    @PostMapping
    public ResponseEntity<Flowchart> createFlowchart(@RequestBody Flowchart flowchart) {
        return ResponseEntity.ok(service.createFlowchart(flowchart));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flowchart> getFlowchart(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFlowchartById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flowchart> updateFlowchart(@PathVariable Long id, @RequestBody Flowchart flowchart) {
        return ResponseEntity.ok(service.updateFlowchart(id, flowchart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowchart(@PathVariable Long id) {
        service.deleteFlowchart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/outgoing/{nodeId}")
    public ResponseEntity<List<Edge>> getOutgoingEdges(@PathVariable Long id, @PathVariable String nodeId) {
        return ResponseEntity.ok(service.getOutgoingEdges(id, nodeId));
    }

    @GetMapping("/{id}/connected/{nodeId}")
    public ResponseEntity<Set<String>> getConnectedNodes(@PathVariable Long id, @PathVariable String nodeId) {
        return ResponseEntity.ok(service.getConnectedNodes(id, nodeId));
    }
}

