package bg.mironov.bogdan.backend.model.trade;

import java.math.BigDecimal;
import java.time.Instant;

public record NewTrade(
    long accountId,
    String symbol,
    Instant executedAt,
    TradeAction action,
    BigDecimal quantity,
    BigDecimal quotePrice,
    BigDecimal transactionDelta
) {
}
