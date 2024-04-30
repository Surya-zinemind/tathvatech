package com.tathvatech.equipment_calibration.service;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.user.enums.DBEnum;
import com.tathvatech.equipment_calibration.report.ReportsManager;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.equipment_calibration.enums.CalibrationStatusEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipment_calibration.common.*;
import com.tathvatech.equipment_calibration.report.EquipmentListFilter;
import com.tathvatech.equipment_calibration.report.EquipmentListReportRow;
import com.tathvatech.report.enums.ReportTypes;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.equipment_calibration.enums.EquipmentStatusEnum;
import com.tathvatech.equipment_calibration.dao.EquipmentDAO;
import com.tathvatech.equipment_calibration.entity.*;
import com.tathvatech.equipment_calibration.dao.LocationMasterDAO;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.equipment_calibration.dao.CalibrationSiteCurrencyDAO;

import java.util.*;

import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.repository.UserRepository;
import com.tathvatech.user.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;




@Service
public class EquipmentCalibrationServiceImpl implements EquipmentCalibrationService {
    private final CalibrationSiteCurrencyDAO calibrationSiteCurrencyDAO;
    private  UserRepository userRepository;
    private final LocationMasterDAO locationMasterDAO;
    private final EquipmentDAO equipmentDAO;
    private final ReportsManager reportsManager;
    private  DBEnum dbEnum;
    private AuthorizationManager authorizationManager;
    private final AccountService accountService;
    private final SiteService siteService;
    private EquipmentEmailSender equipmentEmailSender;

    private final PersistWrapper persistWrapper;

    private final CommonServiceManager commonServiceManager;

    public EquipmentCalibrationServiceImpl(CalibrationSiteCurrencyDAO calibrationSiteCurrencyDAO, LocationMasterDAO locationMasterDAO, EquipmentDAO equipmentDAO, ReportsManager reportsManager, AccountService accountService, SiteService siteService, PersistWrapper persistWrapper, CommonServiceManager commonServiceManager) {
        this.calibrationSiteCurrencyDAO = calibrationSiteCurrencyDAO;

        this.locationMasterDAO = locationMasterDAO;
        this.equipmentDAO = equipmentDAO;
        this.reportsManager = reportsManager;
        this.accountService = accountService;
        this.siteService = siteService;
        this.persistWrapper = persistWrapper;
        this.commonServiceManager = commonServiceManager;
    }


    public  CalibrationSiteCurrencyBean saveCalibrationSiteCurrency(UserContext context,
                                                                          CalibrationSiteCurrencyBean bean) throws Exception
    {

        CalibrationSiteCurrency item = new CalibrationSiteCurrency();
        item.setPk(bean.getPk());
        item.setSiteFk((int) bean.getSiteOID().getPk());
        item.setCurrency(bean.getCurrency());
        item.setAbbreviation(bean.getAbbreviation());
        item.setEstatus(bean.getEstatus());
        item.setCreatedBy(Math.toIntExact(bean.getCreatedBy() != null ? bean.getCreatedBy().getPk() : null));
        item.setCreatedDate(bean.getCreatedDate());
        item.setLastUpdated(bean.getLastUpdated());
        item = calibrationSiteCurrencyDAO.save(context, item);
        return getCalibrationSiteCurrency((int) item.getPk());
    }

    public  void deleteCalibrationSiteCurrency(UserContext context, int pk) throws Exception
    {
        if (pk > 0)
        {
           calibrationSiteCurrencyDAO.delete(context, pk);
        }
    }

    public  List<CalibrationSiteCurrencyBean> getCalibrationSiteCurrencies(List<SiteOID> siteOIDs)
    {

        List<CalibrationSiteCurrency> currencies =calibrationSiteCurrencyDAO.geCurrencies(siteOIDs);
        if (currencies != null)
        {
            return currencies.stream().map(currency -> {
                CalibrationSiteCurrencyBean bean = new CalibrationSiteCurrencyBean();
                User user = accountService.getUser(currency.getCreatedBy());
                bean.setCreatedBy(user.getOID());
                bean.setCreatedDate(currency.getCreatedDate());
                bean.setCurrency(currency.getCurrency());
                bean.setAbbreviation(currency.getAbbreviation());
                bean.setEstatus(currency.getEstatus());
                bean.setLastUpdated(currency.getLastUpdated());
                bean.setPk((int) currency.getPk());
                Site site = siteService.getSite(currency.getSiteFk());
                bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
                return bean;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();

    }

    public  CalibrationSiteCurrencyBean getCalibrationSiteCurrency(SiteOID siteOID)
    {
        CalibrationSiteCurrencyBean bean = new CalibrationSiteCurrencyBean();
        CalibrationSiteCurrency currency = calibrationSiteCurrencyDAO.getCurrency(siteOID);
        if (currency != null)
        {

            User user =accountService.getUser(currency.getCreatedBy());
            bean.setCreatedBy(user.getOID());
            bean.setCreatedDate(currency.getCreatedDate());
            bean.setCurrency(currency.getCurrency());
            bean.setAbbreviation(currency.getAbbreviation());
            bean.setEstatus(currency.getEstatus());
            bean.setLastUpdated(currency.getLastUpdated());
            bean.setPk((int) currency.getPk());
            Site site = siteService.getSite(currency.getSiteFk());
            bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
        }
        return bean;

    }

    public  CalibrationSiteCurrencyBean getCalibrationSiteCurrency(int pk)
    {
        CalibrationSiteCurrencyBean bean = new CalibrationSiteCurrencyBean();
        CalibrationSiteCurrency currency = calibrationSiteCurrencyDAO.get(pk);
        if (currency != null)
        {

            User user = accountService.getUser(currency.getCreatedBy());
            bean.setCreatedBy(user.getOID());
            bean.setCreatedDate(currency.getCreatedDate());
            bean.setCurrency(currency.getCurrency());
            bean.setAbbreviation(currency.getAbbreviation());
            bean.setEstatus(currency.getEstatus());
            bean.setLastUpdated(currency.getLastUpdated());
            bean.setPk((int) currency.getPk());
            Site site = siteService.getSite(currency.getSiteFk());
            bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
        }
        return bean;

    }

    /*
     * locations methods end
     */

    /*
     * locations methods start
     */

    @Transactional
    public  LocationMasterBean saveLocationMaster(UserContext context, LocationMasterBean bean) throws Exception
    {

        LocationMaster item = new LocationMaster();
        item.setPk(bean.getPk());
        if(bean.getSite()!=null) {
            item.setSiteFk((int) bean.getSite().getPk());
            item.setLocationTypeFk((int) bean.getLocationType().getPk());
        }
        if (bean.getParentOID() != null)
        {
            item.setParentPk((int) bean.getParentOID().getPk());
        }
        if(bean.getName()!=null) {
            item.setName(bean.getName());
        }
        item.setDescription(bean.getDescription());
        item.setEstatus(bean.getEstatus());
        item.setCreatedBy(Math.toIntExact(bean.getCreatedBy() != null ? bean.getCreatedBy().getPk() : null));
        item.setCreatedDate(bean.getCreatedDate());
        item.setLastUpdated(bean.getLastUpdated());

        item = locationMasterDAO.save(context, item);
        // int pk;
        // if (bean.getPk() > 0)
        // {
        // PersistWrapper.update(context, item);
        // pk = bean.getPk();
        // } else
        // {
        // pk = PersistWrapper.createEntity(item);
        // }
        return getLocation((int) item.getPk());
    }

    public  void deleteLocation(UserContext context, int pk) throws Exception
    {
        if (pk > 0)
        {
            locationMasterDAO.delete(context, pk);
        }
    }


    public  List<LocationMasterBean> getLocations(List<SiteOID> siteOIDs, List<LocationOID> parents,
                                                        LocationMasterDAO.DATALIST dataList, String filterName)
    {

        List<LocationMaster> locations = locationMasterDAO.gelocations(siteOIDs, parents, dataList, filterName);
        if (locations != null)
        {
            return locations.stream().map(locationMaster -> {
                LocationMasterBean bean = new LocationMasterBean();
                User user = accountService.getUser(locationMaster.getCreatedBy());
                bean.setCreatedBy(user.getOID());
                bean.setCreatedDate(locationMaster.getCreatedDate());
                bean.setDescription(locationMaster.getDescription());
                bean.setEstatus(locationMaster.getEstatus());
                bean.setLastUpdated(locationMaster.getLastUpdated());
                bean.setName(locationMaster.getName());
                if (locationMaster.getParentPk() != null)
                {
                    LocationMaster parentLocation =locationMasterDAO.get(locationMaster.getParentPk());
                    bean.setParentOID(new LocationOID((int) parentLocation.getPk(), parentLocation.getName()));
                }
                bean.setPk((int) locationMaster.getPk());
                Site site = siteService.getSite(locationMaster.getSiteFk());
                if(site!=null) {
                    bean.setSite(site);
                }
                bean.setLocationType((LocationType) getLocationType(locationMaster.getLocationTypeFk()));
                return bean;
            }).collect(Collectors.toList());
        }
        return new ArrayList<LocationMasterBean>();

    }

    public  List<LocationMasterBean> getLocationsofTypes(int locationTypeFk)
    {

        List<LocationMaster> locations =locationMasterDAO.gelocationsofType(locationTypeFk);
        if (locations != null)
        {
            return locations.stream().map(locationMaster -> {
                LocationMasterBean bean = new LocationMasterBean();
                User user = accountService.getUser(locationMaster.getCreatedBy());
                bean.setCreatedBy(user.getOID());
                bean.setCreatedDate(locationMaster.getCreatedDate());
                bean.setDescription(locationMaster.getDescription());
                bean.setEstatus(locationMaster.getEstatus());
                bean.setLastUpdated(locationMaster.getLastUpdated());
                bean.setName(locationMaster.getName());
                if (locationMaster.getParentPk() != null)
                {
                    LocationMaster parentLocation =locationMasterDAO.get(locationMaster.getParentPk());
                    bean.setParentOID(new LocationOID((int) parentLocation.getPk(), parentLocation.getName()));
                }
                bean.setPk((int) locationMaster.getPk());
                Site site = siteService.getSite(locationMaster.getSiteFk());
                if(site!=null) {
                    bean.setSite(site);
                }
                bean.setLocationType(getLocationType(locationMaster.getLocationTypeFk()));
                return bean;
            }).collect(Collectors.toList());
        }
        return new ArrayList<LocationMasterBean>();

    }

    public  List<LocationMasterBean> getLocations(String searchString)
    {

        List<LocationMaster> locations =locationMasterDAO.gelocations(searchString);
        if (locations != null)
        {
            return locations.stream().map(locationMaster -> {
                LocationMasterBean bean = new LocationMasterBean();
                User user =accountService.getUser(locationMaster.getCreatedBy());
                bean.setCreatedBy(user.getOID());
                bean.setCreatedDate(locationMaster.getCreatedDate());
                bean.setDescription(locationMaster.getDescription());
                bean.setEstatus(locationMaster.getEstatus());
                bean.setLastUpdated(locationMaster.getLastUpdated());
                bean.setName(locationMaster.getName());
                if (locationMaster.getParentPk() != null)
                {
                    LocationMaster parentLocation = locationMasterDAO.get(locationMaster.getParentPk());
                    bean.setParentOID(new LocationOID((int) parentLocation.getPk(), parentLocation.getName()));
                }
                bean.setPk((int) locationMaster.getPk());
                Site site = siteService.getSite(locationMaster.getSiteFk());
                if(site!=null) {
                    bean.setSite(site);
                }
                bean.setLocationType(getLocationType(locationMaster.getLocationTypeFk()));
                return bean;
            }).collect(Collectors.toList());
        }
        return new ArrayList<LocationMasterBean>();

    }

    public  LocationMasterBean getLocation(int pk)
    {
        LocationMaster locationMaster = locationMasterDAO.get(pk);
        LocationMasterBean bean = new LocationMasterBean();
        if (locationMaster != null)
        {
            User user = accountService.getUser(locationMaster.getCreatedBy());
            bean.setCreatedBy(user.getOID());
            bean.setCreatedDate(locationMaster.getCreatedDate());
            bean.setDescription(locationMaster.getDescription());
            bean.setEstatus(locationMaster.getEstatus());
            bean.setLastUpdated(locationMaster.getLastUpdated());
            bean.setName(locationMaster.getName());
            if (locationMaster.getParentPk() != null)
            {
                LocationMaster parentLocation = locationMasterDAO.get(locationMaster.getParentPk());
                bean.setParentOID(new LocationOID((int) parentLocation.getPk(), parentLocation.getName()));
            }
            bean.setPk((int) locationMaster.getPk());
            Site site = siteService.getSite(locationMaster.getSiteFk());
            if(site!=null) {
                bean.setSite(site);
            }
            bean.setLocationType(getLocationType(locationMaster.getLocationTypeFk()));
        }
        return bean;
    }

    /*
     * locations methods end
     */

    /*
     * Location type methods start
     */
    public  LocationType saveLocationType(UserContext context, LocationType locationType) throws Exception
    {
        LocationType item;

        if (locationType.getPk() > 0)
        {
            item = (LocationType) persistWrapper.readByPrimaryKey(LocationType.class, locationType.getPk());
        } else {
            item = new LocationType();
            if (item!= null) {
                item.setCreatedBy((int) context.getUser().getPk());
                item.setCreatedDate(new Date());
                item.setEstatus(EStatusEnum.Active.getValue());
            }
        }
        if(item!=null) {
            item.setName(locationType.getName());
            item.setDescription(locationType.getDescription());
        }
        int pk = 0;
        if (locationType.getPk() > 0) {
            if (item != null) {
                persistWrapper.update(item);
                pk = (int) locationType.getPk();
            } else {
                if(item!=null) {
                    pk = (int) persistWrapper.createEntity(item);
                }
            }
            return getLocationType(pk);
        }
        return item;
    }

    public  void deleteLocationType(UserContext context, int pk) throws Exception
    {

        LocationType item = null;
        
            if (pk > 0) {
                if (item != null) {
                    item = (LocationType) persistWrapper.readByPrimaryKey(LocationType.class, pk);
                    item.setEstatus(EStatusEnum.Deleted.getValue());
                    persistWrapper.update(item);

                }
            }
        
    }

    public  List<LocationType> getLocationTypes(String searchString)
    {
        List<Object> params = new ArrayList();
        String sql = "select *  from location_type where estatus = 1 ";
        if (searchString != null && searchString.trim().length() > 0)
        {
            sql = sql + " and LOWER(name) like lower(?) ";
            params.add("%" + searchString + "%");
        }

        return persistWrapper.readList(LocationType.class, sql.toString() + " order by name asc", params.toArray());
    }

    public  LocationType getLocationType(int pk)
    {
        try {
            return persistWrapper.read(LocationType.class, "select *  from location_type where pk = ? ", pk);
        }catch (Exception e){

        }
        return null;
    }
    /*
     * Location Type end
     */

    @Transactional
    public  EquipmentType saveEquipmentType(UserContext context, EquipmentType equipmentType) throws Exception
    {

        EquipmentType item;
        if (equipmentType.getPk() > 0)
        {
            item = (EquipmentType) persistWrapper.readByPrimaryKey(EquipmentType.class, equipmentType.getPk());
        } else
        {
            item = new EquipmentType();
            item.setCreatedBy((int) context.getUser().getPk());
            item.setCreatedDate(new Date());
            item.setEstatus(EStatusEnum.Active.getValue());
        }
        // /*
        // * abbreviation length validation
        // */
        // if (equipmentType.getAbbreviation().length() != 3 &&
        // equipmentType.getAbbreviation().length() != 2)
        // {
        // throw new AppException("Abbreviation must contain 2 or 3 letters.");
        // }
        //
        // /*
        // * Duplication equipment abbreviation
        // */
        // EquipmentType duplicateab =
        // getEquipmentTypeswithAbbreviation(equipmentType.getAbbreviation());
        // if (duplicateab != null && duplicateab.getPk() != item.getPk())
        // {
        // throw new AppException(" Duplicate abbreviation.");
        // }
        // item.setAbbreviation(equipmentType.getAbbreviation());
        // /*
        // * Duplication equipment name check
        // */
        EquipmentType duplicate = getEquipmentTypeswithName(equipmentType.getName());
        if (duplicate != null && duplicate.getPk() != item.getPk())
        {
            throw new AppException(" Duplicate equipment type.");
        }
        item.setName(equipmentType.getName());
        item.setDescription(equipmentType.getDescription());
        int pk;
        if (equipmentType.getPk() > 0)
        {
            persistWrapper.update( item);
            pk = (int) equipmentType.getPk();
        } else
        {
            pk = (int) persistWrapper.createEntity(item);
        }
        return getEquipmentType(new EquipmentTypeOID(pk));
    }

    public  void deleteEquipmentType(UserContext context, EquipmentTypeOID equipmentTypeOID) throws Exception
    {

        EquipmentType item;
        if (equipmentTypeOID != null && equipmentTypeOID.getPk() > 0)
        {
            item = (EquipmentType) persistWrapper.readByPrimaryKey(EquipmentType.class, equipmentTypeOID.getPk());
            item.setEstatus(EStatusEnum.Deleted.getValue());
            persistWrapper.update( item);

        }
    }

    public  List<EquipmentType> getEquipmentTypes(String searchString)
    {
        List<Object> params = new ArrayList();
        String sql = "select *  from equipment_type where estatus = 1 ";
        if (searchString != null && searchString.trim().length() > 0)
        {
            sql = sql + " and LOWER(name) like lower(?) ";
            params.add("%" + searchString + "%");
        }

        return persistWrapper.readList(EquipmentType.class, sql.toString() + " order by name asc", params.toArray());
    }

    // public static EquipmentType getEquipmentTypeswithAbbreviation(String
    // abbreviation)
    // {
    // return PersistWrapper.read(EquipmentType.class,
    // "select * from equipment_type where estatus = 1 and LOWER(abbreviation) =
    // lower(?) order by pk desc",
    // abbreviation);
    // }

    public  EquipmentType getEquipmentTypeswithName(String name)
    {
        try {
            return persistWrapper.read(EquipmentType.class,
                    "select *  from equipment_type where estatus = 1 and LOWER(name) = lower(?) order by pk desc", name);
        }catch (Exception e){

        }
        return null;
    }

    public  EquipmentType getEquipmentType(EquipmentTypeOID typeOID)
    {
        return persistWrapper.read(EquipmentType.class, "select *  from equipment_type where pk = ? ", typeOID.getPk());
    }

    public  EquipmentCalibrationAuthority saveEquipmentCalibrationAuthority(UserContext context,
                                                                                  EquipmentCalibrationAuthority equipmentCalibrationAuthority) throws Exception
    {

        EquipmentCalibrationAuthority item;
        if (equipmentCalibrationAuthority.getPk() > 0)
        {
            item = (EquipmentCalibrationAuthority) persistWrapper.readByPrimaryKey(EquipmentCalibrationAuthority.class,
                    equipmentCalibrationAuthority.getPk());
        } else
        {
            item = new EquipmentCalibrationAuthority();
            item.setCreatedBy((int) context.getUser().getPk());
            item.setCreatedDate(new Date());
            item.setEstatus(EStatusEnum.Active.getValue());
        }
        if (equipmentCalibrationAuthority.getName() != null
                && (equipmentCalibrationAuthority.getName().trim().length() > 0))
        {
            EquipmentCalibrationAuthority duplicate = getCalibrationAuthority(equipmentCalibrationAuthority.getName(),
                    new SiteOID(equipmentCalibrationAuthority.getSiteFk()));
            if (duplicate != null && duplicate.getPk() != equipmentCalibrationAuthority.getPk())
            {
                throw new AppException(" This Calibration Authority already exists");
            }
        }
        item.setName(equipmentCalibrationAuthority.getName());
        item.setAddressLine1(equipmentCalibrationAuthority.getAddressLine1());
        item.setAddressLine2(equipmentCalibrationAuthority.getAddressLine2());
        item.setCity(equipmentCalibrationAuthority.getCity());
        item.setCountry(equipmentCalibrationAuthority.getCity());
        item.setEmail(equipmentCalibrationAuthority.getEmail());
        item.setFax(equipmentCalibrationAuthority.getFax());
        item.setPhone(equipmentCalibrationAuthority.getPhone());
        item.setPostalCode(equipmentCalibrationAuthority.getPostalCode());
        item.setState(equipmentCalibrationAuthority.getState());
        item.setSiteFk(equipmentCalibrationAuthority.getSiteFk()!= 0? equipmentCalibrationAuthority.getSiteFk():null);
        int pk;
        if (equipmentCalibrationAuthority.getPk() > 0)
        {
            persistWrapper.update(item);
            pk = (int) equipmentCalibrationAuthority.getPk();
        } else
        {
            pk = Math.toIntExact(persistWrapper.createEntity(item));
        }
        return getCalibrationAuthority(pk);
    }

    public  void deleteCalibrationAuthority(UserContext context, int pk) throws Exception
    {

        EquipmentCalibrationAuthority item;
        if (pk > 0)
        {
            item = (EquipmentCalibrationAuthority) persistWrapper.readByPrimaryKey(EquipmentCalibrationAuthority.class, pk);
            item.setEstatus(EStatusEnum.Deleted.getValue());
            persistWrapper.update( item);
        }
    }

    public List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(String searchString)
    {
        List<Object> params = new ArrayList();
        String sql = "select *  from equipment_calibration_authority where estatus = 1 ";
        if (searchString != null && searchString.trim().length() > 0)
        {
            sql = sql + " and LOWER(equipment_calibration_authority.name) like lower(?) ";
            params.add("%" + searchString + "%");
        }

        return persistWrapper.readList(EquipmentCalibrationAuthority.class,
                sql.toString() + " order by equipment_calibration_authority.name asc", params.toArray());
    }

    public  List<EquipmentCalibrationAuthority> getCalibrationAuthorityList(SiteOID siteOID)
    {
        List<Object> params = new ArrayList();
        String sql = "select *  from equipment_calibration_authority where estatus = 1 ";
        if (siteOID != null && siteOID.getPk() > 0)
        {
            sql = sql + " and equipment_calibration_authority.siteFk=? ";
            params.add(siteOID.getPk());
        }

        return persistWrapper.readList(EquipmentCalibrationAuthority.class, sql.toString() + " order by pk desc",
                params.toArray());
    }

    public  EquipmentCalibrationAuthority getCalibrationAuthority(int pk)
    {
        return persistWrapper.read(EquipmentCalibrationAuthority.class,
                "select *  from equipment_calibration_authority where pk = ? ", pk);
    }

    public  List<EquipmentBean> getEquipmentHistory(EquipmentOID equipmentOID)
    {

        List<EquipmentObj> objList =equipmentDAO .getEquipmentHistory(equipmentOID);
        return objList.stream().map(obj -> {
            EquipmentBean bean = new EquipmentBean();

            bean.setPk(obj.getPk());
            bean.setDateOfPurchase(obj.getDateOfPurchase());
            bean.setEquipmentId(obj.getEquipmentId());
            bean.setDescription(obj.getDescription());
            bean.setInstruction(obj.getInstruction());
            bean.setEquipmentTypeOID((obj.getEquipmentTypeFk() != null && obj.getEquipmentTypeFk() > 0)
                    ? getEquipmentType(new EquipmentTypeOID(obj.getEquipmentTypeFk())).getOID()
                    : null);
            bean.setManufacturer(obj.getManufacturer());
            bean.setModelNo(obj.getModelNo());
            bean.setCost(obj.getCost());
            bean.setSerialNo(obj.getSerialNo());
            bean.setReference(obj.getReference());

            if (obj.getAuthorityType() != null && EquipmentBean.AUTHORITY_TYPE.External.getIntValue() == obj.getAuthorityType())
            {
                if (obj.getCalibrationAuthorityFk() != null && obj.getCalibrationAuthorityFk() > 0)
                {
                    bean.setCalibrationAuthority(getCalibrationAuthority(obj.getCalibrationAuthorityFk()));
                }
                bean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.External);
            }
            if (obj.getAuthorityType() != null && EquipmentBean.AUTHORITY_TYPE.Internal.getIntValue() == obj.getAuthorityType())

            {
                bean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.Internal);
            }

            if (obj.getCalibrationIntervalFk() != null)
                if(dbEnum!=null) {
                    bean.setCalibrationInterval((EquipmentCalibrationIntervalEnum) dbEnum
                            .getEnum(EquipmentCalibrationIntervalEnum.class, String.valueOf(obj.getCalibrationIntervalFk())));
                }
            else{

                }
            if (obj.getCustodianFk() != null)
               // bean.setCustodianOID(UserRepository.getInstance().getUser(obj.getCustodianFk()).getOID());
            if (obj.getStatus() != null)
                bean.setStatus(EquipmentStatusEnum.valueOf(obj.getStatus()));
            bean.setStatusUpdatedDate(obj.getStatusUpdatedDate());
            bean.setLastUpdated(obj.getLastUpdated());

            Site site = siteService.getSite(obj.getSiteFk());
            bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
            if (obj.getLocationFk() != null && obj.getLocationFk() > 0)
            {
                LocationMasterBean location = getLocation(obj.getLocationFk());
                bean.setLocationOID(location.getOID());
            }
            User user =accountService.getUser(obj.getCreatedBy());
            bean.setCreatedBy(user.getOID());
            EquipmentCalibrationBean calib = getEquipmentCalibrationBean(new EquipmentOID(obj.getPk()));
            bean.setCalibrationBean(calib);
            bean.setCreatedDate(obj.getCreatedDate());
            if (obj.getApprovedBy() != null && obj.getApprovedBy() > 0)
            {
                User approvedBy = accountService.getUser(obj.getApprovedBy());
                bean.setApprovedBy(approvedBy.getOID());
                bean.setApprovedDate(obj.getApprovedDate());
                bean.setApprovedComment(obj.getApprovedComment());
            }

            bean.setAttachments(commonServiceManager.getAttachments(obj.getPk(), EntityTypeEnum.Equipment.getValue()));
            return bean;
        }).collect(Collectors.toList());

    }

    public  EquipmentBean getEquipmentBean(String equipmentSerialNo)
    {

        EquipmentBean bean = null;

        EquipmentObj obj = equipmentDAO.getEquipment(equipmentSerialNo);
        if (obj == null || obj.getPk() == 0)
        {
            return null;
        }

        try
        {
            return getEquipmentBean(new EquipmentOID(obj.getPk()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public  EquipmentBean getEquipmentBean(EquipmentOID equipmentOID) throws Exception
    {

        EquipmentBean bean = null;

        EquipmentObj obj = equipmentDAO.getEquipment(equipmentOID);
        if (obj == null || obj.getPk() == 0)
        {
            return null;
        }

        bean = new EquipmentBean();

        bean.setPk(obj.getPk());
        bean.setDateOfPurchase(obj.getDateOfPurchase());
        bean.setEquipmentId(obj.getEquipmentId());
        bean.setDescription(obj.getDescription());
        bean.setInstruction(obj.getInstruction());
        bean.setEquipmentTypeOID((obj.getEquipmentTypeFk() != null && obj.getEquipmentTypeFk() > 0)
                ? getEquipmentType(new EquipmentTypeOID(obj.getEquipmentTypeFk())).getOID()
                : null);
        bean.setManufacturer(obj.getManufacturer());
        bean.setModelNo(obj.getModelNo());
        bean.setCost(obj.getCost());
        bean.setSerialNo(obj.getSerialNo());
        bean.setReference(obj.getReference());

        if (obj.getAuthorityType() != null)
        {
            if (EquipmentBean.AUTHORITY_TYPE.External.getIntValue() == obj.getAuthorityType())
            {
                if (obj.getCalibrationAuthorityFk() != null && obj.getCalibrationAuthorityFk() > 0)
                {
                    bean.setCalibrationAuthority(getCalibrationAuthority(obj.getCalibrationAuthorityFk()));
                }
                bean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.External);
            }
            if (EquipmentBean.AUTHORITY_TYPE.Internal.getIntValue() == obj.getAuthorityType())

            {
                bean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.Internal);
            }
        }

        if (obj.getCalibrationIntervalFk() != null)
            if(dbEnum!=null) {
                bean.setCalibrationInterval((EquipmentCalibrationIntervalEnum) dbEnum
                        .getEnum(EquipmentCalibrationIntervalEnum.class, obj.getCalibrationIntervalFk()));
            }
        if (obj.getCustodianFk() != null)
        {
            UserQuery user = UserRepository.getInstance().getUser(obj.getCustodianFk());
            if (user != null)
            {
                bean.setCustodianOID(user.getOID());
            }
        }
        if (obj.getStatus() != null)
            bean.setStatus(EquipmentStatusEnum.valueOf(obj.getStatus()));
        bean.setStatusUpdatedDate(obj.getStatusUpdatedDate());
        bean.setLastUpdated(obj.getLastUpdated());

        Site site = siteService.getSite(obj.getSiteFk());
        bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
        if (obj.getLocationFk() != null && obj.getLocationFk() > 0)
        {
            LocationMasterBean location = getLocation(obj.getLocationFk());
            bean.setLocationOID(location.getOID());
        }
        User user = accountService.getUser(obj.getCreatedBy());
        bean.setCreatedBy(user.getOID());
        EquipmentCalibrationBean calib = getEquipmentCalibrationBean(new EquipmentOID(obj.getPk()));
        bean.setCalibrationBean(calib);
        bean.setCreatedDate(obj.getCreatedDate());
        if (obj.getApprovedBy() != null && obj.getApprovedBy() > 0)
        {
            User approvedBy =accountService.getUser(obj.getApprovedBy());
            bean.setApprovedBy(approvedBy.getOID());
            bean.setApprovedDate(obj.getApprovedDate());
            bean.setApprovedComment(obj.getApprovedComment());
        }

        bean.setAttachments(commonServiceManager.getAttachments(obj.getPk(), EntityTypeEnum.Equipment.getValue()));
        return bean;
    }

    public  List<EquipmentCalibrationBean> getEquipmentCalibrationBeans(EquipmentOID equipmentOID)
    {
        List<EquipmentCalibration> calist =equipmentDAO.getEquipmentCalibrations(equipmentOID);
        if (calist == null)
            return new ArrayList<EquipmentCalibrationBean>();

        return calist.stream().map(cali -> {
            EquipmentCalibrationBean calibean = new EquipmentCalibrationBean();
            calibean.setPk((int) cali.getPk());
            calibean.setEquipmentFk(cali.getEquipmentFk());
            calibean.setCalibrationSequenceNo(cali.getCalibrationSequenceNo());
            //calibean.setCalibationStatus(CalibrationStatusEnum.valueOf(cali.getCalibationStatus()));
            if (EquipmentBean.AUTHORITY_TYPE.External.getIntValue() == cali.getAuthorityType())
            {
                calibean.setCalibrationAuthority(getCalibrationAuthority(cali.getCalibrationAuthorityFk()));
                calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.External);
            }
            if (EquipmentBean.AUTHORITY_TYPE.Internal.getIntValue() == cali.getAuthorityType())

            {
                calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.Internal);
            }
            calibean.setCalibrationDate(cali.getCalibrationDate());
            calibean.setNextCalibrationDate(cali.getNextCalibrationDate());
            calibean.setCalibrationReferenceNo(cali.getCalibrationReferenceNo());
            calibean.setComment(cali.getComment());
           // calibean.setCreatedBy(UserRepository.getInstance().getUser(cali.getCreatedBy()).getOID());
            calibean.setCreatedDate(cali.getCreatedDate());
            calibean.setLastUpdated(cali.getLastUpdated());
            List<Attachment> attachments = commonServiceManager.getAttachments((int) cali.getPk(),
                    EntityTypeEnum.EquipmentCalibration.getValue());
            if (attachments != null && attachments.size() > 0)
            {
                Attachment att = attachments.get(0);
                AttachmentInfoBean attInfoBean = new AttachmentInfoBean();
                attInfoBean.setPk((int) att.getPk());
                attInfoBean.setFileDisplayName(att.getFileDisplayName());
                attInfoBean.setFileName(att.getFileName());
                calibean.setAttachment(attInfoBean);
            }
            return calibean;
        }).collect(Collectors.toList());
    }

    public EquipmentCalibrationBean getEquipmentCalibrationBean(int pk)
    {
        EquipmentCalibration cali = equipmentDAO.getEquipmentCalibration(pk);
        if (cali == null)
            return null;

        EquipmentCalibrationBean calibean = new EquipmentCalibrationBean();
        calibean.setPk((int) cali.getPk());
        calibean.setEquipmentFk(cali.getEquipmentFk());
        calibean.setCalibrationSequenceNo(cali.getCalibrationSequenceNo());
        calibean.setCalibationStatus(CalibrationStatusEnum.valueOf(cali.getCalibationStatus()));
        if (EquipmentBean.AUTHORITY_TYPE.External.getIntValue() == cali.getAuthorityType())
        {
            calibean.setCalibrationAuthority(getCalibrationAuthority(cali.getCalibrationAuthorityFk()));
            calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.External);
        }
        if (EquipmentBean.AUTHORITY_TYPE.Internal.getIntValue() == cali.getAuthorityType())

        {
            calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.Internal);
        }
        calibean.setCalibrationDate(cali.getCalibrationDate());
        calibean.setNextCalibrationDate(cali.getNextCalibrationDate());
        calibean.setCalibrationReferenceNo(cali.getCalibrationReferenceNo());
        calibean.setComment(cali.getComment());
        //calibean.setCreatedBy(UserRepository.getInstance().getUser(cali.getCreatedBy()).getOID());
        calibean.setCreatedDate(cali.getCreatedDate());
        calibean.setLastUpdated(cali.getLastUpdated());
        List<Attachment> attachments = commonServiceManager.getAttachments((int) cali.getPk(),
                EntityTypeEnum.EquipmentCalibration.getValue());
        if (attachments != null && attachments.size() > 0)
        {
            Attachment att = attachments.get(0);
            AttachmentInfoBean attInfoBean = new AttachmentInfoBean();
            attInfoBean.setPk((int) att.getPk());
            attInfoBean.setFileDisplayName(att.getFileDisplayName());
            attInfoBean.setFileName(att.getFileName());
            calibean.setAttachment(attInfoBean);
        }

        return calibean;
    }

    public EquipmentCalibrationBean getEquipmentCalibrationBean(EquipmentOID equipmentOID)
    {
        EquipmentCalibration cali = equipmentDAO.getEquipmentCalibration(equipmentOID);
        if (cali == null)
            return null;

        EquipmentCalibrationBean calibean = new EquipmentCalibrationBean();
        calibean.setPk((int) cali.getPk());
        calibean.setEquipmentFk(cali.getEquipmentFk());
        calibean.setCalibrationSequenceNo(cali.getCalibrationSequenceNo());
       // calibean.setCalibationStatus(CalibrationStatusEnum.valueOf(cali.getCalibationStatus()));
        if (EquipmentBean.AUTHORITY_TYPE.External.getIntValue() == cali.getAuthorityType())
        {
            calibean.setCalibrationAuthority(getCalibrationAuthority(cali.getCalibrationAuthorityFk()));
            calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.External);
        }
        if (EquipmentBean.AUTHORITY_TYPE.Internal.getIntValue() == cali.getAuthorityType())

        {
            calibean.setAuthorityType(EquipmentBean.AUTHORITY_TYPE.Internal);
        }

            calibean.setCalibrationDate(cali.getCalibrationDate());
            calibean.setNextCalibrationDate(cali.getNextCalibrationDate());
            calibean.setCalibrationReferenceNo(cali.getCalibrationReferenceNo());
            calibean.setComment(cali.getComment());

    //calibean.setCreatedBy(UserRepository.getInstance().getUser(cali.getCreatedBy()).getOID());
    calibean.setCreatedDate(cali.getCreatedDate());
    calibean.setLastUpdated(cali.getLastUpdated());
    List<Attachment> attachments = commonServiceManager.getAttachments((int) cali.getPk(),
            EntityTypeEnum.EquipmentCalibration.getValue());

    if (attachments != null && attachments.size() > 0) {
        Attachment att = attachments.get(0);
        AttachmentInfoBean attInfoBean = new AttachmentInfoBean();
        attInfoBean.setPk((int) att.getPk());
        attInfoBean.setFileDisplayName(att.getFileDisplayName());
        attInfoBean.setFileName(att.getFileName());
        calibean.setAttachment(attInfoBean);
    }


        return calibean;
    }

    public  EquipmentBean saveEquipment(UserContext context, EquipmentBean eBean) throws Exception
    {

        //UserOID prevCustodian;

        EquipmentObj obj = new EquipmentObj();

        if (eBean.getPk() > 0)
        {
            obj = equipmentDAO.getEquipment(new EquipmentOID(eBean.getPk()));
            /*
             * if (obj.getCustodianFk() != null && obj.getCustodianFk() > 0) {
             * prevCustodian = new UserOID(obj.getCustodianFk()); }
             */

        }
        /*
         * Duplication reference check
         */
        // if (eBean.getReference() != null &&
        // eBean.getReference().trim().length() > 0)
        // {
        // EquipmentBean duplicate =
        // getEquipmentwithReference(eBean.getReference());
        // if (duplicate != null && duplicate.getPk() != obj.getPk())
        // {
        // throw new AppException(" Duplicate reference.");
        // }
        // }
        /*
         * Duplication equipment id ref
         */
        if (eBean.getEquipmentId() != null && eBean.getEquipmentId().trim().length() > 0)
        {
            EquipmentBean duplicate = getEquipmentwithEquipmentId(eBean.getEquipmentId());
            if (duplicate != null && duplicate.getPk() != obj.getPk())
            {
                throw new AppException(" Duplicate equipment id.");
            }
        }

        /*
         * Duplication equipment id ref
         */
        // if (eBean.getSerialNo() != null &&
        // eBean.getSerialNo().trim().length() > 0)
        // {
        // EquipmentBean duplicate =
        // EquipmentDelegate.getEquipmentBean(eBean.getSiteOID(),
        // eBean.getEquipmentTypeOID(), eBean.getSerialNo());
        // if (duplicate != null && duplicate.getPk() != obj.getPk())
        // {
        // throw new AppException("Serial number " + duplicate.getSerialNo()
        // + " is already assigned to the equipment " +
        // duplicate.getEquipmentId());
        // }
        // }

        obj.setPk(eBean.getPk());
        if (eBean.getStatus() != null)
            obj.setStatus(eBean.getStatus().name());
        obj.setDateOfPurchase(eBean.getDateOfPurchase());
        obj.setEquipmentId(eBean.getEquipmentId());
        obj.setDescription(eBean.getDescription());
        obj.setInstruction(eBean.getInstruction());
        obj.setEquipmentTypeFk(Math.toIntExact((eBean.getEquipmentTypeOID() != null) ? eBean.getEquipmentTypeOID().getPk() : null));
        obj.setManufacturer(eBean.getManufacturer());
        obj.setModelNo(eBean.getModelNo());
        obj.setSerialNo(eBean.getSerialNo());
        obj.setReference(eBean.getReference());
        obj.setCost(eBean.getCost());
        if (eBean.getStatusUpdatedDate() != null)
            obj.setStatusUpdatedDate(eBean.getStatusUpdatedDate());
        if (eBean.getAuthorityType() != null)
        {
            obj.setAuthorityType(eBean.getAuthorityType().getIntValue());
            if (eBean.getCalibrationAuthority() != null)
                obj.setCalibrationAuthorityFk((int) eBean.getCalibrationAuthority().getPk());
            else
                obj.setCalibrationAuthorityFk(null);
        }
        if (eBean.getCalibrationInterval() != null)
            obj.setCalibrationIntervalFk((int) eBean.getCalibrationInterval().getPk());
        else
            obj.setCalibrationIntervalFk(null);

        if (eBean.getCustodianOID() != null)
            obj.setCustodianFk((int) eBean.getCustodianOID().getPk());
        else
            obj.setCustodianFk(null);

        obj.setSiteFk((int) eBean.getSiteOID().getPk());
        if (eBean.getLocationOID() != null)
        {
            obj.setLocationFk((int) eBean.getLocationOID().getPk());
        } else
        {
            obj.setLocationFk(null);
        }
        if (eBean.getStatus() != null)
            obj.setStatus(eBean.getStatus().name());
        else
        {
            obj.setStatus(null);
        }
        obj.setCreatedBy((int) context.getUser().getPk());
        obj.setCreatedDate(eBean.getCreatedDate());
        if (eBean.getEstatus() != null)
            obj.setEstatus(eBean.getEstatus().getValue());
        obj.setLastUpdated(eBean.getLastUpdated());
        if (eBean.getApprovedBy() != null)
            obj.setApprovedBy((int) eBean.getApprovedBy().getPk());
        if (eBean.getApprovedComment() != null)
            obj.setApprovedComment(eBean.getApprovedComment());
        if (eBean.getApprovedDate() != null)
            obj.setApprovedDate(eBean.getApprovedDate());
        obj = equipmentDAO.saveEquipment(context, obj);
        updateNextCaliberationDate(new EquipmentOID(obj.getPk()), eBean.getCalibrationInterval());

        List<AttachmentIntf> attachmentlist = new ArrayList<AttachmentIntf>();

        if (eBean.getAttachments() != null && eBean.getAttachments().size() > 0)
        {
            for (AttachmentIntf attachmentIntf : eBean.getAttachments())
            {
                attachmentlist.add(attachmentIntf);
            }
        }
        commonServiceManager.saveAttachments(context, obj.getPk(), EntityTypeEnum.Equipment.getValue(), attachmentlist,
                true);

        EquipmentBean newBean = getEquipmentBean(new EquipmentOID(obj.getPk()));
        if (eBean.getPk() < 1
                && authorizationManager .isUserInRole(context, SiteRolesEnum.CalibrationCoordinator.getId()))
        {
            /*
             * If equipment is created by calibration coordinator then it can be
             * automatically approved
             */
            newBean = approveEquipment(context, new EquipmentOID(obj.getPk()),
                    "Created and approved by calibration coordinator: " + context.getUser().getOID().getDisplayText());
        } else if (eBean.getPk() < 1)
        {
            /*
             * if the equipment is not created by calibration coordinator then
             * sent created notification email to calibration coordinator and
             * created user
             */

            equipmentEmailSender.notifyEquipmentCreated(context, newBean);
        }

        return newBean;
    }

    public  EquipmentBean getEquipmentwithReference(String reference) throws Exception
    {
        EquipmentH eqHCurrent = equipmentDAO.getEquipmentwithReference(reference);
        if (eqHCurrent != null)
        {
            return getEquipmentBean(new EquipmentOID(eqHCurrent.getEquipmentFK()));
        }
        return null;
    }

    public  EquipmentBean getEquipmentwithEquipmentId(String equipmentId) throws Exception
    {
        EquipmentH eqHCurrent =equipmentDAO.getEquipmentwithEquipmentId(equipmentId);
        if (eqHCurrent != null)
        {
            return getEquipmentBean(new EquipmentOID(eqHCurrent.getEquipmentFK()));
        }
        return null;
    }

    public  EquipmentBean getEquipmentBean(SiteOID siteOID, EquipmentTypeOID equipmentTypeOID,
                                                 String serialNumber) throws Exception
    {

        EquipmentH eqHCurrent =equipmentDAO.getEquipmentBean(siteOID, equipmentTypeOID, serialNumber);
        if (eqHCurrent != null)
        {
            return getEquipmentBean(new EquipmentOID(eqHCurrent.getEquipmentFK()));
        }
        return null;
    }

    public  List<EquipmentListReportRow> getEquipments(UserContext context, EquipmentListFilter filter)
            throws Exception
    {
        ReportRequest req = new ReportRequest(ReportTypes.EquipmentList);
        req.setFetchAllRows(true);
        req.setFilter(filter);
        ReportResponse reportResponse =reportsManager.runReport(context, req);
        if(reportResponse!=null) {
            return (List<EquipmentListReportRow>) reportResponse.getReportData();
        }
        return null;
    }

    public  List<EquipmentListReportRow> getPendingEquipmentsApprovalForCalibrationCoordinator(
            UserContext context) throws Exception
    {
        EquipmentListFilter filter = new EquipmentListFilter();
        filter.setPendingApprovalForCalibrationCoordinator(Arrays.asList(context.getUser().getOID()));
        ReportRequest req = new ReportRequest(ReportTypes.EquipmentList);
        req.setFetchAllRows(true);
        req.setFilter(filter);
        ReportResponse reportResponse = reportsManager.runReport(context, req);
        if(reportResponse!=null) {
            return (List<EquipmentListReportRow>) reportResponse.getReportData();
        }
        return null;
    }

    public  List<EquipmentListReportRow> getPendingEquipmentsApprovalForSiteAdmin(UserContext context)
            throws Exception
    {
        EquipmentListFilter filter = new EquipmentListFilter();
        filter.setPendingApprovalForSiteAdmin(Arrays.asList(context.getUser().getOID()));
        filter.setPendingForApprovalLimit(EquipmentListFilter.PendingforApprovalLimit.BEFORE1MONTH);
        ReportRequest req = new ReportRequest(ReportTypes.EquipmentList);
        req.setFetchAllRows(true);
        req.setFilter(filter);
        ReportResponse reportResponse = reportsManager.runReport(context, req);
        if(reportResponse!=null) {
            return (List<EquipmentListReportRow>) reportResponse.getReportData();
        }
        return null;
    }

    @Transactional
    public  EquipmentBean approveEquipment(UserContext context, EquipmentOID equipmentOID, String comment)
            throws Exception
    {
        EquipmentBean bean = null;
        if (equipmentOID != null && equipmentOID.getPk() > 0)
        {
            equipmentDAO.approveEquipment(context, equipmentOID, comment);
            bean = getEquipmentBean(equipmentOID);
            try {
                EquipmentEmailSender.notifyApprovedEquipment(context, bean);
            }
            catch(Exception e){

            }
        }
        return bean;
    }

    public  void deleteEquipment(UserContext context, EquipmentOID equipmentOID) throws Exception
    {
        Equipment item;
        if (equipmentOID != null && equipmentOID.getPk() > 0)
        {
            item = (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, equipmentOID.getPk());
            item.setEstatus(EStatusEnum.Deleted.getValue());
            persistWrapper.update(item);

        }
    }

    public void updateNextCaliberationDate(EquipmentOID equipmentOID,
                                           EquipmentCalibrationIntervalEnum calibrationInterval) throws Exception {
        // calculate the next calibration date based on the calibration interval
        // if the calibration interval is changed in the equipment,
        // we need to re-calculate the interval and update the record.
        EquipmentCalibration calib =equipmentDAO.getEquipmentCalibration(equipmentOID);
        if (calib != null)
        {
            if (calibrationInterval != null && calib.getCalibrationDate() != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(calib.getCalibrationDate());

                TimeZone context = null;
                cal.setTimeZone(context);
                switch ((int) calibrationInterval.getPk()) {
                    case 1:
                        // 1 Day
                        cal.add(Calendar.DATE, 1);
                        break;
                    case 2:
                        // 7 Day
                        cal.add(Calendar.DATE, 7);
                        break;
                    case 3:
                        // 1 Month
                        cal.add(Calendar.MONTH, 1);
                        break;
                    case 4:
                        // 3 Month
                        cal.add(Calendar.MONTH, 3);
                        break;
                    case 5:
                        // 6 Month
                        cal.add(Calendar.MONTH, 6);
                        break;
                    case 6:
                        // 1 Year
                        cal.add(Calendar.YEAR, 1);
                        break;
                    case 7:
                        // 2 Year
                        cal.add(Calendar.YEAR, 2);
                        break;
                    case 8:
                        // 3 Year
                        cal.add(Calendar.YEAR, 3);
                        break;
                    case 9:
                        // 5 Year
                        cal.add(Calendar.YEAR, 5);
                        break;
                    case 10:
                        // 10 Year
                        cal.add(Calendar.YEAR, 10);
                        break;
                    default:
                        break;

                }
                calib.setNextCalibrationDate(cal.getTime());
            } else
            {
                calib.setNextCalibrationDate(null);
            }
            persistWrapper.update(calib);
        }

    }

    @Transactional
    public  EquipmentCalibrationBean saveCalibrationData(UserContext context, EquipmentOID equipmentOID,
                                                               EquipmentCalibrationBean caliBean) throws Exception {
        // we are not letting the user edit an existing calibration entry here.
        // that is not supported now.

        // if an calibration entry already exists for the equipment make it not
        // current.
        EquipmentCalibration calib = equipmentDAO.getEquipmentCalibration(equipmentOID);
        if (calib != null) {
            calib.setCurrent(0);
            persistWrapper.update(calib);
        }

        // now save the new calibration entry.
        EquipmentCalibration newCali = new EquipmentCalibration();
        if(caliBean!=null) {
            if (caliBean.getAuthorityType() != null) {
                if (caliBean.getCalibationStatus() != null)
                    newCali.setCalibationStatus(caliBean.getCalibationStatus().name());
                newCali.setAuthorityType(caliBean.getAuthorityType().getIntValue());
                if (caliBean.getCalibrationAuthority() != null)
                    newCali.setCalibrationAuthorityFk((int) caliBean.getCalibrationAuthority().getPk());
                newCali.setCalibrationDate(caliBean.getCalibrationDate());
            }
        }
        // calculate the next calibration date based on the calibration interval
        // if the calibration interval is changed in the equipment,
        // we need to re-calculate the interval and update the record.
        EquipmentObj obj = equipmentDAO.getEquipment(equipmentOID);
        if(caliBean!=null) {
            if (obj.getCalibrationIntervalFk() != null && caliBean.getCalibrationDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(caliBean.getCalibrationDate());
                cal.setTimeZone((TimeZone) context);
                switch (obj.getCalibrationIntervalFk()) {
                    case 1:
                        // 1 Day
                        cal.add(Calendar.DATE, 1);
                        break;
                    case 2:
                        // 7 Day
                        cal.add(Calendar.DATE, 7);
                        break;
                    case 3:
                        // 1 Month
                        cal.add(Calendar.MONTH, 1);
                        break;
                    case 4:
                        // 3 Month
                        cal.add(Calendar.MONTH, 3);
                        break;
                    case 5:
                        // 6 Month
                        cal.add(Calendar.MONTH, 6);
                        break;
                    case 6:
                        // 1 Year
                        cal.add(Calendar.YEAR, 1);
                        break;
                    case 7:
                        // 2 Year
                        cal.add(Calendar.YEAR, 2);
                        break;
                    case 8:
                        // 3 Year
                        cal.add(Calendar.YEAR, 3);
                        break;
                    case 9:
                        // 5 Year
                        cal.add(Calendar.YEAR, 5);
                        break;
                    case 10:
                        // 10 Year
                        cal.add(Calendar.YEAR, 10);
                        break;
                    default:
                        break;

                }

                newCali.setNextCalibrationDate(cal.getTime());
            }
        }
        int ref = 0;
        List<EquipmentCalibration> calist = equipmentDAO.getEquipmentCalibrations(equipmentOID);
        if (calist != null) {
            ref = calist.size();
        }
        ref++;
        if(caliBean!=null) {
            newCali.setCalibrationReferenceNo(caliBean.getCalibrationReferenceNo());
            newCali.setCalibrationSequenceNo(ref + "");
            newCali.setComment(caliBean.getComment());
            newCali.setCreatedBy((int) context.getUser().getPk());
            newCali.setCreatedDate(new Date());
            newCali.setCurrent(1);
            newCali.setEquipmentFk((int) equipmentOID.getPk());
        }

        int pk = (int) persistWrapper.createEntity(newCali);
if(caliBean!=null) {
    if (caliBean.getAttachment() != null) {
        Attachment att = new Attachment();
        att.setEstatus(EStatusEnum.Active.getValue());
        att.setFileDisplayName(caliBean.getAttachment().getFileDisplayName());
        att.setFileName(caliBean.getAttachment().getFileName());
        commonServiceManager.addAttachments(context, pk, EntityTypeEnum.EquipmentCalibration.getValue(),
                Arrays.asList(new AttachmentIntf[]{att}));
    }
}

        EquipmentBean equipmentBean = getEquipmentBean(String.valueOf(equipmentOID));
        if (equipmentBean != null){
            if (equipmentBean.getApprovedBy() != null) {
                EquipmentEmailSender.notifyCalibrationUpdated(context, equipmentBean);
            }
        return getEquipmentCalibrationBean(equipmentOID);
    }
        return caliBean;
    }

    public  EquipmentCalibrationBean updateCalibrationData(UserContext context, EquipmentCalibrationBean caliBean)
            throws Exception
    {
        // Now user can edit calibration entry only before the approved status
        EquipmentObj obj = equipmentDAO.getEquipment(new EquipmentOID(caliBean.getEquipmentFk()));
        if (obj.getApprovedBy() != null)
        {
            try {
                throw new AppException("Calibration entry is not able to edit after its approval.");
            }catch (Exception e) {
            }

            }
        EquipmentCalibration newCali =equipmentDAO.getEquipmentCalibration(caliBean.getPk());
        if (newCali == null)
        {
            throw new AppException(
                    "There is no calibration entry is avaialble to update, please contact administrator.");
        }

        // now save the new calibration entry.
        if(caliBean.getAuthorityType()!=null) {
            if (caliBean.getCalibationStatus() != null)
                newCali.setCalibationStatus(caliBean.getCalibationStatus().name());
            newCali.setAuthorityType(caliBean.getAuthorityType().getIntValue());
            if (caliBean.getCalibrationAuthority() != null)
                newCali.setCalibrationAuthorityFk((int) caliBean.getCalibrationAuthority().getPk());
            newCali.setCalibrationDate(caliBean.getCalibrationDate());
        }
        // calculate the next calibration date based on the calibration interval
        // if the calibration interval is changed in the equipment,
        // we need to re-calculate the interval and update the record.
        if (obj.getCalibrationIntervalFk() != null && caliBean.getCalibrationDate() != null)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(caliBean.getCalibrationDate());
            cal.setTimeZone((TimeZone) context);
            switch (obj.getCalibrationIntervalFk()) {
                case 1:
                    // 1 Day
                    cal.add(Calendar.DATE, 1);
                    break;
                case 2:
                    // 7 Day
                    cal.add(Calendar.DATE, 7);
                    break;
                case 3:
                    // 1 Month
                    cal.add(Calendar.MONTH, 1);
                    break;
                case 4:
                    // 3 Month
                    cal.add(Calendar.MONTH, 3);
                    break;
                case 5:
                    // 6 Month
                    cal.add(Calendar.MONTH, 6);
                    break;
                case 6:
                    // 1 Year
                    cal.add(Calendar.YEAR, 1);
                    break;
                case 7:
                    // 2 Year
                    cal.add(Calendar.YEAR, 2);
                    break;
                case 8:
                    // 3 Year
                    cal.add(Calendar.YEAR, 3);
                    break;
                case 9:
                    // 5 Year
                    cal.add(Calendar.YEAR, 5);
                    break;
                case 10:
                    // 10 Year
                    cal.add(Calendar.YEAR, 10);
                    break;
                default:
                    break;

            }
            newCali.setNextCalibrationDate(cal.getTime());
        }

        newCali.setCalibrationReferenceNo(caliBean.getCalibrationReferenceNo());
        newCali.setCalibrationSequenceNo(caliBean.getCalibrationSequenceNo());
        newCali.setComment(caliBean.getComment());
        persistWrapper.update( newCali);

        if (caliBean.getAttachment() != null)
        {
            Attachment att = new Attachment();
            att.setEstatus(EStatusEnum.Active.getValue());
            att.setFileDisplayName(caliBean.getAttachment().getFileDisplayName());
            att.setFileName(caliBean.getAttachment().getFileName());
            commonServiceManager.addAttachments(context, (int) newCali.getPk(),
                    EntityTypeEnum.EquipmentCalibration.getValue(), Arrays.asList(new AttachmentIntf[] { att }));
        }
        return getEquipmentCalibrationBean((int) newCali.getPk());
    }

    public  void deleteCalibration(UserContext context, int pk) throws Exception
    {
        EquipmentCalibration newCali = equipmentDAO.getEquipmentCalibration(pk);
        if (newCali == null)
        {
            throw new AppException(
                    "There is no calibration entry is avaialble to update, please contact administrator.");
        }
        persistWrapper.delete(" delete FROM equipment_calibration  where equipment_calibration.pk= ?",
                newCali.getPk());
    }

    public  List<String> getEquipmentManufacturerList()
    {
        return persistWrapper.readList(String.class,
                "select distinct manufacturer from equipment where manufacturer is not null order by manufacturer ");
    }

    public  EquipmentCalibrationAuthority getCalibrationAuthority(String name, SiteOID siteoid) {

        String sql = "select *  from equipment_calibration_authority where estatus = 1 and name = ? and siteFk=?";
        try {
            return persistWrapper.read(EquipmentCalibrationAuthority.class, sql, name, siteoid.getPk());
        } catch (Exception e) {

        }
        return null;
    }
}
