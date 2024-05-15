package com.tathvatech.injuryReport.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sarvasutra.etest.api.BaseResponseBean;
import com.sarvasutra.etest.api.model.AttachmentInfoBean;
import com.sarvasutra.etest.api.model.CommentInfoBean;
import com.tathvatech.ts.core.accounts.AccountManager;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.common.Comment;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.sites.SiteOID;
import com.thirdi.surveyside.project.ProjectManager;

public class InjuryBean extends BaseResponseBean implements Serializable
{
    private int pk;
    private Integer projectPk;
    private String projectName;
    private String projectDescription;

    private Integer locationPk;
    private String locationType;
    private String description;
    private Integer sitePk;
    private int custodianPk;
    private String status;

    private SiteOID siteOID;
    private OID location;
    private ProjectOID projectOID;
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
    private Integer createdBy;
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
    private List<InjuryAssignAfterTreatmentBean> injuryAssignAfterTreatment;
    private List<WatcherBean> watcherBean;

    private Date lastUpdated;
    private String locationName;
    private String locationOther;
    private String siteName;
    private UserOID supervisedByOID;
    private List<AttachmentInfoBean> attachments;
    private Integer typeOfInjury;
    @Deprecated
    private Integer supervisedBy;

    private List<CommentInfoBean> comments = new ArrayList<CommentInfoBean>();

    public Boolean getReferenceRequired()
    {
        return referenceRequired;
    }

    public void setReferenceRequired(Boolean referenceRequired)
    {
        this.referenceRequired = referenceRequired;
    }

    public String getLocationOther()
    {
        return locationOther;
    }

    public void setLocationOther(String locationOther)
    {
        this.locationOther = locationOther;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public List<WatcherBean> getWatcherBean()
    {
        return watcherBean;
    }

    public void setWatcherBean(List<WatcherBean> watcherBean)
    {
        this.watcherBean = watcherBean;
    }

    public String getOtherParts()
    {
        return otherParts;
    }

    public void setOtherParts(String otherParts)
    {
        this.otherParts = otherParts;
    }

    public boolean getFatality()
    {
        return fatality;
    }

    public void setFatality(boolean fatality)
    {
        this.fatality = fatality;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public Integer getLocationPk()
    {
        return locationPk;
    }

    public void setLocationPk(Integer locationPk)
    {
        this.locationPk = locationPk;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public Integer getTypeOfInjury()
    {
        return typeOfInjury;
    }

    public void setTypeOfInjury(Integer typeOfInjury)
    {
        this.typeOfInjury = typeOfInjury;
    }

    public SiteOID getSiteOID()
    {
        return siteOID;
    }

    public void setSiteOID(SiteOID siteOID)
    {
        this.siteOID = siteOID;
    }

    public ProjectOID getProjectOID()
    {
        return projectOID;
    }

    public void setProjectOID(ProjectOID projectOID)
    {
        this.projectOID = projectOID;
    }

    public OID getLocation()
    {
        return location;
    }

    public void setLocation(OID location)
    {
        this.location = location;
    }

    public UserOID getSupervisedByOID()
    {
        return supervisedByOID;
    }

    public void setSupervisedByOID(UserOID supervisedByOID)
    {
        this.supervisedByOID = supervisedByOID;
    }

    public Integer getSupervisedBy()
    {
        return supervisedBy;
    }

    public void setSupervisedBy(Integer supervisedBy)
    {
        this.supervisedBy = supervisedBy;
    }

    public InjuryOID getOID()
    {
        return new InjuryOID(pk, injuryReportNo);
    }

    public static InjuryBean getInjuryBean(InjuryQuery injuryQuery)
    {
        InjuryBean ibeam = new InjuryBean();
        ibeam.setPk(injuryQuery.getPk());
        ibeam.setProjectPk(injuryQuery.getProjectPk());
        ibeam.setProjectName(injuryQuery.getProjectName());
        ibeam.setProjectDescription(injuryQuery.getProjectDescription());
        ibeam.setLocationPk(injuryQuery.getLocationPk());
        ibeam.setLocationType(injuryQuery.getLocationType());
        ibeam.setLocationName(injuryQuery.getLocationName());
        ibeam.setDescription(injuryQuery.getDescription());
        ibeam.setSitePk(injuryQuery.getSitePk());
        ibeam.setCustodianPk(injuryQuery.getCustodianPk());
        ibeam.setStatus(injuryQuery.getStatus());
        ibeam.setInjuryReportNo(injuryQuery.getInjuryReportNo());
        ibeam.setReferenceRequired(injuryQuery.getReferenceRequired());
        ibeam.setReferenceNo(injuryQuery.getReferenceNo());
        ibeam.setInjuredPerson(injuryQuery.getInjuredPerson());
        ibeam.setTypeOfPerson(injuryQuery.getTypeOfPerson());
        ibeam.setDateOfInjury(injuryQuery.getDateOfInjury());
        ibeam.setDetailsEquipment(injuryQuery.getDetailsEquipment());
        ibeam.setDetailsWitnes(injuryQuery.getDetailsWitnes());
        ibeam.setDetailsTreatment(injuryQuery.getDetailsTreatment());
        ibeam.setTreatedBy(injuryQuery.getTreatedBy());
        ibeam.setRootCauseOfInjury(injuryQuery.getRootCauseOfInjury());
        ibeam.setSupplimentaryCause(injuryQuery.getSupplimentaryCause());
        ibeam.setPrecautionDone(injuryQuery.getPrecautionDone());
        ibeam.setPrecautionRequired(injuryQuery.getPrecautionRequired());
        ibeam.setReportingRequired(injuryQuery.getReportingRequired());
        ibeam.setReportingNumber(injuryQuery.getReportingNumber());

        ibeam.setFatality(injuryQuery.getFatality());
        ibeam.setHeadfront(injuryQuery.getHeadfront());
        ibeam.setHeadback(injuryQuery.getHeadback());
        ibeam.setFace(injuryQuery.getFace());
        ibeam.setLefteye(injuryQuery.getLefteye());
        ibeam.setRighteye(injuryQuery.getRighteye());
        ibeam.setLeftear(injuryQuery.getLeftear());
        ibeam.setRightear(injuryQuery.getRightear());
        ibeam.setNeckfront(injuryQuery.getNeckfront());
        ibeam.setNeckback(injuryQuery.getNeckback());
        ibeam.setUpperbodyfront(injuryQuery.getUpperbodyfront());
        ibeam.setUpperbodyback(injuryQuery.getUpperbodyback());
        ibeam.setLowerbodyfront(injuryQuery.getLowerbodyfront());
        ibeam.setLowerbodyback(injuryQuery.getLowerbodyback());
        ibeam.setShoulderright(injuryQuery.getShoulderright());
        ibeam.setShoulderleft(injuryQuery.getShoulderleft());
        ibeam.setUpperarmright(injuryQuery.getUpperarmright());
        ibeam.setUpperarmleft(injuryQuery.getUpperarmleft());
        ibeam.setLowerarmright(injuryQuery.getLowerarmright());
        ibeam.setLowerarmleft(injuryQuery.getLowerarmleft());
        ibeam.setElbowright(injuryQuery.getElbowright());
        ibeam.setElbowleft(injuryQuery.getElbowleft());
        ibeam.setWristleft(injuryQuery.getWristleft());
        ibeam.setWristright(injuryQuery.getWristright());
        ibeam.setFingersleft(injuryQuery.getFingersleft());
        ibeam.setFingersright(injuryQuery.getFingersright());
        ibeam.setUpperlegright(injuryQuery.getUpperlegright());
        ibeam.setUpperlegleft(injuryQuery.getUpperlegleft());
        ibeam.setKneeright(injuryQuery.getKneeright());
        ibeam.setKneeleft(injuryQuery.getKneeleft());
        ibeam.setLowerlegright(injuryQuery.getLowerlegright());
        ibeam.setLowerlegleft(injuryQuery.getLowerlegleft());
        ibeam.setAnkleleft(injuryQuery.getAnkleleft());
        ibeam.setAnkleright(injuryQuery.getAnkleright());
        ibeam.setFootright(injuryQuery.getFootright());
        ibeam.setFootleft(injuryQuery.getFootleft());
        ibeam.setOther(injuryQuery.getOther());
        ibeam.setOtherParts(injuryQuery.getOtherParts());

        ibeam.setCreatedDate(injuryQuery.getCreatedDate());
        ibeam.setCreatedBy(injuryQuery.getCreatedBy());
        ibeam.setCreatedByInitial(injuryQuery.getCreatedByInitial());
        ibeam.setAcknowledgedBy(injuryQuery.getAcknowledgedBy());
        ibeam.setAknowledgeName(injuryQuery.getAknowledgeName());
        ibeam.setAcknowledgedDate(injuryQuery.getAcknowledgedDate());
        ibeam.setVerifiedBy(injuryQuery.getVerifiedBy());
        ibeam.setVerifiedDate(injuryQuery.getVerifiedDate());
        ibeam.setVerifiedByName(injuryQuery.getVerifiedByName());
        ibeam.setClosedDate(injuryQuery.getClosedDate());
        ibeam.setClosedBy(injuryQuery.getClosedBy());
        ibeam.setClosedByName(injuryQuery.getClosedByName());
        ibeam.setDetailsEquipmentStatus(injuryQuery.getDetailsEquipmentStatus());
        ibeam.setDetailsWitnesStatus(injuryQuery.getDetailsWitnesStatus());
        ibeam.setLocationOther(injuryQuery.getLocationOther());
        if (injuryQuery.getSupervisedBy() != null)
        {
            User user = AccountManager.getUser(injuryQuery.getSupervisedBy());
            ibeam.setSupervisedByOID(user.getOID());
            ibeam.setSupervisedBy(injuryQuery.getSupervisedBy());
        }

        ibeam.setTypeOfInjury(injuryQuery.getTypeOfInjury());
        ibeam.setInjuryAssignAfterTreatment(injuryQuery.getInjuryAssignAfterTreatment());
        ibeam.setWatcherBean(injuryQuery.getWatcherBean());
        try
        {
            List<Comment> comments = ProjectManager.getComments(injuryQuery.getPk(), EntityTypeEnum.Injury);
            ibeam.setComments(CommentInfoBean.getCommentInfoBeanList(comments));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ibeam;
    }

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public Integer getProjectPk()
    {
        return projectPk;
    }

    public void setProjectPk(Integer projectPk)
    {
        this.projectPk = projectPk;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectDescription()
    {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription)
    {
        this.projectDescription = projectDescription;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getSitePk()
    {
        return sitePk;
    }

    public void setSitePk(Integer sitePk)
    {
        this.sitePk = sitePk;
    }

    public int getCustodianPk()
    {
        return custodianPk;
    }

    public void setCustodianPk(int custodianPk)
    {
        this.custodianPk = custodianPk;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getInjuryReportNo()
    {
        return injuryReportNo;
    }

    public void setInjuryReportNo(String injuryReportNo)
    {
        this.injuryReportNo = injuryReportNo;
    }

    public String getReferenceNo()
    {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo)
    {
        this.referenceNo = referenceNo;
    }

    public String getInjuredPerson()
    {
        return injuredPerson;
    }

    public void setInjuredPerson(String injuredPerson)
    {
        this.injuredPerson = injuredPerson;
    }

    public Integer getTypeOfPerson()
    {
        return typeOfPerson;
    }

    public void setTypeOfPerson(Integer typeOfPerson)
    {
        this.typeOfPerson = typeOfPerson;
    }

    public Date getDateOfInjury()
    {
        return dateOfInjury;
    }

    public void setDateOfInjury(Date dateOfInjury)
    {
        this.dateOfInjury = dateOfInjury;
    }

    public String getDetailsEquipment()
    {
        return detailsEquipment;
    }

    public void setDetailsEquipment(String detailsEquipment)
    {
        this.detailsEquipment = detailsEquipment;
    }

    public String getDetailsWitnes()
    {
        return detailsWitnes;
    }

    public void setDetailsWitnes(String detailsWitnes)
    {
        this.detailsWitnes = detailsWitnes;
    }

    public String getDetailsTreatment()
    {
        return detailsTreatment;
    }

    public void setDetailsTreatment(String detailsTreatment)
    {
        this.detailsTreatment = detailsTreatment;
    }

    public String getTreatedBy()
    {
        return treatedBy;
    }

    public void setTreatedBy(String treatedBy)
    {
        this.treatedBy = treatedBy;
    }

    public String getRootCauseOfInjury()
    {
        return rootCauseOfInjury;
    }

    public void setRootCauseOfInjury(String rootCauseOfInjury)
    {
        this.rootCauseOfInjury = rootCauseOfInjury;
    }

    public String getSupplimentaryCause()
    {
        return supplimentaryCause;
    }

    public void setSupplimentaryCause(String supplimentaryCause)
    {
        this.supplimentaryCause = supplimentaryCause;
    }

    public String getPrecautionDone()
    {
        return precautionDone;
    }

    public void setPrecautionDone(String precautionDone)
    {
        this.precautionDone = precautionDone;
    }

    public String getPrecautionRequired()
    {
        return precautionRequired;
    }

    public void setPrecautionRequired(String precautionRequired)
    {
        this.precautionRequired = precautionRequired;
    }

    public Boolean getReportingRequired()
    {
        return reportingRequired;
    }

    public void setReportingRequired(Boolean reportingRequired)
    {
        this.reportingRequired = reportingRequired;
    }

    public String getReportingNumber()
    {
        return reportingNumber;
    }

    public void setReportingNumber(String reportingNumber)
    {
        this.reportingNumber = reportingNumber;
    }

    public boolean getHeadfront()
    {
        return headfront;
    }

    public void setHeadfront(boolean headfront)
    {
        this.headfront = headfront;
    }

    public boolean getHeadback()
    {
        return headback;
    }

    public void setHeadback(boolean headback)
    {
        this.headback = headback;
    }

    public boolean getFace()
    {
        return face;
    }

    public void setFace(boolean face)
    {
        this.face = face;
    }

    public boolean getLefteye()
    {
        return lefteye;
    }

    public void setLefteye(boolean lefteye)
    {
        this.lefteye = lefteye;
    }

    public boolean getRighteye()
    {
        return righteye;
    }

    public void setRighteye(boolean righteye)
    {
        this.righteye = righteye;
    }

    public boolean getLeftear()
    {
        return leftear;
    }

    public void setLeftear(boolean leftear)
    {
        this.leftear = leftear;
    }

    public boolean getRightear()
    {
        return rightear;
    }

    public void setRightear(boolean rightear)
    {
        this.rightear = rightear;
    }

    public boolean getNeckfront()
    {
        return neckfront;
    }

    public void setNeckfront(boolean neckfront)
    {
        this.neckfront = neckfront;
    }

    public boolean getNeckback()
    {
        return neckback;
    }

    public void setNeckback(boolean neckback)
    {
        this.neckback = neckback;
    }

    public boolean getUpperbodyfront()
    {
        return upperbodyfront;
    }

    public void setUpperbodyfront(boolean upperbodyfront)
    {
        this.upperbodyfront = upperbodyfront;
    }

    public boolean getUpperbodyback()
    {
        return upperbodyback;
    }

    public void setUpperbodyback(boolean upperbodyback)
    {
        this.upperbodyback = upperbodyback;
    }

    public boolean getLowerbodyfront()
    {
        return lowerbodyfront;
    }

    public void setLowerbodyfront(boolean lowerbodyfront)
    {
        this.lowerbodyfront = lowerbodyfront;
    }

    public boolean getLowerbodyback()
    {
        return lowerbodyback;
    }

    public void setLowerbodyback(boolean lowerbodyback)
    {
        this.lowerbodyback = lowerbodyback;
    }

    public boolean getShoulderright()
    {
        return shoulderright;
    }

    public void setShoulderright(boolean shoulderright)
    {
        this.shoulderright = shoulderright;
    }

    public boolean getShoulderleft()
    {
        return shoulderleft;
    }

    public void setShoulderleft(boolean shoulderleft)
    {
        this.shoulderleft = shoulderleft;
    }

    public boolean getUpperarmright()
    {
        return upperarmright;
    }

    public void setUpperarmright(boolean upperarmright)
    {
        this.upperarmright = upperarmright;
    }

    public boolean getUpperarmleft()
    {
        return upperarmleft;
    }

    public void setUpperarmleft(boolean upperarmleft)
    {
        this.upperarmleft = upperarmleft;
    }

    public boolean getLowerarmright()
    {
        return lowerarmright;
    }

    public void setLowerarmright(boolean lowerarmright)
    {
        this.lowerarmright = lowerarmright;
    }

    public boolean getLowerarmleft()
    {
        return lowerarmleft;
    }

    public void setLowerarmleft(boolean lowerarmleft)
    {
        this.lowerarmleft = lowerarmleft;
    }

    public boolean getElbowright()
    {
        return elbowright;
    }

    public void setElbowright(boolean elbowright)
    {
        this.elbowright = elbowright;
    }

    public boolean getElbowleft()
    {
        return elbowleft;
    }

    public void setElbowleft(boolean elbowleft)
    {
        this.elbowleft = elbowleft;
    }

    public boolean getWristleft()
    {
        return wristleft;
    }

    public void setWristleft(boolean wristleft)
    {
        this.wristleft = wristleft;
    }

    public boolean getWristright()
    {
        return wristright;
    }

    public void setWristright(boolean wristright)
    {
        this.wristright = wristright;
    }

    public boolean getFingersleft()
    {
        return fingersleft;
    }

    public void setFingersleft(boolean fingersleft)
    {
        this.fingersleft = fingersleft;
    }

    public boolean getFingersright()
    {
        return fingersright;
    }

    public void setFingersright(boolean fingersright)
    {
        this.fingersright = fingersright;
    }

    public boolean getUpperlegright()
    {
        return upperlegright;
    }

    public void setUpperlegright(boolean upperlegright)
    {
        this.upperlegright = upperlegright;
    }

    public boolean getUpperlegleft()
    {
        return upperlegleft;
    }

    public void setUpperlegleft(boolean upperlegleft)
    {
        this.upperlegleft = upperlegleft;
    }

    public boolean getKneeright()
    {
        return kneeright;
    }

    public void setKneeright(boolean kneeright)
    {
        this.kneeright = kneeright;
    }

    public boolean getKneeleft()
    {
        return kneeleft;
    }

    public void setKneeleft(boolean kneeleft)
    {
        this.kneeleft = kneeleft;
    }

    public boolean getLowerlegright()
    {
        return lowerlegright;
    }

    public void setLowerlegright(boolean lowerlegright)
    {
        this.lowerlegright = lowerlegright;
    }

    public boolean getLowerlegleft()
    {
        return lowerlegleft;
    }

    public void setLowerlegleft(boolean lowerlegleft)
    {
        this.lowerlegleft = lowerlegleft;
    }

    public boolean getAnkleleft()
    {
        return ankleleft;
    }

    public void setAnkleleft(boolean ankleleft)
    {
        this.ankleleft = ankleleft;
    }

    public boolean getAnkleright()
    {
        return ankleright;
    }

    public void setAnkleright(boolean ankleright)
    {
        this.ankleright = ankleright;
    }

    public boolean getFootright()
    {
        return footright;
    }

    public void setFootright(boolean footright)
    {
        this.footright = footright;
    }

    public boolean getFootleft()
    {
        return footleft;
    }

    public void setFootleft(boolean footleft)
    {
        this.footleft = footleft;
    }

    public boolean getOther()
    {
        return other;
    }

    public void setOther(boolean other)
    {
        this.other = other;
    }

    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedByInitial()
    {
        return createdByInitial;
    }

    public void setCreatedByInitial(String createdByInitial)
    {
        this.createdByInitial = createdByInitial;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Integer getAcknowledgedBy()
    {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(Integer acknowledgedBy)
    {
        this.acknowledgedBy = acknowledgedBy;
    }

    public String getAknowledgeName()
    {
        return aknowledgeName;
    }

    public void setAknowledgeName(String aknowledgeName)
    {
        this.aknowledgeName = aknowledgeName;
    }

    public Date getAcknowledgedDate()
    {
        return acknowledgedDate;
    }

    public void setAcknowledgedDate(Date acknowledgedDate)
    {
        this.acknowledgedDate = acknowledgedDate;
    }

    public Integer getVerifiedBy()
    {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy)
    {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedByName()
    {
        return verifiedByName;
    }

    public void setVerifiedByName(String verifiedByName)
    {
        this.verifiedByName = verifiedByName;
    }

    public Date getVerifiedDate()
    {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate)
    {
        this.verifiedDate = verifiedDate;
    }

    public Integer getClosedBy()
    {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy)
    {
        this.closedBy = closedBy;
    }

    public String getClosedByName()
    {
        return closedByName;
    }

    public void setClosedByName(String closedByName)
    {
        this.closedByName = closedByName;
    }

    public Date getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate(Date closedDate)
    {
        this.closedDate = closedDate;
    }

    public List<InjuryAssignAfterTreatmentBean> getInjuryAssignAfterTreatment()
    {
        return injuryAssignAfterTreatment;
    }

    public void setInjuryAssignAfterTreatment(List<InjuryAssignAfterTreatmentBean> injuryAssignAfterTreatment)
    {
        this.injuryAssignAfterTreatment = injuryAssignAfterTreatment;
    }

    public Boolean getDetailsEquipmentStatus()
    {
        return detailsEquipmentStatus;
    }

    public void setDetailsEquipmentStatus(Boolean detailsEquipmentStatus)
    {
        this.detailsEquipmentStatus = detailsEquipmentStatus;
    }

    public Boolean getDetailsWitnesStatus()
    {
        return detailsWitnesStatus;
    }

    public void setDetailsWitnesStatus(Boolean detailsWitnesStatus)
    {
        this.detailsWitnesStatus = detailsWitnesStatus;
    }

    public List<AttachmentInfoBean> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<AttachmentInfoBean> attachments)
    {
        this.attachments = attachments;
    }

    public List<CommentInfoBean> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentInfoBean> comments)
    {
        this.comments = comments;
    }

}

