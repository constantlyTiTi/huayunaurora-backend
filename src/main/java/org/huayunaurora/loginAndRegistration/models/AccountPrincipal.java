package org.huayunaurora.loginAndRegistration.models;

import lombok.RequiredArgsConstructor;
import org.huayunaurora.loginAndRegistration.enums.AccountStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountPrincipal implements UserDetails {
    private final Account account;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return account.getRoles()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getAccountStatuses().peek().getStatus() == AccountStatusEnum.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
