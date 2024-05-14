package com.tathvatech.equipmentcalibration.Request;

import com.tathvatech.equipmentcalibration.dao.LocationMasterDAO;
import com.tathvatech.equipmentcalibration.oid.LocationOID;
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
