package top.desq.service;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.desq.client.ExchangeClient;
import top.desq.entity.Exchange;
import top.desq.repository.ExchangeRepository;
import top.desq.util.TimeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeClient exchangeClient;
    private final ExchangeRepository exchangeRepository;

    /**
     * {@inheritDoc}
     */
    @Async
    @Scheduled(fixedDelay = 30000)
    public void store() {
        try {
            var exchange = new Exchange();
            var rate = requestCurrentRate();
            exchange.setRate(rate);
            exchangeRepository.save(exchange);
            log.info("Exchange rate = {} saved to DB", rate);
        } catch (RetryableException e) {
            log.info("Failed to save to DB: {}", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getCurrentRate() {
        var rate = requestCurrentRate();
        log.info("Current exchange rate = {}", rate);
        return rate;
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getAverageRate() {
        var from = TimeUtil.getStartOfDay();
        var to = TimeUtil.getEndOfDay();
        return exchangeRepository.findAverageRateForTimeRange(from, to);
    }

    /**
     * Makes request to the exchange API
     *
     * @return exchange rate value
     */
    private BigDecimal requestCurrentRate() {
        var exchangeList = exchangeClient.find();
        var rate = exchangeList.stream().findFirst().orElseThrow().buy();
        return rate.setScale(2, RoundingMode.FLOOR);
    }
}
