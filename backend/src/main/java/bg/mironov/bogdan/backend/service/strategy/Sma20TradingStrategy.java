package bg.mironov.bogdan.backend.service.strategy;

import bg.mironov.bogdan.backend.model.account.Account;
import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.live.PriceTick;
import bg.mironov.bogdan.backend.model.trade.TradeAction;
import bg.mironov.bogdan.backend.model.trade.TradeDecision;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class Sma20TradingStrategy implements TradingStrategy {

    private static final int WINDOW = 20;
    private static final BigDecimal BUY_THRESHOLD = new BigDecimal("0.995");
    private static final BigDecimal SELL_THRESHOLD = new BigDecimal("1.005");
    private static final BigDecimal BUY_FRACTION = new BigDecimal("0.10");
    private static final BigDecimal SELL_FRACTION = new BigDecimal("0.25");

    private final Deque<BigDecimal> prices = new ArrayDeque<>();

    @Override
    public TradeDecision decide(PriceTick tick, Account account, Asset asset) {
        BigDecimal price = tick.price();

        prices.addLast(price);

        while (prices.size() > WINDOW) {
            prices.removeFirst();
        }

        if (prices.size() < WINDOW) {
            return new TradeDecision(TradeAction.HOLD, BigDecimal.ZERO);
        }

        BigDecimal sma = calculateSma();

        if (price.compareTo(sma.multiply(BUY_THRESHOLD)) < 0) {
            return buyIfPossible(account, price);
        }

        if (asset != null && price.compareTo(sma.multiply(SELL_THRESHOLD)) > 0) {
            return sellIfPossible(asset);
        }

        return new TradeDecision(TradeAction.HOLD, BigDecimal.ZERO);
    }

    private BigDecimal calculateSma() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal p : prices) {
            sum = sum.add(p);
        }
        return sum.divide(BigDecimal.valueOf(WINDOW), 8, RoundingMode.HALF_UP);
    }

    private TradeDecision buyIfPossible(Account account, BigDecimal price) {
        BigDecimal budget = account.balance().multiply(BUY_FRACTION);
        BigDecimal quantity = budget.divide(price, 8, RoundingMode.DOWN);

        if (quantity.signum() > 0) {
            return new TradeDecision(TradeAction.BUY, quantity);
        }
        return new TradeDecision(TradeAction.HOLD, BigDecimal.ZERO);
    }

    private TradeDecision sellIfPossible(Asset asset) {
        BigDecimal quantity = asset.quantity().multiply(SELL_FRACTION);

        if (quantity.signum() > 0) {
            return new TradeDecision(TradeAction.SELL, quantity);
        }

        return new TradeDecision(TradeAction.HOLD, BigDecimal.ZERO);
    }
}
