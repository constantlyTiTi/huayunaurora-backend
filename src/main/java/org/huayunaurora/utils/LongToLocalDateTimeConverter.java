package org.huayunaurora.utils;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LongToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {
    public LocalDateTime convert(final Long value) {
        return Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
