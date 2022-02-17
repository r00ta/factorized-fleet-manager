package com.r00ta.ffm.api.models.requests;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.r00ta.ffm.models.Dinosaur;

public class DinosaurRequest {

    @NotEmpty(message = "Bridge name cannot be null or empty")
    @JsonProperty("name")
    private String name;

    public DinosaurRequest() {
    }

    public DinosaurRequest(String name) {
        this.name = name;
    }

    public Dinosaur toEntity() {
        return new Dinosaur(name);
    }

    public String getName() {
        return name;
    }
}
