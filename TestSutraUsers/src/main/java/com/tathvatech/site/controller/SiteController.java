package com.tathvatech.site.controller;

import java.sql.Connection;
import java.util.List;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.part.SupplierOID;
import com.tathvatech.ts.core.sites.Site;
import com.tathvatech.ts.core.sites.SiteGroup;
import com.tathvatech.ts.core.sites.SiteOID;
import com.tathvatech.ts.core.sites.SiteQuery;

public class SiteController {

	public static void createSite(UserContext context, Site site) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SiteManager.createSite(context, site);
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void updateSite(UserContext context, Site site) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SiteManager.updateSite(context, site);
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static void deleteSite(UserContext context, int sitePk) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SiteManager.deleteSite(context, sitePk);
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	/**
	 * use this method to bypass the site security settings and get all the sites
	 * this is a bad thing to do.. but the method which takes context as the first argumant only looks
	 * if a person has specific roles before it allows.. that is not right. (HSE related roles)
	 * @param siteFilter
	 * @return
	 */
	public static List<Site> getAllSites(SiteFilter siteFilter)
	{
		try 
		{
			return SiteManager.getSites(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public static List<Site> getSites(UserContext context, SiteFilter siteFilter)
	{
		try 
		{
			new SiteQuerySecurityProcessor().addAuthorizationFilterParams(context, siteFilter);
			return SiteManager.getSites(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public static List<SiteQuery> getSiteList(UserContext context, SiteFilter siteFilter)
	{
		try 
		{
			new SiteQuerySecurityProcessor().addAuthorizationFilterParams(context, siteFilter);
			return SiteManager.getSiteList(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Site getSite(int sitePk)
	{
		try {
			return PersistWrapper.readByPrimaryKey(Site.class, sitePk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static SiteGroup getSiteGroup(Integer siteGroupFk)
	{
		try {
			return PersistWrapper.readByPrimaryKey(SiteGroup.class, siteGroupFk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static SiteGroup getSiteGroup(SiteOID siteOID)
	{
		try 
		{
			return PersistWrapper.read(SiteGroup.class, "select sg.* from site_group sg, site where site.siteGroupFk = sg.pk and site.pk = ? ", siteOID.getPk());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	

	public static void setLinkedSupplier(UserContext userContext,
			SiteOID siteOID, SupplierOID supplierOID) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SiteManager.setLinkedSupplier(userContext,
        			siteOID, supplierOID);
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static SiteGroup saveSiteGroup(UserContext context, SiteGroup siteGroup)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            siteGroup = SiteManager.saveSiteGroup(context, siteGroup);
            con.commit();
            
            return siteGroup;
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}

	public static List<SiteGroup> getSiteGroupList()
	{
		return SiteManager.getSiteGroupList();
	}
}
