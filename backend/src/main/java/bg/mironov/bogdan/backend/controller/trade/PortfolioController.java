package bg.mironov.bogdan.backend.controller.trade;

import bg.mironov.bogdan.backend.dto.response.PortfolioResponse;
import bg.mironov.bogdan.backend.service.trade.TradingQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final TradingQueryService queryService;

    public PortfolioController(TradingQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<PortfolioResponse> portfolioValue() {
        return ResponseEntity.ok(queryService.getCurrentPortfolioValue());
    }
}
