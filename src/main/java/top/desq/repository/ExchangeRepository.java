package top.desq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.desq.entity.Exchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    @Query("select avg(e.rate) from Exchange e where e.createdAt between :from and :to")
    BigDecimal findAverageRateForTimeRange(LocalDateTime from, LocalDateTime to);
}
