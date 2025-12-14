package bg.mironov.bogdan.backend.model.live;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceTick(
    String symbol,
    BigDecimal price,
    Instant timestamp
) {}
