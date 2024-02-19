package com.tathvatech.user.request;

import lombok.Data;

@Data
public class ChangePassPinRequest {
    private String currentPassPin;
    private String newPassPin;
}
