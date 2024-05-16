package com.tathvatech.injuryReport.common;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.injuryReport.utils.DateFormatter;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InjuryReportPrinter
{
    private static final Logger logger = LoggerFactory.getLogger(InjuryReportPrinter.class);
    UserContext context;

    public static String OPTION_PRINT_RESPONSE = "OPT_PRINT_RESPONSE";
    public static String OPTION_PRINT_EMPTY_FORM = "OPT_PRINT_EMPTY_FORM";

    public static String PRINT_ITEMS_WITH_FLAG = "PRINT_ITEMS_WITH_FLAG";

    List pdfAttachments = new ArrayList();
    private static int HEADER_CONTENT_SPACING = 20;
    private static int TOP_MARGIN_FOR_HEADER = 80;
    private static int LEFT_MARGIN = 50;
    private static int RIGHT_MARGIN = 50;
    private static int BOTTOM_MARGIN_FOR_FOOTER = 40;


    DateFormatter dateFormatter;
    private static Font mainheaderFont = new Font(Font.FontFamily.HELVETICA,18, Font.BOLD);
    private static Font labelFont = new Font(Font.FontFamily.HELVETICA, 7,Font.BOLD);
    private static Font fontStyleCells = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);
    private static Font fontStyleUserInput = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC);
    private static Font fontFooterSmall = FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL);
    private static Font fontStyleHeaders = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);
    private static Font fontSectionTitle = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD);
    private static Font fontStyleUnderLine = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.UNDERLINE);
    private static Font fontStyleContent = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);

    public InjuryReportPrinter(UserContext context)
    {
        this.context = context;
        BaseFont base = null;
        try
        {
            String fontFile = ApplicationProperties.getPdfPrintFont();
            base = BaseFont.createFont(fontFile, BaseFont.IDENTITY_H, false);
        }
        catch (Exception e)
        {
            logger.warn(String.format("Could not load %s font, Please make sure the font is installed on the host machine in the resources folder", ApplicationProperties.getPdfPrintFont()));
        }
        mainheaderFont = new Font(base,18, Font.BOLD);
        labelFont = new Font(base, 7,Font.BOLD);
        fontStyleCells = new Font(base, 9, Font.NORMAL);
        fontStyleUserInput = new Font(base,9, Font.ITALIC);
        fontFooterSmall = new Font(base, 6, Font.NORMAL);
        fontStyleHeaders = new Font(base , 9, Font.BOLD);
        fontStyleContent = new Font(base , 9, Font.BOLD);
        fontSectionTitle = new Font(base, 12, Font.BOLD);
        fontStyleUnderLine = new Font(base, 7, Font.UNDERLINE);
    }

    public ByteArrayOutputStream printForm(InjuryQuery injuryQuery) throws Exception
    {
        dateFormatter = DateFormatter.getInstance(context.getTimeZone());
        ByteArrayOutputStream returnStream = new ByteArrayOutputStream();
        List<InputStream> list = new ArrayList<InputStream>();

        try
        {
            pdfAttachments = new ArrayList();

            // do the print logic here....
            Document document = new Document(PageSize.A4, LEFT_MARGIN, RIGHT_MARGIN, TOP_MARGIN_FOR_HEADER, BOTTOM_MARGIN_FOR_FOOTER);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, buffer);
            TableHeader event = new TableHeader();

            writer.setPageEvent(event);

            document.open();

            event.setProjectName(injuryQuery.getProjectName());
            event.setInjury(injuryQuery);

            printInjuryDetail(injuryQuery, document);

            document.close();

            InputStream is = new ByteArrayInputStream(buffer.toByteArray());
            list.add(is);


            //print attachments
            List<Attachment> attachments = CommonServicesDelegate.getAttachments(injuryQuery.getPk(), EntityTypeEnum.Injury.getValue());
            for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
                Attachment attachment = (Attachment) iterator.next();
                if(attachment.getFileName().toLowerCase().endsWith("pdf"))
                {
                    printPdf(injuryQuery.getInjuryReportNo(), attachment.getFileDisplayName(),
                            attachment.getFileName(), list);
                }
                else
                {
                    Document doc = new Document(PageSize.A4, LEFT_MARGIN, RIGHT_MARGIN, TOP_MARGIN_FOR_HEADER, BOTTOM_MARGIN_FOR_FOOTER);
                    ByteArrayOutputStream buff = new ByteArrayOutputStream();
                    PdfWriter wri = PdfWriter.getInstance(doc, buff);

                    doc.open();
                    printPicture(attachment.getFileDisplayName(), attachment.getFileName(), doc);
                    doc.close();

                    InputStream iss = new ByteArrayInputStream(buff.toByteArray());
                    list.add(iss);
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        returnStream = doMergeByteArray(list);
        return returnStream;
    }

    private void printPicture(String fileDisplayName, String fileName, Document document)throws Exception
    {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setKeepTogether(true);

        PdfPCell q = new PdfPCell();
        q.setBorder(0);
        /*
         * updated on 06 May 2013; to show description along with image heading.
         * previously they where on different lines.
         */
        q.addElement(new Phrase(fileDisplayName, fontStyleHeaders));
        q.addElement(new Phrase("\n", new Font(Font.FontFamily.HELVETICA, 6,Font.NORMAL)));
        Image pdfImage = Image.getInstance(FileStoreManager.getFile(fileName).getAbsolutePath());

        if(pdfImage.getHeight() > getDocumentHeight(document) || pdfImage.getWidth() > getDocumentWidth(document))
        {
            pdfImage.scaleToFit(getDocumentWidth(document), getDocumentHeight(document));
        }
        else
        {
            pdfImage.scaleAbsolute(pdfImage.getWidth(), pdfImage.getHeight());
        }
        q.addElement(pdfImage);
        table.addCell(q);
        table.setSpacingBefore(15);
        document.add(table);
    }

    private void printPdf(String injuryReportNo, String fileDisplayName, String fileName, List<InputStream> list) throws Exception
    {
        //attachment cover sheet print
        Document doc = new Document(PageSize.A4, LEFT_MARGIN, RIGHT_MARGIN, TOP_MARGIN_FOR_HEADER, BOTTOM_MARGIN_FOR_FOOTER);
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        PdfWriter wri = PdfWriter.getInstance(doc, buff);

        doc.open();
        printAttachmentCoverPage(injuryReportNo, fileDisplayName, doc);
        doc.close();

        InputStream iss = new ByteArrayInputStream(buff.toByteArray());
        list.add(iss);

        //attachment print
        InputStream fiss = printFile(FileStoreManager.getFile(fileName));
        if(fiss != null)
        {
            list.add(fiss);
        }

    }

    private void printInjuryDetail(InjuryQuery injuryQuery, Document document)throws Exception
    {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        if(true)
        {
            PdfPCell headingCell1 = new PdfPCell(new Phrase("Injured Person", fontStyleHeaders));
            headingCell1.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell1);

            PdfPCell headingCell2 = new PdfPCell(new Phrase("Type of Person", fontStyleHeaders));
            headingCell2.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell2);

            PdfPCell headingCell3 = new PdfPCell(new Phrase("Injury Type", fontStyleHeaders));
            headingCell3.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell3);

            PdfPCell headingCell5 = new PdfPCell(new Phrase("Status", fontStyleHeaders));
            headingCell5.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell5);

            PdfPCell bodyCell1 = new PdfPCell(new Phrase(injuryQuery.getInjuredPerson(), fontStyleCells));
            table.addCell(bodyCell1);

            String typeOfPersonString = null;
            if(injuryQuery.getTypeOfPerson() != null)
            {
                typeOfPersonString = (injuryQuery.getTypeOfPerson() == 1)?"Internal":"External";
            }
            PdfPCell bodyCell2 = new PdfPCell(new Phrase(ListStringUtil.showString(typeOfPersonString), fontStyleCells));
            table.addCell(bodyCell2);

            String injuryType = null;
            if(injuryQuery.getTypeOfInjury() != null)
            {
                injuryType = (injuryQuery.getTypeOfInjury() == 1)?"Work Related":"Non - Work Related";
            }
            PdfPCell bodyCell3 = new PdfPCell(new Phrase(ListStringUtil.showString(injuryType), fontStyleCells));
            table.addCell(bodyCell3);

            PdfPCell bodyCell4 = new PdfPCell(new Phrase(injuryQuery.getStatus(), fontStyleCells));
            table.addCell(bodyCell4);

        }
        if(true)
        {
            PdfPCell headingCell = new PdfPCell(new Phrase("Site", fontStyleHeaders));
            headingCell.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell);

            PdfPCell headingCell2 = new PdfPCell(new Phrase("Raised By", fontStyleHeaders));
            headingCell2.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell2);

            PdfPCell headingCell3 = new PdfPCell(new Phrase("Verified By", fontStyleHeaders));
            headingCell3.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell3);

            PdfPCell headingCell4 = new PdfPCell(new Phrase("Closed By", fontStyleHeaders));
            headingCell4.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell4);

            PdfPCell bodyCell5 = new PdfPCell(new Phrase(injuryQuery.getSiteName(), fontStyleCells));
            table.addCell(bodyCell5);

            PdfPCell bodyCell2 = new PdfPCell(new Phrase((injuryQuery.getCreatedByName() != null)?injuryQuery.getCreatedByName():injuryQuery.getCreatedByInitial(), fontStyleCells));
            table.addCell(bodyCell2);

            PdfPCell bodyCell3 = new PdfPCell(new Phrase(injuryQuery.getVerifiedByName(), fontStyleCells));
            table.addCell(bodyCell3);

            PdfPCell bodyCell4 = new PdfPCell(new Phrase(injuryQuery.getClosedByName(), fontStyleCells));
            table.addCell(bodyCell4);
        }
        if(true)
        {
            PdfPCell headingCell5 = new PdfPCell(new Phrase("Date of Injury", fontStyleHeaders));
            headingCell5.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell5);

            PdfPCell headingCell2 = new PdfPCell(new Phrase("Raised Time", fontStyleHeaders));
            headingCell2.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell2);

            PdfPCell headingCell3 = new PdfPCell(new Phrase("Verified Time", fontStyleHeaders));
            headingCell3.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell3);

            PdfPCell headingCell4 = new PdfPCell(new Phrase("Closed Time", fontStyleHeaders));
            headingCell4.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell4);

            PdfPCell bodyCell5 = new PdfPCell(new Phrase((injuryQuery.getDateOfInjury() != null)?dateFormatter.formatDateTime(injuryQuery.getDateOfInjury()):"", fontStyleCells));
            table.addCell(bodyCell5);

            PdfPCell bodyCell2 = new PdfPCell(new Phrase(dateFormatter.formatDateTime(injuryQuery.getCreatedDate()), fontStyleCells));
            table.addCell(bodyCell2);

            PdfPCell bodyCell3 = new PdfPCell(new Phrase((injuryQuery.getVerifiedDate() != null)?dateFormatter.formatDateTime(injuryQuery.getVerifiedDate()):"", fontStyleCells));
            table.addCell(bodyCell3);

            PdfPCell bodyCell4 = new PdfPCell(new Phrase((injuryQuery.getClosedDate() != null)?dateFormatter.formatDateTime(injuryQuery.getClosedDate()):"", fontStyleCells));
            table.addCell(bodyCell4);
        }
        if(true)
        {
            PdfPCell headingCell2 = new PdfPCell(new Phrase("Project", fontStyleHeaders));
            headingCell2.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell2);

            PdfPCell headingCell3 = new PdfPCell(new Phrase("Location", fontStyleHeaders));
            headingCell3.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell3);

            PdfPCell headingCell4 = new PdfPCell(new Phrase("Gracis Reporting No", fontStyleHeaders));
            headingCell4.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell4);

            PdfPCell headingCell5 = new PdfPCell(new Phrase("Myosh Reporting No", fontStyleHeaders));
            headingCell5.setBackgroundColor(new BaseColor(230, 230, 230));
            table.addCell(headingCell5);

            PdfPCell bodyCell2 = new PdfPCell(new Phrase((injuryQuery.getProjectName() != null)?injuryQuery.getProjectName():"Not Selected", fontStyleCells));
            table.addCell(bodyCell2);

            String locationName = "";
            locationName = injuryQuery.getLocationName();
            if (injuryQuery.getLocationType().equals("Location")) {
                locationName = locationName + " (" + injuryQuery.getLocationOther() + ")";
            }
            PdfPCell bodyCell3 = new PdfPCell(new Phrase(locationName, fontStyleCells));
            table.addCell(bodyCell3);

            String gracReporting;
            if(injuryQuery.getReportingRequired() == null)
            {
                gracReporting = "";
            }
            else
            {
                if(injuryQuery.getReportingRequired() == true)
                {
                    gracReporting = "Required:";
                    if(injuryQuery.getReportingNumber() != null)
                    {
                        gracReporting = gracReporting + injuryQuery.getReportingNumber();
                    }
                }
                else
                {
                    gracReporting = "Not required";
                }
            }
            PdfPCell bodyCell4 = new PdfPCell(new Phrase(gracReporting, fontStyleCells));
            table.addCell(bodyCell4);

            String myoReporting;
            if(injuryQuery.getReferenceRequired() == null)
            {
                myoReporting = "";
            }
            else
            {
                if(injuryQuery.getReferenceRequired() == true)
                {
                    myoReporting = "Required:";
                    if(injuryQuery.getReferenceNo() != null)
                    {
                        myoReporting = myoReporting + injuryQuery.getReferenceNo();
                    }
                }
                else
                {
                    myoReporting = "Not required";
                }
            }
            PdfPCell bodyCell5 = new PdfPCell(new Phrase(myoReporting, fontStyleCells));
            table.addCell(bodyCell5);
        }
        document.add(table);


        if(true)
        {
            PdfPTable cTable = new PdfPTable(2);
            cTable.setWidthPercentage(100);
            cTable.setSpacingBefore(10);

            PdfPCell hCell01 = new PdfPCell(new Phrase("Nature Of Injury", fontStyleHeaders));
            hCell01.setBorder(0);
            hCell01.setColspan(2);
            cTable.addCell(hCell01);
            PdfPCell bodyCell01 = new PdfPCell(new Phrase(InjuryReportUtil.getNatureOfInjury(injuryQuery), fontStyleCells));
            bodyCell01.setMinimumHeight(30);
            bodyCell01.setColspan(2);
            cTable.addCell(bodyCell01);

            PdfPCell hCell1 = new PdfPCell(new Phrase("Details Of Injury", fontStyleHeaders));
            hCell1.setBorder(0);
            hCell1.setColspan(2);
            cTable.addCell(hCell1);
            PdfPCell bodyCell1 = new PdfPCell(new Phrase(injuryQuery.getDescription(), fontStyleCells));
            bodyCell1.setMinimumHeight(30);
            bodyCell1.setColspan(2);
            cTable.addCell(bodyCell1);

            PdfPCell hCell2 = new PdfPCell(new Phrase("Equipment Involved:", fontStyleHeaders));
            hCell2.setBorder(0);
            hCell2.setColspan(2);
            cTable.addCell(hCell2);
            String equipmentDetailString;
            if(injuryQuery.getDetailsEquipmentStatus() == null || injuryQuery.getDetailsEquipmentStatus() == false)
            {
                equipmentDetailString = "No equipment involved";
            }
            else
            {
                equipmentDetailString = injuryQuery.getDetailsEquipment();
            }
            PdfPCell bodyCell2 = new PdfPCell(new Phrase(equipmentDetailString, fontStyleCells));
            bodyCell2.setMinimumHeight(30);
            bodyCell2.setColspan(2);
            cTable.addCell(bodyCell2);


            PdfPCell hCell3 = new PdfPCell(new Phrase("Details Of Witness:", fontStyleHeaders));
            hCell3.setBorder(0);
            hCell3.setColspan(2);
            cTable.addCell(hCell3);
            String witnessString;
            if(injuryQuery.getDetailsWitnesStatus() == null || injuryQuery.getDetailsWitnesStatus() == false)
            {
                witnessString = "No witness involved";
            }
            else
            {
                witnessString = injuryQuery.getDetailsWitnes();
            }
            PdfPCell bodyCell3 = new PdfPCell(new Phrase(witnessString, fontStyleCells));
            bodyCell3.setMinimumHeight(30);
            bodyCell3.setColspan(2);
            cTable.addCell(bodyCell3);

            PdfPCell hCell4 = new PdfPCell(new Phrase("Details Of Treatment provided:", fontStyleHeaders));
            hCell4.setBorder(0);
            hCell4.setColspan(2);
            cTable.addCell(hCell4);
            PdfPCell bodyCell4 = new PdfPCell(new Phrase(injuryQuery.getDetailsTreatment(), fontStyleCells));
            bodyCell4.setMinimumHeight(30);
            bodyCell4.setColspan(2);
            cTable.addCell(bodyCell4);

            PdfPCell hCell5 = new PdfPCell(new Phrase("Root Cause:", fontStyleHeaders));
            hCell5.setBorder(0);
            hCell5.setColspan(2);
            cTable.addCell(hCell5);
            PdfPCell bodyCell5 = new PdfPCell(new Phrase(injuryQuery.getRootCauseOfInjury(), fontStyleCells));
            bodyCell5.setMinimumHeight(30);
            bodyCell5.setColspan(2);
            cTable.addCell(bodyCell5);

            PdfPCell hCell6 = new PdfPCell(new Phrase("Supplimentary Cause:", fontStyleHeaders));
            hCell6.setBorder(0);
            hCell6.setColspan(2);
            cTable.addCell(hCell6);
            PdfPCell bodyCell6 = new PdfPCell(new Phrase(injuryQuery.getSupplimentaryCause(), fontStyleCells));
            bodyCell6.setMinimumHeight(30);
            bodyCell6.setColspan(2);
            cTable.addCell(bodyCell6);

            PdfPCell hCell7 = new PdfPCell(new Phrase("What has been done:", fontStyleHeaders));
            hCell7.setBorder(0);
            hCell7.setColspan(2);
            cTable.addCell(hCell7);
            PdfPCell bodyCell7 = new PdfPCell(new Phrase(injuryQuery.getPrecautionDone(), fontStyleCells));
            bodyCell7.setMinimumHeight(30);
            bodyCell7.setColspan(2);
            cTable.addCell(bodyCell7);

            PdfPCell hCell8 = new PdfPCell(new Phrase("What still needs to be done:", fontStyleHeaders));
            hCell8.setBorder(0);
            hCell8.setColspan(2);
            cTable.addCell(hCell8);
            PdfPCell bodyCell8 = new PdfPCell(new Phrase(injuryQuery.getPrecautionRequired(), fontStyleCells));
            bodyCell8.setMinimumHeight(30);
            bodyCell8.setColspan(2);
            cTable.addCell(bodyCell8);

            PdfPCell hCell9 = new PdfPCell(new Phrase("Treated By", fontStyleHeaders));
            hCell9.setBorder(0);
            cTable.addCell(hCell9);
            PdfPCell hCell10 = new PdfPCell(new Phrase("Supervisor:", fontStyleHeaders));
            hCell10.setBorder(0);
            cTable.addCell(hCell10);
            PdfPCell bodyCell9 = new PdfPCell(new Phrase(injuryQuery.getTreatedBy(), fontStyleCells));
            bodyCell9.setMinimumHeight(30);
            cTable.addCell(bodyCell9);
            PdfPCell bodyCell10 = new PdfPCell(new Phrase(injuryQuery.getSupervisedByName(), fontStyleCells));
            bodyCell10.setMinimumHeight(30);
            cTable.addCell(bodyCell10);



            document.add(cTable);
        }

        if(true)
        {
            PdfPTable cTable = new PdfPTable(1);
            cTable.setWidthPercentage(100);
            cTable.setSpacingBefore(10);

            PdfPCell hCell001 = new PdfPCell(new Phrase("What happend after treatment", fontStyleHeaders));
            hCell001.setBorder(0);
            cTable.addCell(hCell001);

            StringBuffer sb = new StringBuffer();
            try {
                List<InjuryAssignAfterTreatmentQuery> listAssignAfterTreatment = InjuryAssignAfterTreatmentManager
                        .getAssignAfterTreatmentByInjuryPk(injuryQuery.getPk());
                for (Iterator iterator = listAssignAfterTreatment.iterator(); iterator.hasNext();) {

                    InjuryAssignAfterTreatmentQuery assignAfterTreatmentQuery = (InjuryAssignAfterTreatmentQuery) iterator.next();
                    if (assignAfterTreatmentQuery.getPk() != 0)
                    {
                        sb.append(assignAfterTreatmentQuery.getAfterTreatmentMastername().toString()).append("\n");
                    }
                }
            } catch (Exception e) {
                ((EtestApplication) UI.getCurrent()).showMessage(e.getMessage());
                e.printStackTrace();
            }
            PdfPCell bodyCell1 = new PdfPCell(new Phrase(sb.toString(), fontStyleCells));
            bodyCell1.setMinimumHeight(30);
            cTable.addCell(bodyCell1);


            PdfPCell hCell01 = new PdfPCell(new Phrase("Comments:", fontStyleHeaders));
            hCell01.setBorder(0);
            cTable.addCell(hCell01);

            StringBuffer sbComments = new StringBuffer();
            List<Comment> c = ProjectDelegate.getComments(injuryQuery.getPk(), EntityTypeEnum.Injury);
            for (Iterator iterator2 = c.iterator(); iterator2.hasNext();)
            {
                Comment comment = (Comment) iterator2.next();
                User user = AccountDelegate.getUser(comment.getCreatedBy());
                String time = dateFormatter.formatDate(comment.getCreatedDate());
                sbComments.append(user.getDisplayString() + " : " + time + "\n");
                sbComments.append(comment.getCommentText());
                sbComments.append("\n\n");
            }
            PdfPCell bodyCell2 = new PdfPCell(new Phrase(sbComments.toString(), fontStyleCells));
            bodyCell2.setMinimumHeight(30);
            cTable.addCell(bodyCell2);

            document.add(cTable);
        }
    }


    private void printAttachmentCoverPage(String injuryNumber, String fileDisplayName, Document document)throws Exception
    {
        // Table object
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);

        t.setSpacingBefore(25);

        PdfPCell p = new PdfPCell(new Phrase("Printing attachments for", fontStyleHeaders));
        p.setColspan(2);
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(p);

        PdfPCell c1 = new PdfPCell(new Phrase("Form Name", fontStyleHeaders));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c1);

        PdfPCell c3 = new PdfPCell(new Phrase("File Name", fontStyleHeaders));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c3);

        PdfPCell c33 = new PdfPCell(new Phrase(injuryNumber, fontStyleCells));
        c33.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c33);

        PdfPCell c44 = new PdfPCell(new Phrase(fileDisplayName, fontStyleCells));
        c44.setHorizontalAlignment(Element.ALIGN_CENTER);
        t.addCell(c44);

        document.add(t);
    }


    private InputStream printFile(File file)throws Exception
    {

        InputStream templateInputStream = new FileInputStream(file);
        PdfReader reader = new PdfReader(templateInputStream);

//		PdfDictionary pageDict;
//		for (int i = 1; i <= reader.getNumberOfPages(); i++)
//		{
//        	int rotation = reader.getPageRotation(i);
//            pageDict = reader.getPageN(i);
//            pageDict.put(PdfName.ROTATE, new PdfNumber(rotation));
//        }
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, buff);
        stamper.close();
        reader.close();
        templateInputStream.close();

        InputStream iss = new ByteArrayInputStream(buff.toByteArray());
        return iss;


    }


    /**
     * Inner class to add a table as header and footer.
     */
    class TableHeader extends PdfPageEventHelper
    {
        String projectName;
        InjuryQuery injuryQ;

        /** The header text. */
        String		header;
        /** The template with the total number of pages. */
        PdfTemplate	total;

        boolean formHasInstructions = false;
        /**
         * Allows us to change the content of the header.
         *
         * @param header
         *            The new header String
         */
        public void setHeader(String header)
        {
            this.header = header;
        }

        public void setInjury(InjuryQuery injuryQ)
        {
            this.injuryQ = injuryQ;
        }

        public void setProjectName(String aProjectName)
        {
            this.projectName = aProjectName;
        }

        /**
         * Creates the PdfTemplate that will hold the total number of pages.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
         *      com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document)
        {
            total = writer.getDirectContent().createTemplate(30, 9);
        }

        /**
         * Adds a header to every page
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
         *      com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document)
        {
            float l = document.left();
            float r = document.right();
            float headerWidth = getDocumentWidth(document);

            float t = document.top();
            float b = document.bottom();

            try
            {
                PdfPTable headerTable = new PdfPTable(4);
                headerTable.setTotalWidth(headerWidth);

                Image logo = Image.getInstance(FileStoreManager.getResourceFile(ApplicationProperties.PRINT_HEADER_LOGO_FILENAME).getAbsolutePath());
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                logoCell.setVerticalAlignment(Element.ALIGN_CENTER);
//				headerTable.getDefaultCell().setPaddingLeft(13);
//				headerTable.getDefaultCell().setPaddingTop(6);
                headerTable.addCell(logo);

                //revision number table
                PdfPTable revisionTable = new PdfPTable(1);
                PdfPCell c1 = new PdfPCell(new Phrase("REV_" + dateFormatter.formatDateTime(new Date()), fontStyleHeaders));
                c1.setBorder(0);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                revisionTable.addCell(c1);
                PdfPCell c2 = new PdfPCell(new Phrase("", fontStyleCells));
                c2.setBorder(0);
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                revisionTable.addCell(c2);
                headerTable.getDefaultCell().setPaddingTop(5);
                headerTable.getDefaultCell().setPaddingLeft(0);
                headerTable.addCell(revisionTable);

                //page number table
                PdfPTable pageNumberTable = new PdfPTable(2);
                pageNumberTable.setWidths(new int[]{70, 30});
                pageNumberTable.setWidthPercentage(100);

                PdfPCell pCell2 = new PdfPCell(new Phrase(String.format("Page No: %d of", writer.getPageNumber()), fontStyleHeaders));

                pCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pCell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
                pCell2.setBorder(0);
                pCell2.setNoWrap(true);
                pageNumberTable.addCell(pCell2);
                PdfPCell cell = new PdfPCell(Image.getInstance(total));
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setBorder(0);
                pageNumberTable.addCell(cell);

                PdfPCell tabCell = new PdfPCell(pageNumberTable);
                tabCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(tabCell);

                //Identity Number
                String formNString = injuryQ.getInjuryReportNo();
                PdfPCell c3 = new PdfPCell(new Phrase(formNString, fontStyleHeaders));
                c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(c3);


//2nd line start
                PdfPTable descTable = new PdfPTable(3);
                descTable.setWidths(new int[]{42,16,42});

                String projDetail = (projectName != null)?("Project: " + projectName):"";
                if(injuryQ.getSiteName() != null)
                {
                    projDetail = projDetail+ "\nSite: " + injuryQ.getSiteDescription();
                }
                PdfPCell projDescCell = new PdfPCell(new Phrase(projDetail, labelFont));
                projDescCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                projDescCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                projDescCell.setBorder(0);
                projDescCell.setFixedHeight(20);
                descTable.addCell(projDescCell);

                String testProcCellText = "";
                PdfPCell testProcCell = new PdfPCell(new Phrase(testProcCellText, fontStyleHeaders));
                testProcCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                testProcCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                testProcCell.setBorder(0);
                descTable.addCell(testProcCell);

                PdfPCell formDescCell = new PdfPCell(new Phrase("Injury Report", labelFont));
                formDescCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                formDescCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                formDescCell.setBorder(0);
                formDescCell.setFixedHeight(20);
                descTable.addCell(formDescCell);

                PdfPCell descCell = new PdfPCell(descTable);
                descCell.setColspan(4);
                descCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(descCell);

                headerTable.writeSelectedRows(0, -1, l, t+b+HEADER_CONTENT_SPACING, writer.getDirectContentUnder());



                //footer
                Font fontStyleFooters = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);
                PdfPTable footTable = new PdfPTable(1);
                footTable.setTotalWidth(headerWidth);
                footTable.getDefaultCell().setBorder(Rectangle.TOP);
                footTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                footTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

                // image code
				/*Image footerlogo = Image.getInstance(ApplicationProperties.getImageRoot() + "copyr2.png");
				footTable.addCell(footerlogo);*/

                String footerLine1 = ApplicationProperties.getPrintFooterLine1();
                footerLine1 = footerLine1.replace("{CopyrightSymbol}", "\u00A9");
                footerLine1 = footerLine1.replace("{Date}", DateFormatter.getInstance(context).getYear(new Date()));
                String companyName = AppConfigConstants.getValue(AppConfigConstants.CompanyName, new Date());
                footerLine1 = footerLine1.replace("{CompanyName}", companyName);

                footTable.addCell(new Phrase(footerLine1, fontStyleFooters));
                footTable.getDefaultCell().setBorderWidthTop(0);
                footTable.addCell(new Phrase("Date printed: " + DateFormatter.getInstance(context).formatDate(new Date()) + ", The hard copies of this document are uncontrolled. This is an electronically generated document and no signature is required",
                        fontFooterSmall));

                footTable.writeSelectedRows(0, -1, l, b, writer.getDirectContentUnder());
            }
            catch (Exception de)
            {
                throw new ExceptionConverter(de);
            }
        }

        /**
         * Fills out the total number of pages before the document is closed.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
         *      com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document)
        {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), fontStyleHeaders), 2, 2, 0);
        }
    }

    private float getDocumentWidth(Document document)
    {
        float l = document.left();
        float r = document.right();
        return r-l;
    }

    private float getDocumentHeight(Document document)
    {
        float t = document.top();
        float b = document.bottom();
        return t-b;

    }

    private ByteArrayOutputStream doMergeByteArray(List<InputStream> list)
            throws DocumentException, IOException
    {
        ByteArrayOutputStream printBuffer = new ByteArrayOutputStream();

        Document document = new Document();
        PdfCopy copy = new PdfSmartCopy(document, printBuffer);
        document.open();
        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            InputStream is = (InputStream) iterator.next();
            PdfReader reader = new PdfReader(is);
            copy.addDocument(reader);
            copy.freeReader(reader);
            reader.close();
        }
        copy.close();
        document.close();

        return printBuffer;
    }
}
