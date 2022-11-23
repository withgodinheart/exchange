package top.desq.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.desq.client.ExchangeClient;
import top.desq.entity.Exchange;
import top.desq.repository.ExchangeRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private ExchangeRepository exchangeRepository;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    private final List<BigDecimal> storage = new ArrayList<>();

    @AfterEach
    void clear() {
        storage.clear();
    }

    @Test
    void store_givenValidData_shouldAddToStorage() {
        // given:
        mockExchangeClient();
        doAnswer(invocation -> {
            var exchange = invocation.getArgument(0, Exchange.class);
            storage.add(exchange.getRate());
            return null;
        }).when(exchangeRepository).save(any());

        // when:
        exchangeService.store();

        // then:
        verify(exchangeClient).find();
        verify(exchangeRepository).save(any());
        assertThat(storage).isNotEmpty();
    }

    @Test
    void getCurrentRate_givenValidData_shouldReturnRate() {
        // given:
        mockExchangeClient();

        // when:
        var actual = exchangeService.getCurrentRate();
        var expected = BigDecimal.ONE.setScale(2, RoundingMode.FLOOR);

        // then:
        verify(exchangeClient).find();
        assertEquals(expected, actual);
    }

    @Test
    void getAverageRate_givenFilledStorage_shouldReturnAverageRate() {
        // given:
        doAnswer(invocation -> {
            fillStorage();
            var sum = storage.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(BigDecimal.valueOf(storage.size()), RoundingMode.FLOOR);
        }).when(exchangeRepository).findAverageRateForTimeRange(any(), any());

        // when:
        var actual = exchangeService.getAverageRate();
        var expected = BigDecimal.TEN.setScale(2, RoundingMode.FLOOR);

        // then:
        assertEquals(expected, actual);
    }

    private void mockExchangeClient() {
        var list = getRateList();
        when(exchangeClient.find()).thenReturn(list);
    }

    private List<ExchangeClient.Rate> getRateList() {
        return List.of(new ExchangeClient.Rate("USD", "UAH",
                BigDecimal.ONE, BigDecimal.ONE));
    }

    private void fillStorage() {
        for (int i = 8; i < 13; i++) {
            storage.add(BigDecimal.valueOf(i).setScale(2, RoundingMode.FLOOR));
        }
    }

}