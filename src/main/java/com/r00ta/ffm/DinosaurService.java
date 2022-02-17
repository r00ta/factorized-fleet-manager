package com.r00ta.ffm;

import java.util.List;

import com.r00ta.ffm.api.models.requests.DinosaurRequest;
import com.r00ta.ffm.api.models.responses.DinosaurResponse;
import com.r00ta.ffm.infra.ListResult;
import com.r00ta.ffm.infra.QueryInfo;
import com.r00ta.ffm.infra.dto.DinosaurDTO;
import com.r00ta.ffm.infra.dto.DinosaurStatus;
import com.r00ta.ffm.models.Dinosaur;

public interface DinosaurService {
    Dinosaur createDinosaur(String customerId, DinosaurRequest dinosaurRequest);

    Dinosaur getDinosaur(String id);

    Dinosaur getDinosaur(String id, String customerId);

    Dinosaur getAvailableDinosaur(String dinosaurId, String customerId);

    void deleteDinosaur(String id, String customerId);

    ListResult<Dinosaur> getDinosaurs(String customerId, QueryInfo queryInfo);

    List<Dinosaur> getDinosaursByStatusesAndShardId(List<DinosaurStatus> statuses, String shardId);

    Dinosaur updateDinosaur(DinosaurDTO dinosaurDTO);

    DinosaurDTO toDTO(Dinosaur dinosaur);

    DinosaurResponse toResponse(Dinosaur dinosaur);
}
