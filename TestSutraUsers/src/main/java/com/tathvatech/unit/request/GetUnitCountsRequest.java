package com.tathvatech.unit.request;

import lombok.Data;

@Data
public class GetUnitCountsRequest {
    private int projectPk;
    private boolean includeChildren;
}
