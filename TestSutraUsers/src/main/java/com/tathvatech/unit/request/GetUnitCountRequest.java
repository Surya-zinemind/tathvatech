package com.tathvatech.unit.request;

import lombok.Data;

import java.util.List;

@Data
public class GetUnitCountRequest {
    private List<Integer> projectPks;
    private boolean includeChildren;
}
