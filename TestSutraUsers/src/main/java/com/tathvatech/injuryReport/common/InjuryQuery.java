package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.sf.persist.annotations.NoTable;

@NoTable
public class InjuryQuery implements Serializable {
    private int pk;
    private Integer projectPk;
    private String projectName;
    private String projectDescription;
    private Integer locationPk;
    private String locationType;
    private String locationName;
    private String description;
    private Integer sitePk;
    private int custodianPk;
    //
    private String injuryReportNo;
    private Boolean referenceRequired;
    private String referenceNo;
    private String injuredPerson;
    private Integer typeOfPerson;
    private Date dateOfInjury;
    private Boolean detailsEquipmentStatus;
    private String detailsEquipment;
    private Boolean detailsWitnesStatus;
    private String detailsWitnes;
    private String detailsTreatment;
    private String treatedBy;
    private String rootCauseOfInjury;
    private String supplimentaryCause;
    private String precautionDone;
    private String precautionRequired;
    private Boolean reportingRequired;
    private String reportingNumber;

    // body part
    private boolean fatality;
    private boolean headfront;
    private boolean headback;
    private boolean face;
    private boolean lefteye;
    private boolean righteye;
    private boolean leftear;
    private boolean rightear;
    private boolean neckfront;
    private boolean neckback;
    private boolean upperbodyfront;
    private boolean upperbodyback;
    private boolean lowerbodyfront;
    private boolean lowerbodyback;
    private boolean shoulderright;
    private boolean shoulderleft;
    private boolean upperarmright;
    private boolean upperarmleft;
    private boolean lowerarmright;
    private boolean lowerarmleft;
    private boolean elbowright;
    private boolean elbowleft;
    private boolean wristleft;
    private boolean wristright;
    private boolean fingersleft;
    private boolean fingersright;
    private boolean upperlegright;
    private boolean upperlegleft;
    private boolean kneeright;
    private boolean kneeleft;
    private boolean lowerlegright;
    private boolean lowerlegleft;
    private boolean ankleleft;
    private boolean ankleright;
    private boolean footright;
    private boolean footleft;
    private boolean other;
    private String otherParts;
    // body part
    // action status
    private String status;
    private Integer createdBy;
    private String createdByName;
    private String createdByInitial;
    private Date createdDate;
    private Integer acknowledgedBy;
    private String aknowledgeName;
    private Date acknowledgedDate;
    private Integer verifiedBy;
    private String verifiedByName;
    private Date verifiedDate;
    private Integer closedBy;
    private String closedByName;
    private Date closedDate;
    // action status
    private Date lastUpdated;
    private String locationOther;
    private String siteName;
    private String siteDescription;
    private Integer supervisedBy;
    private String supervisedByName;
    private Integer typeOfInjury;
    // InjuryAssignAfterTreatmentBean
    private List<InjuryAssignAfterTreatmentBean> injuryAssignAfterTreatment;
    // InjuryAssignAfterTreatmentBean
    private List<WatcherBean> watcherBean;

    public String getLocationOther() {
        return locationOther;
    }

    public void setLocationOther(String locationOther) {
        this.locationOther = locationOther;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public Integer getSupervisedBy() {
        return supervisedBy;
    }

    public void setSupervisedBy(Integer supervisedBy) {
        this.supervisedBy = supervisedBy;
    }

    public String getSupervisedByName() {
        return supervisedByName;
    }

    public void setSupervisedByName(String supervisedByName) {
        this.supervisedByName = supervisedByName;
    }

    public Boolean getReferenceRequired() {
        return referenceRequired;
    }

    public void setReferenceRequired(Boolean referenceRequired) {
        this.referenceRequired = referenceRequired;
    }

    public Integer getTypeOfInjury() {
        return typeOfInjury;
    }

    public void setTypeOfInjury(Integer typeOfInjury) {
        this.typeOfInjury = typeOfInjury;
    }

    public static String sql = "SELECT "
            + " injury.pk,injury.injuryReportNo,injury.referenceRequired,injury.referenceNo,injury.injuredPerson,injury.typeOfPerson,injury.typeOfInjury,injury.dateOfInjury,injury.detailsEquipment,"
            + " injury.detailsWitnes,injury.detailsTreatment,injury.treatedBy,injury.rootCauseOfInjury,injury.supplimentaryCause,"
            + " injury.precautionDone,injury.precautionRequired,injury.reportingRequired,injury.reportingNumber,"
            + " injury.fatality,injury.headfront,injury.headback,injury.face,injury.lefteye,injury.righteye,injury.leftear,injury.neckfront,injury.neckback,"
            + " injury.upperbodyfront,injury.upperbodyback,injury.lowerbodyfront,injury.lowerbodyback,injury.shoulderleft,injury.shoulderright,"
            + " injury.upperarmright,injury.lowerarmright,injury.lowerarmleft,injury.elbowright,injury.wristleft,injury.wristright,"
            + " injury.fingersleft,injury.fingersright,injury.upperlegright,injury.upperlegleft,injury.kneeright,injury.kneeleft,"
            + " injury.lowerlegright,injury.lowerlegleft,injury.ankleleft,injury.ankleright,injury.footright,injury.footleft,injury.elbowleft,"
            + " injury.upperarmleft,injury.rightear,injury.other,injury.description,injury.sitePk,"
            + " injury.createdBy,injury.createdByInitial,injury.createdDate,injury.acknowledgedDate,injury.closedDate,injury.projectPk,"
            + " injury.locationPk,injury.locationType,injury.custodianPk,injury.status,injury.lastUpdated,injury.acknowledgedDate,injury.acknowledgedBy,"
            + " injury.verifiedDate,injury.verifiedBy,injury.closedBy,injury.detailsEquipmentStatus,injury.detailsWitnesStatus,injury.otherParts,injury.locationOther,injury.supervisedBy"
            + " ,(SELECT concat(firstName,' ',lastName) FROM TAB_USER where TAB_USER.pk=injury.acknowledgedBy) as aknowledgeName "
            + " ,(SELECT concat(firstName,' ',lastName) FROM TAB_USER where TAB_USER.pk=injury.createdBy) as createdByName "
            + " ,(SELECT concat(firstName,' ',lastName) FROM TAB_USER where TAB_USER.pk=injury.verifiedBy) as verifiedByName "
            + " ,(SELECT concat(firstName,' ',lastName) FROM TAB_USER where TAB_USER.pk=injury.closedBy) as closedByName "
            + " ,(SELECT concat(firstName,' ',lastName) FROM TAB_USER where TAB_USER.pk=injury.supervisedBy) as supervisedByName "
            + " ,(case when (injury.locationType = 'Workstation') THEN (SELECT concat(workstationName,' - ',description) FROM TAB_WORKSTATION where TAB_WORKSTATION.pk =injury.locationPk) ELSE (SELECT name FROM injury_location_master where injury_location_master.pk =injury.locationPk) END) as locationName"
            + " ,(SELECT site.name from site where site.pk = injury.sitePk) as siteName"
            + " ,(SELECT site.description from site where site.pk = injury.sitePk) as siteDescription"
            + " ,(SELECT projectName FROM TAB_PROJECT where TAB_PROJECT.pk=injury.projectPk) as projectName "
            + " FROM injury "
            + " where 1 = 1";
    public static String sql_order = " order by injury.dateOfInjury desc ";

    public String getOtherParts() {
        return otherParts;
    }

    public void setOtherParts(String otherParts) {
        this.otherParts = otherParts;
    }

    public boolean getFatality() {
        return fatality;
    }

    public void setFatality(boolean fatality) {
        this.fatality = fatality;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationPk() {
        return locationPk;
    }

    public void setLocationPk(Integer locationPk) {
        this.locationPk = locationPk;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public Integer getProjectPk() {
        return projectPk;
    }

    public void setProjectPk(Integer projectPk) {
        this.projectPk = projectPk;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSitePk() {
        return sitePk;
    }

    public void setSitePk(Integer sitePk) {
        this.sitePk = sitePk;
    }

    public int getCustodianPk() {
        return custodianPk;
    }

    public void setCustodianPk(int custodianPk) {
        this.custodianPk = custodianPk;
    }

    public String getInjuryReportNo() {
        return injuryReportNo;
    }

    public void setInjuryReportNo(String injuryReportNo) {
        this.injuryReportNo = injuryReportNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getInjuredPerson() {
        return injuredPerson;
    }

    public void setInjuredPerson(String injuredPerson) {
        this.injuredPerson = injuredPerson;
    }

    public Integer getTypeOfPerson() {
        return typeOfPerson;
    }

    public void setTypeOfPerson(Integer typeOfPerson) {
        this.typeOfPerson = typeOfPerson;
    }

    public Date getDateOfInjury() {
        return dateOfInjury;
    }

    public void setDateOfInjury(Date dateOfInjury) {
        this.dateOfInjury = dateOfInjury;
    }

    public String getDetailsEquipment() {
        return detailsEquipment;
    }

    public void setDetailsEquipment(String detailsEquipment) {
        this.detailsEquipment = detailsEquipment;
    }

    public String getDetailsWitnes() {
        return detailsWitnes;
    }

    public void setDetailsWitnes(String detailsWitnes) {
        this.detailsWitnes = detailsWitnes;
    }

    public String getDetailsTreatment() {
        return detailsTreatment;
    }

    public void setDetailsTreatment(String detailsTreatment) {
        this.detailsTreatment = detailsTreatment;
    }

    public String getTreatedBy() {
        return treatedBy;
    }

    public void setTreatedBy(String treatedBy) {
        this.treatedBy = treatedBy;
    }

    public String getRootCauseOfInjury() {
        return rootCauseOfInjury;
    }

    public void setRootCauseOfInjury(String rootCauseOfInjury) {
        this.rootCauseOfInjury = rootCauseOfInjury;
    }

    public String getSupplimentaryCause() {
        return supplimentaryCause;
    }

    public void setSupplimentaryCause(String supplimentaryCause) {
        this.supplimentaryCause = supplimentaryCause;
    }

    public String getPrecautionDone() {
        return precautionDone;
    }

    public void setPrecautionDone(String precautionDone) {
        this.precautionDone = precautionDone;
    }

    public String getPrecautionRequired() {
        return precautionRequired;
    }

    public void setPrecautionRequired(String precautionRequired) {
        this.precautionRequired = precautionRequired;
    }

    public Boolean getReportingRequired() {
        return reportingRequired;
    }

    public void setReportingRequired(Boolean reportingRequired) {
        this.reportingRequired = reportingRequired;
    }

    public String getReportingNumber() {
        return reportingNumber;
    }

    public void setReportingNumber(String reportingNumber) {
        this.reportingNumber = reportingNumber;
    }

    public boolean getHeadfront() {
        return headfront;
    }

    public void setHeadfront(boolean headfront) {
        this.headfront = headfront;
    }

    public boolean getHeadback() {
        return headback;
    }

    public void setHeadback(boolean headback) {
        this.headback = headback;
    }

    public boolean getFace() {
        return face;
    }

    public void setFace(boolean face) {
        this.face = face;
    }

    public boolean getLefteye() {
        return lefteye;
    }

    public void setLefteye(boolean lefteye) {
        this.lefteye = lefteye;
    }

    public boolean getRighteye() {
        return righteye;
    }

    public void setRighteye(boolean righteye) {
        this.righteye = righteye;
    }

    public boolean getLeftear() {
        return leftear;
    }

    public void setLeftear(boolean leftear) {
        this.leftear = leftear;
    }

    public boolean getRightear() {
        return rightear;
    }

    public void setRightear(boolean rightear) {
        this.rightear = rightear;
    }

    public boolean getNeckfront() {
        return neckfront;
    }

    public void setNeckfront(boolean neckfront) {
        this.neckfront = neckfront;
    }

    public boolean getNeckback() {
        return neckback;
    }

    public void setNeckback(boolean neckback) {
        this.neckback = neckback;
    }

    public boolean getUpperbodyfront() {
        return upperbodyfront;
    }

    public void setUpperbodyfront(boolean upperbodyfront) {
        this.upperbodyfront = upperbodyfront;
    }

    public boolean getUpperbodyback() {
        return upperbodyback;
    }

    public void setUpperbodyback(boolean upperbodyback) {
        this.upperbodyback = upperbodyback;
    }

    public boolean getLowerbodyfront() {
        return lowerbodyfront;
    }

    public void setLowerbodyfront(boolean lowerbodyfront) {
        this.lowerbodyfront = lowerbodyfront;
    }

    public boolean getLowerbodyback() {
        return lowerbodyback;
    }

    public void setLowerbodyback(boolean lowerbodyback) {
        this.lowerbodyback = lowerbodyback;
    }

    public boolean getShoulderright() {
        return shoulderright;
    }

    public void setShoulderright(boolean shoulderright) {
        this.shoulderright = shoulderright;
    }

    public boolean getShoulderleft() {
        return shoulderleft;
    }

    public void setShoulderleft(boolean shoulderleft) {
        this.shoulderleft = shoulderleft;
    }

    public boolean getUpperarmright() {
        return upperarmright;
    }

    public void setUpperarmright(boolean upperarmright) {
        this.upperarmright = upperarmright;
    }

    public boolean getUpperarmleft() {
        return upperarmleft;
    }

    public void setUpperarmleft(boolean upperarmleft) {
        this.upperarmleft = upperarmleft;
    }

    public boolean getLowerarmright() {
        return lowerarmright;
    }

    public void setLowerarmright(boolean lowerarmright) {
        this.lowerarmright = lowerarmright;
    }

    public boolean getLowerarmleft() {
        return lowerarmleft;
    }

    public void setLowerarmleft(boolean lowerarmleft) {
        this.lowerarmleft = lowerarmleft;
    }

    public boolean getElbowright() {
        return elbowright;
    }

    public void setElbowright(boolean elbowright) {
        this.elbowright = elbowright;
    }

    public boolean getElbowleft() {
        return elbowleft;
    }

    public void setElbowleft(boolean elbowleft) {
        this.elbowleft = elbowleft;
    }

    public boolean getWristleft() {
        return wristleft;
    }

    public void setWristleft(boolean wristleft) {
        this.wristleft = wristleft;
    }

    public boolean getWristright() {
        return wristright;
    }

    public void setWristright(boolean wristright) {
        this.wristright = wristright;
    }

    public boolean getFingersleft() {
        return fingersleft;
    }

    public void setFingersleft(boolean fingersleft) {
        this.fingersleft = fingersleft;
    }

    public boolean getFingersright() {
        return fingersright;
    }

    public void setFingersright(boolean fingersright) {
        this.fingersright = fingersright;
    }

    public boolean getUpperlegright() {
        return upperlegright;
    }

    public void setUpperlegright(boolean upperlegright) {
        this.upperlegright = upperlegright;
    }

    public boolean getUpperlegleft() {
        return upperlegleft;
    }

    public void setUpperlegleft(boolean upperlegleft) {
        this.upperlegleft = upperlegleft;
    }

    public boolean getKneeright() {
        return kneeright;
    }

    public void setKneeright(boolean kneeright) {
        this.kneeright = kneeright;
    }

    public boolean getKneeleft() {
        return kneeleft;
    }

    public void setKneeleft(boolean kneeleft) {
        this.kneeleft = kneeleft;
    }

    public boolean getLowerlegright() {
        return lowerlegright;
    }

    public void setLowerlegright(boolean lowerlegright) {
        this.lowerlegright = lowerlegright;
    }

    public boolean getLowerlegleft() {
        return lowerlegleft;
    }

    public void setLowerlegleft(boolean lowerlegleft) {
        this.lowerlegleft = lowerlegleft;
    }

    public boolean getAnkleleft() {
        return ankleleft;
    }

    public void setAnkleleft(boolean ankleleft) {
        this.ankleleft = ankleleft;
    }

    public boolean getAnkleright() {
        return ankleright;
    }

    public void setAnkleright(boolean ankleright) {
        this.ankleright = ankleright;
    }

    public boolean getFootright() {
        return footright;
    }

    public void setFootright(boolean footright) {
        this.footright = footright;
    }

    public boolean getFootleft() {
        return footleft;
    }

    public void setFootleft(boolean footleft) {
        this.footleft = footleft;
    }

    public boolean getOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByInitial() {
        return createdByInitial;
    }

    public void setCreatedByInitial(String createdByInitial) {
        this.createdByInitial = createdByInitial;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(Integer acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public String getAknowledgeName() {
        return aknowledgeName;
    }

    public void setAknowledgeName(String aknowledgeName) {
        this.aknowledgeName = aknowledgeName;
    }

    public Date getAcknowledgedDate() {
        return acknowledgedDate;
    }

    public void setAcknowledgedDate(Date acknowledgedDate) {
        this.acknowledgedDate = acknowledgedDate;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedByName() {
        return verifiedByName;
    }

    public void setVerifiedByName(String verifiedByName) {
        this.verifiedByName = verifiedByName;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public Integer getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedByName() {
        return closedByName;
    }

    public void setClosedByName(String closedByName) {
        this.closedByName = closedByName;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Boolean getDetailsEquipmentStatus() {
        return detailsEquipmentStatus;
    }

    public void setDetailsEquipmentStatus(Boolean detailsEquipmentStatus) {
        this.detailsEquipmentStatus = detailsEquipmentStatus;
    }

    public Boolean getDetailsWitnesStatus() {
        return detailsWitnesStatus;
    }

    public void setDetailsWitnesStatus(Boolean detailsWitnesStatus) {
        this.detailsWitnesStatus = detailsWitnesStatus;
    }

    public List<InjuryAssignAfterTreatmentBean> getInjuryAssignAfterTreatment() {
        return injuryAssignAfterTreatment;
    }

    public void setInjuryAssignAfterTreatment(List<InjuryAssignAfterTreatmentBean> injuryAssignAfterTreatment) {
        this.injuryAssignAfterTreatment = injuryAssignAfterTreatment;
    }

    public List<WatcherBean> getWatcherBean() {
        return watcherBean;
    }

    public void setWatcherBean(List<WatcherBean> watcherBean) {
        this.watcherBean = watcherBean;
    }

}
