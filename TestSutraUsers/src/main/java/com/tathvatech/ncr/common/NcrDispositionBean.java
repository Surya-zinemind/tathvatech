package com.tathvatech.ncr.common;

import com.tathvatech.ncr.entity.NcrDispositionTypes;
import com.tathvatech.user.OID.TSBeanBase;

import java.io.Serializable;

public class NcrDispositionBean extends TSBeanBase implements Serializable {
    private int pk;
    private String name;
    private String description;

    public NcrDispositionBean()
    {

    }

    public NcrDispositionBean(int pk, String name, String description)
    {
        super();
        this.pk = pk;
        this.name = name;
        this.description = description;
    }

    public long getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDisplayText()
    {
        return name;
    }

    public static NcrDispositionBean getBean(NcrDispositionTypes aDisp)
    {
        return new NcrDispositionBean((int) aDisp.getPk(), aDisp.getName(), aDisp.getDescription());
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj != null && pk == ((NcrDispositionBean)obj).getPk())
            return true;
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return pk;
    }


}
