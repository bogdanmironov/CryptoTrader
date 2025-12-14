package bg.mironov.bogdan.backend.model.asset;

import java.math.BigDecimal;

public record NewAsset(
    long accountId,
    String symbol,
    BigDecimal quantity
) {
}
