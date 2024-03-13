package com.tathvatech.site.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.tathvatech.site.common.SiteFilter;
import com.tathvatech.user.entity.Site;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.common.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.SupplierOID;
import com.tathvatech.site.common.SiteQuery;
import com.tathvatech.site.entity.SiteGroup;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SiteServiceImpl implements SiteService {
	private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);
	private final PersistWrapper persistWrapper;

    public SiteServiceImpl(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

	@Transactional
	@Override
    public  void createSite(UserContext context, Site site) throws Exception
	{
		site.setCreatedBy(context.getUser().getPk());
		site.setCreatedDate(new Date());
		site.setEstatus(EStatusEnum.Active.getValue());
		persistWrapper.createEntity(site);
	}

	@Transactional
	@Override
    public  void updateSite(UserContext context, Site site) throws Exception
	{
		persistWrapper.update(site);
	}

	@Transactional
	@Override
    public  void deleteSite(UserContext context, int sitePk) throws Exception
	{
		Site site = (Site) persistWrapper.readByPrimaryKey(Site.class, sitePk);
		site.setEstatus(EStatusEnum.Deleted.getValue());
		persistWrapper.update(site);
	}

	@Override
    public  Site getSite(int sitePk)
	{
		try
		{
			return (Site) persistWrapper.readByPrimaryKey(Site.class, sitePk);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
    public  Site getSiteByName(String siteName)
	{
		return persistWrapper.read(Site.class, "select * from site where name = ? and estatus != 9 ", siteName);
	}

	@Override
    public  List<Site> getSites(SiteFilter siteFilter)
	{
		StringBuffer sql = new StringBuffer("select * from site where estatus = 1 ");

		List params = new ArrayList();
		if (siteFilter.getSearchString() != null && siteFilter.getSearchString().trim().length() > 0)
		{
			sql.append(" and ( site.name like ? or site.description like ?) ");
			params.add("%" + siteFilter.getSearchString() + "%");
			params.add("%" + siteFilter.getSearchString() + "%");
		}
		if (siteFilter.getSitePks() != null && siteFilter.getSitePks().length > 0)
		{
			sql.append(" and site.pk in (");
			String sep = "";
			for (int i = 0; i < siteFilter.getSitePks().length; i++)
			{
				sql.append(sep);
				sql.append(siteFilter.getSitePks()[i]);
				sep = ",";
			}
			sql.append(")");
		}

		if (siteFilter.getSiteName() != null)
		{
			sql.append(" and site.name = ?");
			params.add(siteFilter.getSiteName());
		}
		if (siteFilter.getProjectOID() != null)
		{
			sql.append(" and site.pk in (select siteFk from project_site_config where projectFk = ?)");
			params.add(siteFilter.getProjectOID().getPk());
		}
		try
		{
			sql.append(" order by site.name ");
			return persistWrapper.readList(Site.class, sql.toString(), params.toArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
    public  List<SiteQuery> getSiteList(SiteFilter siteFilter)
	{
		StringBuffer sql = new StringBuffer("select "
				+ " site.pk as pk, "
				+ " site.name as name, "
				+ " site.description as description, "
				+ " site.address as address, "
				+ " site.timeZone as timeZone, "
				+ " site.defaultSupplierFk as defaultSupplierFk, "
				+ " site.createdBy as createdBy, "
				+ " site.createdDate as createdDate, "
				+ " site.estatus as estatus, "
				+ " site.siteGroupFk as siteGroupPk, "
				+ " sg.name as siteGroupName, "
				+ " sg.description as siteGroupDescription, "
				+ " siteLicenseAllocT.regLicenseCount as purchasedLicenseCount, "
				+ " siteLicenseAllocT.readOnlyLicensecount as purchasedReadOnlyCount, "
				+ " lstats.lcount as activeLicenseCount, rstats.lcount as activeReadOnlyCount "
				+ " from site "
				+ " left outer join site_group sg on site.siteGroupFk = sg.pk "
				+ " left outer join (select sitePk, count(*) as lcount from TAB_USER u where status = 'Active' and userType in ('Engineer', 'Manager', 'Technician') group by sitePk) lstats on lstats.sitePk = site.pk "
				+ " left outer join (select sitePk, count(*) as lcount from TAB_USER u where status = 'Active' and userType in ('Readonly User') group by sitePk) rstats on rstats.sitePk = site.pk "
				+ " left outer join (select siteFk, sum(regularLicenseCount) as regLicenseCount, sum(readOnlyLicenseCount) as readOnlyLicensecount from site_license_alloc sla where estatus = 1 group by siteFk) siteLicenseAllocT on siteLicenseAllocT.siteFk = site.pk "
				+ " where site.estatus = 1 ");

		List params = new ArrayList();
		if (siteFilter.getSearchString() != null && siteFilter.getSearchString().trim().length() > 0)
		{
			sql.append(" and ( site.name like ? or site.description like ?) ");
			params.add("%" + siteFilter.getSearchString() + "%");
			params.add("%" + siteFilter.getSearchString() + "%");
		}
		if (siteFilter.getSitePks() != null && siteFilter.getSitePks().length > 0)
		{
			sql.append(" and site.pk in (");
			String sep = "";
			for (int i = 0; i < siteFilter.getSitePks().length; i++)
			{
				sql.append(sep);
				sql.append(siteFilter.getSitePks()[i]);
				sep = ",";
			}
			sql.append(")");
		}

		if (siteFilter.getSiteName() != null)
		{
			sql.append(" and site.name = ?");
			params.add(siteFilter.getSiteName());
		}
		if (siteFilter.getProjectOID() != null)
		{
			sql.append(" and site.pk in (select siteFk from project_site_config where projectFk = ?)");
			params.add(siteFilter.getProjectOID().getPk());
		}
		try
		{
			sql.append(" order by site.name ");
			return persistWrapper.readList(SiteQuery.class, sql.toString(), params.toArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Transactional
	@Override
    public  void setLinkedSupplier(UserContext userContext, SiteOID siteOID,
                                   SupplierOID supplierOID) throws Exception {
		Site site = getSite((int) siteOID.getPk());
		site.setDefaultSupplierFk((int) supplierOID.getPk());
		persistWrapper.update(site);
	}

	@Transactional
	@Override
    public  SiteGroup saveSiteGroup(UserContext context, SiteGroup siteGroup)throws Exception
	{
		if(siteGroup.getPk() == 0)
		{
			siteGroup.setCreatedBy((int) context.getUser().getPk());
			siteGroup.setCreatedDate(new Date());
			int pk = Math.toIntExact(persistWrapper.createEntity(siteGroup));
			return (SiteGroup) persistWrapper.readByPrimaryKey(SiteGroup.class, pk);
		}
		else
		{
			persistWrapper.update(siteGroup);
			return (SiteGroup) persistWrapper.readByPrimaryKey(SiteGroup.class, siteGroup.getPk());
		}
	}

	@Override
    public  List<SiteGroup> getSiteGroupList()
	{
		return persistWrapper.readList(SiteGroup.class, "select * from site_group");
	}
}