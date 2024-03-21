package com.tathvatech.survey.common;



public class SurveyForm
{
    private static final int maxLongValLength = new Long(Long.MAX_VALUE).toString().length();

    public static final String RESPONSEMODE_DATAENTRY = "DataEntry";
    public static final String RESPONSEMODE_NORMAL = "Normal";
    public static final String RESPONSEMODE_PREVIEW = "Preview";
	public static final String RESPONSEMODE_UPLOAD = "Upload";
	public static final String RESPONSEMODE_DEVICE_UPLOAD = "Device";
    
    public static final String RESPONSECODE_MESSAGE = "RESPONSE_MESSAGE";
    public static final String RESPONSECODE_SUCCESS_DATAENTRY = "SUCCESS_DATAENTRY";
}
