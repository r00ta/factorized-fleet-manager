package com.r00ta.ffm.infra.exceptions;

import com.r00ta.ffm.infra.ListResult;
import com.r00ta.ffm.infra.QueryInfo;

public interface DinosaurErrorDAO {

    ListResult<DinosaurError> findAllErrorsByType(QueryInfo queryInfo, DinosaurErrorType type);

    DinosaurError findErrorByIdAndType(int errorId, DinosaurErrorType type);

    DinosaurError findByException(Exception ex);

    DinosaurError findByException(Class clazz);
}
