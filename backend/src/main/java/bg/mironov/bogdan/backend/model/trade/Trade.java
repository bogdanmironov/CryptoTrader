package bg.mironov.bogdan.backend.model.trade;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Trade(
    long id,
    UUID publicId,
    long accountId,
    String symbol,
    Instant executedAt,
    TradeAction action,
    BigDecimal quantity,
    BigDecimal quotePrice,
    BigDecimal transactionDelta
) {}
