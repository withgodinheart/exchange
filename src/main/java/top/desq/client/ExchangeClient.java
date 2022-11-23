package top.desq.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "exchange-service", url = "${exchange.api.url}")
public interface ExchangeClient {

    /**
     * Makes request to echange API
     *
     * @return list of {@link Rate}
     */
    @GetMapping("${exchange.api.path}")
    List<Rate> find();

    /**
     * Rate object record
     */
    record Rate(@JsonProperty("ccy") String currency,
                @JsonProperty("base_ccy") String baseCurrency,
                BigDecimal buy,
                BigDecimal sale) {
    }
}
