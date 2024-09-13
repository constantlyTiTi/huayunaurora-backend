package org.huayunaurora.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.huayunaurora.loginAndRegistration.enums.AccountRoleEnum;

import java.io.IOException;

public class AccountRoleDeserializer extends JsonDeserializer<AccountRoleEnum> {

    @Override
    public AccountRoleEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return AccountRoleEnum.valueOf(p.getText().toUpperCase());
    }
}