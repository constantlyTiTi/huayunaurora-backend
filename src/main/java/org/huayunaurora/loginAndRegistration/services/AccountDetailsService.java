package org.huayunaurora.loginAndRegistration.services;

import lombok.RequiredArgsConstructor;
import org.huayunaurora.loginAndRegistration.models.Account;
import org.huayunaurora.loginAndRegistration.models.AccountPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Optional<Account> account = accountService.queryAccountByEmail(emailOrUsername)
                .or(()->accountService.queryAccountByUserName(emailOrUsername));
        if(account.isEmpty()){
            throw new UsernameNotFoundException(emailOrUsername);
        }
        return new AccountPrincipal(account.get());
    }
}
