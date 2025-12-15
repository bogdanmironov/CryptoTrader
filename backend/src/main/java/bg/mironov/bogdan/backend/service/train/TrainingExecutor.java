package bg.mironov.bogdan.backend.service.train;

import bg.mironov.bogdan.backend.model.account.AccountTrain;
import bg.mironov.bogdan.backend.model.asset.AssetTrain;
import bg.mironov.bogdan.backend.model.live.PriceTick;
import bg.mironov.bogdan.backend.model.trade.TradeAction;
import bg.mironov.bogdan.backend.model.trade.TradeDecision;
import bg.mironov.bogdan.backend.model.trade.TradeTrain;

import java.math.BigDecimal;
import java.util.List;

public class TrainingExecutor {

    private TrainingExecutor() {
    }

    public static void execute(
        TradeDecision decision,
        PriceTick tick,
        AccountTrain account,
        AssetTrain asset,
        List<TradeTrain> trades
    ) {
        switch (decision.action()) {
            case BUY -> buy(decision, tick, account, asset, trades);
            case SELL -> sell(decision, tick, account, asset, trades);
            case HOLD -> {}
        }
    }

    private static void buy(
        TradeDecision decision,
        PriceTick tick,
        AccountTrain account,
        AssetTrain asset,
        List<TradeTrain> trades
    ) {
        BigDecimal quantity = decision.quantity();

        if (quantity.signum() <= 0) {
            return;
        }

        BigDecimal price = tick.price();
        BigDecimal cost = quantity.multiply(price);

        if (account.getBalance().compareTo(cost) < 0) {
            return;
        }

        account.setBalance(account.getBalance().subtract(cost));
        asset.setQuantity(asset.getQuantity().add(quantity));

        trades.add(new TradeTrain(
            tick.timestamp(),
            TradeAction.BUY,
            price,
            quantity
        ));


    }

    private static void sell(
        TradeDecision decision,
        PriceTick tick,
        AccountTrain account,
        AssetTrain asset,
        List<TradeTrain> trades
    ) {
        BigDecimal quantity = decision.quantity();
        if (quantity.signum() <= 0) {
            return;
        }

        if (asset.getQuantity().compareTo(quantity) < 0) {
            return;
        }

        BigDecimal price = tick.price();
        BigDecimal cost = quantity.multiply(price);

        account.setBalance(account.getBalance().add(cost));
        asset.setQuantity(asset.getQuantity().subtract(quantity));

        trades.add(new TradeTrain(
            tick.timestamp(),
            TradeAction.SELL,
            price,
            quantity
        ));
    }
}
