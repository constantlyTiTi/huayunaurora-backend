package org.huayunaurora.loginAndRegistration.mappers;

import org.huayunaurora.loginAndRegistration.dtos.AccountStatusDTO;
import org.huayunaurora.loginAndRegistration.models.AccountStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountStatusMapper {
    AccountStatus dtoToDao(AccountStatus dao);
    AccountStatusDTO daoToDto(AccountStatusDTO dto);
}
