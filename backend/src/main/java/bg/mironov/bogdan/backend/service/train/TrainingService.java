package bg.mironov.bogdan.backend.service.train;

import bg.mironov.bogdan.backend.dto.response.TrainingResponse;
import bg.mironov.bogdan.backend.http.MarketDataClient;
import bg.mironov.bogdan.backend.model.account.AccountTrain;
import bg.mironov.bogdan.backend.model.asset.AssetTrain;
import bg.mironov.bogdan.backend.model.live.PriceTick;
import bg.mironov.bogdan.backend.model.portfolio.PortfolioPoint;
import bg.mironov.bogdan.backend.model.trade.TradeDecision;
import bg.mironov.bogdan.backend.model.trade.TradeTrain;
import bg.mironov.bogdan.backend.service.strategy.TradingStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingService {
    private final TradingStrategy strategy;
    private final MarketDataClient marketData;


    public TrainingService(TradingStrategy strategy, MarketDataClient marketData) {
        this.strategy = strategy;
        this.marketData = marketData;
    }

    public TrainingResponse run(
        String symbol,
        Instant from,
        Instant to,
        BigDecimal initialBalance
    ) {
        AccountTrain trainAccount = new AccountTrain(initialBalance);
        AssetTrain trainAsset = new AssetTrain(symbol);
        List<TradeTrain> trades = new ArrayList<>();
        List<PortfolioPoint> equity = new ArrayList<>();

        List<PriceTick> ticks = marketData.getHistoricalTicks(symbol, from, to);
        if (ticks.isEmpty()) {
            return new TrainingResponse(
                initialBalance,
                initialBalance,
                List.of(),
                List.of()
            );
        }

        for (PriceTick tick : ticks) {
            TradeDecision decision =
                strategy.decide(
                    tick,
                    trainAccount.toAccount(),
                    trainAsset.toAsset()
                );

            TrainingExecutor.execute(
                decision,
                tick,
                trainAccount,
                trainAsset,
                trades
            );

            BigDecimal assetValue =
                trainAsset.getQuantity().multiply(tick.price());

            equity.add(
                new PortfolioPoint(
                    tick.timestamp(),
                    trainAccount.getBalance().add(assetValue)
                )
            );
        }

        BigDecimal finalValue =
            trainAccount.getBalance()
                .add(trainAsset.getQuantity().multiply(ticks.getLast().price()));

        return new TrainingResponse(initialBalance, finalValue, trades, equity);
    }
}
