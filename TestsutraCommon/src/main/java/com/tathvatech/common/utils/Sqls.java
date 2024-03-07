/*
 * Created on Mar 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.utils;

import java.util.TimeZone;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Sqls
{
    public static String responseIdPattern = "<responseId>";
    
    public static String responseMasterInsertWithoutRespondent = "insert into TAB_RESPONSE "
    		+ "(surveyPk, responseStartTime, ipaddress, userPk, responseRefNo, status, "
    		+ "responseMode, testProcPk, percentComplete, noOfComments, "
    		+ "totalQCount, totalACount, passCount, failCount, dimentionalFailCount, naCount, current) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String responseMasterSql = "select masterTab.*, flagTab.pk, flagTab.flag, userTab.pk, userTab.userName, userTab.firstName, userTab.lastName, userTab.userType from tab_response masterTab left join TAB_RESPONSE_FLAGS flagTab on masterTab.flagPk=flagTab.pk, TAB_USER userTab where masterTab.userPk = userTab.pk and responseId = '" + responseIdPattern + "'"; 
    public static final String responseDetailSql = "select * from tab_response_desc where responseId = '" + responseIdPattern + "'"; 

    public static String deleteResponseSQL = "delete from tab_response_desc where responseId='" + responseIdPattern + "' and questionId='<surveyItemId>'";



    public static final String sSurveyItemResponseDetailSql = "select * from tab_response_desc where responseId = ? and questionId = ?"; 

    public static String clearDetailResponses = "delete from <detailTableName> where surveyPk='<surveyPk>'";
    
    public static String clearDetailResponse = "delete from <detailTableName> where surveyPk=? and responseId=?";
    public static String clearResponseSkipsSQL = "delete from TAB_QSKIPS where surveyPk=? and responseId=?";

    public static String finalizeResponse = "update TAB_RESPONSE set status=?, responseCompleteTime=?, responseSyncTime=?, userPk=? where responseId=?";
    //Uncomment once ResponseMasterNew is added
   /* public static String verityResponse = "update TAB_RESPONSE set verifiedDate=CURRENT_TIMESTAMP(), status='" + ResponseMasterNew.STATUS_VERIFIED + "', verifiedBy=?, verifyComment=? where responseId=?";
    public static String approveResponse = "update TAB_RESPONSE set approvedDate=CURRENT_TIMESTAMP(), status='" + ResponseMasterNew.STATUS_APPROVED + "', approvedBy=?, approveComment=? where responseId=?";
    public static String approveResponseWithComments = "update TAB_RESPONSE set approvedDate=CURRENT_TIMESTAMP(), status='" + ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS + "', approvedBy=?, approveComment=? where responseId=?";
   */ public static String changeResponseStatus = "update TAB_RESPONSE set status=? where responseId=?";
    public static String markResponseAsOldOrCurrent = "update TAB_RESPONSE set current=? where responseId=?";
    public static String setResponseCompletionDate = "update TAB_RESPONSE set responseCompleteTime=?, responseSyncTime=? where responseId=?";

    public static final String getResponseMastersForRespondentId = "SELECT * FROM TAB_RESPONSE where respondentPk='<respondentPk>' and surveyPk='<surveyPk>' order by responseTime desc";
    public static final String getResponseMaster = "SELECT * FROM TAB_RESPONSE where responseId='<responseId>' and surveyPk='<surveyPk>'";


    
    public static final String getSupportedCurrencies = "select * from TAB_CURRENCY"; 
    public static final String getPlans = "select * from TAB_PLAN where status='active'";
    public static final String getCountries = "select * from TAB_COUNTRY";
    public static final String getBillingCountry = "select * from TAB_COUNTRY where countryCode='<countryCode>'";
    public static final String getPlanFrequencies = "select * from TAB_PLAN_FREQUENCY";
    public static final String getPaymentMethodsForCurrency = "select TAB_PAY_METHOD.id from TAB_PAY_METHOD, TAB_CURRENCY_PAY_METHOD where" +
    		" TAB_PAY_METHOD.id = TAB_CURRENCY_PAY_METHOD.payMethodId and TAB_CURRENCY_PAY_METHOD.currencyCode='<currencyCode>'";

    public static final String logLogin = "insert into TAB_LOGIN_LOG (accountPk, userId, ipAddress, countryCode, loginTime, comments) values(?,?,?,?,?,?)";
    

	public static String getMaxResponseSeqNoForDevice = "SELECT responseRefNo from TAB_RESPONSE where responseNo = (select max(responseNo) from TAB_RESPONSE where userPk=<userPk> group by userPk)";

	public static String getSurveyFlagsAndCount = "select pk, flag, count(responseId) from TAB_RESPONSE_FLAGS left join TAB_RESPONSE on TAB_RESPONSE_FLAGS.pk=TAB_RESPONSE.flagPk where TAB_RESPONSE_FLAGS.surveyPk=<surveyPk> group by TAB_RESPONSE_FLAGS.flag";
	public static String clearFlagForAllResponses = "update TAB_RESPONSE set flagPk=null where flagPk=<flagPk>";
	public static String setFlagForResponse = "update TAB_RESPONSE set flagPk=<flagPk> where responseId=<responseId>";
	public static String clearFlagForResponse = "update TAB_RESPONSE set flagPk=null where responseId=<responseId>";

	public static String getTimeZoneOffsetForDB(TimeZone timeZone)
    {
        //find the timeZone offset for the database
        int minOffset = timeZone.getRawOffset()/(60*1000);
        int hOffset = minOffset/60;
        int mOffset = minOffset % 60;        
        StringBuffer offsetSB = new StringBuffer();
        if(minOffset > -1)
        	offsetSB.append("+");

        offsetSB.append(hOffset);
        offsetSB.append(":");
        offsetSB.append(mOffset);
        
        return offsetSB.toString();
    }


}


 
