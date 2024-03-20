package com.tathvatech.project.Request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.entity.Site;
import lombok.Data;

import java.util.Collection;

@Data
public class SaveSitesForProjectRequest {
    private ProjectOID projectOID;
    private Collection<Site> siteList;
}
