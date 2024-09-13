package org.huayunaurora.loginAndRegistration.graphqlResolvers;

import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import org.huayunaurora.loginAndRegistration.dtos.AccountDTO;
import org.huayunaurora.loginAndRegistration.mappers.AccountMapper;
import org.huayunaurora.loginAndRegistration.models.Account;
import org.huayunaurora.loginAndRegistration.services.AccountService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AccountResolvers {
    private final AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountMapper accountMapper;


    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AccountDTO queryAccountByUserName(@Argument String userName) {

        Optional<Account> account = accountService.queryAccountByUserName(userName);

        return account.map(accountMapper::daoToDto).orElseThrow(() -> new GraphQLException(String.format("%s cannot be found", userName)));
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AccountDTO queryAccountByEmail(@Argument String email) {

        Optional<Account> account = accountService.queryAccountByEmail(email);

        return account.map(accountMapper::daoToDto).orElseThrow(() -> new GraphQLException(String.format("%s cannot be found", email)));
    }

    @MutationMapping
    @Transactional
    public AccountDTO createAccount(@Argument("user_name") String userName,@Argument String email,@Argument String password, @Argument String provider) {

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        Account account = accountService.createAccount(userName, encodedPassword, email, provider);
        return accountMapper.daoToDto(account);


    }

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public String updateAccountUserInfo(@Argument("user_name") String userName, @Argument String email, @Argument String password) {
        accountService.updateAccountUserInfo(userName, bCryptPasswordEncoder.encode(password), email);
        return "update successfully";
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAccountAuthorization(@Argument AccountDTO accountDTO) {
        Account account = accountMapper.dtoToDao(accountDTO);
        accountService.updateAccountFullInfo(account);
        return "update successfully";
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(@Argument String id) {
        accountService.deleteAccount(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccounts(@Argument List<String> ids) {
        accountService.deleteAccounts(ids);
    }

}
