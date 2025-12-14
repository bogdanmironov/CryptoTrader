package bg.mironov.bogdan.backend.service.trade;

import bg.mironov.bogdan.backend.model.account.Account;
import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.asset.NewAsset;
import bg.mironov.bogdan.backend.model.live.PriceTick;
import bg.mironov.bogdan.backend.model.trade.NewTrade;
import bg.mironov.bogdan.backend.model.trade.TradeAction;
import bg.mironov.bogdan.backend.model.trade.TradeDecision;
import bg.mironov.bogdan.backend.repository.account.AccountRepository;
import bg.mironov.bogdan.backend.repository.asset.AssetRepository;
import bg.mironov.bogdan.backend.repository.history.TradeHistoryRepository;
import bg.mironov.bogdan.backend.service.strategy.TradingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TradingService {
    private final AccountRepository accountRepo;
    private final AssetRepository assetRepo;
    private final TradeHistoryRepository tradeRepo;
    private final TradingStrategy strategy;

    public TradingService(AccountRepository accountRepo, AssetRepository assetRepo, TradeHistoryRepository tradeRepo,
                          TradingStrategy strategy) {
        this.accountRepo = accountRepo;
        this.assetRepo = assetRepo;
        this.tradeRepo = tradeRepo;
        this.strategy = strategy;
    }

    @Transactional
    public void tick(PriceTick tick) {
        Account account = accountRepo.findLatestForUpdate();

        if (account == null) {
            return;
        }

        Asset asset = assetRepo.findByAccountAndSymbolForUpdate(account.id(), tick.symbol());

        TradeDecision decision = strategy.decide(tick, account, asset);

        switch (decision.action()) {
            case TradeAction.HOLD -> {}
            case TradeAction.BUY -> buy(account, asset, decision, tick);
            case TradeAction.SELL -> sell(account, asset, decision, tick);
        }
    }

    private void buy(
        Account account,
        Asset asset,
        TradeDecision decision,
        PriceTick tick
    ) {
        BigDecimal quantity = decision.quantity();

        if (quantity.signum() <= 0) {
            return;
        }

        BigDecimal cost = quantity.multiply(tick.price());

        if (account.balance().compareTo(cost) < 0) {
            return;
        }

        accountRepo.updateBalance(
            account.id(),
            account.balance().subtract(cost)
        );

        if (asset == null) {
            assetRepo.insert(new NewAsset(account.id(), tick.symbol(), quantity));
        } else {
            assetRepo.updateQuantity(asset.id(), asset.quantity().add(quantity));
        }

        tradeRepo.insert(
            new NewTrade(
                account.id(),
                tick.symbol(),
                tick.timestamp(),
                TradeAction.BUY,
                quantity,
                tick.price(),
                cost.negate()
            )
        );
    }

    private void sell(
        Account account,
        Asset asset,
        TradeDecision decision,
        PriceTick tick
    ) {
        BigDecimal quantity = decision.quantity();

        if (asset == null) {
            return;
        }

        if (quantity.signum() <= 0) {
            return;
        }

        if (asset.quantity().compareTo(quantity) < 0) {
            return;
        }

        BigDecimal revenue = quantity.multiply(tick.price());

        accountRepo.updateBalance(
            account.id(),
            account.balance().add(revenue)
        );

        assetRepo.updateQuantity(
            asset.id(),
            asset.quantity().subtract(quantity)
        );

        tradeRepo.insert(
            new NewTrade(
                account.id(),
                tick.symbol(),
                tick.timestamp(),
                TradeAction.SELL,
                quantity,
                tick.price(),
                revenue
            )
        );
    }
}
