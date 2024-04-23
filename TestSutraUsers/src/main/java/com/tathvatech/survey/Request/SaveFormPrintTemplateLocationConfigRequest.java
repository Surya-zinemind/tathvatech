package com.tathvatech.survey.Request;

import com.tathvatech.pdf.config.PdfTemplatePrintLocationConfig;
import com.tathvatech.user.OID.FormOID;
import lombok.Data;

@Data
public class SaveFormPrintTemplateLocationConfigRequest {
    private FormOID formOID;
    private PdfTemplatePrintLocationConfig config;
}
