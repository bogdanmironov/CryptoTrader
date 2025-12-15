package bg.mironov.bogdan.backend.controller.trade;


import bg.mironov.bogdan.backend.dto.response.TradePageResponse;
import bg.mironov.bogdan.backend.dto.response.TradingStatusResponse;
import bg.mironov.bogdan.backend.scheduled.TradingEngine;
import bg.mironov.bogdan.backend.service.trade.AccountService;
import bg.mironov.bogdan.backend.service.trade.TradingQueryService;
import bg.mironov.bogdan.backend.service.trade.TradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradingQueryService queryService;
    private final TradingEngine tradingEngine;
    private final AccountService accountService;
    private final TradingService tradingService;

    public TradeController(TradingQueryService queryService, TradingEngine tradingEngine, AccountService accountService,
                           TradingService tradingService) {
        this.queryService = queryService;
        this.tradingEngine = tradingEngine;
        this.accountService = accountService;
        this.tradingService = tradingService;
    }

    @GetMapping
    public TradePageResponse getTrades(
        @RequestParam(defaultValue = "50") int limit,
        @RequestParam(required = false) Instant cursor
    ) {
        return queryService.getTrades(limit, cursor);
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start() {
        tradingEngine.start();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stop() {
        tradingEngine.stop();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<TradingStatusResponse> status() {
        return ResponseEntity.ok(new TradingStatusResponse(tradingEngine.isRunning()));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(
        @RequestParam(defaultValue = "10000") BigDecimal initialBalance
    ) {
        accountService.reset(initialBalance);
        tradingService.reset();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/top-up")
    public ResponseEntity<Void> topUp(
        @RequestParam BigDecimal amount
    ) {
        accountService.topUp(amount);
        return ResponseEntity.ok().build();
    }
}
