package com.tathvatech.user.entity;

import com.tathvatech.common.service.CommonServicesDelegate;
import com.tathvatech.user.OID.SiteOID;

import java.util.HashMap;


public class SiteCache extends HashMap<Integer, Site> {

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
			sd = (Site) new CommonServicesDelegate().getObjectByPk(Site.class, siteOID.getPk());
			this.put(siteOID.getPk(), sd);
		}
		return sd;
	}
}
