package com.tathvatech.equipment_calibration.service;

import com.tathvatech.equipment_calibration.common.CalibrationSiteCurrencyBean;
import com.tathvatech.equipment_calibration.common.EquipmentBean;
import com.tathvatech.equipment_calibration.common.EquipmentCalibrationBean;
import com.tathvatech.equipment_calibration.common.LocationMasterBean;
import com.tathvatech.equipment_calibration.dao.LocationMasterDAO;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibrationAuthority;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibrationIntervalEnum;
import com.tathvatech.equipment_calibration.entity.EquipmentType;
import com.tathvatech.equipment_calibration.entity.LocationType;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.equipment_calibration.report.EquipmentListFilter;
import com.tathvatech.equipment_calibration.report.EquipmentListReportRow;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.common.UserContext;

import java.util.List;

public interface EquipmentCalibrationService {
     CalibrationSiteCurrencyBean saveCalibrationSiteCurrency(UserContext context,
                                                                   CalibrationSiteCurrencyBean bean) throws Exception;
      void deleteCalibrationSiteCurrency(UserContext context, int pk) throws Exception;
     List<CalibrationSiteCurrencyBean> getCalibrationSiteCurrencies(List<SiteOID> siteOIDs);
      CalibrationSiteCurrencyBean getCalibrationSiteCurrency(SiteOID siteOID);
     CalibrationSiteCurrencyBean getCalibrationSiteCurrency(int pk);
     LocationMasterBean saveLocationMaster(UserContext context, LocationMasterBean bean) throws Exception;
      void deleteLocation(UserContext context, int pk) throws Exception;
      List<LocationMasterBean> getLocations(List<SiteOID> siteOIDs, List<LocationOID> parents,
                                                  LocationMasterDAO.DATALIST dataList, String filterName);
     List<LocationMasterBean> getLocationsofTypes(int locationTypeFk);
      List<LocationMasterBean> getLocations(String searchString);
     LocationMasterBean getLocation(int pk);
    LocationType saveLocationType(UserContext context, LocationType locationType) throws Exception;
     void deleteLocationType(UserContext context, int pk) throws Exception;
     List<LocationType> getLocationTypes(String searchString);
     LocationType getLocationType(int pk);
    EquipmentType saveEquipmentType(UserContext context, EquipmentType equipmentType) throws Exception;
      void deleteEquipmentType(UserContext context, EquipmentTypeOID equipmentTypeOID) throws Exception;
      List<EquipmentType> getEquipmentTypes(String searchString);
      EquipmentType getEquipmentTypeswithName(String name);
     EquipmentType getEquipmentType(EquipmentTypeOID typeOID);
     EquipmentCalibrationAuthority saveEquipmentCalibrationAuthority(UserContext context,
                                                                           EquipmentCalibrationAuthority equipmentCalibrationAuthority) throws Exception;
      void deleteCalibrationAuthority(UserContext context, int pk) throws Exception;
     List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(String searchString);
      List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(SiteOID siteOID);
     EquipmentCalibrationAuthority getCalibrationAuthority(int pk);
     List<EquipmentBean> getEquipmentHistory(EquipmentOID equipmentOID);
     EquipmentBean getEquipmentBean(String equipmentSerialNo);
      EquipmentBean getEquipmentBean(EquipmentOID equipmentOID) throws Exception;
     List<EquipmentCalibrationBean> getEquipmentCalibrationBeans(EquipmentOID equipmentOID);
      EquipmentCalibrationBean getEquipmentCalibrationBean(int pk);

     EquipmentCalibrationBean getEquipmentCalibrationBean(EquipmentOID equipmentOID);
     EquipmentBean saveEquipment(UserContext context, EquipmentBean eBean) throws Exception;
     EquipmentBean getEquipmentwithReference(String reference) throws Exception;
      EquipmentBean getEquipmentwithEquipmentId(String equipmentId) throws Exception;
      EquipmentBean getEquipmentBean(SiteOID siteOID, EquipmentTypeOID equipmentTypeOID,
                                           String serialNumber) throws Exception;
      List<EquipmentListReportRow> getEquipments(UserContext context, EquipmentListFilter filter)
            throws Exception;
      List<EquipmentListReportRow> getPendingEquipmentsApprovalForCalibrationCoordinator(
            UserContext context) throws Exception;
     List<EquipmentListReportRow> getPendingEquipmentsApprovalForSiteAdmin(UserContext context)
            throws Exception;
     EquipmentBean approveEquipment(UserContext context, EquipmentOID equipmentOID, String comment)
            throws Exception;
     void deleteEquipment(UserContext context, EquipmentOID equipmentOID) throws Exception;
      void updateNextCaliberationDate(EquipmentOID equipmentOID,
                                             EquipmentCalibrationIntervalEnum calibrationInterval) throws Exception ;
      EquipmentCalibrationBean saveCalibrationData(UserContext context, EquipmentOID equipmentOID,
                                                         EquipmentCalibrationBean caliBean) throws Exception;
     EquipmentCalibrationBean updateCalibrationData(UserContext context, EquipmentCalibrationBean caliBean)
            throws Exception;
     void deleteCalibration(UserContext context, int pk) throws Exception;
      List<String> getEquipmentManufacturerList();
     EquipmentCalibrationAuthority getCalibrationAuthority(String name, SiteOID siteoid);

}
