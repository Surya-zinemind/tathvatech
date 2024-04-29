package com.tathvatech.survey.common;

import com.tathvatech.common.enums.EntityType;

public class WorkItemEntityType implements EntityType {
    int value;

    String name;

    public WorkItemEntityType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public WorkItemEntityType() {
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }
}
