package bg.mironov.bogdan.backend.http;

import bg.mironov.bogdan.backend.model.live.PriceTick;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class BinanceClient implements MarketDataClient {
    @Override
    public List<PriceTick> getHistoricalTicks(String symbol, Instant from, Instant to) {
        return List.of();
    }
}
