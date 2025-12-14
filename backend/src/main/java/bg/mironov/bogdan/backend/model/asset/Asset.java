package bg.mironov.bogdan.backend.model.asset;

import java.math.BigDecimal;

public record Asset (
    long id,
    long accountId,
    String symbol,
    BigDecimal quantity
) {
}
