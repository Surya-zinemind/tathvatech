package com.tathvatech.workstation.request;

import lombok.Data;

@Data
public class WorkstationsForSiteAndProjectRequest {
  private  int sitePk;
  private  int projectPk;
}
