package com.flowchartapp.flowchartapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowchartapp.flowchartapp.entity.Edge;
import com.flowchartapp.flowchartapp.entity.Flowchart;
import com.flowchartapp.flowchartapp.service.FlowchartService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FlowchartappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlowchartService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Test: Create Flowchart
    @Test
    public void testCreateFlowchart_Success() throws Exception {
        Flowchart flowchart = new Flowchart();
        flowchart.setNodes(List.of("A", "B"));
        flowchart.setEdges(List.of(new Edge("A", "B")));

        when(service.createFlowchart(any(Flowchart.class))).thenReturn(flowchart);

        mockMvc.perform(post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flowchart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nodes").isArray())
                .andExpect(jsonPath("$.edges").isArray())
                .andExpect(jsonPath("$.nodes[0]").value("A"));
    }

    @Test
    public void testCreateFlowchart_InvalidData() throws Exception {
        Flowchart flowchart = new Flowchart(); // Missing required fields

        mockMvc.perform(post("/flowcharts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flowchart)))
                .andExpect(status().isBadRequest());
    }

    // Test: Get Flowchart by ID
    @Test
    public void testGetFlowchartById_Success() throws Exception {
        Flowchart flowchart = new Flowchart();
        flowchart.setId(1L);
        flowchart.setNodes(List.of("A", "B"));
        flowchart.setEdges(List.of(new Edge("A", "B")));

        when(service.getFlowchartById(1L)).thenReturn(flowchart);

        mockMvc.perform(get("/flowcharts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nodes[0]").value("A"));
    }

    @Test
    public void testGetFlowchartById_NotFound() throws Exception {
        when(service.getFlowchartById(1L)).thenThrow(new RuntimeException("Flowchart not found"));

        mockMvc.perform(get("/flowcharts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Flowchart not found"));
    }

    // Test: Update Flowchart
    @Test
    public void testUpdateFlowchart_Success() throws Exception {
        Flowchart updatedFlowchart = new Flowchart();
        updatedFlowchart.setId(1L);
        updatedFlowchart.setNodes(List.of("A", "B", "C"));
        updatedFlowchart.setEdges(List.of(new Edge("A", "B"), new Edge("B", "C")));

        when(service.updateFlowchart(eq(1L), any(Flowchart.class))).thenReturn(updatedFlowchart);

        mockMvc.perform(put("/flowcharts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFlowchart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nodes").isArray())
                .andExpect(jsonPath("$.nodes[2]").value("C"));
    }

    @Test
    public void testUpdateFlowchart_NotFound() throws Exception {
        Flowchart flowchart = new Flowchart();
        flowchart.setNodes(List.of("A", "B"));

        when(service.updateFlowchart(eq(1L), any(Flowchart.class))).thenThrow(new RuntimeException("Flowchart not found"));

        mockMvc.perform(put("/flowcharts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flowchart)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Flowchart not found"));
    }

    // Test: Delete Flowchart
    @Test
    public void testDeleteFlowchart_Success() throws Exception {
        doNothing().when(service).deleteFlowchart(1L);

        mockMvc.perform(delete("/flowcharts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFlowchart_NotFound() throws Exception {
        doThrow(new RuntimeException("Flowchart not found")).when(service).deleteFlowchart(1L);

        mockMvc.perform(delete("/flowcharts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Flowchart not found"));
    }

    // Test: Get Outgoing Edges
    @Test
    public void testGetOutgoingEdges_Success() throws Exception {
        List<Edge> outgoingEdges = List.of(new Edge("A", "B"), new Edge("A", "C"));

        when(service.getOutgoingEdges(1L, "A")).thenReturn(outgoingEdges);

        mockMvc.perform(get("/flowcharts/1/outgoing/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].source").value("A"))
                .andExpect(jsonPath("$[0].target").value("B"))
                .andExpect(jsonPath("$[1].target").value("C"));
    }

    @Test
    public void testGetOutgoingEdges_NodeNotFound() throws Exception {
        when(service.getOutgoingEdges(1L, "A")).thenThrow(new RuntimeException("Node not found"));

        mockMvc.perform(get("/flowcharts/1/outgoing/A"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Node not found"));
    }

    // Test: Get Connected Nodes
    @Test
    public void testGetConnectedNodes_Success() throws Exception {
        Set<String> connectedNodes = Set.of("A", "B", "C");

        when(service.getConnectedNodes(1L, "A")).thenReturn(connectedNodes);

        mockMvc.perform(get("/flowcharts/1/connected/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("A"));
    }

    @Test
    public void testGetConnectedNodes_NodeNotFound() throws Exception {
        when(service.getConnectedNodes(1L, "A")).thenThrow(new RuntimeException("Node not found"));

        mockMvc.perform(get("/flowcharts/1/connected/A"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Node not found"));
    }
}
