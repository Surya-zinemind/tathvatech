package com.tathvatech.unit.request;

import com.tathvatech.unit.common.UnitBean;
import lombok.Data;

@Data
public class CreateUnitRequest {
    private int projectPk;
    private UnitBean unit;
    private boolean createAsPlannedUnit;
}
