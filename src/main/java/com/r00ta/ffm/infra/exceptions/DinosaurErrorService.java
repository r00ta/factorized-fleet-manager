package com.r00ta.ffm.infra.exceptions;

import java.util.Optional;

import com.r00ta.ffm.infra.ListResult;
import com.r00ta.ffm.infra.QueryInfo;

public interface DinosaurErrorService {

    ListResult<DinosaurError> getUserErrors(QueryInfo pageInfo);

    Optional<DinosaurError> getUserError(int errorId);

    Optional<DinosaurError> getError(Exception e);

    Optional<DinosaurError> getError(Class clazz);
}
