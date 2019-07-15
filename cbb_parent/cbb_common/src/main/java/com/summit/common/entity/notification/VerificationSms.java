package com.summit.common.entity.notification;

import lombok.Data;

@Data
public class VerificationSms {
    private String phoneNumber;
    private String signName;
    private String templateCode;

}
