package bg.mironov.bogdan.backend.service.trade;

import bg.mironov.bogdan.backend.repository.account.AccountRepository;
import bg.mironov.bogdan.backend.scheduled.TradingEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TradingEngine tradingEngine;

    public AccountService(AccountRepository accountRepository, TradingEngine tradingEngine) {
        this.accountRepository = accountRepository;
        this.tradingEngine = tradingEngine;
    }

    @Transactional
    public void reset(BigDecimal initialBalance) {
        initialBalance = initialBalance == null ? BigDecimal.ZERO : initialBalance;

        tradingEngine.stop();
        accountRepository.create(initialBalance);
    }

    @Transactional
    public void topUp(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        var account = accountRepository.findLatestForUpdate();

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        accountRepository.updateBalance(
            account.id(),
            account.balance().add(amount)
        );
    }
}
