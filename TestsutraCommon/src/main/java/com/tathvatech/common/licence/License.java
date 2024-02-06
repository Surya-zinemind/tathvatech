package com.tathvatech.common.licence;

public class License 
{
	int licenseCount;
	int readonlyLicenseCount;
	public License(int licenseCount, int readonlyLicenseCount)
	{
		this.licenseCount = licenseCount;
		this.readonlyLicenseCount = readonlyLicenseCount;
	}
}
