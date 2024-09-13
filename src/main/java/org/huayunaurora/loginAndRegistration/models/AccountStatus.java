package org.huayunaurora.loginAndRegistration.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.*;
import org.huayunaurora.loginAndRegistration.enums.AccountStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;


@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class AccountStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
//    @DynamoDBTypeConverted(converter = AccountStatusEnumConverter.class)
    @DynamoDBTypeConvertedEnum
    private AccountStatusEnum status;

    private OffsetDateTime localDateTime;
}
