package bg.mironov.bogdan.backend.repository.account;

import bg.mironov.bogdan.backend.model.account.Account;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class JdbcAccountRepository implements AccountRepository {
    private final JdbcClient jdbc;

    public JdbcAccountRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public Account create(BigDecimal initialBalance) {
        jdbc.sql("""
                    INSERT INTO account (public_id, balance)
                    VALUES (?, ?)
                """)
            .params(UUID.randomUUID().toString(), initialBalance)
            .update();

        return findLatest();
    }

    @Override
    public Account findLatest() {
        return jdbc.sql("""
                   SELECT id, public_id, balance FROM account
                   ORDER BY id DESC LIMIT 1
                """)
            .query(Account.class)
            .optional()
            .orElse(null);
    }

    @Override
    public Account findLatestForUpdate() {
        return jdbc.sql("""
                    SELECT id, public_id, balance
                    FROM account
                    ORDER BY id DESC
                    LIMIT 1
                """)
            .query(Account.class)
            .optional()
            .orElse(null);
    }

    @Override
    public Account findById(long id) {
        return jdbc.sql("""
                    SELECT id, public_id, balance
                    FROM account
                    WHERE id = ?
                """)
            .param(id)
            .query(Account.class)
            .single();
    }

    @Override
    public Account findByIdForUpdate(long id) {
        return jdbc.sql("""
                    SELECT id, public_id, balance
                    FROM account
                    WHERE id = ?
                    FOR UPDATE
                """)
            .param(id)
            .query(Account.class)
            .single();
    }

    @Override
    public void updateBalance(long accountId, BigDecimal newBalance) {
        jdbc.sql("""
                UPDATE account SET balance = ? WHERE id = ?
                """)
            .params(newBalance, accountId)
            .update();
    }
}
