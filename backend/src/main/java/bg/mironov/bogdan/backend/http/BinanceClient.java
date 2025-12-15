package bg.mironov.bogdan.backend.http;

import bg.mironov.bogdan.backend.model.live.PriceTick;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;

//TODO implement with DTOs and object mapping
@Component
public class BinanceClient implements MarketDataClient {

    private static final String BASE_API_ENDPOINT = "https://api.binance.com";
    private static final String LIVE_DATA_PATH = "/api/v3/ticker/price";
    private static final String SYMBOL_QUERY_PARAM = "symbol";
    private static final String RESPONSE_PRICE_FIELD = "price";
    private static final String HISTORICAL_DATA_PATH = "/api/v3/klines";
    private static final String INTERVAL_QUERY_PARAM = "interval";
    private static final String START_TIME_QUERY_PARAM = "startTime";
    private static final String END_TIME_QUERY_PARAM = "endTime";
    private static final String LIMIT_QUERY_PARAM = "limit";
    private static final int HISTORICAL_DATA_LIMIT = 1000;
    private static final int OPEN_TIME_RESPONSE_POSITION = 0;
    private static final int CLOSE_PRICE_RESPONSE_POSITION = 4;
    //TODO make historical data interval be passed from FE.
    private static final String HISTORICAL_DATA_INTERVAL = "1d";

    private final RestClient restClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public BinanceClient() {
        this.restClient = RestClient.builder()
            .baseUrl(BASE_API_ENDPOINT)
            .build();
    }


    @Override
    public List<PriceTick> getHistoricalTicks(
        String symbol,
        Instant from,
        Instant to
    ) {
        String response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(HISTORICAL_DATA_PATH)
                .queryParam(SYMBOL_QUERY_PARAM, symbol)
                .queryParam(INTERVAL_QUERY_PARAM, HISTORICAL_DATA_INTERVAL)
                .queryParam(START_TIME_QUERY_PARAM, from.toEpochMilli())
                .queryParam(END_TIME_QUERY_PARAM, to.toEpochMilli())
                .queryParam(LIMIT_QUERY_PARAM, HISTORICAL_DATA_LIMIT)
                .build()
            )
            .retrieve()
            .body(String.class);

        try {
            var root = mapper.readTree(response);
            List<PriceTick> ticks = new java.util.ArrayList<>();

            for (var candle : root) {
                long openTime = candle.get(OPEN_TIME_RESPONSE_POSITION).asLong();
                var closePrice = new java.math.BigDecimal(candle.get(CLOSE_PRICE_RESPONSE_POSITION).asText());

                ticks.add(
                    new PriceTick(symbol, closePrice, Instant.ofEpochMilli(openTime))
                );
            }

            return ticks;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Binance klines", e);
        }
    }

    @Override
    public PriceTick getLatestTick(String symbol) {
        String response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(LIVE_DATA_PATH)
                .queryParam(SYMBOL_QUERY_PARAM, symbol)
                .build()
            )
            .retrieve()
            .body(String.class);

        try {
            var root = mapper.readTree(response);
            var price = new java.math.BigDecimal(root.get(RESPONSE_PRICE_FIELD).asText());

            return new PriceTick(symbol, price, Instant.now());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Binance latest price", e);
        }
    }
}
