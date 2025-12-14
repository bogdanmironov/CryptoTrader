package bg.mironov.bogdan.backend.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TradeHistory (
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
