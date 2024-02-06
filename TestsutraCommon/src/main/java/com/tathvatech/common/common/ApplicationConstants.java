/*
 * Created on Apr 4, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.common;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ApplicationConstants
{
    public static final int SEQID_FETCH_LOTSIZE = new Integer(ApplicationProperties.getProperty("config/SeqIdFetchLotSize")).intValue();

    //email address from which messages are send to customers.
    public static final String SERVICE_EMAIL_ADDRESS = ApplicationProperties.getProperty("config/email/ServiceEmailAddress");


    public static final String GEOIP_DATA_FILE = TestSutraProperties.getProperty("config/GeoIPDataFile");


	public static final long responseIdBatchSize = 25;

}
