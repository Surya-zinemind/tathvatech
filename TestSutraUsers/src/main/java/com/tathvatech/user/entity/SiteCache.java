package com.tathvatech.user.entity;

import com.tathvatech.user.service.CommonServicesDelegate;
import com.tathvatech.user.OID.SiteOID;

import java.util.HashMap;


public class SiteCache extends HashMap<Long, Site> {

	private static SiteCache instance;
	private SiteCache()
	{
	}
	public static SiteCache getInstance()
	{
		if(instance == null)
			instance = new SiteCache();
		return instance;
	}
	
	public Site getSite(SiteOID siteOID)
	{
		Site sd = this.get(siteOID.getPk());
		if(sd == null)
		{
			sd = (Site) new CommonServicesDelegate().getObjectByPk(Site.class, (int) siteOID.getPk());
			this.put(siteOID.getPk(), sd);
		}
		return sd;
	}
}
