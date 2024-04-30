package com.tathvatech.equipment_calibration.dao;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.utils.SequenceIdGenerator;
import com.tathvatech.equipment_calibration.common.EquipmentSequenceKeyGenerator;
import com.tathvatech.equipment_calibration.entity.CalibrationSiteCurrency;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipment_calibration.common.EquipmentObj;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibration;
import com.tathvatech.equipment_calibration.entity.EquipmentH;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import java.util.Objects;
import com.tathvatech.user.entity.Equipment;
import com.tathvatech.user.utils.DateUtils;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import java.util.Date;
import java.util.List;
import com.tathvatech.user.common.UserContext;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentDAO {
    Date now;
    private  final PersistWrapper persistWrapper;

   private final CalibrationSiteCurrencyDAO calibrationSiteCurrencyDAO;
    public EquipmentDAO(PersistWrapper persistWrapper, CalibrationSiteCurrencyDAO calibrationSiteCurrencyDAO)
    {
        this.persistWrapper = persistWrapper;
        this.calibrationSiteCurrencyDAO = calibrationSiteCurrencyDAO;
        now = DateUtils.getNowDateForEffectiveDateFrom();
    }

    public EquipmentObj getEquipment(String equipmentSerialNo)
    {
        try {
            return persistWrapper.read(EquipmentObj.class, fetchSql + " and eqh.serialNo = ? ", equipmentSerialNo);
        }catch (Exception e) {

        }


        return null;
    }

    public EquipmentObj getEquipment(EquipmentOID equipmentOID)
    {
        return persistWrapper.read(EquipmentObj.class, fetchSql + " and eq.pk = ? ", equipmentOID.getPk());
    }

    public List<EquipmentObj> getEquipmentHistory(EquipmentOID equipmentOID)
    {
        return persistWrapper.readList(EquipmentObj.class,
                fetchSqlForHistory + " and eq.pk = ?  order by eqh.createdDate desc", equipmentOID.getPk());
    }

    public EquipmentObj approveEquipment(UserContext context, EquipmentOID equipmentOID, String comment)
            throws Exception
    {

        if (equipmentOID != null && equipmentOID.getPk() > 0)
        {

            Equipment equipment = (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, equipmentOID.getPk());
            /*
             * sequence number generator
             */
            equipment.setApprovedBy((int) context.getUser().getPk());
            equipment.setApprovedDate(now);
            equipment.setApprovedComment(comment);
            persistWrapper.update( equipment);

            EquipmentH eqHCurrent = persistWrapper.read(EquipmentH.class,
                    "select * from equipment_h where equipmentFk = ? and now() between effectiveFrom and effectiveTo",
                    equipment.getPk());
            // if (eqHCurrent.getEquipmentId() == null ||
            // eqHCurrent.getEquipmentId().trim().length() == 0)
            // {
            eqHCurrent.setEquipmentId(getEquipmentId(eqHCurrent.getSiteFk()));
            persistWrapper.update( eqHCurrent);
            // }

            return getEquipment(equipmentOID);
        }
        return null;
    }

    private String getEquipmentId(int siteFk) throws Exception
    {
        CalibrationSiteCurrency calibrationSiteCurrency =calibrationSiteCurrencyDAO
                .getCurrency(new SiteOID(siteFk));
        if (calibrationSiteCurrency == null || calibrationSiteCurrency.getPk() == 0
                || calibrationSiteCurrency.getAbbreviation() == null
                || calibrationSiteCurrency.getAbbreviation().trim().length() == 0)
        {
            try {
                throw new AppException("Equipment abbreviation is not available for this site.");
            }catch(Exception e){

            }
        }
        if(calibrationSiteCurrency!=null) {
            String seq = new EquipmentSequenceKeyGenerator().getNextSeq(calibrationSiteCurrency.getAbbreviation());

            return seq;
        }
        return null;
    }

    public EquipmentObj saveEquipment(UserContext context, EquipmentObj obj) throws Exception
    {
        CalibrationSiteCurrency calibrationSiteCurrency =calibrationSiteCurrencyDAO
                .getCurrency(new SiteOID(obj.getSiteFk()));
        if (calibrationSiteCurrency == null || calibrationSiteCurrency.getPk() == 0
                || calibrationSiteCurrency.getAbbreviation() == null
                || calibrationSiteCurrency.getAbbreviation().trim().length() == 0)
        {
            try {
                throw new AppException("Equipment abbreviation is not available in this site.");
            }catch(Exception e) {

            }
        }
        // boolean siteUpdated = false;
        Equipment equipment = null;
        if (obj.getPk() > 0)
        {
            equipment = (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, obj.getPk());
            if (equipment.getLastUpdated().after(obj.getLastUpdated()))
            {
                throw new AppException("Another user has updated the details, Please reload and try again.");
            }

        } else
        {
            equipment = new Equipment();
            equipment.setCreatedDate(new Date());
            equipment.setCreatedBy((int) context.getUser().getPk());
            equipment.setEstatus(EStatusEnum.Active.getValue());
            // siteUpdated = true;
            // EquipmentType type = EquipmentManager.getEquipmentType(new
            // EquipmentTypeOID(obj.getEquipmentTypeFk()));
            // String seq = new
            // EquipmentSequenceKeyGenerator().getNextSeq(type.getAbbreviation());
            // obj.setEquipmentId(seq);
        }
        equipment.setDateOfPurchase(obj.getDateOfPurchase());
        equipment.setDescription(obj.getDescription());
        equipment.setEquipmentTypeFk(obj.getEquipmentTypeFk());
        equipment.setManufacturer(obj.getManufacturer());
        equipment.setModelNo(obj.getModelNo());

        if (obj.getPk() > 0)
        {
            persistWrapper.update( equipment);
            equipment = (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, obj.getPk());
        } else
        {
            int pk = (int) persistWrapper.createEntity(equipment);
            equipment = (Equipment) persistWrapper.readByPrimaryKey(Equipment.class, pk);
        }
        EquipmentH eqHCurrent = persistWrapper.read(EquipmentH.class,
                "select * from equipment_h where equipmentFk = ? and now() between effectiveFrom and effectiveTo",
                equipment.getPk());
        // if (eqHCurrent == null || obj.getSiteFk() != eqHCurrent.getSiteFk())
        // {
        // siteUpdated = true;
        // }
        //
        // if (siteUpdated)
        // {
        // Site site = SiteManager.getSite(obj.getSiteFk());
        // String seq = new
        // EquipmentSequenceKeyGenerator().getNextSeq(site.getName());
        // obj.setEquipmentId(seq);
        // }

        if (eqHCurrent == null)
        {
            EquipmentH eqHNew = new EquipmentH();
            eqHNew.setEquipmentFK((int) equipment.getPk());
            populateEquipmentHDataToEntityDate(context, obj, eqHNew);
            eqHNew.setEffectiveFrom(now);
            eqHNew.setEffectiveTo(DateUtils.getMaxDate());
            persistWrapper.createEntity(eqHNew);
        } else
        {
            if (isNewHistoryRequired(obj, eqHCurrent))
            {
                eqHCurrent.setEffectiveTo(new Date(now.getTime() - 1000));
                persistWrapper.update( eqHCurrent);

                EquipmentH eqHNew = new EquipmentH();
                eqHNew.setEquipmentFK((int) equipment.getPk());
                populateEquipmentHDataToEntityDate(context, obj, eqHNew);
                eqHNew.setEffectiveFrom(now);
                eqHNew.setEffectiveTo(DateUtils.getMaxDate());
                persistWrapper.createEntity(eqHNew);
            }
        }

        return getEquipment(new EquipmentOID((int) equipment.getPk(), null));
    }

    private void populateEquipmentHDataToEntityDate(UserContext context, EquipmentObj obj, EquipmentH eqH)
    {
        eqH.setEquipmentId(obj.getEquipmentId());
        eqH.setSerialNo(obj.getSerialNo());
        eqH.setCalibrationAuthorityFk(obj.getCalibrationAuthorityFk());
        eqH.setCalibrationIntervalFk(obj.getCalibrationIntervalFk());
        eqH.setCreatedBy((int) context.getUser().getPk());
        eqH.setCreatedDate(now);
        eqH.setCustodianFk(obj.getCustodianFk());
        eqH.setLocationFk(obj.getLocationFk());
        eqH.setStatus(obj.getStatus());
        // if (!(Objects.equals(obj.getStatus(), eqH.getStatus())))
        eqH.setStatusUpdatedDate(now);
        eqH.setSiteFk(obj.getSiteFk());
        eqH.setReference(obj.getReference());
        eqH.setCost(obj.getCost());
        eqH.setAuthorityType(obj.getAuthorityType());
        eqH.setInstruction(obj.getInstruction());

    }

    private boolean isNewHistoryRequired(EquipmentObj obj, EquipmentH eqH)
    {
        if (!(Objects.equals(obj.getCalibrationAuthorityFk(), eqH.getCalibrationAuthorityFk())))
            return true;
        if (!(Objects.equals(obj.getCalibrationIntervalFk(), eqH.getCalibrationIntervalFk())))
            return true;
        if (!(Objects.equals(obj.getCreatedBy(), eqH.getCreatedBy())))
            return true;
        if (!(Objects.equals(obj.getCustodianFk(), eqH.getCustodianFk())))
            return true;
        if (!(Objects.equals(obj.getLocationFk(), eqH.getLocationFk())))
            return true;
        if (!(Objects.equals(obj.getStatus(), eqH.getStatus())))
            return true;
        if (!(Objects.equals(obj.getSiteFk(), eqH.getSiteFk())))
            return true;
        if (!(Objects.equals(obj.getEquipmentId(), eqH.getEquipmentId())))
            return true;
        if (!(Objects.equals(obj.getSerialNo(), eqH.getSerialNo())))
            return true;
        if (!(Objects.equals(obj.getReference(), eqH.getReference())))
            return true;
        if (!(Objects.equals(obj.getCost(), eqH.getCost())))
            return true;
        if (!(Objects.equals(obj.getAuthorityType(), eqH.getAuthorityType())))
            return true;
        if (!(Objects.equals(obj.getInstruction(), eqH.getInstruction())))
            return true;
        return false;
    }

    public EquipmentH getEquipmentwithReference(String reference)
    {
        return persistWrapper.read(EquipmentH.class,
                "select equipment_h.* from equipment_h inner join equipment on equipment.pk=equipment_h.equipmentFk where now() between equipment_h.effectiveFrom and equipment_h.effectiveTo and LOWER(equipment_h.reference)=LOWER(?) and equipment.estatus=?",
                reference, EStatusEnum.Active.getValue());
    }

    public EquipmentH getEquipmentwithEquipmentId(String equipmentId)
    {
        return persistWrapper.read(EquipmentH.class,
                "select equipment_h.* from equipment_h inner join equipment on equipment.pk=equipment_h.equipmentFk where now() between equipment_h.effectiveFrom and equipment_h.effectiveTo and equipment_h.equipmentId=? and equipment.estatus=?",
                equipmentId, EStatusEnum.Active.getValue());
    }

    public EquipmentH getEquipmentBean(SiteOID siteOID, EquipmentTypeOID equipmentTypeOID, String serialNumber)
    {
        try {
            return persistWrapper.read(EquipmentH.class,
                    "select equipment_h.* from equipment_h inner join equipment on equipment.pk=equipment_h.equipmentFk where now() between equipment_h.effectiveFrom and equipment_h.effectiveTo and equipment_h.siteFk=? and equipment_h.serialNo=? and equipment.equipmentTypeFk=? and equipment.estatus=?",
                    siteOID.getPk(), serialNumber, equipmentTypeOID.getPk(), EStatusEnum.Active.getValue());
        }catch (Exception e) {

        }
        return null;
    }
    public EquipmentCalibration getEquipmentCalibration(int pk)
    {
        return persistWrapper.read(EquipmentCalibration.class,
                "select * from equipment_calibration where pk = ?", pk);
    }

    public EquipmentCalibration getEquipmentCalibration(EquipmentOID equipmentOID)
    {
        return persistWrapper.read(EquipmentCalibration.class,
                "select * from equipment_calibration where equipmentFk = ? and current = 1 ", equipmentOID.getPk());
    }

    public List<EquipmentCalibration> getEquipmentCalibrations(EquipmentOID equipmentOID)
    {
        return persistWrapper.readList(EquipmentCalibration.class,
                "select * from equipment_calibration where equipmentFk = ? order by calibrationDate desc ",
                equipmentOID.getPk());
    }

    String fetchSql = " SELECT "
            + " eq.pk,eq.approvedBy,eq.approvedDate,eq.approvedComment, eqh.equipmentId, eqh.serialNo, eq.modelNo, eq.description,eqh.instruction, eq.manufacturer, eq.dateOfPurchase, "
            + " eq.equipmentTypeFk, " + " eqh.custodianFk, " + " eqh.locationFk, " + " eqh.siteFk, " + " eqh.status, "
            + " eqh.statusUpdatedDate, " + " eqh.authorityType, " + " eqh.cost, " + " eqh.calibrationAuthorityFk, "
            + " eqh.reference," + " eq.createdBy, " + " eq.createdDate, " + " eq.lastUpdated, "
            + " eqh.calibrationIntervalFk " + " FROM equipment eq "
            + " inner join equipment_h eqh on eqh.equipmentFK=eq.pk and now() between eqh.effectiveFrom and eqh.effectiveTo "
            + " where 1 = 1 ";
    String fetchSqlForHistory = " SELECT "
            + " eq.pk,eq.approvedBy,eq.approvedDate,eq.approvedComment, eqh.equipmentId, eqh.serialNo, eq.modelNo, eq.description,eqh.instruction, eq.manufacturer, eq.dateOfPurchase, "
            + " eq.equipmentTypeFk, " + " eqh.custodianFk, " + " eqh.locationFk, " + " eqh.siteFk, " + " eqh.status, "
            + " eqh.statusUpdatedDate, " + " eqh.authorityType, " + " eqh.cost, " + " eqh.calibrationAuthorityFk, "
            + " eqh.reference," + " eq.createdBy, " + " eq.createdDate, " + " eq.lastUpdated, "
            + " eqh.calibrationIntervalFk " + " FROM equipment eq "
            + " inner join equipment_h eqh on eqh.equipmentFK=eq.pk " + " where 1 = 1 ";

}
