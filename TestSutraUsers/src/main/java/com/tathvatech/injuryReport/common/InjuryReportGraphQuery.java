package com.tathvatech.injuryReport.common;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.workstation.common.WorkstationQuery;
import com.tathvatech.workstation.common.DummyWorkstation;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
public class InjuryReportGraphQuery implements Serializable
{
    private final DummyWorkstation dummyWorkstation;
    private int pk;
    private Integer locationPk;
    private String locationType;
    private String locationName;
    private String locationDescription;
    private int orderNo;

    // body part
    private Integer fatality;
    private Integer headfront;
    private Integer headback;
    private Integer face;
    private Integer lefteye;
    private Integer righteye;
    private Integer leftear;
    private Integer rightear;
    private Integer neckfront;
    private Integer neckback;
    private Integer upperbodyfront;
    private Integer upperbodyback;
    private Integer lowerbodyfront;
    private Integer lowerbodyback;
    private Integer shoulderright;
    private Integer shoulderleft;
    private Integer upperarmright;
    private Integer upperarmleft;
    private Integer lowerarmright;
    private Integer lowerarmleft;
    private Integer elbowright;
    private Integer elbowleft;
    private Integer wristleft;
    private Integer wristright;
    private Integer fingersleft;
    private Integer fingersright;
    private Integer upperlegright;
    private Integer upperlegleft;
    private Integer kneeright;
    private Integer kneeleft;
    private Integer lowerlegright;
    private Integer lowerlegleft;
    private Integer ankleleft;
    private Integer ankleright;
    private Integer footright;
    private Integer footleft;
    private Integer other;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public Integer getLocationPk()
    {
        return locationPk;
    }

    public void setLocationPk(Integer locationPk)
    {
        this.locationPk = locationPk;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public Integer getFatality()
    {
        return fatality;
    }

    public void setFatality(Integer fatality)
    {
        this.fatality = fatality;
    }

    public Integer getHeadfront()
    {
        return headfront;
    }

    public void setHeadfront(Integer headfront)
    {
        this.headfront = headfront;
    }

    public Integer getHeadback()
    {
        return headback;
    }

    public void setHeadback(Integer headback)
    {
        this.headback = headback;
    }

    public Integer getFace()
    {
        return face;
    }

    public void setFace(Integer face)
    {
        this.face = face;
    }

    public Integer getLefteye()
    {
        return lefteye;
    }

    public void setLefteye(Integer lefteye)
    {
        this.lefteye = lefteye;
    }

    public Integer getRighteye()
    {
        return righteye;
    }

    public void setRighteye(Integer righteye)
    {
        this.righteye = righteye;
    }

    public Integer getLeftear()
    {
        return leftear;
    }

    public void setLeftear(Integer leftear)
    {
        this.leftear = leftear;
    }

    public Integer getRightear()
    {
        return rightear;
    }

    public void setRightear(Integer rightear)
    {
        this.rightear = rightear;
    }

    public Integer getNeckfront()
    {
        return neckfront;
    }

    public void setNeckfront(Integer neckfront)
    {
        this.neckfront = neckfront;
    }

    public Integer getNeckback()
    {
        return neckback;
    }

    public void setNeckback(Integer neckback)
    {
        this.neckback = neckback;
    }

    public Integer getUpperbodyfront()
    {
        return upperbodyfront;
    }

    public void setUpperbodyfront(Integer upperbodyfront)
    {
        this.upperbodyfront = upperbodyfront;
    }

    public Integer getUpperbodyback()
    {
        return upperbodyback;
    }

    public void setUpperbodyback(Integer upperbodyback)
    {
        this.upperbodyback = upperbodyback;
    }

    public Integer getLowerbodyfront()
    {
        return lowerbodyfront;
    }

    public void setLowerbodyfront(Integer lowerbodyfront)
    {
        this.lowerbodyfront = lowerbodyfront;
    }

    public Integer getLowerbodyback()
    {
        return lowerbodyback;
    }

    public void setLowerbodyback(Integer lowerbodyback)
    {
        this.lowerbodyback = lowerbodyback;
    }

    public Integer getShoulderright()
    {
        return shoulderright;
    }

    public void setShoulderright(Integer shoulderright)
    {
        this.shoulderright = shoulderright;
    }

    public Integer getShoulderleft()
    {
        return shoulderleft;
    }

    public void setShoulderleft(Integer shoulderleft)
    {
        this.shoulderleft = shoulderleft;
    }

    public Integer getUpperarmright()
    {
        return upperarmright;
    }

    public void setUpperarmright(Integer upperarmright)
    {
        this.upperarmright = upperarmright;
    }

    public Integer getUpperarmleft()
    {
        return upperarmleft;
    }

    public void setUpperarmleft(Integer upperarmleft)
    {
        this.upperarmleft = upperarmleft;
    }

    public Integer getLowerarmright()
    {
        return lowerarmright;
    }

    public void setLowerarmright(Integer lowerarmright)
    {
        this.lowerarmright = lowerarmright;
    }

    public Integer getLowerarmleft()
    {
        return lowerarmleft;
    }

    public void setLowerarmleft(Integer lowerarmleft)
    {
        this.lowerarmleft = lowerarmleft;
    }

    public Integer getElbowright()
    {
        return elbowright;
    }

    public void setElbowright(Integer elbowright)
    {
        this.elbowright = elbowright;
    }

    public Integer getElbowleft()
    {
        return elbowleft;
    }

    public void setElbowleft(Integer elbowleft)
    {
        this.elbowleft = elbowleft;
    }

    public Integer getWristleft()
    {
        return wristleft;
    }

    public void setWristleft(Integer wristleft)
    {
        this.wristleft = wristleft;
    }

    public Integer getWristright()
    {
        return wristright;
    }

    public void setWristright(Integer wristright)
    {
        this.wristright = wristright;
    }

    public Integer getFingersleft()
    {
        return fingersleft;
    }

    public void setFingersleft(Integer fingersleft)
    {
        this.fingersleft = fingersleft;
    }

    public Integer getFingersright()
    {
        return fingersright;
    }

    public void setFingersright(Integer fingersright)
    {
        this.fingersright = fingersright;
    }

    public Integer getUpperlegright()
    {
        return upperlegright;
    }

    public void setUpperlegright(Integer upperlegright)
    {
        this.upperlegright = upperlegright;
    }

    public Integer getUpperlegleft()
    {
        return upperlegleft;
    }

    public void setUpperlegleft(Integer upperlegleft)
    {
        this.upperlegleft = upperlegleft;
    }

    public Integer getKneeright()
    {
        return kneeright;
    }

    public void setKneeright(Integer kneeright)
    {
        this.kneeright = kneeright;
    }

    public Integer getKneeleft()
    {
        return kneeleft;
    }

    public void setKneeleft(Integer kneeleft)
    {
        this.kneeleft = kneeleft;
    }

    public Integer getLowerlegright()
    {
        return lowerlegright;
    }

    public void setLowerlegright(Integer lowerlegright)
    {
        this.lowerlegright = lowerlegright;
    }

    public Integer getLowerlegleft()
    {
        return lowerlegleft;
    }

    public void setLowerlegleft(Integer lowerlegleft)
    {
        this.lowerlegleft = lowerlegleft;
    }

    public Integer getAnkleleft()
    {
        return ankleleft;
    }

    public void setAnkleleft(Integer ankleleft)
    {
        this.ankleleft = ankleleft;
    }

    public Integer getAnkleright()
    {
        return ankleright;
    }

    public void setAnkleright(Integer ankleright)
    {
        this.ankleright = ankleright;
    }

    public Integer getFootright()
    {
        return footright;
    }

    public void setFootright(Integer footright)
    {
        this.footright = footright;
    }

    public Integer getFootleft()
    {
        return footleft;
    }

    public void setFootleft(Integer footleft)
    {
        this.footleft = footleft;
    }

    public Integer getOther()
    {
        return other;
    }

    public void setOther(Integer other)
    {
        this.other = other;
    }

    public int getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(int orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getLocationDescription()
    {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription)
    {
        this.locationDescription = locationDescription;
    }

    public  QueryObject getQuery(InjuryFilter injuryFilter)
    {
        List<Object> params = new ArrayList();
        StringBuffer sql = new StringBuffer();
        String comma = "";
        sql.append("select ");
        if (injuryFilter.getGraphType().equals(InjuryFilter.GraphType.lcation))
        {
            sql.append("location.pk as pk,location.locationType as locationType,location.orderNo as orderNo, location.locationName as locationName,location.locationDescription as locationDescription ");
            comma = ",";
        }

        if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
        {
            for (String name : injuryFilter.getNatureOfInjury())
            {
                sql.append(comma).append("injuryT." + name + " as " + name);
                comma = ",";
            }
        } else
        {
            sql.append(comma).append(
                    " injuryT.fatality as fatality,injuryT.headfront as headfront,injuryT.headback as headback,injuryT.face as face,injuryT.lefteye as lefteye,injuryT.righteye as righteye,injuryT.leftear as leftear,injuryT.neckfront as neckfront,injuryT.neckback as neckback,injuryT.upperbodyfront as upperbodyfront,injuryT.upperbodyback as upperbodyback,injuryT.lowerbodyfront as lowerbodyfront,injuryT.lowerbodyback as lowerbodyback,injuryT.shoulderleft as shoulderleft,injuryT.shoulderright as shoulderright,injuryT.upperarmright as upperarmright,injuryT.lowerarmright as lowerarmright,injuryT.lowerarmleft as lowerarmleft,injuryT.elbowright as elbowright,injuryT.wristleft as wristleft,injuryT.wristright as wristright,injuryT.fingersleft as fingersleft,injuryT.fingersright as fingersright,injuryT.upperlegright as upperlegright,injuryT.upperlegleft as upperlegleft,injuryT.kneeright as kneeright,injuryT.kneeleft as kneeleft,injuryT.lowerlegright as lowerlegright,injuryT.lowerlegleft as lowerlegleft,injuryT.ankleleft as ankleleft,injuryT.ankleright as ankleright,injuryT.footright as footright,injuryT.footleft as footleft,injuryT.elbowleft as elbowleft,injuryT.upperarmleft as upperarmleft,injuryT.rightear as rightear,injuryT.other as other ");
            comma = ",";
        }

        sql.append(" from ");

        sql.append(" ( ");
        sql.append(" select injury.pk as injuryPk, ");
        comma = "";
        if (injuryFilter.getGraphType().equals(InjuryFilter.GraphType.lcation))
        {
            sql.append(
                    " injury.locationPk as pk,injury.locationType as locationType");
            comma = ",";
        }

        if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
        {
            for (String name : injuryFilter.getNatureOfInjury())
            {
                sql.append(comma).append(" sum( injury." + name + ") as " + name);
                comma = ",";
            }
        } else
        {
            sql.append(comma).append(
                    " sum(injury.fatality) as fatality,sum( injury.headfront) as headfront,sum(injury.headback) as headback,sum( injury.face) as face,sum(injury.lefteye) as lefteye,sum(injury.righteye) as righteye,sum(injury.leftear) as leftear,sum(injury.neckfront) as neckfront,sum(injury.neckback) as neckback,sum(injury.upperbodyfront) as upperbodyfront,sum( injury.upperbodyback) as upperbodyback,sum( injury.lowerbodyfront) as lowerbodyfront, sum(injury.lowerbodyback) as lowerbodyback,sum(injury.shoulderleft) as shoulderleft,sum( injury.shoulderright) as shoulderright,sum(injury.upperarmright) as upperarmright,sum(injury.lowerarmright) as lowerarmright,sum( injury.lowerarmleft) as lowerarmleft,sum( injury.elbowright) as elbowright,sum( injury.wristleft) as wristleft,sum( injury.wristright) as wristright,sum( injury.fingersleft) as fingersleft,sum( injury.fingersright) as fingersright,sum( injury.upperlegright) as upperlegright,sum( injury.upperlegleft) as upperlegleft,sum( injury.kneeright) as kneeright,sum( injury.kneeleft) as kneeleft,sum( injury.lowerlegright) as lowerlegright,sum(injury.lowerlegleft) as lowerlegleft,sum(injury.ankleleft) as ankleleft,sum(injury.ankleright) as ankleright,sum( injury.footright) as footright,sum( injury.footleft) as footleft,sum( injury.elbowleft) as elbowleft,sum(injury.upperarmleft) as upperarmleft, sum(injury.rightear) as rightear,sum(injury.other) as other ");
            comma = ",";
        }
        sql.append(" from injury ");
        sql.append(" where 1=1 ");
        if(injuryFilter.getAfterTreatmentList() != null && injuryFilter.getAfterTreatmentList().size() > 0){
            sql.append("  and injury.pk in (select injury_assign_after_treatment.injuryPk as pk from  injury_assign_after_treatment inner join injury_after_treatment_master on injury_after_treatment_master.pk=injury_assign_after_treatment.afterTreatmentMasterPk  " );

            sql.append(" where  injury_after_treatment_master.pk in (");
            String sep = "";
            for (Integer afterTreatmentPk : injuryFilter.getAfterTreatmentList())
            {
                sql.append(sep);
                sql.append("'").append(afterTreatmentPk).append("'");
                sep = ",";
            }
            sql.append("))");

        }
        if (injuryFilter.getCreatedDate() != null)
        {
            Date[] dateRange = injuryFilter.getCreatedDate()
                    .getResolvedDateRangeValues();
            if (dateRange != null)
            {
                Date createdDateFrom = dateRange[0];
                Date createdDateTo = dateRange[1];
                if (createdDateFrom != null)
                {
                    sql.append(" and injury.createdDate > ? ");
                    params.add(createdDateFrom);
                }
                if (createdDateTo != null)
                {
                    sql.append(" and injury.createdDate  < ?");
                    params.add(createdDateTo);
                }

            }

        }

//		if (injuryFilter.getFromCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate > ?");
//			Date dateFrom = DateUtils.truncate(injuryFilter.getFromCreatedDate(), Calendar.DATE);
//			params.add(dateFrom);
//			//params.add(DateUtils.ceiling(injuryFilter.getFromCreatedDate(), Calendar.DATE));
//		}
//		if (injuryFilter.getToCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate < ?");
//			Date d = DateUtils.truncate(injuryFilter.getToCreatedDate(), Calendar.DATE);
//			d = DateUtils.addDays(d, 1);
//			params.add(d);
//		}
        if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
        {
            sql.append(" and ( ");
            String orflag = "";
            for (String name : injuryFilter.getNatureOfInjury())
            {
                sql.append(orflag).append(" injury." + name + " is true");
                orflag = " or ";
            }
            sql.append(" ) ");
        }
        if (injuryFilter.getStatus() != null && injuryFilter.getStatus().length > 0)
        {
            sql.append(" and injury.status in ( ");
            String commaFlag = "";
            for (String status : injuryFilter.getStatus())
            {
                sql.append(commaFlag).append("'" + status + "'");
                commaFlag = ",";
            }
            sql.append(")");
        }
        if (injuryFilter.getTypeOfInjury() != null)
        {
            sql.append(" and injury.typeOfInjury=? ");
            params.add(injuryFilter.getTypeOfInjury());
        }
        if (injuryFilter.getSitePks() != null && injuryFilter.getSitePks().length > 0 )
        {
            sql.append(" and injury.sitePk in ( ");
            String commaFlag = "";
            for (int sitePk : injuryFilter.getSitePks())
            {
                sql.append(commaFlag).append("'" + sitePk + "'");
                commaFlag = ",";
            }
            sql.append(")");
        }
        if (injuryFilter.getProjectPks() != null && injuryFilter.getProjectPks().size() > 0)
        {
            sql.append(" and injury.projectPk in ");
            String filterProjectPks = Arrays.deepToString(injuryFilter.getProjectPks().toArray());
            filterProjectPks = filterProjectPks.replace('[', '(');
            filterProjectPks = filterProjectPks.replace(']', ')');
            sql.append(filterProjectPks);
        }

        if (injuryFilter.getTypeOfPerson() != null)
        {
            sql.append(" and injury.typeOfPerson=? ");
            params.add(injuryFilter.getTypeOfPerson());
        }
        if (injuryFilter.getLocations() != null)
        {
            String commaFlag = "";
            StringBuffer workstationBuffer = new StringBuffer();
            for (Object location : injuryFilter.getLocations())
            {
                if (location instanceof WorkstationQuery)
                {
                    workstationBuffer.append(commaFlag).append(((WorkstationQuery) location).getPk());
                    commaFlag = ",";
                }
            }
            commaFlag = "";
            StringBuffer locationBuffer = new StringBuffer();
            for (Object location : injuryFilter.getLocations())
            {
                if (location instanceof InjuryLocationMasterQuery)
                {
                    locationBuffer.append(commaFlag).append(((InjuryLocationMasterQuery) location).getPk());
                    commaFlag = ",";
                }
            }
            sql.append(" and ((injury.locationType = 'Workstation' and injury.locationPk in (");
            if (workstationBuffer.length() > 0)
            {
                sql.append(workstationBuffer);
            } else
            {
                sql.append(0);
            }
            sql.append("))  ");
            sql.append(" or (injury.locationType = 'Location' and injury.locationPk in (");
            if (locationBuffer.length() > 0)
            {
                sql.append(locationBuffer);
            } else
            {
                sql.append(0);
            }
            sql.append(") ))");
        }

        sql.append(" group by injury.locationType,injury.locationPk");
        sql.append(") injuryT ");



        if (injuryFilter.getGraphType().equals(InjuryFilter.GraphType.lcation))
        {
            sql.append(" right outer join (");
            sql.append(
                    " select TAB_WORKSTATION.pk as pk,'Workstation' as locationType,TAB_WORKSTATION.orderNo as orderNo, TAB_WORKSTATION.workstationName as locationName,CONCAT(COALESCE(TAB_WORKSTATION.workstationName,''),'-',COALESCE(TAB_WORKSTATION.description,'')) as locationDescription from TAB_WORKSTATION ");
            sql.append(" inner join tab_project_workstations tpw on tpw.workstationPk=TAB_WORKSTATION.pk ");

            sql.append(" where TAB_WORKSTATION.pk !=? ");
            params.add(dummyWorkstation.getPk());
            if (injuryFilter.getSitePks() != null && injuryFilter.getSitePks().length > 0 )
            {
                sql.append(" and TAB_WORKSTATION.sitePk in ( ");
                String commaFlag = "";
                for (int sitePk : injuryFilter.getSitePks())
                {
                    sql.append(commaFlag).append("'" + sitePk + "'");
                    commaFlag = ",";
                }
                sql.append(")");
            }
            if (injuryFilter.getProjectPks() != null && injuryFilter.getProjectPks().size() > 0)
            {
                sql.append(" and tpw.projectPk in ");
                String filterProjectPks = Arrays.deepToString(injuryFilter.getProjectPks().toArray());
                filterProjectPks = filterProjectPks.replace('[', '(');
                filterProjectPks = filterProjectPks.replace(']', ')');
                sql.append(filterProjectPks);
            }

            sql.append(" group by TAB_WORKSTATION.pk");

            sql.append(" union ");
            sql.append(
                    " select injury_location_master.pk as pk,'Location' as locationType,100 as orderNo, injury_location_master.name as locationName ,'' as locationDescription from injury_location_master ");
            sql.append(" )  location ");
            sql.append(" on location.pk= injuryT.pk and location.locationType=injuryT.locationType ");
            sql.append(" order by orderNo  asc");
        }


        QueryObject resultQuery = new QueryObject();
        resultQuery.setQuery(sql.toString());
        resultQuery.setParams(params);
        return resultQuery;
    }

//	public static QueryObject getQuery1(InjuryFilter injuryFilter)
//	{
//		List<Object> params = new ArrayList();
//		StringBuffer sql = new StringBuffer();
//
//		// get list of injury report based on the workstations
//		sql.append("select ");
//		String comma = "";
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(
//					" TAB_WORKSTATION.pk as pk,'Workstation' as locationType,TAB_WORKSTATION.orderNo as orderNo, TAB_WORKSTATION.workstationName as locationName,CONCAT(COALESCE(TAB_WORKSTATION.workstationName,''),'-',COALESCE(TAB_WORKSTATION.description,'')) as locationDescription");
//			comma = ",";
//		}
//		if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
//		{
//			for (String name : injuryFilter.getNatureOfInjury())
//			{
//				sql.append(comma).append(" sum( injury." + name + ") as " + name);
//				comma = ",";
//			}
//		} else
//		{
//			sql.append(comma).append(
//					" sum(injury.fatality) as fatality,sum( injury.headfront) as headfront,sum(injury.headback) as headback,sum( injury.face) as face,sum(injury.lefteye) as lefteye,sum(injury.righteye) as righteye,sum(injury.leftear) as leftear,sum(injury.neckfront) as neckfront,sum(injury.neckback) as neckback,sum(injury.upperbodyfront) as upperbodyfront,sum( injury.upperbodyback) as upperbodyback,sum( injury.lowerbodyfront) as lowerbodyfront, sum(injury.lowerbodyback) as lowerbodyback,sum(injury.shoulderleft) as shoulderleft,sum( injury.shoulderright) as shoulderright,sum(injury.upperarmright) as upperarmright,sum(injury.lowerarmright) as lowerarmright,sum( injury.lowerarmleft) as lowerarmleft,sum( injury.elbowright) as elbowright,sum( injury.wristleft) as wristleft,sum( injury.wristright) as wristright,sum( injury.fingersleft) as fingersleft,sum( injury.fingersright) as fingersright,sum( injury.upperlegright) as upperlegright,sum( injury.upperlegleft) as upperlegleft,sum( injury.kneeright) as kneeright,sum( injury.kneeleft) as kneeleft,sum( injury.lowerlegright) as lowerlegright,sum(injury.lowerlegleft) as lowerlegleft,sum(injury.ankleleft) as ankleleft,sum(injury.ankleright) as ankleright,sum( injury.footright) as footright,sum( injury.footleft) as footleft,sum( injury.elbowleft) as elbowleft,sum(injury.upperarmleft) as upperarmleft, sum(injury.rightear) as rightear,sum(injury.other) as other ");
//			comma = ",";
//		}
//
//		sql.append(" from injury ");
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(
//					" right outer join TAB_WORKSTATION on TAB_WORKSTATION.pk=injury.locationPk and injury.locationType='Workstation'");
//		} else
//		{
//			sql.append("where 1=1 ");
//		}
//
//		if (injuryFilter.getFromCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate > ?");
//			params.add(DateUtils.ceiling(injuryFilter.getFromCreatedDate(), Calendar.DATE));
//		}
//		if (injuryFilter.getToCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate < ?");
//			params.add(DateUtils.ceiling(injuryFilter.getToCreatedDate(), Calendar.DATE));
//		}
//		if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
//		{
//			sql.append(" and ( ");
//			String orflag = "";
//			for (String name : injuryFilter.getNatureOfInjury())
//			{
//				sql.append(orflag).append(" injury." + name + " is true");
//				orflag = " or ";
//			}
//			sql.append(" ) ");
//		}
//		if (injuryFilter.getStatus() != null && injuryFilter.getStatus().length > 0)
//		{
//			sql.append(" and injury.status in ( ");
//			String commaFlag = "";
//			for (String status : injuryFilter.getStatus())
//			{
//				sql.append(commaFlag).append("'" + status + "'");
//				commaFlag = ",";
//			}
//			sql.append(")");
//		}
//		if (injuryFilter.getLocations() != null)
//		{
//			String commaFlag = "";
//			StringBuffer locationBuffer = new StringBuffer();
//			for (Object location : injuryFilter.getLocations())
//			{
//				if (location instanceof WorkstationQuery)
//				{
//					locationBuffer.append(commaFlag).append(((WorkstationQuery) location).getPk());
//					commaFlag = ",";
//				}
//			}
//			if (injuryFilter.getGraphType().equals(GraphType.lcation))
//			{
//				sql.append("and  TAB_WORKSTATION.pk in (");
//			} else if (injuryFilter.getGraphType().equals(GraphType.human))
//			{
//				sql.append(" and injury.locationType='Workstation' and injury.locationPk in (");
//			}
//			if (locationBuffer.length() > 0)
//			{
//				sql.append(locationBuffer);
//			} else
//			{
//				sql.append(0);
//
//			}
//			sql.append(")");
//		}
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(" where 1=1 ");
//			sql.append(" and TAB_WORKSTATION.pk !=?");
//			params.add(DummyWorkstation.getPk());
//			sql.append(" group by TAB_WORKSTATION.pk ");
//		} else
//		{
//			sql.append(" and injury.locationPk !=? and injury.locationType='Workstation'");
//			params.add(DummyWorkstation.getPk());
//		}
//
//		sql.append(" union ");
//		sql.append(" select ");
//		comma = "";
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(
//					" injury_location_master.pk as pk,'Location' as locationType,100 as orderNo, injury_location_master.name as locationName ,'' as locationDescription ");
//			comma = ",";
//		}
//
//		if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
//		{
//			for (String name : injuryFilter.getNatureOfInjury())
//			{
//				sql.append(comma).append(" sum( injury." + name + ") as " + name);
//				comma = ",";
//			}
//		} else
//		{
//			sql.append(comma).append(
//					"sum(injury.fatality) as fatality,sum( injury.headfront) as headfront,sum(injury.headback) as headback,sum( injury.face) as face,sum(injury.lefteye) as lefteye,sum(injury.righteye) as righteye,sum(injury.leftear) as leftear,sum(injury.neckfront) as neckfront,sum(injury.neckback) as neckback,sum(injury.upperbodyfront) as upperbodyfront,sum( injury.upperbodyback) as upperbodyback,sum( injury.lowerbodyfront) as lowerbodyfront, sum(injury.lowerbodyback) as lowerbodyback,sum(injury.shoulderleft) as shoulderleft,sum( injury.shoulderright) as shoulderright,sum(injury.upperarmright) as upperarmright,sum(injury.lowerarmright) as lowerarmright,sum( injury.lowerarmleft) as lowerarmleft,sum( injury.elbowright) as elbowright,sum( injury.wristleft) as wristleft,sum( injury.wristright) as wristright,sum( injury.fingersleft) as fingersleft,sum( injury.fingersright) as fingersright,sum( injury.upperlegright) as upperlegright,sum( injury.upperlegleft) as upperlegleft,sum( injury.kneeright) as kneeright,sum( injury.kneeleft) as kneeleft,sum( injury.lowerlegright) as lowerlegright,sum(injury.lowerlegleft) as lowerlegleft,sum(injury.ankleleft) as ankleleft,sum(injury.ankleright) as ankleright,sum( injury.footright) as footright,sum( injury.footleft) as footleft,sum( injury.elbowleft) as elbowleft,sum(injury.upperarmleft) as upperarmleft, sum(injury.rightear) as rightear,sum(injury.other) as other ");
//			comma = ",";
//		}
//		sql.append(" from injury ");
//
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(
//					" right outer join injury_location_master  on injury_location_master.pk=injury.locationPk and injury.locationType='Location'");
//
//		} else
//		{
//			sql.append("where 1=1 ");
//		}
//		if (injuryFilter.getStatus() != null && injuryFilter.getStatus().length > 0)
//		{
//			sql.append(" and injury.status in ( ");
//			String commaFlag = "";
//			for (String status : injuryFilter.getStatus())
//			{
//				sql.append(commaFlag).append("'" + status + "'");
//				commaFlag = ",";
//			}
//			sql.append(")");
//		}
//		if (injuryFilter.getFromCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate > ?");
//			params.add(DateUtils.ceiling(injuryFilter.getFromCreatedDate(), Calendar.DATE));
//		}
//		if (injuryFilter.getToCreatedDate() != null)
//		{
//			sql.append(" and injury.createdDate < ?");
//			params.add(DateUtils.ceiling(injuryFilter.getToCreatedDate(), Calendar.DATE));
//		}
//		if (injuryFilter.getNatureOfInjury() != null && injuryFilter.getNatureOfInjury().length > 0)
//		{
//			sql.append(" and ( ");
//			String orflag = "";
//			for (String name : injuryFilter.getNatureOfInjury())
//			{
//				sql.append(orflag).append(" injury." + name + " is true");
//				orflag = " or ";
//			}
//			sql.append(" ) ");
//		}
//
//		if (injuryFilter.getLocations() != null)
//		{
//			String commaFlag = "";
//			StringBuffer locationBuffer = new StringBuffer();
//			for (Object location : injuryFilter.getLocations())
//			{
//				if (location instanceof InjuryLocationMasterQuery)
//				{
//					locationBuffer.append(commaFlag).append(((InjuryLocationMasterQuery) location).getPk());
//					commaFlag = ",";
//				}
//			}
//			if (injuryFilter.getGraphType().equals(GraphType.lcation))
//			{
//				sql.append(" and injury_location_master.pk in (");
//			} else if (injuryFilter.getGraphType().equals(GraphType.human))
//			{
//				sql.append(" and injury.locationType='Location' and injury.locationPk in (");
//			}
//			if (locationBuffer.length() > 0)
//			{
//				sql.append(locationBuffer);
//			} else
//			{
//				sql.append(0);
//			}
//			sql.append(")");
//
//		}
//		if (injuryFilter.getGraphType().equals(GraphType.lcation))
//		{
//			sql.append(" where 1=1 ");
//			sql.append(" group by injury_location_master.pk");
//			sql.append(" order by orderNo  asc");
//		} else
//		{
//			sql.append(" and injury.locationType='Location' ");
//		}
//		QueryObject resultQuery = new QueryObject();
//		resultQuery.setQuery(sql.toString());
//		resultQuery.setParams(params);
//		return resultQuery;
//	}

}

