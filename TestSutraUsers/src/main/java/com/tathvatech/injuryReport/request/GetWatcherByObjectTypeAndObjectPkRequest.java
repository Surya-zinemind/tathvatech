package com.tathvatech.injuryReport.request;

import com.tathvatech.common.enums.EntityType;
import lombok.Data;

@Data
public class GetWatcherByObjectTypeAndObjectPkRequest {
    private int objectPk;
    private EntityType objectType;
}
