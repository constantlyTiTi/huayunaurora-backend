package org.huayunaurora.loginAndRegistration.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.huayunaurora.loginAndRegistration.dtos.AccountDTO;
import org.huayunaurora.loginAndRegistration.models.Account;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class Oauth2OidcAccountService extends OidcUserService {
    private final AccountService accountService;

    @Override
    @SneakyThrows
    public OidcUser loadUser(OidcUserRequest userRequest) {
        System.out.println("load user");
        log.trace("Load user {}", userRequest);
        OidcUser oidcUser = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oidcUser);
    }

    private OidcUser processOAuth2User(OidcUserRequest oAuth2UserRequest, OidcUser oAuth2User) {
        AccountDTO userInfoDto = AccountDTO
                .builder()
                .email(oAuth2User.getAttributes().get("email").toString())
                .build();

        log.trace("User info is {}", userInfoDto);
        Optional<Account> userOptional = accountService.queryAccountByEmail(userInfoDto.getEmail());
        log.trace("User is {}", userOptional);
        userOptional
                .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
        return oAuth2User;
    }

    private Account registerNewUser(OidcUserRequest oAuth2UserRequest, AccountDTO userInfoDto) {

        Account account = Account.builder().email(userInfoDto.getEmail())
                .provider(oAuth2UserRequest.getClientRegistration().getClientId()).build();

        return accountService.createAccount(account.getUserName(), account.getPassword(), account.getEmail(), account.getProvider());
    }

}
