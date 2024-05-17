package com.tathvatech.injuryReport.controller;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.injuryReport.common.*;
import com.tathvatech.injuryReport.entity.InjuryAfterTreatment;
import com.tathvatech.injuryReport.processor.InjuryQuerySecurityProcessor;
import com.tathvatech.injuryReport.request.CreateInjuryReportRequest;
import com.tathvatech.injuryReport.request.SaveRequest;
import com.tathvatech.injuryReport.request.VerifyInjuryReportRequest;
import com.tathvatech.injuryReport.service.InjuryService;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.injuryReport.email.InjuryEmailSender;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.user.common.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/injury")
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



    @PostMapping("/createInjuryReport")
    public  InjuryQuery createInjuryReport(@RequestBody CreateInjuryReportRequest createInjuryReportRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryQuery inj = injuryService.create(context, createInjuryReportRequest.getInjuryBean(),createInjuryReportRequest.getAttachments(), createInjuryReportRequest.isAndroidDevice());
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

   @PostMapping("/save")
   public  InjuryBean save(@RequestBody SaveRequest saveRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean isNewInjury = false;
            if (saveRequest.getInjuryBean().getPk() < 1)
            {
                isNewInjury = true;
            }
            InjuryBean inj =injuryService .save(context, saveRequest.getInjuryBean(), saveRequest.getAttachments());

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
    @PutMapping("/verifyInjuryReport")
    public  InjuryBean verifyInjuryReport(@RequestBody VerifyInjuryReportRequest verifyInjuryReportRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryBean inj = null;

            inj =injuryService.verifyInjuryReport(context, verifyInjuryReportRequest.getInjuryOID(), verifyInjuryReportRequest.getMessage());
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

    @PutMapping("/closeInjuryReport")
    public InjuryBean closeInjuryReport(@RequestBody VerifyInjuryReportRequest verifyInjuryReportRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        InjuryBean inj = null;

            inj =injuryService .closeInjuryReport(context,verifyInjuryReportRequest.getInjuryOID(),verifyInjuryReportRequest.getMessage());

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

    @PutMapping("/reopenInjuryReport")
    public  InjuryBean reopenInjuryReport(@RequestBody VerifyInjuryReportRequest verifyInjuryReportRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InjuryBean inj = null;

            inj =injuryService .reopenInjuryReport(context, verifyInjuryReportRequest.getInjuryOID(), verifyInjuryReportRequest.getMessage());


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

    @GetMapping("/getInjuryReportList")
    public List<InjuryQuery> getInjuryReportList(@RequestBody InjuryFilter injuryFilter) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        injuryQuerySecurityProcessor.addAuthorizationFilterParams(context, injuryFilter);
        List<InjuryQuery> l =injuryService .getInjuryReportList(injuryFilter);
        return l;
    }

    @GetMapping("/getPendingVerificationMorethan1daysForHSECordinator")
    public  List<InjuryQuery> getPendingVerificationMorethan1daysForHSECordinator()
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InjuryQuery> l =injuryService.getPendingVerificationMorethan1daysForHSECordinator(context);
        return l;
    }

    @GetMapping("/getPendingVerificationMorethan2daysForHSEDirector")
    public  List<InjuryQuery> getPendingVerificationMorethan2daysForHSEDirector()
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InjuryQuery> l =injuryService.getPendingVerificationMorethan2daysForHSEDirector(context);
        return l;
    }

    @GetMapping("/getInjuryReportList")
    public  List<InjuryQuery> getInjuryReportList(@RequestBody InjuryReportQueryFilter injuryQueryFilter)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return injuryService.getInjuryReportList(context, injuryQueryFilter);
    }

    @GetMapping("/getInjuryReportByPk/{injuryPk}")
    public  InjuryQuery getInjuryReportByPk(@PathVariable("injuryPk") int injuryPk) throws Exception
    {
        return injuryService.getInjuryReportByPk(injuryPk);
    }

    @GetMapping("/getInjuryReportBean")
    public  InjuryBean getInjuryReportBean(@RequestBody InjuryOID injuryOID) throws Exception
    {
        return getInjuryReportBean(injuryOID);
    }

    @DeleteMapping("/deleteInjuryReport/{injuryPk}")
    public  void deleteInjuryReport(@PathVariable("injuryPk") int injuryPk) throws Exception
    {
      injuryService.deleteInjuryReport(injuryPk);
    }

    @GetMapping("/getInjuryUserList/{sitePk}")
    public  List<InjuryUserQuery> getInjuryUserList(@PathVariable("sitePk") int sitePk) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InjuryUserQuery> l = null;

            l = injuryService.getInjuryUserList(sitePk);



        return l;
    }

    @GetMapping("/getMyAssignedInjuryReportsForVerification")
    public  List<InjuryQuery> getMyAssignedInjuryReportsForVerification( @RequestBody Object object)
            throws Exception
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    @GetMapping("/getMyAssignedInjuryReportsForClose")
    public List<InjuryQuery> getMyAssignedInjuryReportsForClose()
            throws Exception
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

   @GetMapping("/getMyAssignedInjuryReportsForCloseMorethan7Days")
   public  List<InjuryQuery> getMyAssignedInjuryReportsForCloseMorethan7Days()
            throws Exception
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    @GetMapping("/getMyAssignedInjuryReports")
    public  List<InjuryQuery> getMyAssignedInjuryReports(@RequestBody Object object) throws Exception
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

   @GetMapping("/getInjuriesOfWorkstation")
   public  List<InjuryReportGraphQuery> getInjuriesOfWorkstation(@RequestBody InjuryFilter injuryFilter) throws Exception
    {

            return injuryService.getInjuriesOfWorkstation(injuryFilter);

    }

    @GetMapping("/getInjuryAfterTreatmentList")
    public  List<InjuryAfterTreatmentQuery> getInjuryAfterTreatmentList() throws Exception
    {

            return injuryService.getInjuryAfterTreatmentList();


    }

    @GetMapping("/getInjuryAfterTreatment/{pk}")
    public  InjuryAfterTreatment getInjuryAfterTreatment(@PathVariable("pk") int pk) throws Exception
    {

            return injuryService.getInjuryAfterTreatment(pk);


    }

    @GetMapping("/getAssignedSupervisors/{filterString}")
    public  List<User> getAssignedSupervisors(@PathVariable("filterString") String filterString) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return injuryService.getAssignedSupervisors(filterString);
    }
}

