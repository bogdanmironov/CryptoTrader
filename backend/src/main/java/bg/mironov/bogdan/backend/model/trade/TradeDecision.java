package bg.mironov.bogdan.backend.model.trade;

import java.math.BigDecimal;

public record TradeDecision(
    TradeAction action,
    BigDecimal quantity
) {
}