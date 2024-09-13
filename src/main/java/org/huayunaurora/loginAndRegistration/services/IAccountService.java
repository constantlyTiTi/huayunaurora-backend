package org.huayunaurora.loginAndRegistration.services;

import org.huayunaurora.loginAndRegistration.models.Account;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Account createAccount(String userName, String password, String email, String provider) throws SQLDataException;

    void updateAccountFullInfo(Account account);

    void updateAccountUserInfo(String userName, String password, String email);

    void deleteAccount(String id);

    void deleteAccounts(List<String> ids);

    Optional<Account> queryAccountByUserName(String userName);

    Optional<Account> queryAccountByEmail(String email);
}
