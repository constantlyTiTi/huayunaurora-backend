package org.huayunaurora.loginAndRegistration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusDTO {
    @JsonProperty("status")
    private String status;

    @JsonProperty("local_datetime")
    private OffsetDateTime localDateTime;
}
