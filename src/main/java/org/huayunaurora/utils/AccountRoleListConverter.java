package org.huayunaurora.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.huayunaurora.loginAndRegistration.enums.AccountRoleEnum;

import java.io.IOException;
import java.util.List;

public class AccountRoleListConverter implements DynamoDBTypeConverter<String, List<AccountRoleEnum>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<AccountRoleEnum> roles) {
        try {
            return roles == null ? null : objectMapper.writeValueAsString(roles);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert list of roles to JSON", e);
        }
    }

    @Override
    public List<AccountRoleEnum> unconvert(String json) {
        try {
            return json == null ? null : objectMapper.readValue(json, new TypeReference<List<AccountRoleEnum>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON to list of roles", e);
        }
    }
}
