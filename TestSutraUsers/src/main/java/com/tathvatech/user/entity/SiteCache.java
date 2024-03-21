package com.tathvatech.user.entity;

import com.tathvatech.user.service.CommonServicesDelegate;
import com.tathvatech.user.OID.SiteOID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;


public class SiteCache extends HashMap<Long, Site> {
	@Autowired
	private CommonServicesDelegate commonServicesDelegate;


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
			sd = (Site) commonServicesDelegate.getObjectByPk(Site.class, (int) siteOID.getPk());
			this.put(siteOID.getPk(), sd);
		}
		return sd;
	}
}
