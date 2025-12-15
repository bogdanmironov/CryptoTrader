package bg.mironov.bogdan.backend.model.trade;

import java.math.BigDecimal;
import java.time.Instant;

public record TradeTrain (
   Instant time,
   TradeAction action,
   BigDecimal price,
   BigDecimal quantity
) {}
