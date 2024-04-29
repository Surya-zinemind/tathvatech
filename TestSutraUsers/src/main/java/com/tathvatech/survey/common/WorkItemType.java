package com.tathvatech.survey.common;

import com.tathvatech.common.enums.WorkItem;

public class WorkItemType implements WorkItem {

    private long pk;
    private WorkItemEntityType entityType;

    public WorkItemType(long pk, WorkItemEntityType entityType) {
        this.pk = pk;
        this.entityType = entityType;
    }
    @Override
    public long getPk() {
        return pk;
    }

    @Override
    public WorkItemEntityType getEntityType() {
        return entityType;
    }
}
