package com.tathvatech.user.controller;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.user.request.AuthenticationRequest;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.response.AuthenticationResponse;
import com.tathvatech.user.security.config.JwtService;
import com.tathvatech.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AccountService accountService;

    /**
     * this is the general login for users except the operator users.
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest)throws Exception
    {
        User user = accountService.login(authenticationRequest.getAccountNo(), authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if(user != null && User.USER_OPERATOR.equals(user.getUserType()))
        {
            throw new AppException("User is not authorized to login.");
        }
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse loginResponse = new AuthenticationResponse().setUser(user).setToken(jwtToken);
        return ResponseEntity.ok(loginResponse);
    }


}
