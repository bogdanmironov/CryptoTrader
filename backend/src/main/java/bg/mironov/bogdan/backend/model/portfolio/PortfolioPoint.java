package bg.mironov.bogdan.backend.model.portfolio;

import java.math.BigDecimal;
import java.time.Instant;

public record PortfolioPoint(
    Instant time,
    BigDecimal value
) {}
