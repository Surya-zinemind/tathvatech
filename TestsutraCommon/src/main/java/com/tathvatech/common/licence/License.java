package com.tathvatech.common.licence;

public class License 
{
	public int licenseCount;
	public int readonlyLicenseCount;
	public License(int licenseCount, int readonlyLicenseCount)
	{
		this.licenseCount = licenseCount;
		this.readonlyLicenseCount = readonlyLicenseCount;
	}
}
