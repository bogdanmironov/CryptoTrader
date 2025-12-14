package bg.mironov.bogdan.backend.startup;

import bg.mironov.bogdan.backend.model.account.Account;
import bg.mironov.bogdan.backend.repository.account.AccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountInitializer implements ApplicationRunner {

    private final AccountRepository accountRepository;

    public AccountInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Account latest = accountRepository.findLatest();

        if (latest == null) {
            accountRepository.create(BigDecimal.ZERO);
        }
    }
}