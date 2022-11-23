package top.desq.service;

import java.math.BigDecimal;

public interface ExchangeService {

    /**
     * Stores usd/uah rate to DB
     */
    void store();

    /**
     * Gets current usd/uah rate
     *
     * @return exchange rate value
     */
    BigDecimal getCurrentRate();

    /**
     * Gets today's average usd/uah rate
     *
     * @return exchange rate value
     */
    BigDecimal getAverageRate();
}
