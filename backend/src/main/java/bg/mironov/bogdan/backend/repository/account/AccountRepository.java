package bg.mironov.bogdan.backend.repository.account;

import bg.mironov.bogdan.backend.model.account.Account;

import java.math.BigDecimal;

public interface AccountRepository {
    Account create(BigDecimal initialBalance);

    Account findLatest();

    Account findLatestForUpdate();

    Account findById(long id);

    Account findByIdForUpdate(long id);

    void updateBalance(long accountId, BigDecimal newBalance);
}
