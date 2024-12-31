package com.flowchartapp.flowchartapp.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowchartapp.flowchartapp.entity.Edge;
import com.flowchartapp.flowchartapp.entity.Flowchart;
import com.flowchartapp.flowchartapp.repository.FlowchartRepository;

@Service
public class FlowchartService {

    @Autowired
    private FlowchartRepository repository;

    // Create a new flowchart
    public Flowchart createFlowchart(Flowchart flowchart) {
        validateFlowchart(flowchart);
        return repository.save(flowchart);
    }

    // Fetch a flowchart by ID
    public Flowchart getFlowchartById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flowchart not found"));
    }

    // Update an existing flowchart
    public Flowchart updateFlowchart(Long id, Flowchart updatedFlowchart) {
        Flowchart existing = getFlowchartById(id);
        validateFlowchart(updatedFlowchart);
        existing.setNodes(updatedFlowchart.getNodes());
        existing.setEdges(updatedFlowchart.getEdges());
        return repository.save(existing);
    }

    // Delete a flowchart by ID
    public void deleteFlowchart(Long id) {
        repository.deleteById(id);
    }

    // Fetch all outgoing edges for a given node
    public List<Edge> getOutgoingEdges(Long id, String nodeId) {
        Flowchart flowchart = getFlowchartById(id);
        return flowchart.getEdges().stream()
                .filter(edge -> edge.getSource().equals(nodeId))
                .collect(Collectors.toList());
    }

    // Fetch all nodes connected to a specific node (directly or indirectly)
    public Set<String> getConnectedNodes(Long id, String nodeId) {
        Flowchart flowchart = getFlowchartById(id);
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(nodeId);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            flowchart.getEdges().stream()
                    .filter(edge -> edge.getSource().equals(current))
                    .forEach(edge -> queue.add(edge.getTarget()));
        }

        return visited;
    }

    // Validate the flowchart structure
    private void validateFlowchart(Flowchart flowchart) {
        Set<String> nodeSet = new HashSet<>(flowchart.getNodes());
        for (Edge edge : flowchart.getEdges()) {
            if (!nodeSet.contains(edge.getSource()) || !nodeSet.contains(edge.getTarget())) {
                throw new RuntimeException("Edge contains a non-existent node");
            }
        }
    }
}

