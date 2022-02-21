package com.r00ta.ffm.api.models.responses;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.r00ta.ffm.infra.dto.DinosaurStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DinosaurResponse extends BaseResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    @JsonProperty("submitted_at")
    private ZonedDateTime submittedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    @JsonProperty("published_at")
    private ZonedDateTime publishedAt;

    @JsonProperty("status")
    private DinosaurStatus status;

    @JsonProperty("endpoint")
    private String endpoint;

    public DinosaurResponse() {
        super("Dinosaur");
    }

    public ZonedDateTime getSubmittedAt() {
        return submittedAt;
    }

    public ZonedDateTime getPublishedAt() {
        return publishedAt;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setSubmittedAt(ZonedDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setPublishedAt(ZonedDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setStatus(DinosaurStatus status) {
        this.status = status;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
