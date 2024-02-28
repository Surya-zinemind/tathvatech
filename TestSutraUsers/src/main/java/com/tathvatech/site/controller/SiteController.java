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
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;

@RequestMapping("/site")
@RestController
@RequiredArgsConstructor
public class SiteController {
	private  final  SiteService siteService;

	private final  PersistWrapper persistWrapper;

	private final EmailServiceManager emailServiceManager;
	@PostMapping("/createSite")
	public  void createSite(UserContext context, @RequestBody Site site) throws Exception{
		siteService.createSite(context, site);
	}

	@PutMapping("/updateSite")
	public void updateSite(UserContext context, @RequestBody Site site) throws Exception{
		siteService.updateSite(context, site);
	}

	@DeleteMapping("/deleteSite/{sitePk}")
	public  void deleteSite(UserContext context,@PathVariable("sitePk") int sitePk) throws Exception{
		siteService.deleteSite(context, sitePk);
	}

	/**
	 * use this method to bypass the site security settings and get all the sites
	 * this is a bad thing to do.. but the method which takes context as the first argumant only looks
	 * if a person has specific roles before it allows.. that is not right. (HSE related roles)
	 * @param siteFilter
	 * @return
	 */
	@GetMapping("/getAllSites")
	public  List<Site> getAllSites(@RequestBody SiteFilter siteFilter)
	{
			return siteService.getSites(siteFilter);
	}

	@GetMapping("/getSites")
	public  List<Site> getSites(UserContext context, @RequestBody SiteFilter siteFilter)
	{
		return siteService.getSites(siteFilter);
	}

	@GetMapping("/getSiteList")
	public  List<SiteQuery> getSiteList(UserContext context, @RequestBody SiteFilter siteFilter)
	{
		return siteService.getSiteList(siteFilter);
	}

	@GetMapping("/getSite/{sitePk}")
	public  Site getSite(@PathVariable("sitePk") int sitePk)
	{
		return (Site) persistWrapper.readByPrimaryKey(Site.class, sitePk);
	}

	@GetMapping("/getSiteGroup/{siteGroupFk}")
	public SiteGroup getSiteGroup(@PathVariable("siteGroupFk") Integer siteGroupFk)
	{
		return (SiteGroup) persistWrapper.readByPrimaryKey(SiteGroup.class, siteGroupFk);
	}
	

	@GetMapping("/getSiteGroup")
	public  SiteGroup getSiteGroup( @RequestBody SiteOID siteOID)
	{
		return persistWrapper.read(SiteGroup.class, "select sg.* from site_group sg, site where site.siteGroupFk = sg.pk and site.pk = ? ", siteOID.getPk());
	}
	

	@PutMapping("/setLinkedSupplier")
	public  void setLinkedSupplier(UserContext userContext,
			@RequestBody SiteOID siteOID,@RequestBody SupplierOID supplierOID) throws Exception
	{
		siteService.setLinkedSupplier(userContext,
        			siteOID, supplierOID);
	}

	@PostMapping("/saveSiteGroup")
	public  SiteGroup saveSiteGroup(UserContext context, @RequestBody SiteGroup siteGroup)throws Exception
	{
		siteGroup =siteService.saveSiteGroup(context, siteGroup);
		return siteGroup;
	}

	@GetMapping("/getSiteGroupList")
	public  List<SiteGroup> getSiteGroupList()
	{
		return siteService.getSiteGroupList();
	}
}
