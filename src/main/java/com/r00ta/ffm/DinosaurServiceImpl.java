package com.r00ta.ffm;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.r00ta.ffm.api.models.requests.DinosaurRequest;
import com.r00ta.ffm.api.models.responses.DinosaurResponse;
import com.r00ta.ffm.dao.DinosaurDAO;
import com.r00ta.ffm.infra.APIConstants;
import com.r00ta.ffm.infra.ListResult;
import com.r00ta.ffm.infra.QueryInfo;
import com.r00ta.ffm.infra.dto.DinosaurDTO;
import com.r00ta.ffm.infra.dto.DinosaurStatus;
import com.r00ta.ffm.infra.exceptions.definitions.user.AlreadyExistingItemException;
import com.r00ta.ffm.infra.exceptions.definitions.user.ItemNotFoundException;
import com.r00ta.ffm.models.Dinosaur;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DinosaurServiceImpl implements DinosaurService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DinosaurServiceImpl.class);

    @Inject
    DinosaurDAO dinosaurDAO;

    @Inject
    MeterRegistry meterRegistry;

    @Inject
    ShardService shardService;
    @Override
    public Dinosaur createDinosaur(String customerId, DinosaurRequest dinosaurRequest) {
        if (dinosaurDAO.findByNameAndCustomerId(dinosaurRequest.getName(), customerId) != null) {
            throw new AlreadyExistingItemException(String.format("Dinosaur with name '%s' already exists for customer with id '%s'", dinosaurRequest.getName(), customerId));
        }

        Dinosaur dinosaur = dinosaurRequest.toEntity();
        dinosaur.setStatus(DinosaurStatus.REQUESTED);
        dinosaur.setSubmittedAt(ZonedDateTime.now(ZoneOffset.UTC));
        dinosaur.setCustomerId(customerId);
        dinosaur.setShardId(shardService.getAssignedShardId(dinosaur.getId()));
        dinosaurDAO.persist(dinosaur);

        LOGGER.info("Dinosaur with id '{}' has been created for customer '{}'", dinosaur.getId(), dinosaur.getCustomerId());
        return dinosaur;    }

    @Override
    public Dinosaur getDinosaur(String id) {
        Dinosaur d = dinosaurDAO.findById(id);
        if (d == null) {
            throw new ItemNotFoundException(String.format("Dinosaur with id '%s' does not exist", id));
        }
        return d;
    }

    @Override
    public Dinosaur getDinosaur(String id, String customerId) {
        return findByIdAndCustomerId(id, customerId);
    }

    @Override
    public Dinosaur getAvailableDinosaur(String dinosaurId, String customerId) {
        return null;
    }

    @Override
    public void deleteDinosaur(String id, String customerId) {
        Dinosaur dinosaur = findByIdAndCustomerId(id, customerId);
        dinosaur.setStatus(DinosaurStatus.DELETION_REQUESTED);
        LOGGER.info("Bridge with id '{}' for customer '{}' has been marked for deletion", dinosaur.getId(), dinosaur.getCustomerId());
    }

    @Override
    public ListResult<Dinosaur> getDinosaurs(String customerId, QueryInfo queryInfo) {
        return dinosaurDAO.findByCustomerId(customerId, queryInfo);
    }

    @Override
    public List<Dinosaur> getDinosaursByStatusesAndShardId(List<DinosaurStatus> statuses, String shardId) {
        return dinosaurDAO.findByStatusesAndShardId(statuses, shardId);
    }

    @Override
    public Dinosaur updateDinosaur(DinosaurDTO dinosaurDTO) {
        Dinosaur bridge = getDinosaur(dinosaurDTO.getId(), dinosaurDTO.getCustomerId());
        bridge.setStatus(dinosaurDTO.getStatus());

        if (dinosaurDTO.getStatus().equals(DinosaurStatus.DELETED)) {
            dinosaurDAO.deleteById(bridge.getId());
        }

        // Update metrics
        meterRegistry.counter("manager.dinosaur.status.change",
                              Collections.singletonList(Tag.of("status", dinosaurDTO.getStatus().toString()))).increment();

        LOGGER.info("Dinosaur with id '{}' has been updated for customer '{}'", bridge.getId(), bridge.getCustomerId());
        return bridge;    }

    @Override
    public DinosaurDTO toDTO(Dinosaur dinosaur) {
        DinosaurDTO dto = new DinosaurDTO();
        dto.setId(dinosaur.getId());
        dto.setName(dinosaur.getName());
        dto.setStatus(dinosaur.getStatus());
        dto.setCustomerId(dinosaur.getCustomerId());
        return dto;
    }

    @Override
    public DinosaurResponse toResponse(Dinosaur dinosaur) {
        DinosaurResponse response = new DinosaurResponse();
        response.setId(dinosaur.getId());
        response.setName(dinosaur.getName());
        response.setEndpoint(dinosaur.getEndpoint());
        response.setSubmittedAt(dinosaur.getSubmittedAt());
        response.setPublishedAt(dinosaur.getPublishedAt());
        response.setStatus(dinosaur.getStatus());
        response.setHref(APIConstants.USER_API_BASE_PATH + dinosaur.getId());
        return response;    }

    private Dinosaur findByIdAndCustomerId(String id, String customerId) {
        Dinosaur dinosaur = dinosaurDAO.findByIdAndCustomerId(id, customerId);
        if (dinosaur == null) {
            throw new ItemNotFoundException(String.format("Bridge with id '%s' for customer '%s' does not exist", id, customerId));
        }
        return dinosaur;
    }
}
