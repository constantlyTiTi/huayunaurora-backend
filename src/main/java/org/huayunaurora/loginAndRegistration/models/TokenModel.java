package org.huayunaurora.loginAndRegistration.models;

import lombok.Data;

@Data
public class TokenModel {
    private String accessToken;
    private String refreshToken;
}
