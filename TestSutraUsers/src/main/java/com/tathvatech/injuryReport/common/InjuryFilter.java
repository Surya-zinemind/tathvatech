package com.tathvatech.injuryReport.common;

import java.util.Date;
import java.util.List;

import com.sarvasutra.etest.components.bocomponents.DateRangeFilter;

public class InjuryFilter implements Cloneable
{
    public static enum FilterModeEnum {
        SelectAllOnNoSelect, SelectNoneOnNoSelect
    };

    private FilterModeEnum filterMode = FilterModeEnum.SelectAllOnNoSelect;

    private int pk;
    private List<Integer> projectPks;
    private List<Object> locations;
    private int workstationPk;
    private int[] sitePks;
    //	private Date fromCreatedDate;
//	private Date toCreatedDate;
    private DateRangeFilter createdDate;
    private DateRangeFilter verifiedDate;
    private DateRangeFilter closedDate;
    private String[] status;
    private String[] natureOfInjury;
    private GraphType graphType = GraphType.lcation;
    private List<Integer> afterTreatmentList;
    private Integer typeOfInjury;
    private Integer typeOfPerson;

    public Integer getTypeOfInjury()
    {
        return typeOfInjury;
    }

    public void setTypeOfInjury(Integer typeOfInjury)
    {
        this.typeOfInjury = typeOfInjury;
    }

    public Integer getTypeOfPerson()
    {
        return typeOfPerson;
    }

    public void setTypeOfPerson(Integer typeOfPerson)
    {
        this.typeOfPerson = typeOfPerson;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public enum GraphType {
        human, lcation
    }

    public InjuryFilter()
    {

    }

    public InjuryFilter(FilterModeEnum filterMode)
    {
        this.filterMode = filterMode;
    }

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }


    public List<Integer> getProjectPks()
    {
        return projectPks;
    }

    public void setProjectPks(List<Integer> projectPks)
    {
        this.projectPks = projectPks;
    }

    public int getWorkstationPk()
    {
        return workstationPk;
    }

    public void setWorkstationPk(int workstationPk)
    {
        this.workstationPk = workstationPk;
    }

    public int[] getSitePks()
    {
        return sitePks;
    }

    public void setSitePks(int[] sitePks)
    {
        this.sitePks = sitePks;
    }

//	public Date getFromCreatedDate()
//	{
//		return fromCreatedDate;
//	}
//
//	public void setFromCreatedDate(Date fromCreatedDate)
//	{
//		this.fromCreatedDate = fromCreatedDate;
//	}
//
//	public Date getToCreatedDate()
//	{
//		return toCreatedDate;
//	}
//
//	public void setToCreatedDate(Date toCreatedDate)
//	{
//		this.toCreatedDate = toCreatedDate;
//	}

    public String[] getStatus()
    {
        return status;
    }

    public DateRangeFilter getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(DateRangeFilter createdDate)
    {
        this.createdDate = createdDate;
    }

    public void setStatus(String[] status)
    {
        this.status = status;
    }

    public FilterModeEnum getFilterMode()
    {
        return filterMode;
    }

    public void setFilterMode(FilterModeEnum filterMode)
    {
        this.filterMode = filterMode;
    }

    public List<Object> getLocations()
    {
        return locations;
    }

    public void setLocations(List<Object> locations)
    {
        this.locations = locations;
    }

    public String[] getNatureOfInjury()
    {
        return natureOfInjury;
    }

    public void setNatureOfInjury(String[] natureOfInjury)
    {
        this.natureOfInjury = natureOfInjury;
    }

    public GraphType getGraphType()
    {
        return graphType;
    }

    public void setGraphType(GraphType graphType)
    {
        this.graphType = graphType;
    }

    public List<Integer> getAfterTreatmentList()
    {
        return afterTreatmentList;
    }

    public void setAfterTreatmentList(List<Integer> afterTreatmentList)
    {
        this.afterTreatmentList = afterTreatmentList;
    }

    public DateRangeFilter getVerifiedDate()
    {
        return verifiedDate;
    }

    public void setVerifiedDate(DateRangeFilter verifiedDate)
    {
        this.verifiedDate = verifiedDate;
    }

    public DateRangeFilter getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate(DateRangeFilter closedDate)
    {
        this.closedDate = closedDate;
    }


}

