package bg.mironov.bogdan.backend.dto.response;

import bg.mironov.bogdan.backend.model.portfolio.PortfolioPoint;
import bg.mironov.bogdan.backend.model.trade.TradeTrain;

import java.math.BigDecimal;
import java.util.List;

public record TrainingResponse(
    BigDecimal initialBalance,
    BigDecimal finalBalance,
    List<TradeTrain> trades,
    List<PortfolioPoint> portfolioTimeline
) {}
