package bg.mironov.bogdan.backend.dto.response;

import bg.mironov.bogdan.backend.model.trade.TradeAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TradeResponse(
    UUID id,
    String symbol,
    Instant executedAt,
    TradeAction action,
    BigDecimal quantity,
    BigDecimal quotePrice,
    BigDecimal transactionDelta
) {}
