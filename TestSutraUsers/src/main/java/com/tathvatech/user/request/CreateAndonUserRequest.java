package com.tathvatech.user.request;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class CreateAndonUserRequest {

   private User user;
   private AttachmentIntf profilePicAttachment;
   private boolean sendWelcomeKit;
}
