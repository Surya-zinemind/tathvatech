package com.tathvatech.injuryReport.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.ModeOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_MODE")
public class Mode extends AbstractEntity implements Serializable
{
    @Id
    private long pk;
    private int projectPk;
    private String modeNo;
    private Integer assignedTo;
    private String revision;
    private Integer siteId;
    private String drlRef;
    private String carTypes;
    private String productionOrder;
    private String category;
    private String noticeReason;
    private String toBeActionedBy;
    private Float hours;
    private Integer completedBy;
    private Date completedDate;
    private Integer verifiedBy;
    private Date verifiedDate;
    private String verifyComment;
    private Integer approvedBy;
    private Date approvedDate;
    private String approveComment;
    private String description;
    private String comments;
    private String partsRequiredDesc;
    private Integer createdBy;
    private Date createdDate;
    private Integer ownerPk;
    private String status;
    private Date updateDate;
    private Date lastUpdated;


    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getProjectPk() {
        return projectPk;
    }

    public void setProjectPk(int projectPk) {
        this.projectPk = projectPk;
    }

    public String getModeNo() {
        return modeNo;
    }

    public void setModeNo(String modeNo) {
        if(modeNo != null)
            this.modeNo = modeNo.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPartsRequiredDesc() {
        return partsRequiredDesc;
    }

    public void setPartsRequiredDesc(String partsRequiredDesc) {
        this.partsRequiredDesc = partsRequiredDesc;
    }

    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Integer getOwnerPk() {
        return ownerPk;
    }

    public void setOwnerPk(Integer ownerPk) {
        this.ownerPk = ownerPk;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getDrlRef() {
        return drlRef;
    }

    public void setDrlRef(String drlRef) {
        this.drlRef = drlRef;
    }

    public String getCarTypes() {
        return carTypes;
    }

    public void setCarTypes(String carTypes) {
        this.carTypes = carTypes;
    }

    public String getProductionOrder() {
        return productionOrder;
    }

    public void setProductionOrder(String productionOrder) {
        this.productionOrder = productionOrder;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNoticeReason() {
        return noticeReason;
    }

    public void setNoticeReason(String noticeReason) {
        this.noticeReason = noticeReason;
    }

    public String getToBeActionedBy() {
        return toBeActionedBy;
    }

    public void setToBeActionedBy(String toBeActionedBy) {
        this.toBeActionedBy = toBeActionedBy;
    }

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    public Integer getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(Integer completedBy) {
        this.completedBy = completedBy;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getVerifyComment() {
        return verifyComment;
    }

    public void setVerifyComment(String verifyComment) {
        this.verifyComment = verifyComment;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public ModeOID getOID() {
        return new ModeOID((int) pk, modeNo);
    }


    @Override
    public int hashCode() {
        return (int) pk;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Mode && ((Mode)obj).getPk() == pk)
            return true;
        return false;
    }



    public static String STATUS_OPEN = "Open";
    public static final String STATUS_Verified = "Completed";
    public static final String STATUS_CLOSED = "Closed";
    public static final String COMMENTCONTEXT_GENERAL = "General";
}

