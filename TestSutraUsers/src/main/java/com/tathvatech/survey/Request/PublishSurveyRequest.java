package com.tathvatech.survey.Request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.entity.User;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class PublishSurveyRequest {
    private int surveyPk;
    private List<ProjectOID> projectUpgradeList;
    private HashMap<ProjectOID, User> projectNotificationMap;
  private   HashMap<ProjectOID, List<Integer>> formsUpgradeMap;
}
