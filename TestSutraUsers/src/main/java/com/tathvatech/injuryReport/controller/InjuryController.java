package com.tathvatech.injuryReport.controller;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.common.*;
import com.tathvatech.injuryReport.entity.InjuryAfterTreatment;
import com.tathvatech.injuryReport.processor.InjuryQuerySecurityProcessor;
import com.tathvatech.injuryReport.service.InjuryService;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.injuryReport.email.InjuryEmailSender;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class InjuryController
{
    private  final Logger logger = LoggerFactory.getLogger(InjuryController.class);
    private final InjuryService injuryService;
    private final PersistWrapper persistWrapper;
    private final CommonServiceManager commonServiceManager;
    private final AuthorizationManager authorizationManager;
    private InjuryEmailSender injuryEmailSender;
    private InjuryQuerySecurityProcessor injuryQuerySecurityProcessor;



    public  InjuryQuery createInjuryReport(UserContext context, InjuryBean injuryBean,
                                                 List<AttachmentIntf> attachments, boolean isAndroidDevice) throws Exception
    {
        InjuryQuery inj = injuryService.create(context, injuryBean, attachments, isAndroidDevice);
        Injury injuryToBackup = injuryService.get(new InjuryOID(inj.getPk(), null));
            commonServiceManager.saveSnapshot(context, injuryToBackup);
            InjuryQuery iQ =injuryService .getInjuryReportByPk(inj.getPk());
            injuryEmailSender.notifyInjuryReportCreated(context, iQ);
            // StringBuffer sbText = new StringBuffer();
            // StringBuffer sbHtml = new StringBuffer();
            //
            // String createdByName = iQ.getCreatedByName();
            // if (createdByName == null || createdByName.trim().length() == 0)
            // createdByName = iQ.getCreatedByInitial();
            //
            // sbText.append("Hi\n\n").append("A new Injury report " +
            // iQ.getInjuryReportNo() + " has been submitted by ")
            // .append(createdByName).append(". Please find the report attached.
            // \n\n");
            // sbText.append("Thank You\n").append("TestSutra Support\n");
            //
            // sbHtml.append("Hi<br/><br/>")
            // .append("A new Injury report " + iQ.getInjuryReportNo() + " has
            // been
            // submitted by ")
            // .append(createdByName).append(". Please find the report attached.
            // <br/><br/>");
            // sbHtml.append("Thank You<br/>").append("TestSutra Support<br/>");
            //
            // String text = sbText.toString();
            // String html = sbHtml.toString();
            // String subject = "New Injury report " + inj.getInjuryReportNo() +
            // "
            // submitted";
            // sendEmail(context, subject, text, html, iQ);

            return inj;

    }

    public  InjuryBean save(UserContext context, InjuryBean injuryBean, List<AttachmentIntf> attachments)
            throws Exception
    {

            boolean isNewInjury = false;
            if (injuryBean.getPk() < 1)
            {
                isNewInjury = true;
            }
            InjuryBean inj =injuryService .save(context, injuryBean, attachments);

            Injury injuryToBackup = injuryService.get(new InjuryOID(inj.getPk(), null));
            commonServiceManager.saveSnapshot(context, injuryToBackup);

            if (isNewInjury)
            {
                InjuryQuery iQ =injuryService .getInjuryReportByPk(inj.getPk());
                injuryEmailSender.notifyInjuryReportCreated(context, iQ);
            }
            return inj;

    }

    // public static Injury saveInjuryBean1(UserContext context, Injury injury,
    // List<AttachmentIntf> attachments)
    // throws Exception
    // {
    // Connection con = null;
    // try
    // {
    // con = ServiceLocator.locate().getConnection();
    // con.setAutoCommit(false);
    //
    // Injury inj = .saveInjuryReport(context, injury,
    // attachments);
    //
    // con.commit();
    //
    // Injury injuryToBackup = .get(new InjuryOID(inj.getPk(),
    // null));
    // CommonServiceManager.saveSnapshot(context, injuryToBackup);
    // con.commit();
    //
    // return inj;
    // }
    // catch (Exception ex)
    // {
    // con.rollback();
    // throw ex;
    // }
    // finally
    // {
    // }
    // }
    //
    // public static Injury updateInjuryReport(UserContext context, InjuryBean
    // injuryBean,
    // List<AttachmentIntf> attachments) throws Exception
    // {
    // Connection con = null;
    // Injury inj = null;
    // try
    // {
    // con = ServiceLocator.locate().getConnection();
    // con.setAutoCommit(false);
    // inj = .update(context, injuryBean, attachments);
    // con.commit();
    //
    // Injury injuryToBackup =.get(new InjuryOID(inj.getPk(),
    // null));
    // CommonServiceManager.saveSnapshot(context, injuryToBackup);
    // con.commit();
    //
    // }
    // catch (Exception ex)
    // {
    // ex.printStackTrace();
    // con.rollback();
    // throw ex;
    // }
    // finally
    // {
    // con.commit();
    // }
    // return inj;
    // }
    //
    public  InjuryBean verifyInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {

        InjuryBean inj = null;

            inj =injuryService.verifyInjuryReport(context, injuryOID, message);
            Injury injuryToBackup =injuryService .get(new InjuryOID(inj.getPk(), null));
            commonServiceManager.saveSnapshot(context, injuryToBackup);
            InjuryQuery iQ =injuryService .getInjuryReportByPk(inj.getPk());
            injuryEmailSender.notifyVerifyInjuryReport(context, iQ);

            // StringBuffer sbText = new StringBuffer();
            // StringBuffer sbHtml = new StringBuffer();
            // sbText.append("Hi\n\n").append("Injury report " +
            // inj.getInjuryReportNo() + "
            // has been verified by ")
            // .append(iQ.getVerifiedByName()).append(". Please find the report
            // attached.
            // \n\n");
            // sbText.append("Thank You\n").append("TestSutra Support\n");
            //
            // sbHtml.append("Hi<br/><br/>").append("Injury report " +
            // inj.getInjuryReportNo() + " has been verified by ")
            // .append(iQ.getVerifiedByName()).append(". Please find the report
            // attached.
            // <br/><br/>");
            // sbHtml.append("Thank You<br/>").append("TestSutra Support<br/>");
            //
            // String text = sbText.toString();
            // String html = sbHtml.toString();
            // String subject = "New Injury report " + inj.getInjuryReportNo() +
            // "
            // verified";
            //
            // sendEmail(context, subject, text, html, iQ);

        return inj;
    }

    public InjuryBean closeInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {

        InjuryBean inj = null;

            inj =injuryService .closeInjuryReport(context, injuryOID, message);

            Injury injuryToBackup =injuryService .get(new InjuryOID(inj.getPk(), null));
            commonServiceManager.saveSnapshot(context, injuryToBackup);
            InjuryQuery iQ =injuryService.getInjuryReportByPk(inj.getPk());
            injuryEmailSender.notifyCloseInjuryReport(context, iQ);

            // StringBuffer sbText = new StringBuffer();
            // StringBuffer sbHtml = new StringBuffer();
            // sbText.append("Hi\n\n").append("Injury report " +
            // inj.getInjuryReportNo() + "
            // has been closed by ")
            // .append(iQ.getClosedByName()).append(". Please find the report
            // attached.
            // \n\n");
            // sbText.append("Thank You\n").append("TestSutra Support\n");
            //
            // sbHtml.append("Hi<br/><br/>").append("Injury report " +
            // inj.getInjuryReportNo() + " has been closed by ")
            // .append(iQ.getClosedByName()).append(". Please find the report
            // attached.
            // <br/><br/>");
            // sbHtml.append("Thank You<br/>").append("TestSutra Support<br/>");
            //
            // String text = sbText.toString();
            // String html = sbHtml.toString();
            // String subject = "New Injury report " + inj.getInjuryReportNo() +
            // " closed";
            //
            // sendEmail(context, subject, text, html, iQ);

        return inj;
    }

    public  InjuryBean reopenInjuryReport(UserContext context, InjuryOID injuryOID, String messsage)
            throws Exception
    {
        InjuryBean inj = null;

            inj =injuryService .reopenInjuryReport(context, injuryOID, messsage);


            Injury injuryToBackup =injuryService .get(new InjuryOID(inj.getPk(), null));
            commonServiceManager.saveSnapshot(context, injuryToBackup);
            InjuryQuery iQ =injuryService.getInjuryReportByPk(inj.getPk());
            injuryEmailSender.notifyReopenInjuryReport(context, iQ);
            // StringBuffer sbText = new StringBuffer();
            // StringBuffer sbHtml = new StringBuffer();
            // sbText.append("Hi\n\n").append("Injury report " +
            // inj.getInjuryReportNo() + "
            // has been reopened by ")
            // .append(context.getUser().getFirstName() + " " +
            // context.getUser().getLastName())
            // .append(". Please find the report attached. \n\n");
            // sbText.append("Thank You\n").append("TestSutra Support\n");
            //
            // sbHtml.append("Hi<br/><br/>").append("Injury report " +
            // inj.getInjuryReportNo() + " has been reopened by ")
            // .append(context.getUser().getFirstName() + " " +
            // context.getUser().getLastName())
            // .append(". Please find the report attached. <br/><br/>");
            // sbHtml.append("Thank You<br/>").append("TestSutra Support<br/>");
            //
            // String text = sbText.toString();
            // String html = sbHtml.toString();
            // String subject = "New Injury report " + inj.getInjuryReportNo() +
            // "
            // reopened";
            //
            // sendEmail(context, subject, text, html, iQ);

        return inj;
    }

    public List<InjuryQuery> getInjuryReportList(UserContext context, InjuryFilter injuryFilter) throws Exception
    {
        injuryQuerySecurityProcessor.addAuthorizationFilterParams(context, injuryFilter);
        List<InjuryQuery> l =injuryService .getInjuryReportList(injuryFilter);
        return l;
    }

    public  List<InjuryQuery> getPendingVerificationMorethan1daysForHSECordinator(UserContext context)
            throws Exception
    {
        List<InjuryQuery> l =injuryService.getPendingVerificationMorethan1daysForHSECordinator(context);
        return l;
    }

    public  List<InjuryQuery> getPendingVerificationMorethan2daysForHSEDirector(UserContext context)
            throws Exception
    {
        List<InjuryQuery> l =injuryService.getPendingVerificationMorethan2daysForHSEDirector(context);
        return l;
    }

    public  List<InjuryQuery> getInjuryReportList(UserContext context, InjuryReportQueryFilter injuryQueryFilter)
            throws Exception
    {
        return injuryService.getInjuryReportList(context, injuryQueryFilter);
    }

    public  InjuryQuery getInjuryReportByPk(int injuryPk) throws Exception
    {
        return injuryService.getInjuryReportByPk(injuryPk);
    }

    public  InjuryBean getInjuryReportBean(InjuryOID injuryOID) throws Exception
    {
        return getInjuryReportBean(injuryOID);
    }

    public  void deleteInjuryReport(int injuryPk) throws Exception
    {
      injuryService.deleteInjuryReport(injuryPk);
    }

    public  List<InjuryUserQuery> getInjuryUserList(UserContext context, int sitePk) throws Exception
    {

        List<InjuryUserQuery> l = null;

            l = injuryService.getInjuryUserList(sitePk);



        return l;
    }

    public  List<InjuryQuery> getMyAssignedInjuryReportsForVerification(UserContext userContext, Object object)
            throws Exception
    {
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks =authorizationManager.getEntitiesWithRole(userContext, EntityTypeEnum.Site,
                SiteRolesEnum.HSECoordinator);
        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" and ((");
        } else
        {
            sql.append(" and ");
        }
        sql.append(" injury.supervisedBy  = ? and injury.status in ('Open', 'Reopened')");
        params.add(userContext.getUser().getPk());
        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" )");
            sql.append(" or (");
            sql.append(" injury.sitePk in (");
            String sep = "";
            for (Iterator iterator = sitePks.iterator(); iterator.hasNext();)
            {
                Integer aSitePk = (Integer) iterator.next();
                sql.append(sep).append("?");
                params.add(aSitePk);
                sep = ",";
            }
            sql.append(" ) and injury.status in ( 'Reopened','Open') ))");

        }

        return persistWrapper.readList(InjuryQuery.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

    }

    public List<InjuryQuery> getMyAssignedInjuryReportsForClose(UserContext userContext)
            throws Exception
    {
        /*
         * injury report for close assigned to HSE coordinators
         */
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks =authorizationManager .getEntitiesWithRole(userContext, EntityTypeEnum.Site,
                SiteRolesEnum.HSECoordinator);

        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" and ");
            sql.append(" injury.sitePk in (");
            String sep = "";
            for (Iterator iterator = sitePks.iterator(); iterator.hasNext();)
            {
                Integer aSitePk = (Integer) iterator.next();
                sql.append(sep).append("?");
                params.add(aSitePk);
                sep = ",";
            }
            sql.append(" ) and injury.status in ('Verified') ");
            return persistWrapper.readList(InjuryQuery.class, sql.toString(),
                    (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

        }
        return null;

    }

    public  List<InjuryQuery> getMyAssignedInjuryReportsForCloseMorethan7Days(UserContext userContext)
            throws Exception
    {
        /*
         * injury report for close assigned to HSE coordinators
         */
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = authorizationManager.getEntitiesWithRole(userContext, EntityTypeEnum.Site,
                SiteRolesEnum.HSEDirector);

        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" and ");
            sql.append(" injury.sitePk in (");
            String sep = "";
            for (Iterator iterator = sitePks.iterator(); iterator.hasNext();)
            {
                Integer aSitePk = (Integer) iterator.next();
                sql.append(sep).append("?");
                params.add(aSitePk);
                sep = ",";
            }
            sql.append(" ) and injury.status in ('Verified') and injury.verifiedDate <= subdate(NOW(), 7)");
            return persistWrapper.readList(InjuryQuery.class, sql.toString(),
                    (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

        }
        return null;

    }

    public  List<InjuryQuery> getMyAssignedInjuryReports(UserContext userContext, Object object) throws Exception
    {
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = authorizationManager.getEntitiesWithRole(userContext, EntityTypeEnum.Site,
                SiteRolesEnum.HSECoordinator);
        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" and ((");
        } else
        {
            sql.append(" and ");
        }
        sql.append(" injury.supervisedBy  = ? and injury.status in ('Open', 'Reopened')");
        params.add(userContext.getUser().getPk());
        if (sitePks != null && sitePks.size() > 0)
        {
            sql.append(" )");
            sql.append(" or (");
            sql.append(" injury.sitePk in (");
            String sep = "";
            for (Iterator iterator = sitePks.iterator(); iterator.hasNext();)
            {
                Integer aSitePk = (Integer) iterator.next();
                sql.append(sep).append("?");
                params.add(aSitePk);
                sep = ",";
            }
            sql.append(" ) and injury.status in ('Verified', 'Reopened','Open') ))");

        }

        return persistWrapper.readList(InjuryQuery.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

    }

    public  List<InjuryReportGraphQuery> getInjuriesOfWorkstation(InjuryFilter injuryFilter) throws Exception
    {

            return injuryService.getInjuriesOfWorkstation(injuryFilter);

    }

    public  List<InjuryAfterTreatmentQuery> getInjuryAfterTreatmentList() throws Exception
    {

            return injuryService.getInjuryAfterTreatmentList();


    }

    public  InjuryAfterTreatment getInjuryAfterTreatment(int pk) throws Exception
    {

            return injuryService.getInjuryAfterTreatment(pk);


    }

    public  List<User> getAssignedSupervisors(UserContext context, String filterString) throws Exception
    {
        return injuryService.getAssignedSupervisors(filterString);
    }
}

