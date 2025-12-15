package bg.mironov.bogdan.backend.dto.response;

import java.math.BigDecimal;

public record PortfolioResponse(
    BigDecimal cash,
    BigDecimal assetQuantity,
    BigDecimal totalValue
) {
    public static PortfolioResponse zero() {
        return new PortfolioResponse(
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );
    }
}
