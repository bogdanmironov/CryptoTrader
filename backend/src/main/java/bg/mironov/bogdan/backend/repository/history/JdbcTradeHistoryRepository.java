package bg.mironov.bogdan.backend.repository.history;

import bg.mironov.bogdan.backend.model.trade.NewTrade;
import bg.mironov.bogdan.backend.model.trade.Trade;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcTradeHistoryRepository implements TradeHistoryRepository {
    private final JdbcClient jdbc;

    public JdbcTradeHistoryRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(NewTrade newTrade) {
        jdbc.sql("""
                INSERT INTO trade_history (
                               public_id,
                               account_id,
                               symbol,
                               executed_at,
                               action,
                               quantity,
                               quote_price,
                               transaction_delta
                   ) values (?, ?, ?, ?, ?, ?, ?, ?)
                """)
            .params(
                UUID.randomUUID().toString(),
                newTrade.accountId(),
                newTrade.symbol(),
                newTrade.executedAt(),
                newTrade.action().name(),
                newTrade.quantity(),
                newTrade.quotePrice(),
                newTrade.transactionDelta()
            )
            .update();
    }

    @Override
    public List<Trade> findBetween(long accountId, Instant from, Instant to) {
        return jdbc.sql("""
                        SELECT
                            id,
                            public_id,
                            account_id,
                            symbol,
                            executed_at,
                            action,
                            quantity,
                            quote_price,
                            transaction_delta
                        FROM trade_history
                        WHERE account_id = ?
                          AND executed_at >= ?
                          AND executed_at <= ?
                        ORDER BY executed_at DESC
                """)
            .params(accountId, from, to)
            .query(Trade.class)
            .list();
    }

    @Override
    public List<Trade> findPageBefore(long accountId, Instant before, int limit) {
        return jdbc.sql("""
                        SELECT
                            id,
                            public_id,
                            account_id,
                            symbol,
                            executed_at,
                            action,
                            quantity,
                            quote_price,
                            transaction_delta
                        FROM trade_history
                        WHERE account_id = ?
                          AND executed_at < ?
                        ORDER BY executed_at DESC
                        LIMIT ?
                """)
            .params(accountId, before, limit)
            .query(Trade.class)
            .list();
    }
}
