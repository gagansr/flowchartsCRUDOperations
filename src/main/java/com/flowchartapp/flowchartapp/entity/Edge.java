package com.flowchartapp.flowchartapp.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
public class Edge {
    private String source;
    private String target;

    public Edge() {}

    public Edge(String source, String target) {
        this.source = source;
        this.target = target;
    }
}

