package bg.mironov.bogdan.backend.dto.request;

import java.math.BigDecimal;
import java.time.Instant;

public record TrainingRequest(
    String symbol,
    Instant from,
    Instant to,
    BigDecimal initialBalance
) {}
