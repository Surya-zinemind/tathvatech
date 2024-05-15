package com.tathvatech.injuryReport.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.injuryReport.common.*;
import com.tathvatech.injuryReport.entity.InjuryAfterTreatment;
import com.tathvatech.injuryReport.processor.InjuryQuerySecurityProcessor;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.injuryReport.email.InjuryEmailSender;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.injuryReport.service.InjuryManager;
import com.tathvatech.testsutra.injury.web.InjuryReportPrinter;
import com.tathvatech.ts.core.utils.TempFileUtil;
import com.tathvatech.user.common.UserContext;

public class InjuryDelegate
{
    public static InjuryQuery createInjuryReport(UserContext context, InjuryBean injuryBean,
                                                 List<AttachmentIntf> attachments, boolean isAndroidDevice) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            InjuryQuery inj = InjuryManager.create(context, injuryBean, attachments, isAndroidDevice);

            con.commit();

            Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(), null));
            CommonServiceManager.saveSnapshot(context, injuryToBackup);
            con.commit();

            InjuryQuery iQ = InjuryManager.getInjuryReportByPk(inj.getPk());
            InjuryEmailSender.notifyInjuryReportCreated(context, iQ);
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
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
    }

    public static InjuryBean save(UserContext context, InjuryBean injuryBean, List<AttachmentIntf> attachments)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            boolean isNewInjury = false;
            if (injuryBean.getPk() < 1)
            {
                isNewInjury = true;
            }
            InjuryBean inj = InjuryManager.save(context, injuryBean, attachments);
            con.commit();

            Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(), null));
            CommonServiceManager.saveSnapshot(context, injuryToBackup);
            con.commit();
            if (isNewInjury)
            {
                InjuryQuery iQ = InjuryManager.getInjuryReportByPk(inj.getPk());
                InjuryEmailSender.notifyInjuryReportCreated(context, iQ);
            }
            return inj;
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
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
    // Injury inj = InjuryManager.saveInjuryReport(context, injury,
    // attachments);
    //
    // con.commit();
    //
    // Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(),
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
    // inj = InjuryManager.update(context, injuryBean, attachments);
    // con.commit();
    //
    // Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(),
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
    public static InjuryBean verifyInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {
        Connection con = null;
        InjuryBean inj = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            inj = InjuryManager.verifyInjuryReport(context, injuryOID, message);
            con.commit();

            Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(), null));
            CommonServiceManager.saveSnapshot(context, injuryToBackup);
            con.commit();

            InjuryQuery iQ = InjuryManager.getInjuryReportByPk(inj.getPk());
            InjuryEmailSender.notifyVerifyInjuryReport(context, iQ);

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
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
        return inj;
    }

    public static InjuryBean closeInjuryReport(UserContext context, InjuryOID injuryOID, String message)
            throws Exception
    {
        Connection con = null;
        InjuryBean inj = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            inj = InjuryManager.closeInjuryReport(context, injuryOID, message);
            con.commit();

            Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(), null));
            CommonServiceManager.saveSnapshot(context, injuryToBackup);
            con.commit();

            InjuryQuery iQ = InjuryManager.getInjuryReportByPk(inj.getPk());
            InjuryEmailSender.notifyCloseInjuryReport(context, iQ);

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
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
        return inj;
    }

    public static InjuryBean reopenInjuryReport(UserContext context, InjuryOID injuryOID, String messsage)
            throws Exception
    {
        Connection con = null;
        InjuryBean inj = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            inj = InjuryManager.reopenInjuryReport(context, injuryOID, messsage);
            con.commit();

            Injury injuryToBackup = InjuryManager.get(new InjuryOID(inj.getPk(), null));
            CommonServiceManager.saveSnapshot(context, injuryToBackup);
            con.commit();

            InjuryQuery iQ = InjuryManager.getInjuryReportByPk(inj.getPk());
            InjuryEmailSender.notifyReopenInjuryReport(context, iQ);
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
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
        return inj;
    }

    public static List<InjuryQuery> getInjuryReportList(UserContext context, InjuryFilter injuryFilter) throws Exception
    {
        new InjuryQuerySecurityProcessor().addAuthorizationFilterParams(context, injuryFilter);
        List<InjuryQuery> l = InjuryManager.getInjuryReportList(injuryFilter);
        return l;
    }

    public static List<InjuryQuery> getPendingVerificationMorethan1daysForHSECordinator(UserContext context)
            throws Exception
    {
        List<InjuryQuery> l = InjuryManager.getPendingVerificationMorethan1daysForHSECordinator(context);
        return l;
    }

    public static List<InjuryQuery> getPendingVerificationMorethan2daysForHSEDirector(UserContext context)
            throws Exception
    {
        List<InjuryQuery> l = InjuryManager.getPendingVerificationMorethan2daysForHSEDirector(context);
        return l;
    }

    public static List<InjuryQuery> getInjuryReportList(UserContext context, InjuryReportQueryFilter injuryQueryFilter)
            throws Exception
    {
        return InjuryManager.getInjuryReportList(context, injuryQueryFilter);
    }

    public static InjuryQuery getInjuryReportByPk(int injuryPk) throws Exception
    {
        return InjuryManager.getInjuryReportByPk(injuryPk);
    }

    public static InjuryBean getInjuryReportBean(InjuryOID injuryOID) throws Exception
    {
        return InjuryManager.getInjuryReportBean(injuryOID);
    }

    public static void deleteInjuryReport(int injuryPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            InjuryManager.deleteInjuryReport(injuryPk);

        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public static List<InjuryUserQuery> getInjuryUserList(UserContext context, int sitePk) throws Exception
    {
        Connection con = null;
        List<InjuryUserQuery> l = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            l = InjuryManager.getInjuryUserList(sitePk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }

        return l;
    }

    public static List<InjuryQuery> getMyAssignedInjuryReportsForVerification(UserContext userContext, Object object)
            throws Exception
    {
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = new AuthorizationDelegate().getEntitiesWithRole(userContext, EntityTypeEnum.Site,
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

        return PersistWrapper.readList(InjuryQuery.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

    }

    public static List<InjuryQuery> getMyAssignedInjuryReportsForClose(UserContext userContext)
            throws Exception
    {
        /*
         * injury report for close assigned to HSE coordinators
         */
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = new AuthorizationDelegate().getEntitiesWithRole(userContext, EntityTypeEnum.Site,
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
            return PersistWrapper.readList(InjuryQuery.class, sql.toString(),
                    (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

        }
        return null;

    }

    public static List<InjuryQuery> getMyAssignedInjuryReportsForCloseMorethan7Days(UserContext userContext)
            throws Exception
    {
        /*
         * injury report for close assigned to HSE coordinators
         */
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = new AuthorizationDelegate().getEntitiesWithRole(userContext, EntityTypeEnum.Site,
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
            return PersistWrapper.readList(InjuryQuery.class, sql.toString(),
                    (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

        }
        return null;

    }

    public static List<InjuryQuery> getMyAssignedInjuryReports(UserContext userContext, Object object) throws Exception
    {
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer(InjuryQuery.sql);
        List<Integer> sitePks = new AuthorizationDelegate().getEntitiesWithRole(userContext, EntityTypeEnum.Site,
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

        return PersistWrapper.readList(InjuryQuery.class, sql.toString(),
                (params.size() > 0) ? params.toArray(new Object[params.size()]) : null);

    }

    public static List<InjuryReportGraphQuery> getInjuriesOfWorkstation(InjuryFilter injuryFilter) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            return InjuryManager.getInjuriesOfWorkstation(injuryFilter);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }

    }

    public static List<InjuryAfterTreatmentQuery> getInjuryAfterTreatmentList() throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            return InjuryManager.getInjuryAfterTreatmentList();
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }

    }

    public static InjuryAfterTreatment getInjuryAfterTreatment(int pk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);
            return InjuryManager.getInjuryAfterTreatment(pk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }

    }

    public static List<User> getAssignedSupervisors(UserContext context, String filterString) throws Exception
    {
        return InjuryManager.getAssignedSupervisors(filterString);
    }
}

