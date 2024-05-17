package com.tathvatech.injuryReport.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.tathvatech.common.common.QueryObject;
import com.tathvatech.injuryReport.common.*;
import com.tathvatech.injuryReport.controller.InjuryAssignAfterTreatmentController;
import com.tathvatech.injuryReport.controller.WatcherController;
import com.tathvatech.injuryReport.email.InjuryEmailSender;
import com.tathvatech.injuryReport.entity.InjuryAfterTreatment;
import com.tathvatech.injuryReport.entity.Mode;
import com.tathvatech.injuryReport.enums.DateLimit;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.controller.InjuryLocationMasterController;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.entity.InjuryLocationMaster;
import com.tathvatech.injuryReport.utils.InjuryReportSequenceKeyGenerator;
import com.tathvatech.user.OID.LocationTypeOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.common.WorkstationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class InjuryServiceImpl implements  InjuryService
{
    private static final Logger logger = LoggerFactory.getLogger(InjuryServiceImpl.class);
    private final PersistWrapper persistWrapper;
    private final SiteService siteService;
    private final AccountService accountService;
    private final CommonServiceManager commonServiceManager;
    private final WatcherManager watcherManager;
    private final InjuryAssignAfterTreatmentManager injuryAssignAfterTreatmentManager;
    private InjuryEmailSender injuryEmailSender;
    private Injury injury;
    private  InjuryHelper injuryHelper;
    private InjuryReportGraphQuery injuryReportGraphQuery;
  private Attachments attachmentsObj;
    private static String ACTIVE = "Active";

    public InjuryServiceImpl(PersistWrapper persistWrapper, SiteService siteService, AccountService accountService, CommonServiceManager commonServiceManager, WatcherManager watcherManager, InjuryAssignAfterTreatmentManager injuryAssignAfterTreatmentManager) {
        this.persistWrapper = persistWrapper;
        this.siteService = siteService;
        this.accountService = accountService;
        this.commonServiceManager = commonServiceManager;

        this.watcherManager = watcherManager;
        this.injuryAssignAfterTreatmentManager = injuryAssignAfterTreatmentManager;
    }


    public  InjuryBean save(UserContext context, InjuryBean bean, List<AttachmentIntf> attachments)
            throws Exception
    {
        boolean isSupervisorUpdated = false;
        Integer previousSupervisor = null;
        Injury injury = null;
        if (bean.getPk() > 0)
        {
            injury = (Injury) persistWrapper.readByPrimaryKey(Injury.class, bean.getPk());
        } else
        {

            injury.setCreatedBy((int) context.getUser().getPk());
            injury.setCreatedDate(new Date());
            injury.setCreatedByInitial(bean.getCreatedByInitial());
            Site site = null;
            if (bean.getSiteOID() != null)
            {
                site = siteService.getSite((int) bean.getSiteOID().getPk());

            } else
            {
                site = siteService.getSite(bean.getSitePk());

            }
            String seqNo = new InjuryReportSequenceKeyGenerator((int) site.getPk(),site.getName()).getNextSeq();
            injury.setInjuryReportNo(seqNo);
            injury.setStatus(Injury.StatusEnum.Open.name());
        }

        if (bean.getSiteOID() != null)
        {
            injury.setSitePk((int) bean.getSiteOID().getPk());
        } else
        {
            injury.setSitePk(bean.getSitePk());
        }
        if (bean.getProjectOID() != null)
        {
            injury.setProjectPk((int) bean.getProjectOID().getPk());
        } else
        {
            injury.setProjectPk(bean.getProjectPk());
        }
        if (bean.getLocation() != null)
        {
            if (bean.getLocation() instanceof WorkstationOID)
            {
                WorkstationOID wQuery = (WorkstationOID) bean.getLocation();
                injury.setLocationPk((int) wQuery.getPk());
                injury.setLocationType(EntityTypeEnum.Workstation.name());
            } else if (bean.getLocation() instanceof LocationTypeOID)
            {
                LocationTypeOID wQuery = (LocationTypeOID) bean.getLocation();
                if (wQuery.getPk() > 0)
                {
                    injury.setLocationPk((int) wQuery.getPk());
                    injury.setLocationType("Location");
                } else
                {
                    InjuryLocationMasterBean ilmBean = new InjuryLocationMasterBean();
                    // ilmBean.setPk(wQuery.getPk());
                    ilmBean.setName(wQuery.getDisplayText());
                    ilmBean.setParentPk(1);
                    ilmBean.setStatus("Active");
                    //InjuryLocationMaster locationMaster = injuryLocationMasterManager.create(context, injuryLocationMasterBean);
                   // injury.setLocationPk((int) locationMaster.getPk());
                    injury.setLocationType("Location");
                }
            }
        } else
        {
            injury.setLocationPk(bean.getLocationPk());
            injury.setLocationType(bean.getLocationType());
            injury.setLocationOther(bean.getLocationOther());
        }

        injury.setCustodianPk(bean.getCustodianPk());
        injury.setTypeOfInjury(bean.getTypeOfInjury());
        injury.setDescription(bean.getDescription());
        injury.setInjuredPerson(bean.getInjuredPerson());
        injury.setTypeOfPerson(bean.getTypeOfPerson());
        injury.setDateOfInjury(bean.getDateOfInjury());
        injury.setDetailsEquipment(bean.getDetailsEquipment());
        injury.setDetailsWitnes(bean.getDetailsWitnes());
        injury.setDetailsTreatment(bean.getDetailsTreatment());
        injury.setTreatedBy(bean.getTreatedBy());
        injury.setRootCauseOfInjury(bean.getRootCauseOfInjury());
        injury.setSupplimentaryCause(bean.getSupplimentaryCause());
        injury.setPrecautionDone(bean.getPrecautionDone());
        injury.setPrecautionRequired(bean.getPrecautionRequired());
        injury.setReportingRequired(bean.getReportingRequired());
        injury.setReportingNumber(bean.getReportingNumber());
        injury.setReferenceRequired(bean.getReferenceRequired());
        injury.setReferenceNo(bean.getReferenceNo());
        injury.setDetailsEquipmentStatus(bean.getDetailsEquipmentStatus());
        injury.setDetailsWitnesStatus(bean.getDetailsWitnesStatus());
        if (bean.getSupervisedByOID() == null && bean.getSupervisedBy() != null)
        {
            User supervisor = accountService.getUser(bean.getSupervisedBy());
            bean.setSupervisedByOID(supervisor.getOID());
        }
        if (injury.getSupervisedBy() != null && bean.getSupervisedByOID() != null
                && !injury.getSupervisedBy().equals(bean.getSupervisedByOID().getPk()))
        {
            isSupervisorUpdated = true;
            previousSupervisor = injury.getSupervisedBy();
        }
        if (bean.getSupervisedByOID() != null)
        {
            injury.setSupervisedBy((int) bean.getSupervisedByOID().getPk());
        } else
        {
            injury.setSupervisedBy(null);
        }

        // injury.setStatus(bean.getStatus());

        // body-part
        injury.setFatality(bean.getFatality());
        injury.setHeadfront(bean.getHeadfront());
        injury.setHeadback(bean.getHeadback());
        injury.setFace(bean.getFace());
        injury.setLefteye(bean.getLefteye());
        injury.setRighteye(bean.getRighteye());
        injury.setLeftear(bean.getLeftear());
        injury.setRightear(bean.getRightear());
        injury.setNeckfront(bean.getNeckfront());
        injury.setNeckback(bean.getNeckback());
        injury.setUpperbodyfront(bean.getUpperbodyfront());
        injury.setUpperbodyback(bean.getUpperbodyback());
        injury.setLowerbodyfront(bean.getLowerbodyfront());
        injury.setLowerbodyback(bean.getLowerbodyback());
        injury.setShoulderright(bean.getShoulderright());
        injury.setShoulderleft(bean.getShoulderleft());
        injury.setUpperarmright(bean.getUpperarmright());
        injury.setUpperarmleft(bean.getUpperarmleft());
        injury.setLowerarmright(bean.getLowerarmright());
        injury.setLowerarmleft(bean.getLowerarmleft());
        injury.setElbowright(bean.getElbowright());
        injury.setElbowleft(bean.getElbowleft());
        injury.setWristleft(bean.getWristleft());
        injury.setWristright(bean.getWristright());
        injury.setFingersleft(bean.getFingersleft());
        injury.setFingersright(bean.getFingersright());
        injury.setUpperlegright(bean.getUpperlegright());
        injury.setUpperlegleft(bean.getUpperlegleft());
        injury.setKneeright(bean.getKneeright());
        injury.setKneeleft(bean.getKneeleft());
        injury.setLowerlegright(bean.getLowerlegright());
        injury.setLowerlegleft(bean.getLowerlegleft());
        injury.setAnkleleft(bean.getAnkleleft());
        injury.setAnkleright(bean.getAnkleright());
        injury.setFootright(bean.getFootright());
        injury.setFootleft(bean.getFootleft());
        injury.setOther(bean.getOther());
        injury.setOtherParts(bean.getOtherParts());
        // body-part

        int pk;
        if (bean.getPk() > 0)
        {
            persistWrapper.update(injury);
            pk = (int) injury.getPk();
        } else
        {
            pk = (int) persistWrapper.createEntity(injury);
        }
        // fetch the new project back

        commonServiceManager.saveAttachments( context, pk,
                EntityTypeEnum.Injury.getValue(), attachments, true);

        List<WatcherBean> previousWatcher = watcherManager.getWatcherBeanByObjectTypeAndObjectPk(pk,
                EntityTypeEnum.Injury);
        if (bean.getWatcherBean() != null)
        {
            for (WatcherBean watcherBean : bean.getWatcherBean())
            {
                if (watcherBean.getPk() < 1)
                {
                    watcherBean.setObjectPk(pk);
                    watcherBean.setObjectType(Integer.toString(EntityTypeEnum.Injury.getValue()));
                    watcherBean.setCreatedDate(new Date());
                    watcherBean.setStatus("Active");
                    watcherBean = watcherManager.createWatcher(context, watcherBean);
                }
                previousWatcher.remove(watcherBean);
            }
        }
        if (previousWatcher != null)
        {
            for (WatcherBean watcherBean : previousWatcher)
            {
                watcherManager.deleteWatcher(watcherBean.getPk());
            }
        }
        List<InjuryAssignAfterTreatmentBean> previousAfterTreatment = injuryAssignAfterTreatmentManager
                .getAssignAfterTreatmentBeanByInjuryPk(pk);
        if (bean.getInjuryAssignAfterTreatment() != null)
        {
            for (InjuryAssignAfterTreatmentBean assignAfterTreatmentBean : bean.getInjuryAssignAfterTreatment())
            {
                if (assignAfterTreatmentBean.getPk() < 1)
                {
                    assignAfterTreatmentBean.setInjuryPk(pk);
                    //injuryAssignAfterTreatmentManager.create(context, assignAfterTreatmentBean);
                }
                previousAfterTreatment.remove(assignAfterTreatmentBean);
            }
        }
        if (previousAfterTreatment != null)
        {
            for (InjuryAssignAfterTreatmentBean assignAfterTreatmentBean : previousAfterTreatment)
            {
                injuryAssignAfterTreatmentManager.deleteAssignAfterTreatment(pk,
                        assignAfterTreatmentBean.getAfterTreatmentMasterPk());
            }
        }
        if (isSupervisorUpdated)
        {
            InjuryQuery iQ = getInjuryReportByPk(pk);
            User supervisor = accountService.getUser(previousSupervisor);
            ProjectManager.addComment(
                    context, pk, EntityTypeEnum.Injury.getValue(), "Supervisor changed from " + iQ.getSupervisedByName()
                            + " to " + supervisor.getDisplayString() + " by " + context.getUser(),
                    Mode.COMMENTCONTEXT_GENERAL);
            injuryEmailSender.notifyInjuryReportSupervisorChanged(context, iQ, supervisor);
        }
        return getInjuryReportBean(new InjuryOID(pk, injury.getInjuryReportNo()));
    }

    public  InjuryQuery create(UserContext context, InjuryBean bean, List<AttachmentIntf> attachments,
                                     boolean isAndroidDevice) throws Exception
    {

        BeanUtils.copyProperties(injury, bean);
        injury.setCreatedDate(new Date());

        Site site = siteService.getSite(bean.getSitePk());
        String seqNo = new InjuryReportSequenceKeyGenerator((int) site.getPk(),site.getName()).getNextSeq();
        injury.setInjuryReportNo(seqNo);
        if (bean.getLocationPk() == 1 && bean.getLocationType().equals("Location"))
        {
            InjuryLocationMaster injuryLocationMaster = persistWrapper.read(InjuryLocationMaster.class,
                    "select * from injury_location_master where name=?", bean.getLocationOther().trim());
            if (injuryLocationMaster != null && injuryLocationMaster.getPk() > 0)
            {
                injury.setLocationPk((int) injuryLocationMaster.getPk());
                injury.setLocationType("Location");
            } else
            {
                InjuryLocationMasterBean ilmBean = new InjuryLocationMasterBean();
                // ilmBean.setPk(wQuery.getPk());
                ilmBean.setName(bean.getLocationOther());
                ilmBean.setParentPk(1);
                ilmBean.setStatus("Active");
               // InjuryLocationMaster locationMaster = injuryLocationMasterManager.create(context, injuryLocationMasterBean);
                //injury.setLocationPk((int) locationMaster.getPk());
                injury.setLocationType("Location");
            }
        }

        int pk = (int) persistWrapper.createEntity(injury);
        if (isAndroidDevice)
        {
            if (bean.getAttachments() != null && bean.getAttachments().size() > 0)
            {
                attachmentsObj.saveResponseImage(context, pk, EntityTypeEnum.Injury, bean.getAttachments());
            }
        } else
        {
            if (attachments != null)
            {
                commonServiceManager.saveAttachments(context, pk, EntityTypeEnum.Injury.getValue(), attachments, true);
            }
        }
        if (bean.getInjuryAssignAfterTreatment() != null)
        {
            List<InjuryAssignAfterTreatmentBean> listAssignAfterTreatmentBean = bean.getInjuryAssignAfterTreatment();
            for (int i = 0; i < listAssignAfterTreatmentBean.size(); i++)
            {
                InjuryAssignAfterTreatmentBean assignAfterTreatmentBean = new InjuryAssignAfterTreatmentBean();
                assignAfterTreatmentBean.setInjuryPk(pk);
                assignAfterTreatmentBean
                        .setAfterTreatmentMasterPk(listAssignAfterTreatmentBean.get(i).getAfterTreatmentMasterPk());

                //injuryAssignAfterTreatmentManager.create(context, assignAfterTreatmentBean);
            }
        }
        if (bean.getWatcherBean() != null)
        {
            List<WatcherBean> listInjuryWatcherList = new ArrayList<WatcherBean>();
            listInjuryWatcherList = bean.getWatcherBean();
            for (int i = 0; i < listInjuryWatcherList.size(); i++)
            {
                // WatcherBean watcherList = new WatcherBean();
                WatcherBean watcherBean = new WatcherBean();
                watcherBean.setObjectPk(pk);
                watcherBean.setObjectType(Integer.toString(EntityTypeEnum.Injury.getValue()));
                watcherBean.setUserPk(listInjuryWatcherList.get(i).getUserPk());
                // watcherBean.setCreatedBy(context.getUser().getPk());
                watcherBean.setCreatedDate(new Date());
                watcherBean.setStatus("Active");
               // watcherBean = watcherManager.createWatcher(context, watcherBean);
            }
        }
        // fetch the new project back
        InjuryQuery injuryQuery = getInjuryReportByPk(pk);
        return injuryQuery;

    }

    // public static Injury saveInjuryReport(UserContext context, Injury injury,
    // List<AttachmentIntf> attachments)
    // throws Exception
    // {
    // Site site = SiteDelegate.getSite(injury.getSitePk());
    // String seqNo = new InjuryReportSequenceKeyGenerator((SiteOID)
    // site.getOID()).getNextSeq();
    // injury.setInjuryReportNo(seqNo);
    //
    // int pk = PersistWrapper.createEntity(injury);
    //
    // //
    // SurveyMaster.saveAttachments(EtestApplication.getInstance().getUserContext(),
    // // pk, EntityTypeEnum.Injury.getValue(), attachments, true);
    //
    // // fetch the new project back
    // injury = PersistWrapper.readByPrimaryKey(Injury.class, pk);
    // return injury;
    //
    // }

    // public static Injury update(UserContext context, InjuryBean bean,
    // List<AttachmentIntf> attachments) throws Exception
    // {
    // Injury dbReport = PersistWrapper.readByPrimaryKey(Injury.class,
    // bean.getPk());
    // // BeanUtils.copyProperties(dbReport, bean);
    // // temporary bean assigning option
    // dbReport.setPk(bean.getPk());
    // dbReport.setInjuryReportNo(bean.getInjuryReportNo());
    // dbReport.setSitePk(bean.getSitePk());
    // dbReport.setProjectPk(bean.getProjectPk());
    // dbReport.setLocationPk(bean.getLocationPk());
    // dbReport.setLocationType(bean.getLocationType());
    // dbReport.setCustodianPk(bean.getCustodianPk());
    // dbReport.setTypeOfInjury(bean.getTypeOfInjury());
    // dbReport.setDescription(bean.getDescription());
    // dbReport.setInjuredPerson(bean.getInjuredPerson());
    // dbReport.setTypeOfPerson(bean.getTypeOfPerson());
    // dbReport.setDateOfInjury(bean.getDateOfInjury());
    // dbReport.setDetailsEquipment(bean.getDetailsEquipment());
    // dbReport.setDetailsWitnes(bean.getDetailsWitnes());
    // dbReport.setDetailsTreatment(bean.getDetailsTreatment());
    // dbReport.setTreatedBy(bean.getTreatedBy());
    // dbReport.setRootCauseOfInjury(bean.getRootCauseOfInjury());
    // dbReport.setSupplimentaryCause(bean.getSupplimentaryCause());
    // dbReport.setPrecautionDone(bean.getPrecautionDone());
    // dbReport.setPrecautionRequired(bean.getPrecautionRequired());
    // dbReport.setReportingRequired(bean.getReportingRequired());
    // dbReport.setReportingNumber(bean.getReportingNumber());
    // dbReport.setReferenceRequired(bean.getReferenceRequired());
    // dbReport.setReferenceNo(bean.getReferenceNo());
    // dbReport.setDetailsEquipmentStatus(bean.getDetailsEquipmentStatus());
    // dbReport.setDetailsWitnesStatus(bean.getDetailsWitnesStatus());
    // // body-part
    // dbReport.setFatality(bean.getFatality());
    // dbReport.setHeadfront(bean.getHeadfront());
    // dbReport.setHeadback(bean.getHeadback());
    // dbReport.setFace(bean.getFace());
    // dbReport.setLefteye(bean.getLefteye());
    // dbReport.setRighteye(bean.getRighteye());
    // dbReport.setLeftear(bean.getLeftear());
    // dbReport.setRightear(bean.getRightear());
    // dbReport.setNeckfront(bean.getNeckfront());
    // dbReport.setNeckback(bean.getNeckback());
    // dbReport.setUpperbodyfront(bean.getUpperbodyfront());
    // dbReport.setUpperbodyback(bean.getUpperbodyback());
    // dbReport.setLowerbodyfront(bean.getLowerbodyfront());
    // dbReport.setLowerbodyback(bean.getLowerbodyback());
    // dbReport.setShoulderright(bean.getShoulderright());
    // dbReport.setShoulderleft(bean.getShoulderleft());
    // dbReport.setUpperarmright(bean.getUpperarmright());
    // dbReport.setUpperarmleft(bean.getUpperarmleft());
    // dbReport.setLowerarmright(bean.getLowerarmright());
    // dbReport.setLowerarmleft(bean.getLowerarmleft());
    // dbReport.setElbowright(bean.getElbowright());
    // dbReport.setElbowleft(bean.getElbowleft());
    // dbReport.setWristleft(bean.getWristleft());
    // dbReport.setWristright(bean.getWristright());
    // dbReport.setFingersleft(bean.getFingersleft());
    // dbReport.setFingersright(bean.getFingersright());
    // dbReport.setUpperlegright(bean.getUpperlegright());
    // dbReport.setUpperlegleft(bean.getUpperlegleft());
    // dbReport.setKneeright(bean.getKneeright());
    // dbReport.setKneeleft(bean.getKneeleft());
    // dbReport.setLowerlegright(bean.getLowerlegright());
    // dbReport.setLowerlegleft(bean.getLowerlegleft());
    // dbReport.setAnkleleft(bean.getAnkleleft());
    // dbReport.setAnkleright(bean.getAnkleright());
    // dbReport.setFootright(bean.getFootright());
    // dbReport.setFootleft(bean.getFootleft());
    // dbReport.setOther(bean.getOther());
    // dbReport.setOtherParts(bean.getOtherParts());
    // // body-part
    //
    // // dbReport.setLastUpdated(bean.getLastUpdated());
    // // status update steps
    // dbReport.setLocationOther(bean.getLocationOther());
    // dbReport.setSupervisedBy(bean.getSupervisedBy());
    // dbReport.setStatus(bean.getStatus());
    // dbReport.setCreatedBy(bean.getCreatedBy());
    // dbReport.setCreatedByInitial(bean.getCreatedByInitial());
    // dbReport.setAcknowledgedBy(bean.getAcknowledgedBy());
    // dbReport.setAcknowledgedDate(bean.getAcknowledgedDate());
    // dbReport.setVerifiedBy(bean.getVerifiedBy());
    // dbReport.setVerifiedDate(bean.getVerifiedDate());
    // dbReport.setClosedBy(bean.getClosedBy());
    // dbReport.setClosedDate(bean.getClosedDate());
    // // dbReport.setTypeOfInjury(bean.getTypeOfInjury());
    // // status update steps
    // // --temporary bean assigning option
    // PersistWrapper.update(dbReport);
    // // fetch the new project back
    //
    // SurveyMaster.saveAttachments(EtestApplication.getInstance().getUserContext(),
    // dbReport.getPk(),
    // EntityTypeEnum.Injury.getValue(), attachments, true);
    //
    // dbReport = PersistWrapper.readByPrimaryKey(Injury.class, bean.getPk());
    // return dbReport;
    // }

    public  InjuryBean verifyInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {
        if (injuryOID == null || injuryOID.getPk() < 1)
            return null;
        Injury inj = (Injury) persistWrapper.readByPrimaryKey(Injury.class, injuryOID.getPk());
        if (!Injury.StatusEnum.Verified.name().equals(inj.getStatus()))
        {
            inj.setStatus(Injury.StatusEnum.Verified.name());
            inj.setVerifiedDate(new Date());
            inj.setVerifiedBy((int) context.getUser().getPk());
            persistWrapper.update(inj);
            if (message != null)
            {
                ProjectManager.addComment(context, injuryOID.getPk(), EntityTypeEnum.Injury.getValue(),
                        "Verified comment:- " + message, Mode.COMMENTCONTEXT_GENERAL);
            }
        }
        return getInjuryReportBean(injuryOID);
    }

    public  InjuryBean closeInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {
        if (injuryOID == null || injuryOID.getPk() < 1)
            return null;
        Injury inj = (Injury) persistWrapper.readByPrimaryKey(Injury.class, injuryOID.getPk());

        if (!Injury.StatusEnum.Closed.name().equals(inj.getStatus()))
        {
            if (!Injury.StatusEnum.Verified.name().equals(inj.getStatus()))
            {
                verifyInjuryReport(context, injuryOID, message);
            }
            inj = (Injury) persistWrapper.readByPrimaryKey(Injury.class, injuryOID.getPk());
            inj.setStatus(Injury.StatusEnum.Closed.name());
            inj.setClosedDate(new Date());
            inj.setClosedBy((int) context.getUser().getPk());
            persistWrapper.update(inj);
            if (message != null)
            {
                ProjectManager.addComment(context, injuryOID.getPk(), EntityTypeEnum.Injury.getValue(),
                        "Closed comment:- " + message, Mode.COMMENTCONTEXT_GENERAL);
            }

        }
        return getInjuryReportBean(injuryOID);
    }

    public  InjuryBean reopenInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {
        if (injuryOID == null || injuryOID.getPk() < 1)
            return null;
        Injury inj = (Injury) persistWrapper.readByPrimaryKey(Injury.class, injuryOID.getPk());
        if (!Injury.StatusEnum.Reopened.name().equals(inj.getStatus()))
        {
            inj.setStatus(Injury.StatusEnum.Reopened.name());
            inj.setVerifiedBy(null);
            inj.setVerifiedDate(null);
            inj.setClosedDate(null);
            inj.setClosedBy(null);
            persistWrapper.update(inj);
            ProjectManager.addComment(context, injuryOID.getPk(), EntityTypeEnum.Injury.getValue(),
                    "Re-Open comment:- " + message, Mode.COMMENTCONTEXT_GENERAL);
        }

        return getInjuryReportBean(injuryOID);
    }

    public  Injury get(InjuryOID oid) throws Exception
    {
        return (Injury) persistWrapper.readByPrimaryKey(Injury.class, oid.getPk());
    }

    public  List<InjuryQuery> list(InjuryFilter filter) throws Exception
    {
        String sql = InjuryQuery.sql + " and  ws.workstationName != ? order by ws.orderNo";

        return persistWrapper.readList(InjuryQuery.class, sql, DummyWorkstation.DUMMY);
    }

    public  List<InjuryQuery> getInjuryReportList(InjuryFilter injuryFilter) throws Exception
    {

        StringBuffer sql = new StringBuffer(InjuryQuery.sql);

        List<Object> params = new ArrayList();

        if ((injuryFilter.getSitePks() == null || injuryFilter.getSitePks().length == 0)
                && injuryFilter.getFilterMode() == InjuryFilter.FilterModeEnum.SelectNoneOnNoSelect)
        {
            sql.append(" and injury.sitePk = -1 ");
        } else
        {
            if (injuryFilter.getSitePks() != null && injuryFilter.getSitePks().length > 0)
            {
                sql.append(" and injury.sitePk in (");
                String sep = "";
                for (int i = 0; i < injuryFilter.getSitePks().length; i++)
                {
                    sql.append(sep);
                    sql.append(injuryFilter.getSitePks()[i]);
                    sep = ",";
                }
                sql.append(")");
            }
        }
        if (injuryFilter.getPk() != 0)
        {
            sql.append(" and injury.pk=?");
            params.add(injuryFilter.getPk());
        } else
        {
            if (injuryFilter.getCreatedDate() != null)
            {
                Date[] dateRange = injuryFilter.getCreatedDate()
                        .getResolvedDateRangeValues();
                if (dateRange != null)
                {
                    Date createdDateFrom = dateRange[0];
                    Date createdDateTo = dateRange[1];
                    if (createdDateFrom != null)
                    {
                        sql.append(" and injury.createdDate > ? ");
                        params.add(createdDateFrom);
                    }
                    if (createdDateTo != null)
                    {
                        sql.append(" and injury.createdDate  < ?");
                        params.add(createdDateTo);
                    }

                }

            }

//			if (injuryFilter.getFromCreatedDate() != null)
//			{
//				sql.append(" and injury.createdDate > ?");
//				Date dateFrom = DateUtils.truncate(injuryFilter.getFromCreatedDate(), Calendar.DATE);
//				params.add(dateFrom);
//				// params.add(DateUtils.ceiling(injuryFilter.getFromCreatedDate(),
//				// Calendar.DATE));
//			}
//			if (injuryFilter.getToCreatedDate() != null)
//			{
//				sql.append(" and injury.createdDate < ?");
//				Date d = DateUtils.truncate(injuryFilter.getToCreatedDate(), Calendar.DATE);
//				d = DateUtils.addDays(d, 1);
//				params.add(d);
//			}

//			if (injuryFilter.getProjectPk() != 0)
//			{
//				sql.append(" and injury.projectPk=?");
//				params.add(injuryFilter.getProjectPk());
//			}
            if (injuryFilter.getProjectPks() != null && injuryFilter.getProjectPks().size() > 0)
            {
                sql.append(" and injury.projectPk in ");
                String filterProjectPks = Arrays.deepToString(injuryFilter.getProjectPks().toArray());
                filterProjectPks = filterProjectPks.replace('[', '(');
                filterProjectPks = filterProjectPks.replace(']', ')');
                sql.append(filterProjectPks);
            }
            if (injuryFilter.getWorkstationPk() != 0)
            {
                sql.append(" and injury.locationType = 'Workstation' and injury.locationPk=?");
                params.add(injuryFilter.getWorkstationPk());
            }
            if (injuryFilter.getLocations() != null)
            {
                String commaFlag = "";
                StringBuffer workstationBuffer = new StringBuffer();
                for (Object location : injuryFilter.getLocations())
                {
                    if (location instanceof WorkstationQuery)
                    {
                        workstationBuffer.append(commaFlag).append(((WorkstationQuery) location).getPk());
                        commaFlag = ",";
                    }
                }
                commaFlag = "";
                StringBuffer locationBuffer = new StringBuffer();
                for (Object location : injuryFilter.getLocations())
                {
                    if (location instanceof InjuryLocationMasterQuery)
                    {
                        locationBuffer.append(commaFlag).append(((InjuryLocationMasterQuery) location).getPk());
                        commaFlag = ",";
                    }
                }
                sql.append(" and ((injury.locationType = 'Workstation' and injury.locationPk in (");
                if (workstationBuffer.length() > 0)
                {
                    sql.append(workstationBuffer);
                } else
                {
                    sql.append(0);
                }
                sql.append("))  ");
                sql.append(" or (injury.locationType = 'Location' and injury.locationPk in (");
                if (locationBuffer.length() > 0)
                {
                    sql.append(locationBuffer);
                } else
                {
                    sql.append(0);
                }
                sql.append(") ))");
            }

            if ((injuryFilter.getStatus() == null || injuryFilter.getStatus().length == 0)
                    && injuryFilter.getFilterMode() == InjuryFilter.FilterModeEnum.SelectNoneOnNoSelect)
            {
                sql.append(" and injury.status = 'None' ");
            } else
            {
                if (injuryFilter.getStatus() != null && injuryFilter.getStatus().length > 0)
                {
                    sql.append(" and injury.status in (");
                    String sep = "";
                    for (int i = 0; i < injuryFilter.getStatus().length; i++)
                    {
                        sql.append(sep);
                        sql.append("'").append(injuryFilter.getStatus()[i]).append("'");
                        sep = ",";
                    }
                    sql.append(")");
                }
            }
            if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
            {
                sql.append(" and ( ");
                String orflag = "";
                for (String name : injuryFilter.getNatureOfInjury())
                {
                    sql.append(orflag).append(" injury." + name + " is true");
                    orflag = " or ";
                }
                sql.append(" ) ");
            }
            if (injuryFilter.getTypeOfPerson() != null)
            {
                sql.append(" and injury.typeOfPerson=? ");
                params.add(injuryFilter.getTypeOfPerson());
            }
            if (injuryFilter.getTypeOfInjury() != null)
            {
                sql.append(" and injury.typeOfInjury=? ");
                params.add(injuryFilter.getTypeOfInjury());
            }
            if (injuryFilter.getAfterTreatmentList() != null && injuryFilter.getAfterTreatmentList().size() > 0)
            {
                sql.append(
                        "  and injury.pk in (select injury_assign_after_treatment.injuryPk as pk from  injury_assign_after_treatment inner join injury_after_treatment_master on injury_after_treatment_master.pk=injury_assign_after_treatment.afterTreatmentMasterPk  ");

                sql.append(" where  injury_after_treatment_master.pk in (");
                String sep = "";
                for (Integer afterTreatmentPk : injuryFilter.getAfterTreatmentList())
                {
                    sql.append(sep);
                    sql.append("'").append(afterTreatmentPk).append("'");
                    sep = ",";
                }
                sql.append("))");

            }
        }

        return persistWrapper.readList(InjuryQuery.class, sql.toString() + InjuryQuery.sql_order,
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);
    }

    /*
     * For getting injury list based on NOnContainerFIlter
     */
    public  List<InjuryQuery> getInjuryReportList(UserContext context, InjuryReportQueryFilter injuryQueryFilter)
    {
        List<InjuryQuery> injuryQueryList = null;
        try
        {

            InjuryReportQueryBuilder injuryFilter = new InjuryReportQueryBuilder((AuthorizationManager) context, (ProjectService) injuryQueryFilter);
            QueryObject result = injuryFilter.getQuery();

            injuryQueryList = persistWrapper.readList(InjuryQuery.class, result.getQuery(),
                    (result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
                            : null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return injuryQueryList;
    }

    public  List<InjuryQuery> getPendingVerificationMorethan1daysForHSECordinator(UserContext context)
    {
        List<InjuryQuery> injuryQueryList = null;
        try
        {
            InjuryReportQueryFilter injuryQueryFilter = new InjuryReportQueryFilter();
            injuryQueryFilter.setPendingVerificationLimit(
                    new LimitObject(context.getUser().getOID(), DateLimit.MORETHAN1DAYS));
            List<String> statusList = new ArrayList<>();
            statusList.add(Injury.StatusEnum.Open.name());
            injuryQueryFilter.setStatusList(statusList);
            InjuryReportQueryBuilder injuryFilter = new InjuryReportQueryBuilder((AuthorizationManager) context, (ProjectService) injuryQueryFilter);
            QueryObject result = injuryFilter.getQuery();

            injuryQueryList = persistWrapper.readList(InjuryQuery.class, result.getQuery(),
                    (result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
                            : null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return injuryQueryList;
    }
    public  List<InjuryQuery> getPendingVerificationMorethan2daysForHSEDirector(UserContext context)
    {
        List<InjuryQuery> injuryQueryList = null;
        try
        {
            InjuryReportQueryFilter injuryQueryFilter = new InjuryReportQueryFilter();
            injuryQueryFilter.setPendingVerificationLimit(
                    new LimitObject(context.getUser().getOID(), DateLimit.MORETHAN2DAYS));
            List<String> statusList = new ArrayList<>();
            statusList.add(Injury.StatusEnum.Open.name());
            injuryQueryFilter.setStatusList(statusList);
            InjuryReportQueryBuilder injuryFilter = new InjuryReportQueryBuilder((AuthorizationManager) context, (ProjectService) injuryQueryFilter);
            QueryObject result = injuryFilter.getQuery();

            injuryQueryList = persistWrapper.readList(InjuryQuery.class, result.getQuery(),
                    (result.getParams().size() > 0) ? result.getParams().toArray(new Object[result.getParams().size()])
                            : null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return injuryQueryList;
    }

    public  void deleteInjuryReport(int injuryPk) throws Exception
    {
        persistWrapper.delete("delete from injury where pk=?", injuryPk);
    }

    public  InjuryBean getInjuryReportBean(InjuryOID injuryOID) throws Exception
    {
        if (injuryOID == null || injuryOID.getPk() < 1)
            return null;
        InjuryBean injuryBean = null;
        Injury injury = (Injury) persistWrapper.readByPrimaryKey(Injury.class, injuryOID.getPk());
        if (injury != null && injury.getPk() > 0)
        {
            injuryBean = injuryHelper.getBean(injury);
        }
        return injuryBean;
    }

    public  InjuryQuery getInjuryReportByPk(int injuryPk) throws Exception
    {
        InjuryQuery injuryQuery = persistWrapper.read(InjuryQuery.class,
                InjuryQuery.sql + " and injury.pk=?" + InjuryQuery.sql_order, injuryPk);
        if (injuryQuery != null)
        {
            injuryQuery.setInjuryAssignAfterTreatment(
                    injuryAssignAfterTreatmentManager.getAssignAfterTreatmentBeanByInjuryPk(injuryPk));
            injuryQuery.setWatcherBean(
                    watcherManager.getWatcherBeanByObjectTypeAndObjectPk(injuryPk, EntityTypeEnum.Injury));
        }
        return injuryQuery;
    }

    public  List<InjuryUserQuery> getInjuryUserList(int sitePk) throws Exception
    {
        return persistWrapper.readList(InjuryUserQuery.class,
                InjuryUserQuery.sql_TAB_USER.toString() + " and TAB_USER.sitePk=? " + InjuryUserQuery.sql_union
                        + InjuryUserQuery.sql_injury_injuryperson + " and injury.sitePk=? " + InjuryUserQuery.sql_union
                        + InjuryUserQuery.sql_injury_treatedBy + " and injury.sitePk=? " + InjuryUserQuery.sql_order,
                sitePk, sitePk, sitePk);
    }

    public  List<InjuryReportGraphQuery> getInjuriesOfWorkstation(InjuryFilter injuryFilter)
    {
        QueryObject resultQuery = injuryReportGraphQuery.getQuery(injuryFilter);
        List<InjuryReportGraphQuery> listInjuryQuery = persistWrapper.readList(InjuryReportGraphQuery.class,
                resultQuery.getQuery(),
                (resultQuery.getParams().size() > 0)
                        ? resultQuery.getParams().toArray(new Object[resultQuery.getParams().size()])
                        : null);

        return listInjuryQuery;

    }

    public  List<InjuryAfterTreatmentQuery> getInjuryAfterTreatmentList()
    {
        List<InjuryAfterTreatmentQuery> injuryAfterTreatmentQuery = null;
        injuryAfterTreatmentQuery = persistWrapper.readList(InjuryAfterTreatmentQuery.class,
                " SELECT injury_after_treatment_master.pk as pk,injury_after_treatment_master.name as name,injury_after_treatment_master.createdBy as createdBy,"
                        + " injury_after_treatment_master.status as status,injury_after_treatment_master.createdDate as createdDate"
                        + " from injury_after_treatment_master where 1 = 1 and status = ?",
                ACTIVE);
        return injuryAfterTreatmentQuery;
    }

    public  InjuryAfterTreatment getInjuryAfterTreatment(int pk)
    {
        InjuryAfterTreatment treatment = (InjuryAfterTreatment) persistWrapper.readByPrimaryKey(InjuryAfterTreatment.class, pk);
        ;
        return treatment;
    }

    public  List<User> getAssignedSupervisors(String filterString)
    {
        String sql = "Select distinct TAB_USER.* from TAB_USER inner join injury on injury.supervisedBy=TAB_USER.pk";
        if(filterString != null && filterString.trim().length() > 0) {
            sql = sql + " and (upper(userName) like '" + filterString.toUpperCase() + "%' or upper(firstName) like '"
                    + filterString.toUpperCase() + "%' or upper(lastName) like '" + filterString.toUpperCase() + "%')";
        }
        sql = sql + " order by TAB_USER.firstName asc";
        return persistWrapper.readList(User.class,sql);
    }
}

