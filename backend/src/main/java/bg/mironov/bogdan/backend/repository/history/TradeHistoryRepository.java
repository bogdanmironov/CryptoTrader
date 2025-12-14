package bg.mironov.bogdan.backend.repository.history;

import bg.mironov.bogdan.backend.model.trade.NewTrade;
import bg.mironov.bogdan.backend.model.trade.Trade;

import java.time.Instant;
import java.util.List;

public interface TradeHistoryRepository {
    void insert(NewTrade newTrade);

    List<Trade> findBetween(
        long accountId,
        Instant from,
        Instant to
    );

    List<Trade> findPageBefore(
        long accountId,
        Instant before,
        int limit
    );
}
