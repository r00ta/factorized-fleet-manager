package com.r00ta.ffm.infra.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DinosaurDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("status")
    private DinosaurStatus status;

    public DinosaurDTO() {
    }

    public DinosaurDTO(String id, String name, String customerId, DinosaurStatus status) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
        this.status = status;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setStatus(DinosaurStatus status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DinosaurDTO dinosaurDTO = (DinosaurDTO) o;
        return id.equals(dinosaurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DinosaurDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", status=" + status + '\'' +
                '}';
    }
}
