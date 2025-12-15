package bg.mironov.bogdan.backend.http;

import bg.mironov.bogdan.backend.model.live.PriceTick;

import java.time.Instant;
import java.util.List;

public interface MarketDataClient {

    List<PriceTick> getHistoricalTicks(String symbol, Instant from, Instant to);

    PriceTick getLatestTick(String symbol);
}
