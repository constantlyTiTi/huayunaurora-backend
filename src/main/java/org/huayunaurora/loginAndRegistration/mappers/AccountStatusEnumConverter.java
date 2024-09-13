package org.huayunaurora.loginAndRegistration.mappers;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.huayunaurora.loginAndRegistration.enums.AccountStatusEnum;

public class AccountStatusEnumConverter implements DynamoDBTypeConverter<String, AccountStatusEnum> {
    @Override
    public String convert(AccountStatusEnum object) {
        return object.name();
    }

    @Override
    public AccountStatusEnum unconvert(String object) {
        return AccountStatusEnum.valueOf(object);
    }
}
