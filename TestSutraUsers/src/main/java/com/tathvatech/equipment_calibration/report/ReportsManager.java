package com.tathvatech.equipment_calibration.report;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.ncr.report.NcrCorrectiveActionReport;
import com.tathvatech.ncr.report.NcrItemListReport;
import com.tathvatech.report.enums.ReportTypes;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.survey.report.FormListReport;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.report.UserListReport;
import org.springframework.stereotype.Component;

@Component
public class ReportsManager
{
    private final PersistWrapper persistWrapper;
    private UserListReport userListReport;
    private FormListReport formListReport;

    public ReportsManager(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

    /*   public static void saveReportTemplate(UserContext userContext, ReportDefBean reportDefBean, ProjectOID projectOID,
                                             OID objectOID, ReportTemplate reportTemplate) throws Exception
       {
           if (reportTemplate.getPk() != 0)
           {
               reportTemplate.setReportDef(reportDefBean.getDefString());
               PersistWrapper.update(reportTemplate);
           } else
           {
               reportTemplate.setCreatedBy(userContext.getUser().getPk());
               reportTemplate.setCreatedDate(new Date());
               reportTemplate.setProjectPk(projectOID.getPk());
               reportTemplate.setObjectPk(objectOID.getPk());
               reportTemplate.setObjectType(objectOID.getEntityType().getValue());
               reportTemplate.setReportDef(reportDefBean.getDefString());
               reportTemplate.setReportType(reportDefBean.getReportType().getValue());

               PersistWrapper.createEntity(reportTemplate);
           }
       }

       /**
        * this is used by the old report template saver
        *
        * @deprecated
        * @param userContext
        * @param oid
        * @param reportType
        * @return
        * @throws Exception
        */
  /*  public static List<ReportTemplate> getReportTemplates(UserContext userContext, OID oid, ReportTypes reportType)
            throws Exception
    {
        return PersistWrapper.readList(ReportTemplate.class,
                "select * from report_template where objectPk=? and objectType=? and reportType = ? and createdBy = ? order by createdDate",
                oid.getPk(), oid.getEntityType().getValue(), reportType.getValue(), userContext.getUser().getPk());
    }

    public static List<ReportTemplate> getReportTemplatesForProject(UserContext userContext, ProjectOID projectOID,
                                                                    ReportTypes reportType) throws Exception
    {
        return PersistWrapper.readList(ReportTemplate.class,
                "select * from report_template where projectPk=? and reportType = ? order by createdDate",
                projectOID.getPk(), reportType.getValue());
    }
*/
    public  ReportResponse runReport(UserContext context, ReportRequest reportRequest)
    {
        if (ReportTypes.UserListReport == reportRequest.getReportType())
        {

            return userListReport.runReport(reportRequest);
        } else if (ReportTypes.FormListReport == reportRequest.getReportType())
        {

            return formListReport.runReport(reportRequest);
        } else if (ReportTypes.NcrItemListReport == reportRequest.getReportType())
        {
            NcrItemListReport report = new NcrItemListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.NcrCorrectionActionListReport == reportRequest.getReportType())
        {
            NcrCorrectiveActionReport report = new NcrCorrectiveActionReport(persistWrapper,context, reportRequest);
            return report.runReport();
        } /*else if (ReportTypes.PartsListReport == reportRequest.getReportType())
        {
            PartsListReport report = new PartsListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.PurchaseOrderListReport == reportRequest.getReportType())
        {
            PurchaseOrderListReport report = new PurchaseOrderListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.POListReportProcurement == reportRequest.getReportType())
        {
            POListReport report = new POListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.SupplierListReport == reportRequest.getReportType())
        {
            SupplierListReport report = new SupplierListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.HazardAndMaintenanceListReport == reportRequest.getReportType())
        {
            HazardAndMaintenanceListReport report = new HazardAndMaintenanceListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.OpenItemListReport == reportRequest.getReportType())
        {
            OpenItemListReport report = new OpenItemListReport(context, reportRequest);
            return report.runReport();
        }  else if (ReportTypes.TestProcListReport == reportRequest.getReportType())
        {
            TestProcListReport report = new TestProcListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.InjuryListReport == reportRequest.getReportType())
        {
            InjuryListReport report = new InjuryListReport(context,reportRequest);
            return report.runReport();
        } else if (ReportTypes.MRFListReport == reportRequest.getReportType())
        {
            MrfListReport report = new MrfListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.InspectionRegisterListReport == reportRequest.getReportType())
        {
            InspectionRegisterListReport report = new InspectionRegisterListReport(context,reportRequest);
            return report.runReport();
        } else if (ReportTypes.InspectedPartListReport == reportRequest.getReportType())
        {
            InspectedPartListReport report = new InspectedPartListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.InspectionLineItemListReport == reportRequest.getReportType())
        {
            InspectionLineItemListReport report = new InspectionLineItemListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.WorkOrderListReport == reportRequest.getReportType())
        {
            WorkOrderListReport report = new WorkOrderListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.NearMissListReport == reportRequest.getReportType())
        {
            NearMissListReport report = new NearMissListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.HelpTopicListReport == reportRequest.getReportType())
        {
            HelpTopicListReport report = new HelpTopicListReport(reportRequest);
            return report.runReport();
        } else if (ReportTypes.AndonListReport == reportRequest.getReportType())
        {
            AndonListReport report = new AndonListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.VCRListReport == reportRequest.getReportType())
        {
            VcrListReport report = new VcrListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.NCRGroupListReport == reportRequest.getReportType())
        {
            NCRGroupListReport report = new NCRGroupListReport(context, reportRequest);
            return report.runReport();
        } else if (ReportTypes.SuppoerTicketListReport == reportRequest.getReportType())
        {
            SupportTicketListReport report = new SupportTicketListReport(context, reportRequest);
            return report.runReport();
        }else if (ReportTypes.EquipmentList == reportRequest.getReportType())
        {
            EquipmentListReport report = new EquipmentListReport(context, reportRequest);
            return report.runReport();
        }else if (ReportTypes.NcrUnitAssignListReport == reportRequest.getReportType())
        {
            NcrUnitListReport report = new NcrUnitListReport(context, reportRequest);
            return report.runReport();
        }*/

        return null;
    }

}
