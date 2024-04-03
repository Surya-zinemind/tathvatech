package com.tathvatech.unit.request;

import com.tathvatech.unit.common.UnitBean;
import lombok.Data;

@Data
public class CreateUnitsRequest {
   private int projectPk;
   private UnitBean unitBean;
   private boolean createAsPlannedUnit;
   private boolean pendingReview;
}
