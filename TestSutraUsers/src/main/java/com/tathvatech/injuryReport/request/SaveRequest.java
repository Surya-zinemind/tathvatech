package com.tathvatech.injuryReport.request;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.injuryReport.common.InjuryBean;
import lombok.Data;

import java.util.List;

@Data
public class SaveRequest {
    private InjuryBean injuryBean;
    private List<AttachmentIntf> attachments;
}
