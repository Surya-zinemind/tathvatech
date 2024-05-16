package com.tathvatech.injuryReport.service;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.injuryReport.common.*;
import com.tathvatech.injuryReport.entity.Injury;
import com.tathvatech.injuryReport.entity.InjuryAfterTreatment;
import com.tathvatech.injuryReport.oid.InjuryOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;

import java.util.List;

public interface InjuryService {
    InjuryBean save(UserContext context, InjuryBean bean, List<AttachmentIntf> attachments) throws Exception;
    InjuryQuery create(UserContext context, InjuryBean bean, List<AttachmentIntf> attachments, boolean isAndroidDevice) throws Exception;
    InjuryBean verifyInjuryReport(UserContext context, InjuryOID injuryOID, String message) throws Exception;
    InjuryBean closeInjuryReport(UserContext context, InjuryOID injuryOID, String message) throws Exception;
    InjuryBean reopenInjuryReport(UserContext context, InjuryOID injuryOID, String message) throws Exception;
    Injury get(InjuryOID oid) throws Exception;
    List<InjuryQuery> getInjuryReportList(InjuryFilter injuryFilter) throws Exception;
    List<InjuryQuery> getInjuryReportList(UserContext context, InjuryReportQueryFilter injuryQueryFilter);
    List<InjuryQuery> getPendingVerificationMorethan1daysForHSECordinator(UserContext context);
    List<InjuryQuery> getPendingVerificationMorethan2daysForHSEDirector(UserContext context);
    void deleteInjuryReport(int injuryPk) throws Exception;
    InjuryQuery getInjuryReportByPk(int injuryPk) throws Exception;
    List<InjuryUserQuery> getInjuryUserList(int sitePk) throws Exception;
    List<InjuryReportGraphQuery> getInjuriesOfWorkstation(InjuryFilter injuryFilter);
    List<InjuryAfterTreatmentQuery> getInjuryAfterTreatmentList();
    InjuryAfterTreatment getInjuryAfterTreatment(int pk);
    List<User> getAssignedSupervisors(String filterString);
}
