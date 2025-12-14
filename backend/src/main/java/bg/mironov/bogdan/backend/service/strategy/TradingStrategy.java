package bg.mironov.bogdan.backend.service.strategy;

import bg.mironov.bogdan.backend.model.account.Account;
import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.live.PriceTick;
import bg.mironov.bogdan.backend.model.trade.TradeDecision;

public interface TradingStrategy {
    TradeDecision decide(PriceTick tick, Account account, Asset asset);
}
