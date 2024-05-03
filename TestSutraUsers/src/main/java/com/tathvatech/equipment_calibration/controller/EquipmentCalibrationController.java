package com.tathvatech.equipment_calibration.controller;
import com.tathvatech.equipment_calibration.Request.*;
import com.tathvatech.equipment_calibration.common.CalibrationSiteCurrencyBean;
import com.tathvatech.equipment_calibration.common.EquipmentBean;
import com.tathvatech.equipment_calibration.common.EquipmentCalibrationBean;
import com.tathvatech.equipment_calibration.common.LocationMasterBean;
import com.tathvatech.equipment_calibration.dao.LocationMasterDAO;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibrationAuthority;
import com.tathvatech.equipment_calibration.entity.EquipmentType;
import com.tathvatech.equipment_calibration.entity.LocationType;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.equipment_calibration.report.EquipmentListFilter;
import com.tathvatech.equipment_calibration.report.EquipmentListReportRow;
import com.tathvatech.equipment_calibration.service.EquipmentCalibrationService;

import com.tathvatech.project.controller.ProjectController;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.OID.SiteOID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/equipmentCalibration")
@RestController
public class EquipmentCalibrationController {
    private  final Logger logger = LoggerFactory.getLogger(EquipmentCalibrationController.class);
    private final EquipmentCalibrationService equipmentCalibrationService;

    public EquipmentCalibrationController(EquipmentCalibrationService equipmentCalibrationService) {
        this.equipmentCalibrationService = equipmentCalibrationService;
    }


    @Transactional
    @PostMapping("/saveCalibrationSiteCurrency")
    public  CalibrationSiteCurrencyBean saveCalibrationSiteCurrency(@RequestBody CalibrationSiteCurrencyBean calibrationSiteCurrencyBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            CalibrationSiteCurrencyBean bean =equipmentCalibrationService.saveCalibrationSiteCurrency(context,
                    calibrationSiteCurrencyBean);

            return bean;

    }

    @Transactional
    @DeleteMapping("/deleteCalibrationSiteCurrency/{pk}")
    public  void deleteCalibrationSiteCurrency(@PathVariable("pk") int pk) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           equipmentCalibrationService.deleteCalibrationSiteCurrency(context, pk);


    }

    @GetMapping("/getCalibrationSiteCurrencies")
    public  List<CalibrationSiteCurrencyBean> getCalibrationSiteCurrencies(@RequestBody List<SiteOID> siteOIDs)
    {
        return equipmentCalibrationService.getCalibrationSiteCurrencies(siteOIDs);
    }

    @GetMapping("/getCalibrationSiteCurrency")
    public  CalibrationSiteCurrencyBean getCalibrationSiteCurrency(@RequestBody SiteOID siteOID)
    {
        return equipmentCalibrationService.getCalibrationSiteCurrency(siteOID);
    }

    @GetMapping("/getCalibrationSiteCurrency/{pk}")
    public  CalibrationSiteCurrencyBean getCalibrationSiteCurrency(@PathVariable("pk") int pk)
    {
        return equipmentCalibrationService.getCalibrationSiteCurrency(pk);
    }

    /*
     * Currency master methods end
     */

    /*
     * Location master methods start
     */

  @PostMapping("/saveLocationMaster")
    public  LocationMasterBean saveLocationMaster(@RequestBody LocationMasterBean locationMasterBean)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LocationMasterBean bean =equipmentCalibrationService.saveLocationMaster(context, locationMasterBean);

            return bean;

    }

    @Transactional
    @DeleteMapping("/deleteLocation/{pk}")
    public  void deleteLocation(@PathVariable("pk") int pk) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            equipmentCalibrationService.deleteLocation(context, pk);


    }

    @GetMapping("/getLocations")
    public  List<LocationMasterBean> getLocations(@RequestBody GetLocationsRequest getLocationsRequest)
    {
        return equipmentCalibrationService.getLocations(getLocationsRequest.getSiteOIDs(), getLocationsRequest.getParents(), getLocationsRequest.getDataList(),getLocationsRequest.getFilterName());
    }

   @GetMapping("/getLocations/{searchString}")
   public  List<LocationMasterBean> getLocations(@PathVariable("searchString") String searchString)
    {
        return equipmentCalibrationService.getLocations(searchString);
    }

   @GetMapping("/getLocationsofTypes/{locationTypeFk}")
   public  List<LocationMasterBean> getLocationsofTypes(@PathVariable("locationTypeFk") int locationTypeFk)
    {
        return equipmentCalibrationService.getLocationsofTypes(locationTypeFk);
    }

   @GetMapping("/getLocation/{pk}")
   public  LocationMasterBean getLocation(@PathVariable("pk") int pk)
    {
        return equipmentCalibrationService.getLocation(pk);
    }

    /*
     * Location master methods end
     */


    @PostMapping("/saveLocationType")
    public  LocationType saveLocationType(@RequestBody LocationType locationType) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LocationType bean = equipmentCalibrationService.saveLocationType(context, locationType);

            return bean;

    }

    @DeleteMapping("/deleteLocationType/{pk}")
    public  void deleteLocationType(@PathVariable("pk") int pk) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            equipmentCalibrationService.deleteLocationType(context, pk);


    }

    @GetMapping("/getLocationType/{searchString}")
    public  List<LocationType> getLocationType(@PathVariable("searchString") String searchString)
    {
        return equipmentCalibrationService.getLocationTypes(searchString);
    }

    @GetMapping("/getLocationTypeByPk/{pk}")
    public  LocationType getLocationType(@PathVariable("pk") int pk)
    {
        return equipmentCalibrationService.getLocationType(pk);
    }

    @Transactional
    @PostMapping("/saveEquipmentType")
    public  EquipmentType saveEquipmentType(@RequestBody EquipmentType equipmentType) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EquipmentType bean =equipmentCalibrationService.saveEquipmentType(context, equipmentType);

            return bean;

    }

    @Transactional
    @DeleteMapping("/deleteEquipmentType")
    public  void deleteEquipmentType(@RequestBody EquipmentTypeOID equipmentTypeOID) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          equipmentCalibrationService.deleteEquipmentType(context, equipmentTypeOID);


    }

    @GetMapping("/getEquipmentTypes/{searchString}")
    public  List<EquipmentType> getEquipmentTypes(@PathVariable("searchString") String searchString)
    {
        return equipmentCalibrationService.getEquipmentTypes(searchString);
    }

    @GetMapping("/getEquipmentType")
    public  EquipmentType getEquipmentType(@RequestBody EquipmentTypeOID typeOID)
    {
        return equipmentCalibrationService.getEquipmentType(typeOID);
    }

    @Transactional
    @PostMapping("/saveEquipmentCalibrationAuthority")
    public  EquipmentCalibrationAuthority saveEquipmentCalibrationAuthority(@RequestBody EquipmentCalibrationAuthority equipmentCalibrationAuthority) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            EquipmentCalibrationAuthority bean = equipmentCalibrationService.saveEquipmentCalibrationAuthority(context,
                    equipmentCalibrationAuthority);

            return bean;

    }

    @Transactional
    @DeleteMapping("/deleteCalibrationAuthority/{pk}")
    public  void deleteCalibrationAuthority(@PathVariable("pk") int pk) throws Exception
    {UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            equipmentCalibrationService.deleteCalibrationAuthority(context, pk);


    }

    @GetMapping("/getCalibrationAuthorityList/{searchString}")
    public  List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(@PathVariable("searchString") String searchString)
    {
        return equipmentCalibrationService.getCalibrationAuthorityList(searchString);
    }

   @GetMapping("/getCalibrationAuthorityList")
   public  List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(@RequestBody SiteOID siteOID)
    {
        return equipmentCalibrationService.getCalibrationAuthorityList(siteOID);
    }

   @GetMapping("/getCalibrationAuthority/{pk}")
   public  EquipmentCalibrationAuthority getCalibrationAuthority(@PathVariable("pk") int pk)
    {
        return equipmentCalibrationService.getCalibrationAuthority(pk);
    }

    @GetMapping("/getEquipmentBeanByOID")
    public EquipmentBean getEquipmentBean(@RequestBody EquipmentOID equipmentOID) throws Exception
    {
        return equipmentCalibrationService.getEquipmentBean(equipmentOID);
    }

    @GetMapping("/getEquipmentBean/{equipmentSerialNo}")
    public  EquipmentBean getEquipmentBean(@PathVariable("equipmentSerialNo") String equipmentSerialNo)
    {
        return equipmentCalibrationService.getEquipmentBean(equipmentSerialNo);
    }

    @GetMapping("/getEquipmentCalibrationBeans")
    public  List<EquipmentCalibrationBean> getEquipmentCalibrationBeans(@RequestBody EquipmentOID equipmentOID)
            throws Exception
    {
        return equipmentCalibrationService.getEquipmentCalibrationBeans(equipmentOID);
    }

    @Transactional
    @PostMapping("/saveEquipment")
    public  EquipmentBean saveEquipment(@RequestBody EquipmentBean equipmentBean) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EquipmentBean bean = equipmentCalibrationService.saveEquipment(context, equipmentBean);

            return bean;

    }

    @Transactional
    @PostMapping("/saveEquipmentandAddCalibration")
    public  EquipmentBean saveEquipmentandAddCalibration( @RequestBody SaveEquipmentandAddCalibrationRequest saveEquipmentandAddCalibrationRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EquipmentBean bean =equipmentCalibrationService.saveEquipment(context,saveEquipmentandAddCalibrationRequest.getEquipmentBean());
if(saveEquipmentandAddCalibrationRequest.getCalibrationBean()!=null) {
    if (saveEquipmentandAddCalibrationRequest.getCalibrationBean().getPk() > 0) {
        EquipmentCalibrationBean ecBean = equipmentCalibrationService.updateCalibrationData(context, saveEquipmentandAddCalibrationRequest.getCalibrationBean());
    } else {
        EquipmentCalibrationBean ecBean = equipmentCalibrationService.saveCalibrationData(context, bean.getOID(),
                saveEquipmentandAddCalibrationRequest.getCalibrationBean());
    }
    // if (bean.getApprovedBy() != null)
    // {

    // } else
    // {
    // throw new AppException("Equipment is not in the approved
    // state.");
    // }

}
            return bean;

    }

    @DeleteMapping("/deleteCalibration/{pk}")
    public  void deleteCalibration(@PathVariable("pk") int pk) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           equipmentCalibrationService.deleteCalibration(context, pk);


    }

    @Transactional
    @GetMapping("/approveEquipment")
    public  EquipmentBean approveEquipment( @RequestBody ApproveEquipmentRequest approveEquipmentRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EquipmentBean bean =equipmentCalibrationService.approveEquipment(context,approveEquipmentRequest.getEquipmentOID(), approveEquipmentRequest.getMessage());

            return bean;

    }

    @GetMapping("/getEquipments")
    public  List<EquipmentListReportRow> getEquipments(@RequestBody EquipmentListFilter filter)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return equipmentCalibrationService.getEquipments(context, filter);
    }

    @GetMapping("/getPendingEquipmentsApprovalForCalibrationCoordinator")
    public  List<EquipmentListReportRow> getPendingEquipmentsApprovalForCalibrationCoordinator(
            ) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return equipmentCalibrationService.getPendingEquipmentsApprovalForCalibrationCoordinator(context);
    }

    @GetMapping("/getPendingEquipmentsApprovalForSiteAdmin")
    public  List<EquipmentListReportRow> getPendingEquipmentsApprovalForSiteAdmin()
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return equipmentCalibrationService.getPendingEquipmentsApprovalForSiteAdmin(context);
    }

    @GetMapping("/getEquipmentHistory")
    public  List<EquipmentBean> getEquipmentHistory(@RequestBody EquipmentOID equipmentOID) throws Exception
    {
        return equipmentCalibrationService.getEquipmentHistory(equipmentOID);
    }

    @GetMapping("/getEquipmentBean")
    public  EquipmentBean getEquipmentBean(@RequestBody GetEquipmentBeanRequest getEquipmentBeanRequest) throws Exception
    {
        return equipmentCalibrationService.getEquipmentBean(getEquipmentBeanRequest.getSiteOID(),getEquipmentBeanRequest.getEquipmentTypeOID(), getEquipmentBeanRequest.getSerialNumber());
    }

  @Transactional
  @DeleteMapping("/deleteEquipment")
   public  void deleteEquipment(@RequestBody EquipmentOID equipmentOID) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            equipmentCalibrationService.deleteEquipment(context, equipmentOID);



    }

    @GetMapping("/getEquipmentManufacturerList")
    public  List<String> getEquipmentManufacturerList()
    {
        return equipmentCalibrationService.getEquipmentManufacturerList();
    }


    @PutMapping("/saveCalibrationData")
    public  EquipmentCalibrationBean saveCalibrationData(@RequestBody SaveCalibrationDataRequest saveCalibrationDataRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EquipmentCalibrationBean bean = equipmentCalibrationService.saveCalibrationData(context, saveCalibrationDataRequest.getEquipmentOID(), saveCalibrationDataRequest.getCaliBean());

            return bean;

    }

   @Transactional
   @PutMapping("/updateCalibrationData")
    public  EquipmentCalibrationBean updateCalibrationData(@RequestBody EquipmentCalibrationBean caliBean)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            EquipmentCalibrationBean bean = equipmentCalibrationService.updateCalibrationData(context, caliBean);

            return bean;

    }

}