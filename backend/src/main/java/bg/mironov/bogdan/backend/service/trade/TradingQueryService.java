package bg.mironov.bogdan.backend.service.trade;

import bg.mironov.bogdan.backend.dto.response.PortfolioResponse;
import bg.mironov.bogdan.backend.dto.response.TradePageResponse;
import bg.mironov.bogdan.backend.dto.response.TradeResponse;
import bg.mironov.bogdan.backend.http.MarketDataClient;
import bg.mironov.bogdan.backend.model.account.Account;
import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.trade.Trade;
import bg.mironov.bogdan.backend.repository.account.AccountRepository;
import bg.mironov.bogdan.backend.repository.asset.AssetRepository;
import bg.mironov.bogdan.backend.repository.history.TradeHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class TradingQueryService {

    private static final String TRADING_ASSET = "BTCUSDT";
    private final AccountRepository accountRepo;
    private final AssetRepository assetRepo;
    private final MarketDataClient marketData;
    private final TradeHistoryRepository tradeHistoryRepo;

    public TradingQueryService(
        AccountRepository accountRepo,
        AssetRepository assetRepo,
        MarketDataClient marketData,
        TradeHistoryRepository tradeHistoryRepo
    ) {
        this.accountRepo = accountRepo;
        this.assetRepo = assetRepo;
        this.marketData = marketData;
        this.tradeHistoryRepo = tradeHistoryRepo;
    }

    public PortfolioResponse getCurrentPortfolioValue() {
        Account account = accountRepo.findLatest();

        if (account == null) {
            return PortfolioResponse.zero();
        }

        Asset asset = assetRepo.findByAccountAndSymbol(account.id(), TRADING_ASSET);

        if (asset == null) {
            return new PortfolioResponse(
                account.balance(),
                BigDecimal.ZERO,
                account.balance()
            );
        }

        BigDecimal price =
            marketData.getLatestTick(asset.symbol()).price();

        var totalValue = account.balance()
            .add(asset.quantity().multiply(price));

        return new PortfolioResponse(
            account.balance(),
            asset.quantity(),
            totalValue
        );
    }

    public TradePageResponse getTrades(int limit, Instant cursor) {
        Account account = accountRepo.findLatest();

        if (account == null) {
            return new TradePageResponse(List.of(), null);
        }

        cursor = cursor != null ? cursor : Instant.now();

        List<Trade> trades =
            tradeHistoryRepo.findPageBefore(
                account.id(),
                cursor,
                limit
            );

        List<TradeResponse> dtos =
            trades.stream()
                .map(this::toDto)
                .toList();

        Instant nextCursor =
            trades.isEmpty()
                ? null
                : trades.getLast().executedAt();

        return new TradePageResponse(dtos, nextCursor);
    }

    private TradeResponse toDto(Trade trade) {
        return new TradeResponse(
            trade.publicId(),
            trade.symbol(),
            trade.executedAt(),
            trade.action(),
            trade.quantity(),
            trade.quotePrice(),
            trade.transactionDelta()
        );
    }
}
