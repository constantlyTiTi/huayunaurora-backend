package org.huayunaurora.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.huayunaurora.loginAndRegistration.models.AccountStatus;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

public class AccountStatuesListConverter implements DynamoDBTypeConverter<String, Stack<AccountStatus>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String convert(Stack<AccountStatus> statuses) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return statuses == null ? null : objectMapper.writeValueAsString(statuses);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert list of statuses to JSON", e);
        }
    }

    @Override
    public Stack<AccountStatus> unconvert(String json) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return json == null ? null : objectMapper.readValue(json, new TypeReference<Stack<AccountStatus>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON to list of statuses", e);
        }
    }
}
