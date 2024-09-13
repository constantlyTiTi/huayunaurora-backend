package org.huayunaurora.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.huayunaurora.loginAndRegistration.enums.AccountRoleEnum;

import java.io.IOException;

public class AccountRoleJsonSerializer extends JsonSerializer<AccountRoleEnum> {

    @Override
    public void serialize(AccountRoleEnum role, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(role.name());
    }
}
