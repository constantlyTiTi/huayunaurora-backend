package org.huayunaurora.loginAndRegistration.services;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.huayunaurora.loginAndRegistration.enums.AccountRoleEnum;
import org.huayunaurora.loginAndRegistration.enums.AccountStatusEnum;
import org.huayunaurora.loginAndRegistration.exceptions.UserAlreadyExistException;
import org.huayunaurora.loginAndRegistration.models.Account;
import org.huayunaurora.loginAndRegistration.models.AccountStatus;
import org.huayunaurora.loginAndRegistration.repositories.AccountRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    private final String RANDOM_USER_NAME_PREFIX = "SystemRandomGenerated_";

    @Override
    @Transactional
    public Account createAccount(String userName, String password, String email, String provider) throws UserAlreadyExistException {
        Optional<Account> findAccountUserName = Objects.nonNull(userName) && !userName.isEmpty() ? accountRepository.findByUserName(userName) : Optional.empty();
        Optional<Account> findAccountEmail = Objects.nonNull(email) && !email.isEmpty() ? accountRepository.findByEmail(email) : Optional.empty();

        if (findAccountUserName.isEmpty() && findAccountEmail.isEmpty()) {
            Stack<AccountStatus> stack = new Stack<>();
//            stack.addAll(List.of( AccountStatus.builder().status(AccountStatusEnum.ACTIVE).localDateTime(LocalDateTime.now()).build()));
            stack.addAll(List.of(AccountStatus.builder().status(AccountStatusEnum.ACTIVE).localDateTime(OffsetDateTime.now(ZoneOffset.UTC)).build()));

            Account account = Account.builder().userName(userName)
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .roles(List.of(AccountRoleEnum.ROLE_USER))
                    .accountStatuses(stack)
                    .build();

//            if(userName.isEmpty()){
//                 String randomUserName = RANDOM_USER_NAME_PREFIX + UUID.randomUUID();
//                Optional<Account> checkRandomUserName = accountRepository.findByUserName(randomUserName);
//                while(checkRandomUserName.isPresent()){
//                    randomUserName = RANDOM_USER_NAME_PREFIX + UUID.randomUUID();
//                    checkRandomUserName = accountRepository.findByUserName(randomUserName);
//                }
//                account.setUserName(randomUserName);
//            }


            return accountRepository.save(account);
        }
        throw new UserAlreadyExistException("account has been created");

    }

    @Override
    public void updateAccountFullInfo(Account account){

        Optional<Account> findAccount = Optional.empty();

        if(Objects.nonNull(account.getUserName()) && !account.getUserName().isEmpty()){
            findAccount = accountRepository.findByUserName(account.getUserName());
        }else if(Objects.nonNull(account.getEmail()) && !account.getEmail().isEmpty()){
            findAccount = accountRepository.findByEmail(account.getEmail());
        }

        findAccount.ifPresentOrElse((value) -> {

            if(Objects.nonNull(account.getRoles()) && !account.getRoles().isEmpty()){
                value.setRoles(account.getRoles());
            }

            if(Objects.nonNull(account.getAccountStatuses()) && !account.getAccountStatuses().isEmpty()){
                value.setAccountStatuses(account.getAccountStatuses());
            }

//            if(Objects.nonNull(account.getAccountPrivileges()) && account.getAccountPrivileges().size() > 0){
//                value.setAccountPrivileges(account.getAccountPrivileges());
//            }

            accountRepository.save(value);
        },()->{
            throw new NotFoundException("account not found");
        });

    }

    @Override
    public void updateAccountUserInfo(String userName, String password, String email) {

        Optional<Account> findAccount = Optional.empty();

        if(Objects.nonNull(userName) && !userName.isEmpty()){
            findAccount = accountRepository.findByUserName(userName);
        }else if(Objects.nonNull(email) && !email.isEmpty()){
            findAccount = accountRepository.findByEmail(email);
        }

        findAccount.ifPresentOrElse((value) -> {
            value.setUserName(userName);
            value.setPassword(password);
            value.setEmail(email);
            accountRepository.save(value);
        },()->{
            throw new NotFoundException("account not found");
        });


    }

    @Override
    public void deleteAccount(@Argument String id) {
        accountRepository.deleteById(id);
    }

    @Override
    public void deleteAccounts(@Argument List<String> ids) {
        accountRepository.deleteAllById(ids);
    }

    @Override
    public Optional<Account> queryAccountByUserName(@Argument String userName){
        return accountRepository.findByUserName(userName);
    }

    @Override
    public Optional<Account> queryAccountByEmail(@Argument String email){
        return accountRepository.findByEmail(email);
    }
}
