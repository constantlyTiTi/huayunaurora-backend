package org.huayunaurora.loginAndRegistration.repositories;

import org.huayunaurora.loginAndRegistration.models.Account;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Optional<Account> findByUserName(String userName);

    Optional<Account> findByEmail(String email);
}
