package org.huayunaurora.loginAndRegistration.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.huayunaurora.loginAndRegistration.enums.AccountRoleEnum;
import org.huayunaurora.loginAndRegistration.models.AccountStatus;

import java.util.List;
import java.util.Stack;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("password")
    private String password;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("email")
    @Email(message = "Email is not valid",regexp ="^(.+)@(\\S+)$")
    private String email;

    @JsonProperty("roles")
    private List<AccountRoleEnum> roles;

//    @JsonProperty("account_privileges")
//    private List<AccountPrivilege> accountPrivileges;

    @JsonProperty("account_statuses")
    private Stack<AccountStatus> accountStatuses;

}
