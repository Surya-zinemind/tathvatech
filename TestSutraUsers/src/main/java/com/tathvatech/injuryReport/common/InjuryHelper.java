package com.tathvatech.injuryReport.common;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.service.InjuryAssignAfterTreatmentManager;
import com.tathvatech.injuryReport.service.InjuryLocationMasterManager;
import com.tathvatech.injuryReport.service.WatcherManager;
import com.tathvatech.project.entity.Project;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.workstation.common.WorkstationQuery;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class InjuryHelper
{
    private final ProjectService projectService;
    private final WorkstationService workstationService;
    private final SiteService siteService;
    private final AccountService accountService;
    private final CommonServiceManager commonServiceManager;
    private final InjuryAssignAfterTreatmentManager injuryAssignAfterTreatmentManager;
    private final WatcherManager watcherManager;
    private final InjuryLocationMasterManager injuryLocationMasterManager;
    public  InjuryBean getBean(Injury injury)
    {
        InjuryBean bean = null;
        if (injury != null && injury.getPk() > 0)
        {
            bean = new InjuryBean(accountService);
            bean.setPk((int) injury.getPk());
            bean.setProjectPk(injury.getProjectPk());
            if (injury.getProjectPk() != null && injury.getProjectPk() > 0)
            {
                Project project = projectService.getProject(injury.getProjectPk());
                if (project != null)
                {
                    ProjectOID projectOID = project.getOID();
                    bean.setProjectOID(projectOID);
                    bean.setProjectName(projectOID.getDisplayText());

                    bean.setProjectDescription(project.getProjectDescription());
                }
            }

            if (injury.getLocationPk() > 0)
            {
                bean.setLocationPk(injury.getLocationPk());
                bean.setLocationType(injury.getLocationType());
                if (injury.getLocationType().equals("Workstation"))
                {
                    WorkstationQuery workstation;
                    try
                    {
                        workstation =workstationService
                                .getWorkstationQueryByPk(new WorkstationOID(injury.getLocationPk()));
                        bean.setLocationName(workstation.getOID().getDisplayText());
                        bean.setLocation(new WorkstationOID((int) workstation.getPk(), workstation.getDisplayText()));

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else if (injury.getLocationType().equals("Location"))
                {
                    InjuryLocationMasterQuery inLocationMaster;
                    try
                    {
                        inLocationMaster = injuryLocationMasterManager
                                .getInjuryLocationMasterQueryByPk(injury.getLocationPk());
                        bean.setLocationName(inLocationMaster.getName());
                        bean.setLocation(inLocationMaster.getOID());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            bean.setLocationOther(injury.getLocationOther());
            bean.setDescription(injury.getDescription());
            bean.setSitePk(injury.getSitePk());
            if (injury.getSitePk() > 0)
            {
                Site site = siteService.getSite(injury.getSitePk());
                SiteOID siteOID = site.getOID();
                bean.setSiteName(siteOID.getDisplayText());
                bean.setSiteOID(new SiteOID(site.getPk(), site.getName() + " - " + site.getDescription()));
            }
            bean.setCustodianPk(injury.getCustodianPk());
            bean.setStatus(injury.getStatus());
            bean.setInjuryReportNo(injury.getInjuryReportNo());
            bean.setReferenceRequired(injury.getReferenceRequired());
            bean.setReferenceNo(injury.getReferenceNo());
            bean.setInjuredPerson(injury.getInjuredPerson());
            bean.setTypeOfPerson(injury.getTypeOfPerson());
            bean.setDateOfInjury(injury.getDateOfInjury());
            bean.setDetailsEquipmentStatus(injury.getDetailsEquipmentStatus());
            bean.setDetailsEquipment(injury.getDetailsEquipment());
            bean.setDetailsWitnesStatus(injury.getDetailsWitnesStatus());
            bean.setDetailsWitnes(injury.getDetailsWitnes());
            bean.setDetailsTreatment(injury.getDetailsTreatment());
            bean.setTreatedBy(injury.getTreatedBy());
            bean.setRootCauseOfInjury(injury.getRootCauseOfInjury());
            bean.setSupplimentaryCause(injury.getSupplimentaryCause());
            bean.setPrecautionDone(injury.getPrecautionDone());
            bean.setPrecautionRequired(injury.getPrecautionRequired());
            bean.setReportingRequired(injury.getReportingRequired());
            bean.setReportingNumber(injury.getReportingNumber());
            bean.setLastUpdated(injury.getLastUpdated());
            if (injury.getSupervisedBy() != null)
            {
                User supervisor = accountService.getUser(injury.getSupervisedBy());
                bean.setSupervisedByOID(supervisor.getOID());
                bean.setSupervisedBy(injury.getSupervisedBy());
            }

            bean.setTypeOfInjury(injury.getTypeOfInjury());

            // body part
            bean.setFatality(injury.getFatality());
            bean.setHeadfront(injury.getHeadfront());
            bean.setHeadback(injury.getHeadback());
            bean.setFace(injury.getFace());
            bean.setLefteye(injury.getLefteye());
            bean.setRighteye(injury.getRighteye());
            bean.setLeftear(injury.getLeftear());
            bean.setRightear(injury.getRightear());
            bean.setNeckfront(injury.getNeckfront());
            bean.setNeckback(injury.getNeckback());
            bean.setUpperbodyfront(injury.getUpperbodyfront());
            bean.setUpperbodyback(injury.getUpperbodyback());
            bean.setLowerbodyfront(injury.getLowerbodyfront());
            bean.setLowerbodyback(injury.getLowerbodyback());
            bean.setShoulderright(injury.getShoulderright());
            bean.setShoulderleft(injury.getShoulderleft());
            bean.setUpperarmright(injury.getUpperarmright());
            bean.setUpperarmleft(injury.getUpperarmleft());
            bean.setLowerarmright(injury.getLowerarmright());
            bean.setLowerarmleft(injury.getLowerarmleft());
            bean.setElbowright(injury.getElbowright());
            bean.setElbowleft(injury.getElbowleft());
            bean.setWristleft(injury.getWristleft());
            bean.setWristright(injury.getWristright());
            bean.setFingersleft(injury.getFingersleft());
            bean.setFingersright(injury.getFingersright());
            bean.setUpperlegright(injury.getUpperlegright());
            bean.setUpperlegleft(injury.getUpperlegleft());
            bean.setKneeright(injury.getKneeright());
            bean.setKneeleft(injury.getKneeleft());
            bean.setLowerlegright(injury.getLowerlegright());
            bean.setLowerlegleft(injury.getLowerlegleft());
            bean.setAnkleleft(injury.getAnkleleft());
            bean.setAnkleright(injury.getAnkleright());
            bean.setFootright(injury.getFootright());
            bean.setFootleft(injury.getFootleft());
            bean.setOther(injury.getOther());
            bean.setOtherParts(injury.getOtherParts());
            // body part

            try
            {
                List<Comment> comments = ProjectManager.getComments(injury.getPk(), EntityTypeEnum.Injury);
                List<CommentInfoBean> commentList = CommentInfoBean.getCommentInfoBeanList(comments);
                bean.setComments(commentList);
            }
            catch (Exception e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // action status
            bean.setCreatedBy(injury.getCreatedBy());
            if (injury.getCreatedByInitial() != null)
            {
                bean.setCreatedByInitial(injury.getCreatedByInitial());
            } else
            {
                User user =accountService.getUser(injury.getCreatedBy());
                bean.setCreatedByInitial(user.getDisplayString());
            }
            bean.setCreatedDate(injury.getCreatedDate());
            if (injury.getAcknowledgedBy() != null)
            {
                bean.setAcknowledgedBy(injury.getAcknowledgedBy());
                User acknowledgedBy =accountService.getUser(injury.getAcknowledgedBy());
                bean.setAknowledgeName(acknowledgedBy.getDisplayString());
                bean.setAcknowledgedDate(injury.getAcknowledgedDate());
            }
            if (injury.getVerifiedBy() != null)
            {
                bean.setVerifiedBy(injury.getVerifiedBy());
                User verifiedBy = accountService.getUser(injury.getVerifiedBy());
                bean.setVerifiedByName(verifiedBy.getDisplayString());
                bean.setVerifiedDate(injury.getVerifiedDate());
            }
            if (injury.getClosedBy() != null)
            {
                bean.setClosedBy(injury.getClosedBy());
                User closedBy = accountService.getUser(injury.getClosedBy());
                bean.setClosedByName(closedBy.getDisplayString());
                bean.setClosedDate(injury.getClosedDate());
            }
            // action status

            try
            {
                bean.setInjuryAssignAfterTreatment(
                        injuryAssignAfterTreatmentManager.getAssignAfterTreatmentBeanByInjuryPk((int) injury.getPk()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                bean.setWatcherBean(
                        watcherManager.getWatcherBeanByObjectTypeAndObjectPk((int) injury.getPk(), EntityTypeEnum.Injury));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            bean.setAttachments(
                    commonServiceManager.getAttachments((int) injury.getPk(), EntityTypeEnum.Injury.getValue()));

        }
        return bean;
    }

}

