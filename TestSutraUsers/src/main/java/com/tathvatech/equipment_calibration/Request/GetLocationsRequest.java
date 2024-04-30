package com.tathvatech.equipment_calibration.Request;

import com.tathvatech.equipment_calibration.dao.LocationMasterDAO;
import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.user.OID.SiteOID;
import lombok.Data;

import java.util.List;

@Data
public class GetLocationsRequest {
    private List<SiteOID> siteOIDs;
    private List<LocationOID> parents;
    private LocationMasterDAO.DATALIST dataList;
    private String filterName;
}
