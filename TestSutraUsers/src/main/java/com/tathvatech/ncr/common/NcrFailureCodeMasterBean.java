package com.tathvatech.ncr.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.user.OID.TSBeanBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NcrFailureCodeMasterBean extends TSBeanBase implements Serializable {
    private int pk;
    private String name;
    private String description;
    private int estatus;
    private Integer parentFk;
    private Date createdDate;
    private int createdBy;
    private Date lastUpdated;
    List<NcrFailureCodeMasterBean> children = new ArrayList<NcrFailureCodeMasterBean>();

    public List<NcrFailureCodeMasterBean> getChildren() {
        return children;
    }

    @Override
    public long getPk() {
        return 0;
    }

    @Override
    public String getDisplayText() {
        return null;
    }
}