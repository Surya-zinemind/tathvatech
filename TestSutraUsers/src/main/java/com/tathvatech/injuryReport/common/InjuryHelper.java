package com.tathvatech.injuryReport.common;

import java.util.List;

import com.sarvasutra.etest.api.model.CommentInfoBean;
import com.tathvatech.testsutra.injury.service.Injury;
import com.tathvatech.testsutra.injury.service.InjuryAssignAfterTreatmentManager;
import com.tathvatech.testsutra.injury.service.InjuryLocationMasterManager;
import com.tathvatech.testsutra.injury.service.WatcherManager;
import com.tathvatech.testsutra.suggestionscheme.SuggestionSchemeEntityTypeEnum;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.delegate.AccountDelegate;
import com.tathvatech.ts.core.common.Comment;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.service.CommonServicesDelegate;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.tathvatech.ts.core.project.WorkstationQuery;
import com.tathvatech.ts.core.sites.Site;
import com.tathvatech.ts.core.sites.SiteOID;
import com.thirdi.surveyside.project.Project;
import com.thirdi.surveyside.project.ProjectDelegate;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.project.SiteDelegate;

public class InjuryHelper
{
    public static InjuryBean getBean(Injury injury)
    {
        InjuryBean bean = null;
        if (injury != null && injury.getPk() > 0)
        {
            bean = new InjuryBean();
            bean.setPk(injury.getPk());
            bean.setProjectPk(injury.getProjectPk());
            if (injury.getProjectPk() != null && injury.getProjectPk() > 0)
            {
                Project project = ProjectDelegate.getProject(injury.getProjectPk());
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
                        workstation = ProjectManager
                                .getWorkstationQueryByPk(new WorkstationOID(injury.getLocationPk()));
                        bean.setLocationName(workstation.getOID().getDisplayText());
                        bean.setLocation(new WorkstationOID(workstation.getPk(), workstation.getDisplayText()));

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
                        inLocationMaster = InjuryLocationMasterManager
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
                Site site = SiteDelegate.getSite(injury.getSitePk());
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
                User supervisor = AccountDelegate.getUser(injury.getSupervisedBy());
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
                User user = AccountDelegate.getUser(injury.getCreatedBy());
                bean.setCreatedByInitial(user.getDisplayString());
            }
            bean.setCreatedDate(injury.getCreatedDate());
            if (injury.getAcknowledgedBy() != null)
            {
                bean.setAcknowledgedBy(injury.getAcknowledgedBy());
                User acknowledgedBy = AccountDelegate.getUser(injury.getAcknowledgedBy());
                bean.setAknowledgeName(acknowledgedBy.getDisplayString());
                bean.setAcknowledgedDate(injury.getAcknowledgedDate());
            }
            if (injury.getVerifiedBy() != null)
            {
                bean.setVerifiedBy(injury.getVerifiedBy());
                User verifiedBy = AccountDelegate.getUser(injury.getVerifiedBy());
                bean.setVerifiedByName(verifiedBy.getDisplayString());
                bean.setVerifiedDate(injury.getVerifiedDate());
            }
            if (injury.getClosedBy() != null)
            {
                bean.setClosedBy(injury.getClosedBy());
                User closedBy = AccountDelegate.getUser(injury.getClosedBy());
                bean.setClosedByName(closedBy.getDisplayString());
                bean.setClosedDate(injury.getClosedDate());
            }
            // action status

            try
            {
                bean.setInjuryAssignAfterTreatment(
                        InjuryAssignAfterTreatmentManager.getAssignAfterTreatmentBeanByInjuryPk(injury.getPk()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                bean.setWatcherBean(
                        WatcherManager.getWatcherBeanByObjectTypeAndObjectPk(injury.getPk(), EntityTypeEnum.Injury));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            bean.setAttachments(
                    CommonServicesDelegate.getAttachments(injury.getPk(), EntityTypeEnum.Injury.getValue()));

        }
        return bean;
    }

}

