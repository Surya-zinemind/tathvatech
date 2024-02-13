package com.tathvatech.user.response;

import com.tathvatech.user.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {
    private String token;
    private User user;
}
