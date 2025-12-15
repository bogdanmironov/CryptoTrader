package bg.mironov.bogdan.backend.scheduled;

import bg.mironov.bogdan.backend.service.trade.TradingService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@EnableScheduling
public class TradingEngine {
    private final TradingService tradingService;

    private volatile boolean running = false;

    public TradingEngine(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @Scheduled(fixedDelay = 5000)
    public void tick() {
        if (!running) {
            return;
        }

        try {
            tradingService.tick();
        } catch (Exception e) {
            //TODO Adequate logging
            System.out.println("Trading tick failed\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
