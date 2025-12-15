package bg.mironov.bogdan.backend.dto.response;

import java.time.Instant;
import java.util.List;

public record TradePageResponse (
    List<TradeResponse> trades,
    Instant nextCursor
) {}
