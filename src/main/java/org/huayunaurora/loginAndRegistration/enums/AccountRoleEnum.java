package org.huayunaurora.loginAndRegistration.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.huayunaurora.utils.AccountRoleDeserializer;
import org.huayunaurora.utils.AccountRoleJsonSerializer;

@JsonDeserialize(using = AccountRoleDeserializer.class)
@JsonSerialize(using = AccountRoleJsonSerializer.class)
public enum AccountRoleEnum {
    ROLE_USER, ROLE_ADMIN, ROLE_POSTER
}
