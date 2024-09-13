package org.huayunaurora.loginAndRegistration.mappers;

import org.huayunaurora.loginAndRegistration.dtos.AccountDTO;
import org.huayunaurora.loginAndRegistration.models.Account;
import org.huayunaurora.loginAndRegistration.models.AccountStatus;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

//@Mapper(uses = AccountStatusMapper.class)
@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account dtoToDao(AccountDTO accountDTO);
    AccountDTO daoToDto(Account account);

    default Stack<AccountStatus> mapListToStack(List<AccountStatus> list) {
        Stack<AccountStatus> stack = new Stack<>();
        stack.addAll(list);
        return stack;
    }

    default List<AccountStatus> mapStackToList(Stack<AccountStatus> stack) {
        return new ArrayList<>(stack);
    }

    default Stack<AccountStatus> mapStackToStack(Stack<AccountStatus> stack) {
        Stack<AccountStatus> newStack = new Stack<>();
        newStack.addAll(stack);
        return newStack;
    }
}
