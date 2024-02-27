package com.tathvatech.site.controller;

import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.site.common.SiteQuery;
import com.tathvatech.site.entity.SiteFilter;
import com.tathvatech.site.entity.SiteGroup;
import com.tathvatech.site.processor.SiteQuerySecurityProcessor;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.SupplierOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.service.EmailServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.util.List;

@RequestMapping("/site")
@RestController
@RequiredArgsConstructor
public class SiteController {
	private  final  SiteService siteService;

	private final  PersistWrapper persistWrapper;

	private final EmailServiceManager emailServiceManager;
	public  void createSite(UserContext context, Site site) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

			siteService.createSite(context, site);
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

	public void updateSite(UserContext context, Site site) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

			siteService.updateSite(context, site);
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

	public  void deleteSite(UserContext context, int sitePk) throws Exception{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

			siteService.deleteSite(context, sitePk);
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
	public  List<Site> getAllSites(SiteFilter siteFilter)
	{
		try 
		{
			return siteService.getSites(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public  List<Site> getSites(UserContext context, SiteFilter siteFilter)
	{
		try 
		{
			new SiteQuerySecurityProcessor().addAuthorizationFilterParams( context,siteFilter);
			return siteService.getSites(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public  List<SiteQuery> getSiteList(UserContext context, SiteFilter siteFilter)
	{
		try 
		{
			new SiteQuerySecurityProcessor().addAuthorizationFilterParams(context, siteFilter);
			return siteService.getSiteList(siteFilter);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public  Site getSite(int sitePk)
	{
		try {
			return (Site) persistWrapper.readByPrimaryKey(Site.class, sitePk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SiteGroup getSiteGroup(Integer siteGroupFk)
	{
		try {return (SiteGroup) persistWrapper.readByPrimaryKey(SiteGroup.class, siteGroupFk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public  SiteGroup getSiteGroup(SiteOID siteOID)
	{
		try 
		{
			return persistWrapper.read(SiteGroup.class, "select sg.* from site_group sg, site where site.siteGroupFk = sg.pk and site.pk = ? ", siteOID.getPk());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	

	public  void setLinkedSupplier(UserContext userContext,
			SiteOID siteOID, SupplierOID supplierOID) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

			siteService.setLinkedSupplier(userContext,
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

	public  SiteGroup saveSiteGroup(UserContext context, SiteGroup siteGroup)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            siteGroup =siteService.saveSiteGroup(context, siteGroup);
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

	public  List<SiteGroup> getSiteGroupList()
	{
		return siteService.getSiteGroupList();
	}
}
